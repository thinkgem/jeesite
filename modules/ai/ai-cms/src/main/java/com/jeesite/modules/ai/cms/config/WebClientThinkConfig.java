/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.config;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.http.okhttp.OpenAiHttpClientBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 推理模型兼容处理：将 reasoning_content / thinking 内容包裹在 &lt;think&gt; 标签中。
 * <p>
 * Spring AI 2.0.0 架构变化：
 * <ul>
 *   <li>OpenAI 模型：底层切换到官方 openai-java SDK（OkHttp），不再使用 WebClient；
 *       通过 {@code OpenAiHttpClientBuilderCustomizer} + OkHttp {@code Interceptor} 拦截 SSE 响应</li>
 *   <li>Ollama 模型：仍使用 WebClient，但 Spring Boot 4 移除了 WebClientCustomizer 机制；
 *       改为提供自定义 {@code WebClient.Builder} Bean，供 {@code OllamaApiAutoConfiguration}
 *       通过 {@code ObjectProvider&lt;WebClient.Builder&gt;} 获取</li>
 * </ul>
 *
 * @author ThinkGem
 */
@Configuration
public class WebClientThinkConfig {

	private final Logger logger = LoggerFactory.getLogger(WebClientThinkConfig.class);

	/** 后台处理 SSE 流的线程池（守护线程） */
	private static final ExecutorService SSE_EXECUTOR = Executors.newCachedThreadPool(r -> {
		Thread t = new Thread(r, "sse-think-processor");
		t.setDaemon(true);
		return t;
	});

	// ==================== OpenAI 模型：OkHttp Interceptor 方案 ====================

	/**
	 * 为 OpenAI 兼容模型注册 OkHttp 拦截器，拦截 SSE 流式响应并转换 thinking 内容。
	 */
	@Bean
	@ConditionalOnClass(name = "org.springframework.ai.openai.http.okhttp.OpenAiHttpClientBuilderCustomizer")
	public OpenAiHttpClientBuilderCustomizer openAiHttpClientThinkCustomizer() {
		Interceptor thinkInterceptor = new OkHttpThinkInterceptor();
		return builder -> {
			builder.interceptor(thinkInterceptor);
			logger.info("OpenAI think OkHttp interceptor registered");
		};
	}

	// ==================== Ollama 模型：WebClient.Builder Bean 方案 ====================

	/**
	 * 提供自定义的 WebClient.Builder Bean，供 {@code OllamaApiAutoConfiguration} 使用。
	 */
	@Bean
	@ConditionalOnClass(name = "org.springframework.ai.ollama.api.OllamaApi")
	@ConditionalOnMissingBean(name = "thinkWebClientBuilder")
	public WebClient.Builder thinkWebClientBuilder() {
		return WebClient.builder().filter(thinkResponseFilter());
	}

	/** 响应转换过滤器：将 reasoning_content/thinking 包裹为 &lt;think&gt; 标签 */
	private ExchangeFilterFunction thinkResponseFilter() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			AtomicBoolean thinkingFlag = new AtomicBoolean(false);
			Flux<DataBuffer> modifiedBody = clientResponse.bodyToFlux(DataBuffer.class)
					.map(this::dataBufferToString)
					.map(eventString -> processSseEventString(eventString, thinkingFlag))
					.map(str -> new DefaultDataBufferFactory().wrap(str.getBytes(StandardCharsets.UTF_8)));
			return Mono.just(ClientResponse.create(clientResponse.statusCode())
					.headers(h -> h.putAll(clientResponse.headers().asHttpHeaders()))
					.cookies(c -> c.putAll(clientResponse.cookies()))
					.body(modifiedBody)
					.build());
		});
	}

	// ==================== SSE 事件处理（WebClient 多行 chunk） ====================

	/**
	 * 处理 WebClient 的 SSE 事件 chunk（可能含多行），逐行处理。
	 */
	private String processSseEventString(String eventString, AtomicBoolean thinkingFlag) {
		List<String> lines = new ArrayList<>();
		for (String line : eventString.split("\\n", -1)) {
			lines.add(processSseLine(line, thinkingFlag));
		}
		return StringUtils.join(lines, "\n");
	}

	// ==================== OkHttp SSE 拦截器 ====================

	/**
	 * OkHttp Interceptor：拦截 SSE 流式响应，使用 {@link Pipe} 异步转换。
	 */
	private class OkHttpThinkInterceptor implements Interceptor {

		@Override
		public Response intercept(Chain chain) throws IOException {
			Response response = chain.proceed(chain.request());
			ResponseBody originalBody = response.body();
			MediaType contentType = originalBody != null ? originalBody.contentType() : null;

			if (originalBody == null
					|| contentType == null
					|| !contentType.toString().contains("text/event-stream")) {
				return response;
			}

			Pipe pipe = new Pipe(Long.MAX_VALUE);
			AtomicBoolean thinkingFlag = new AtomicBoolean(false);

			SSE_EXECUTOR.execute(() -> pipeSseStream(originalBody, pipe, thinkingFlag));

			return response.newBuilder()
					.body(newSseResponseBody(contentType, pipe))
					.build();
		}

		/** 从原始 ResponseBody 逐行读取 → 转换 → 写入 Pipe */
		private void pipeSseStream(ResponseBody originalBody, Pipe pipe, AtomicBoolean thinkingFlag) {
			try (BufferedSource source = Okio.buffer(originalBody.source());
				 BufferedSink sink = Okio.buffer(pipe.sink())) {
				String line;
				while ((line = source.readUtf8Line()) != null) {
					sink.writeUtf8(processSseLine(line, thinkingFlag));
					sink.writeUtf8("\n");
					sink.flush();
				}
			} catch (Exception e) {
				logger.error("Error processing OpenAI SSE thinking stream", e);
			}
		}
	}

	// ==================== 核心 SSE 行处理逻辑 ====================

	/**
	 * 处理单行 SSE 事件，将 thinking/reasoning 内容包裹在 &lt;think&gt; 标签中。
	 * 兼容 OpenAI 格式（delta.reasoning_content）和 Ollama 格式（message.thinking）。
	 */
	@SuppressWarnings("unchecked")
	private String processSseLine(String line, AtomicBoolean thinkingFlag) {
		boolean dataPrefix = false;
		String jsonPart = line;
		if (line.startsWith("data: ")) {
			jsonPart = line.substring("data: ".length()).trim();
			dataPrefix = true;
		}
		// 跳过非 JSON 行
		if (!StringUtils.startsWith(jsonPart, "{") || !StringUtils.endsWith(jsonPart, "}")) {
			return line;
		}
		Map<String, Object> map = JsonMapper.fromJson(jsonPart, Map.class);
		if (map == null) {
			return line;
		}
		// 提取 choices：Ollama 格式顶层为 message，OpenAI 格式为 choices
		boolean ollamaEvent = false;
		List<Object> choices = (List<Object>) map.get("choices");
		if (choices == null) {
			Map<String, Object> message = (Map<String, Object>) map.get("message");
			if (message == null) {
				return line;
			}
			choices = List.of(message);
			ollamaEvent = true;
		}
		// 处理每个 choice 中的 content/think 字段
		for (Object o : choices) {
			Map<String, Object> choice = (Map<String, Object>) o;
			if (choice == null) {
				continue;
			}
			applyThinkTags(choice, thinkingFlag, ollamaEvent);
		}
		// 重新序列化
		if (ollamaEvent) {
			List<Object> finalChoices = choices;
			map.put("message", finalChoices.get(0));
			map.remove("choices");
		}
		return (dataPrefix ? "data: " : "") + JsonMapper.toJson(map);
	}

	/**
	 * 从 choice 中提取 content/reasoningContent，应用 &lt;think&gt; 标签逻辑，写回。
	 */
	@SuppressWarnings("unchecked")
	private void applyThinkTags(Map<String, Object> choice, AtomicBoolean thinkingFlag, boolean ollamaEvent) {
		Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
		String content = delta != null
				? (String) delta.get("content")
				: (String) choice.get("content");
		String reasoningContent = delta != null
				? (String) delta.get("reasoning_content")
				: (String) choice.get("thinking");

		// 将 thinking 内容包裹在 <think> 标签中
		if (StringUtils.isNotEmpty(reasoningContent) && StringUtils.isEmpty(content)) {
			content = (!thinkingFlag.getAndSet(true) ? "<think>\n" : "") + reasoningContent;
		} else if (thinkingFlag.getAndSet(false)) {
			content = "</think>\n" + (content == null ? "" : content);
		}

		// 写回修改后的 content
		if (ollamaEvent) {
			choice.put("content", content);
		} else if (delta != null) {
			delta.put("content", content);
		}
	}

	// ==================== 工具方法 ====================

	/** DataBuffer → UTF-8 字符串 */
	private String dataBufferToString(DataBuffer buf) {
		byte[] bytes = new byte[buf.readableByteCount()];
		buf.read(bytes);
		DataBufferUtils.release(buf);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/** 创建流式 SSE ResponseBody（contentLength = -1 表示流式传输） */
	private static ResponseBody newSseResponseBody(MediaType contentType, Pipe pipe) {
		return new ResponseBody() {
			@Override public MediaType contentType() { return contentType; }
			@Override public long contentLength() { return -1; }
			@Override public BufferedSource source() { return Okio.buffer(pipe.source()); }
		};
	}
}

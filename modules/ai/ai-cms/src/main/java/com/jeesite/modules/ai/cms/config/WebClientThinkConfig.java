/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.config;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 推理模型OpenAI兼容处理
 * @author ThinkGem
 */
@Configuration
public class WebClientThinkConfig {

	private final Logger logger = LoggerFactory.getLogger(WebClientThinkConfig.class);

	@Bean
	@ConditionalOnMissingBean
	@SuppressWarnings("unchecked")
	public WebClientCustomizer webClientCustomizerThink() {
		return webClientBuilder -> {
			ExchangeFilterFunction requestFilter = ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
				logger.trace("Request url: {}: {}", clientRequest.method(), clientRequest.url());
				return Mono.just(clientRequest);
			});
			ExchangeFilterFunction responseFilter = ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
				logger.trace("Response status: {}", clientResponse.statusCode());
				AtomicBoolean thinkingFlag = new AtomicBoolean(false);
				Flux<DataBuffer> modifiedBody = clientResponse.bodyToFlux(DataBuffer.class)
					.map(buf -> {
						byte[] bytes = new byte[buf.readableByteCount()];
						buf.read(bytes);
						DataBufferUtils.release(buf);
						return new String(bytes, StandardCharsets.UTF_8);
					})
					.flatMap(eventString -> {
						logger.trace("Original response: ==> {}", eventString);
						List<String> lines = new ArrayList<>();
						String[] list = eventString.split("\\n", -1);
						for (String line : list) {
							String jsonPart = line;
							boolean dataPrefix = false;
							if (line.startsWith("data: ")) {
								jsonPart = line.substring("data: ".length()).trim();
								dataPrefix = true;
							}
							if (!(StringUtils.startsWith(jsonPart, "{")
									&& StringUtils.endsWith(jsonPart, "}")
									&& !"data: [DONE]".equals(line))) {
								lines.add(line);
								continue;
							}
							Map<String, Object> map = JsonMapper.fromJson(jsonPart, Map.class);
							if (map == null) {
								lines.add(line);
								continue;
							}
							// 修改内容字段
							boolean ollamaEvent = false;
							List<Object> choices = (List<Object>) map.get("choices");
							if (choices == null) {
								Map<String, Object> message = (Map<String, Object>) map.get("message");
								if (message == null) {
									lines.add(line);
									continue;
								}
								choices = List.of(message);
								ollamaEvent = true;
							}
							for (Object o : choices) {
								Map<String, Object> choice = (Map<String, Object>) o;
								if (choice == null) {
									continue;
								}
								String content;
								String reasoningContent;
								Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
								if (delta != null) {
									content = (String) delta.get("content");
									reasoningContent = (String) delta.get("reasoning_content");
								} else {
									content = (String) choice.get("content");
									reasoningContent = (String) choice.get("thinking");
								}
								if (StringUtils.isNotEmpty(reasoningContent) && StringUtils.isEmpty(content)) {
									if (!thinkingFlag.get()) {
										thinkingFlag.set(true);
										content = "<think>\n" + reasoningContent;
									} else {
										content = reasoningContent;
									}
								} else {
									if (thinkingFlag.get()) {
										thinkingFlag.set(false);
										content = "</think>\n" + (content == null ? "" : content);
									}
								}
								if (ollamaEvent) {
									choice.put("content", content);
									map.put("message", choice);
								} else if (delta != null) {
									delta.put("content", content);
								}
							}
							// 重新生成事件字符串
							lines.add((dataPrefix ? "data: " : "") + JsonMapper.toJson(map));
						}
						String finalLine = StringUtils.join(lines, "\n");
						logger.trace("Modified response: ==> {}", finalLine);
						return Mono.just(finalLine);
					})
					.map(str -> {
						byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
						return new DefaultDataBufferFactory().wrap(bytes);
					});
				ClientResponse modifiedResponse = ClientResponse.from(clientResponse)
					.headers(headers -> headers.remove(HttpHeaders.CONTENT_LENGTH))
					.body(modifiedBody)
					.build();
				return Mono.just(modifiedResponse);
			});
			webClientBuilder.filter(requestFilter).filter(responseFilter);
		};
	}
}

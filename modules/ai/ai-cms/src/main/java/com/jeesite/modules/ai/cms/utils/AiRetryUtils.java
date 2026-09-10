/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.utils;

import com.jeesite.modules.ai.cms.properties.AiCmsProperties;
import com.openai.errors.OpenAIIoException;
import com.openai.errors.OpenAIServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * AI 模型调用重试工具类，所有模型（对话、生图、向量等）统一使用，且为唯一的重试入口。
 * <p>
 * 重试参数使用 {@link AiCmsProperties#getAutoRetry()}（{@code spring.ai.autoRetry.*}），
 * 不复用 Spring AI 的 {@code spring.ai.retry.*} —— 否则会与 Spring AI 自动装配在
 * ChatClient/ImageModel 上的内置重试叠加为“双重重试”，导致一次限流（429）被多次调用、
 * 正常返回的调用也被反复重试。
 * <p>
 * 仅对临时性错误重试：模型限流（429）、服务端错误（5xx）、网络超时（InterruptedIOException）/IO（OpenAIIoException）。
 * 正常返回（无论同步还是流式）都不会重试。
 *
 * @author ThinkGem
 */
@Component
public class AiRetryUtils {

	private static final Logger logger = LoggerFactory.getLogger(AiRetryUtils.class);

	private final AiCmsProperties.AutoRetry retryProperties;

	public AiRetryUtils(AiCmsProperties properties) {
		this.retryProperties = properties.getAutoRetry();
	}

	/**
	 * 流式调用重试（聊天流输出等）
	 * <p>
	 * 已经向下游输出过数据时不再重试，避免客户端出现重复内容；
	 * 限流类错误都是流开始前就返回的，因此可以安全重试。
	 * @param flux 模型流式响应
	 */
	public <T> Flux<T> retry(Flux<T> flux) {
		if (getMaxAttempts() <= 1) {
			return flux;
		}
		AtomicBoolean emitted = new AtomicBoolean(false);
		return flux.doOnNext(value -> emitted.set(true))
				.retryWhen(buildRetry(error -> !emitted.get() && isRetryable(error)));
	}

	/**
	 * 同步调用重试（聊天文本、结构化输出、生图等 call() 场景），简单调用示例：
	 * <pre>
	 * String text = aiRetryUtils.execute(() -&gt; chatClient.prompt(message).call().content());
	 * </pre>
	 * <p>
	 * 采用同步循环重试：成功即返回，绝不会为“正常返回”的调用再次发起请求；
	 * 仅当抛出可重试的临时错误且未达最大次数时才退避后重试。
	 * @param supplier 模型调用
	 */
	public <T> T execute(Supplier<T> supplier) {
		int maxAttempts = getMaxAttempts();
		if (maxAttempts <= 1) {
			return supplier.get();
		}
		AiCmsProperties.AutoRetry.Backoff backoff = retryProperties.getBackoff();
		long interval = backoff.getInitialInterval().toMillis();
		long maxInterval = backoff.getMaxInterval().toMillis();
		double multiplier = backoff.getMultiplier();
		RuntimeException last = null;
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				return supplier.get();
			} catch (RuntimeException ex) {
				last = ex;
				// 已达最大次数，或错误不可重试，直接抛出（不再重试）
				if (attempt >= maxAttempts || !isRetryable(ex)) {
					throw ex;
				}
				if (logger.isWarnEnabled()) {
					logger.warn("AI model call retry {} : {}", attempt, ex.getMessage());
				}
				try {
					Thread.sleep(interval);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw ex;
				}
				// 指数退避，不超过上限
				interval = Math.min((long) (interval * multiplier), maxInterval);
			}
		}
		throw last;
	}

	/**
	 * 构建指数退避重试策略，参数取自 spring.ai.autoRetry 配置
	 */
	private Retry buildRetry(Predicate<Throwable> filter) {
		AiCmsProperties.AutoRetry.Backoff backoff = retryProperties.getBackoff();
		return Retry.backoff(getMaxAttempts(), backoff.getInitialInterval())
				.multiplier(backoff.getMultiplier())
				.maxBackoff(backoff.getMaxInterval())
				.filter(filter)
				.doBeforeRetry(retrySignal -> {
					if (logger.isWarnEnabled()) {
						logger.warn("AI model call retry {} : {}", retrySignal.totalRetries(),
								retrySignal.failure().getMessage());
					}
				});
	}

	private int getMaxAttempts() {
		return retryProperties != null ? retryProperties.getMaxAttempts() : 1;
	}

	/**
	 * 是否为可重试的临时性错误：模型限流（429）、服务端错误（5xx）、网络超时/IO 等。
	 * 注意 SDK 抛出的异常可能被 CompletionException 等包装，需要遍历异常链判断。
	 * @author ThinkGem
	 */
	public static boolean isRetryable(Throwable error) {
		Throwable cause = error;
		while (cause != null) {
			if (cause instanceof OpenAIServiceException serviceException) {
				int statusCode = serviceException.statusCode();
				return statusCode == 429 || statusCode >= 500;
			}
			if (cause instanceof WebClientResponseException responseException) {
				int statusCode = responseException.getStatusCode().value();
				return statusCode == 429 || statusCode >= 500;
			}
			if (cause instanceof OpenAIIoException) {
				// 网络/IO 类错误（含 okhttp 超时、连接被取消），属临时性故障，可重试
				return true;
			}
			if (cause instanceof IOException) {
				// 兜底：底层网络 IO 异常（InterruptedIOException 超时、IOException: Canceled 等），可重试
				return true;
			}
			cause = cause.getCause();
		}
		return false;
	}

}

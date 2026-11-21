/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.web;

import com.jeesite.common.config.Global;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 异步任务池配置
 * <p>
 * 流式输出（如 AI 对话的 SSE）、{@code DeferredResult}、{@code Callable} 等异步请求统一使用本任务池，
 * 避免 Spring MVC 回退到默认的 {@code SimpleAsyncTaskExecutor}（每个请求新建线程、无上限，
 * 生产环境高并发下不可用，启动或首次使用时输出 "Performing asynchronous handling through the
 * default Spring MVC SimpleAsyncTaskExecutor" 警告）。
 * <p>
 * 参数可在 application.yml 中调整：
 * <ul>
 *   <li>{@code web.taskPool.corePoolSize}：核心线程数，默认 8</li>
 *   <li>{@code web.taskPool.maxPoolSize}：最大线程数，默认 20</li>
 *   <li>{@code web.taskPool.keepAliveSeconds}：空闲线程存活秒数，默认 60</li>
 *   <li>{@code web.taskPool.queueCapacity}：队列容量，默认 1000</li>
 * </ul>
 * 注意：异步请求默认超时（{@code AsyncSupportConfigurer#setDefaultTimeout}）未改动，
 * 保持各容器的原有行为，避免影响长连接的流式输出。
 * @author ThinkGem
 * @version 2026-11-21
 */
@Configuration
public class WebMvcAsyncConfig implements WebMvcConfigurer {

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(webMvcAsyncTaskExecutor());
	}

	@Bean
	public ThreadPoolTaskExecutor webMvcAsyncTaskExecutor() {
		ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
		bean.setCorePoolSize(Global.getPropertyToInteger("web.taskPool.corePoolSize", "8"));
		bean.setMaxPoolSize(Global.getPropertyToInteger("web.taskPool.maxPoolSize", "20"));
		bean.setKeepAliveSeconds(Global.getPropertyToInteger("web.taskPool.keepAliveSeconds", "60"));
		bean.setQueueCapacity(Global.getPropertyToInteger("web.taskPool.queueCapacity", "1000"));
		bean.setThreadNamePrefix("web-async-");
		return bean;
	}

}

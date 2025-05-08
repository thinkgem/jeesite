/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.config;

import com.jeesite.common.config.Global;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 异步任务池定义
 * @author ThinkGem
 */
@Configuration
public class CmsAiWebMvcConfig implements WebMvcConfigurer {

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
		bean.setQueueCapacity(Global.getPropertyToInteger("web.taskPool.queueCapacity", String.valueOf(Integer.MAX_VALUE)));
        bean.setThreadNamePrefix("web-async-");
        return bean;
    }
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Listener 配置
 * @author ThinkGem
 * @version 2017年11月29日
 */
@Configuration
public class ListenerConfig {
	
	/**
	 * Request Context Listener
	 */
	@Bean
	@Order(1000)
	public ServletListenerRegistrationBean<RequestContextListener> requestContextListener() {
		ServletListenerRegistrationBean<RequestContextListener> bean = new ServletListenerRegistrationBean<>();
		bean.setListener(new RequestContextListener());
		return bean;
	}

}

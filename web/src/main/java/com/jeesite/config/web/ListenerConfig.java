/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.config.web;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import com.jeesite.common.shiro.cas.CasOutSessionListener;

/**
 * Listener 配置
 * @author ThinkGem
 * @version 2017年11月29日
 */
@Configuration
public class ListenerConfig {

	/**
	 * CAS Session Listener
	 */
	@Bean
	public ServletListenerRegistrationBean<CasOutSessionListener> casOutSessionListener() {
		ServletListenerRegistrationBean<CasOutSessionListener> bean = new ServletListenerRegistrationBean<>();
		bean.setListener(new CasOutSessionListener());
		bean.setOrder(1000);
		return bean;
	}

	/**
	 * Request Context Listener
	 */
	@Bean
	public ServletListenerRegistrationBean<RequestContextListener> requestContextListener() {
		ServletListenerRegistrationBean<RequestContextListener> bean = new ServletListenerRegistrationBean<>();
		bean.setListener(new RequestContextListener());
		bean.setOrder(2000);
		return bean;
	}

}

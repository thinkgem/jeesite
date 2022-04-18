/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 是否启用默认 Servlet 映射（启用后可访问 webapp 下的静态资源）
 * @author ThinkGem
 * @version 2022年4月18日
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name="server.servlet.register-default-servlet", havingValue="true", matchIfMissing=false)
public class DefaultServletConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config;

import org.springframework.context.annotation.Configuration;

/**
 * 网关跨域过滤器
 * @author ThinkGem
 */
@Configuration(proxyBeanMethods = false)
public class WebmvcCorsFilter {
	
//	@Bean
//	public FilterRegistrationBean<Filter> crossFilter() {
//		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//		bean.setName("WebmvcCorsFilter");
//		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		bean.setFilter((servletRequest, servletResponse, chain) -> {
//			HttpServletRequest request = (HttpServletRequest) servletRequest;
//			HttpServletResponse response = (HttpServletResponse) servletResponse;
//			String originHeader = request.getHeader("Origin");
//			if (originHeader != null) {
//				response.setHeader("Access-Control-Allow-Origin", originHeader);
//				response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//				response.setHeader("Access-Control-Allow-Headers", "content-type, x-requested-with, __ajax, __sid, x-remember");
//			}
//			if (HttpMethod.OPTIONS.matches(request.getMethod())){
//				return;
//			}
//			chain.doFilter(request, response);
//		});
//		bean.addUrlPatterns("/*");
//		return bean;
//	}

}

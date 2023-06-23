/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.web;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 将请求协议转换为 https
 * @author ThinkGem
 * @version 2020年1月21日
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name="server.schemeHttps", havingValue="true", matchIfMissing=false)
public class SchemeHttpsConfig {

	@Bean
	public FilterRegistrationBean<Filter> schemeFilterRegistrationBean() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		bean.setFilter(new Filter() {
			
			@Override
			public void init(FilterConfig filterConfig) throws ServletException {}

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
				chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
					
					@Override
					public String getScheme() {
						return "https";
					}

					@Override
					public StringBuffer getRequestURL() {
						StringBuffer sb = super.getRequestURL();
						if ("http:".equals(sb.substring(0, 5))){
							return sb.replace(0, 5, "https:");
						}else{
							return sb;
						}
					}
					
				}, response);
			}

			@Override
			public void destroy() {}
		});
		bean.addUrlPatterns("/*");
		return bean;
	}

}

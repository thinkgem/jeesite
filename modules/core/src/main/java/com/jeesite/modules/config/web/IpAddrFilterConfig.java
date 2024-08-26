/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * IP地址黑白名单过滤器配置
 */
@Configuration(proxyBeanMethods = false)
public class IpAddrFilterConfig {

	private static long clearCacheTime;
	private static String[] allowPrefixes;
	private static String[] denyPrefixes;

	@Bean
	public FilterRegistrationBean<Filter> ipAddrFilter() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setName("ipAddrFilter");
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
		bean.setFilter((servletRequest, servletResponse, chain) -> {
			if (isAccessAllowed(servletRequest, servletResponse)) {
				chain.doFilter(servletRequest, servletResponse);
			} else {
				HttpServletResponse response = (HttpServletResponse) servletResponse;
				response.setStatus(403);
				ServletUtils.renderString(response, Global.getText("访问拒绝"));
			}
		});
		bean.addUrlPatterns("/*");
		return bean;
	}

	private boolean isAccessAllowed(ServletRequest request, ServletResponse response) {
		if (clearCacheTime == 0 || clearCacheTime != Global.getClearCacheTime()) {
			allowPrefixes = Global.getConfigToArray("sys.filter.allowIpAddrs", StringUtils.EMPTY);
			denyPrefixes = Global.getConfigToArray("sys.filter.denyIpAddrs", StringUtils.EMPTY);
			clearCacheTime = Global.getClearCacheTime();
		}
		// 如果未初始化，直接拒绝
		if (allowPrefixes == null || denyPrefixes == null) {
			return false;
		}
		// 如果未设置黑白名单，直接通过
		if (allowPrefixes.length == 0 && denyPrefixes.length == 0) {
			return true;
		}
		// 如果未设置白名单，则直接通过白名单，再从黑名单中检查
		boolean result = allowPrefixes.length == 0;
		String ip = request.getRemoteAddr() + "]";
		for (String prefix : allowPrefixes) {
			if (StringUtils.startsWithIgnoreCase(ip, StringUtils.trim(prefix))){
				result = true;
				break;
			}
		}
		for (String prefix : denyPrefixes) {
			if (StringUtils.startsWithIgnoreCase(ip, StringUtils.trim(prefix))){
				result = false;
				break;
			}
		}
		if (result) {
			return true;
		}
		return false;
	}
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.LocaleUtils;
import com.jeesite.common.web.http.ServletUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.LocaleContextResolver;

/**
 * 全局过滤器：
 * 1、IP地址黑白名单过滤器配置
 * 2、本地化时区上下文设置
 * 3、Slf4j MDC 标识设置
 */
@Configuration(proxyBeanMethods = false)
public class GlobalFilterConfig {

	private static final String TRACE_ID = "TRACE_ID";
	private static long clearCacheTime;
	private static String[] allowPrefixes;
	private static String[] denyPrefixes;

	@Bean
	public FilterRegistrationBean<Filter> jeesiteGlobalFilter(LocaleContextResolver localeResolver) {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setName("jeesiteGlobalFilter");
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
		bean.setFilter((servletRequest, servletResponse, chain) -> {
			if (StringUtils.isBlank(MDC.get(TRACE_ID))) {
				MDC.put(TRACE_ID, IdGen.randomShortString());
			}
			if (isAccessAllowed(servletRequest, servletResponse)) {
				chain.doFilter(servletRequest, servletResponse);
			} else {
				HttpServletResponse response = (HttpServletResponse) servletResponse;
				response.setStatus(403);
				ServletUtils.renderString(response, Global.getText("访问拒绝"));
			}
			LocaleUtils.removeTimeZoneAwareLocaleContext();
			MDC.remove(TRACE_ID);
		});
		LocaleUtils.setLocaleResolver(localeResolver);
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
		return result;
	}
}

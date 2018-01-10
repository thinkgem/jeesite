/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.config.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.RequestContextFilter;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.PageCachingFilter;

/**
 * Filter 配置
 * @author ThinkGem
 * @version 2017年11月30日
 */
@Configuration
public class FilterConfig {

	/**
	 * Encoding Filter
	 */
	@Bean
	public FilterRegistrationBean characterEncodingFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new CharacterEncodingFilter());
		bean.addInitParameter("encoding", "UTF-8");
		bean.addInitParameter("forceEncoding", "true");
		bean.addUrlPatterns("/*");
		bean.setOrder(1000);
		return bean;
	}

	/**
	 * PageCache Filter, cache .html suffix.
	 */
	@Bean
	@ConditionalOnProperty(name = "ehcache.pageCaching.enabled", havingValue = "true")
	public FilterRegistrationBean pageCachingFilter(EhCacheManagerFactoryBean ehCacheManager) {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		PageCachingFilter pageCachingFilter = new PageCachingFilter();
		pageCachingFilter.setCacheManager(ehCacheManager.getObject());
		bean.setFilter(pageCachingFilter);
		bean.addInitParameter("cacheName", "pageCachingFilter");
		bean.addUrlPatterns(StringUtils.split(Global.getProperty(
				"ehcache.pageCaching.urlPatterns"), ","));
		bean.setOrder(2000);
		return bean;
	}

	/**
	 * Apache Shiro Filter
	 */
	@Bean
	public FilterRegistrationBean shiroFilterProxy() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new DelegatingFilterProxy("shiroFilter"));
		bean.addInitParameter("targetFilterLifecycle", "true");
		bean.addUrlPatterns("/*");
		bean.setOrder(3000);
		return bean;
	}

	/**
	 * Request Context Filter 需要放在shiroFilter后，否则request获取不到session
	 */
	@Bean
	public FilterRegistrationBean requestContextFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new RequestContextFilter());
		bean.addUrlPatterns("/*");
		bean.setOrder(4000);
		return bean;
	}

}

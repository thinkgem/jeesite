/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web;

import javax.servlet.Filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.jeesite.common.config.Global;
import com.jeesite.common.shiro.web.ShiroFilterFactoryBean;
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
	@Order(1000)
	public FilterRegistrationBean characterEncodingFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new CharacterEncodingFilter());
		bean.addInitParameter("encoding", "UTF-8");
		bean.addInitParameter("forceEncoding", "true");
		bean.addUrlPatterns("/*");
		return bean;
	}

	/**
	 * PageCache Filter, cache .html suffix.
	 */
	@Bean
	@Order(2000)
	@ConditionalOnProperty(name = "ehcache.pageCaching.enabled", havingValue = "true")
	public FilterRegistrationBean pageCachingFilter(EhCacheManagerFactoryBean ehCacheManager) {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		PageCachingFilter pageCachingFilter = new PageCachingFilter();
		pageCachingFilter.setCacheManager(ehCacheManager.getObject());
		bean.setFilter(pageCachingFilter);
		bean.addInitParameter("cacheName", "pageCachingFilter");
		bean.addUrlPatterns(StringUtils.split(Global.getProperty(
				"ehcache.pageCaching.urlPatterns"), ","));
		return bean;
	}

	/**
	 * Apache Shiro Filter
	 * @throws Exception 
	 */
	@Bean
	@Order(3000)
	public FilterRegistrationBean shiroFilterProxy(ShiroFilterFactoryBean shiroFilter) throws Exception {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter((Filter) shiroFilter.getInstance());
		bean.addUrlPatterns("/*");
		return bean;
	}
	
}

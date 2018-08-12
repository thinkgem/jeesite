/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.PageCachingFilter;

/**
 * Filter 配置
 * @author ThinkGem
 * @version 2017年11月30日
 */
@Configuration
public class PageCacheConfig {
	
	/**
	 * PageCache Filter, cache .html suffix.
	 */
	@Bean
	@Order(2000)
	@ConditionalOnProperty(name = "ehcache.pageCaching.enabled", havingValue = "true")
	@ConditionalOnMissingBean(name="pageCachingFilter")
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
	
}

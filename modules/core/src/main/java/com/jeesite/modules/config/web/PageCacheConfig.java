/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web;

/**
 * 页面缓存，如果需要，则加入如下依赖并取消下面注释

1、pom.xml:
<dependency>
	<groupId>net.sf.ehcache</groupId>  
	<artifactId>ehcache-web</artifactId>  
	<version>2.0.4</version>
</dependency>

2、application.yml:
# 页面缓存配置
ehcache:
  pageCaching:
    enabled: false
    urlPatterns: "*.html"
    
 * @author ThinkGem
 * @version 2017年11月30日
 */
//@Configuration
public class PageCacheConfig {

//	/**
//	 * PageCache Filter, cache .html suffix.
//	 */
//	@Bean
//	@Order(2000)
//	@ConditionalOnProperty(name = "ehcache.pageCaching.enabled", havingValue = "true")
//	@ConditionalOnMissingBean(name="pageCachingFilter")
//	public FilterRegistrationBean<PageCachingFilter> pageCachingFilter(EhCacheManagerFactoryBean ehCacheManager) {
//		FilterRegistrationBean<PageCachingFilter> bean = new FilterRegistrationBean<>();
//		SimplePageCachingFilter pageCachingFilter = new SimplePageCachingFilter();
//		pageCachingFilter.setCacheManager(ehCacheManager.getObject());
//		bean.setFilter(pageCachingFilter);
//		bean.addInitParameter("cacheName", "pageCachingFilter");
//		bean.addUrlPatterns(StringUtils.split(Global.getProperty(
//				"ehcache.pageCaching.urlPatterns"), ","));
//		return bean;
//	}
	
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Druid 配置
 * @author ThinkGem
 * @version 2017年11月30日
 */
@Configuration
@ConditionalOnProperty(name="state.enabled", havingValue="true", matchIfMissing=true)
public class DruidStatConfig {

	/**
	 * 注册DruidFilter拦截
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> duridFilter() {
		FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new WebStatFilter());
		bean.addInitParameter("exclusions", "*.css,*.js,*.png,"
				+ "*.jpg,*.gif,*.jpeg,*.bmp,*.ico,*.swf,*.psd,*.htc,*.htm,*.html,"
				+ "*.crx,*.xpi,*.exe,*.ipa,*.apk,*.otf,*.eot,*.svg,*.ttf,*.woff,"
				+ "/druid/*");
		bean.addUrlPatterns("/*");
		return bean;
	}
	
	/**
	 * 注册DruidServlet
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet() {
		ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>();
		bean.setServlet(new StatViewServlet());
		bean.addUrlMappings("/druid/*");
		return bean;
	}
	
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.config.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Servlet 配置
 * @author ThinkGem
 * @version 2017年11月30日
 */
@Configuration
public class DruidStatConfig {

	/**
	 * 注册DruidFilter拦截
	 */
	@Bean
	public FilterRegistrationBean duridFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
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
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean bean = new ServletRegistrationBean();
		bean.setServlet(new StatViewServlet());
		bean.addUrlMappings("/druid/*");
		return bean;
	}
	
}

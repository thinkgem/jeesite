/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config.web;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.Utils;
import com.jeesite.common.web.http.ServletUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Druid 配置
 * @author ThinkGem
 * @version 2017年11月30日
 */
@Configuration(proxyBeanMethods = false)
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
		bean.addInitParameter("sessionStatEnable", "false");
		bean.addUrlPatterns("/*");
		return bean;
	}
	
	/**
	 * 注册DruidServlet
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet() {
		ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>();
		bean.setServlet(new StatViewServlet(){
			@Override
			public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				String requestURI = request.getRequestURI();
				if (requestURI.endsWith("/druid/js/common.js")) {
					String text = Utils.readFromResource("support/http/resources/js/common.js");
					text = text.replaceAll("<a.*?banner\"></a><br/>", "JeeSite 数据监控, ");
					ServletUtils.renderString(response, text, "text/javascript;charset=UTF-8");
					return;
				}
				super.service(request, response);
			}
		});
		bean.addUrlMappings("/druid/*");
		return bean;
	}
	
}

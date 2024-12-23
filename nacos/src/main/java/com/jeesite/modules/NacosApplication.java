/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.alibaba.nacos.core.listener.StartingApplicationListener;
import com.alibaba.nacos.sys.filter.NacosTypeExcludeFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Nacos Application
 * @author ThinkGem
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.alibaba.nacos", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {NacosTypeExcludeFilter.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {TypeExcludeFilter.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {AutoConfigurationExcludeFilter.class})})
@ServletComponentScan
@EnableScheduling
public class NacosApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(StartingApplicationListener.class);

	private static void initialize() {
		System.setProperty("nacos.standalone", "true");
		if (StringUtils.isBlank(System.getProperty("nacos.home"))) {
			System.setProperty("nacos.home", System.getProperty("user.home") + "/nacos5boot2");
		}
		System.setProperty("nacos.logs.path", System.getProperty("nacos.home") + "/logs");
		System.setProperty("derby.stream.error.file", System.getProperty("nacos.home") + "/.derby.log");
	}

	public static void main(String[] args) {
		NacosApplication.initialize();
	    new SpringApplicationBuilder(NacosApplication.class).run(args);
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   " + NacosApplication.class.getName() + " 启动完成。"
				+ "\r\n\r\n==============================================================\r\n");
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		NacosApplication.initialize();
		super.onStartup(servletContext);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(NacosApplication.class);
	}

}
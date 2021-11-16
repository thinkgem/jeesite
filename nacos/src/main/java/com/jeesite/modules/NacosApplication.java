/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Nacos Application
 * @author ThinkGem
 * @version 2021-8-28
 */
@SpringBootApplication(scanBasePackages = "com.alibaba.nacos")
@ServletComponentScan
@EnableScheduling
public class NacosApplication extends SpringBootServletInitializer {
	
	private static void initialize() {
		System.setProperty("nacos.standalone", "true");
		System.setProperty("derby.stream.error.file",".derby.log");
	}
	
	public static void main(String[] args) {
		NacosApplication.initialize();
	    new SpringApplicationBuilder(NacosApplication.class).run(args);
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
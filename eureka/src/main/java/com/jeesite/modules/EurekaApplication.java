/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Application
 * @author ThinkGem
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(EurekaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
		logger.info(
				"\n\n==============================================================\n"
				+ "\n   " + EurekaApplication.class.getName() + " 启动完成。\n"
				+ "\n==============================================================\n");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(EurekaApplication.class);
	}
	
}
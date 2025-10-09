/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.jeesite.modules.cloud.feign.EnableFeignClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Application
 * @author ThinkGem
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.jeesite.modules"})
public class FilesApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(FilesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FilesApplication.class, args);
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   " + FilesApplication.class.getName() + " 启动完成。"
				+ "\r\n\r\n==============================================================\r\n");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(FilesApplication.class);
	}
	
}
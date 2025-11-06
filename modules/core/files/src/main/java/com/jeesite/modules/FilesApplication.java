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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

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
		ConfigurableApplicationContext context = SpringApplication.run(FilesApplication.class, args);
		Environment env = context.getEnvironment();
		logger.info(
				"\n\n==============================================================\n"
				+ "\n   启动完成，访问地址：http://127.0.0.1:{}{}\n"
				+ "\n==============================================================\n",
				env.getProperty("local.server.port"), env.getProperty("server.servlet.context-path"));
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(FilesApplication.class);
	}
	
}
/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Gateway Application
 * @author ThinkGem
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
		Environment env = context.getEnvironment();
		logger.info(
				"\n\n==============================================================\n"
				+ "\n   启动完成，访问地址：http://127.0.0.1:{}{}\n"
				+ "\n==============================================================\n",
				env.getProperty("local.server.port"), env.getProperty("server.servlet.context-path"));
	}
	
}
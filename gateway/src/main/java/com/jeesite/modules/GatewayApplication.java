/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway Application
 * @author ThinkGem
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   " + GatewayApplication.class.getName() + " 启动完成。"
				+ "\r\n\r\n==============================================================\r\n");
	}
	
}
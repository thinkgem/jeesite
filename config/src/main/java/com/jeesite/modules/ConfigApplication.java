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
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Application
 * @author ThinkGem
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigApplication {

	private static final Logger logger = LoggerFactory.getLogger(ConfigApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   " + ConfigApplication.class.getName() + " 启动完成。"
				+ "\r\n\r\n==============================================================\r\n");
	}

}
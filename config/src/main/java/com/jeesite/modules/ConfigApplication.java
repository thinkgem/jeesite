/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Application
 * @author ThinkGem
 * @version 2018-10-13
 */
@EnableConfigServer
@SpringCloudApplication
public class ConfigApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}
	
}
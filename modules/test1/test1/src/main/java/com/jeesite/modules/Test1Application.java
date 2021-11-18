/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application
 * @author ThinkGem
 * @version 2018-10-13
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages={"com.jeesite.modules"})
public class Test1Application extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(Test1Application.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(Test1Application.class);
	}
	
}
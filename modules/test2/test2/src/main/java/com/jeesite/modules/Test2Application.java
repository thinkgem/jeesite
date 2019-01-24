/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application
 * @author ThinkGem
 * @version 2018-10-13
 */
@SpringCloudApplication
@EnableFeignClients(basePackages={"com.jeesite.modules"})
public class Test2Application extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(Test2Application.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(Test2Application.class);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Web Application
 * @author ThinkGem
 * @version 2018-9-9
 */
@SpringBootApplication(scanBasePackages={"com.jeesite.modules"})
public class Application extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(Application.class);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.io.PropertiesUtils;

/**
 * JeeSite Web
 * @author ThinkGem
 * @version 2018-1-8
 */
@ActiveProfiles("test")
@SpringBootApplication
public class ApplicationTest {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ApplicationTest.class);
		app.setDefaultProperties(PropertiesUtils.getInstance().getProperties());
		app.run(args);
	}
	
}
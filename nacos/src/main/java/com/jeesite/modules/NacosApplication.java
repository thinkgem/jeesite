/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Nacos Application
 * @author ThinkGem
 * @version 2021-6-20
 */
@SpringBootApplication(scanBasePackages = "com.alibaba.nacos")
@ServletComponentScan
@EnableScheduling
public class NacosApplication {

	public static void main(String[] args) {
		System.setProperty("nacos.standalone", "true");
		System.setProperty("derby.stream.error.file",".derby.log");
	    new SpringApplicationBuilder(NacosApplication.class).run(args);
	}

}
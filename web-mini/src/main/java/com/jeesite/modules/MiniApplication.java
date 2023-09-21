/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Application
 * @author ThinkGem
 */
@SpringBootApplication
public class MiniApplication extends SpringBootServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(MiniApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MiniApplication.class, args);
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   启动完成，系统监控地址：http://127.0.0.1:"
				+ Global.getProperty("server.port") + FileUtils.path("/"
				+ Global.getProperty("server.servlet.context-path")
				+ Global.getAdminPath()) + "state/server/index"
				+ "\r\n\r\n==============================================================\r\n");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(MiniApplication.class);
	}
	
}
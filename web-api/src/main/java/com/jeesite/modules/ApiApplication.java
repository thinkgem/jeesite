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
public class ApiApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(ApiApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		logger.info(
				"\n\n==============================================================\n"
				+ "\n   启动完成，接口地址：http://127.0.0.1:{}\n"
				+ "\n   默认管理账号： system   密码： admin\n"
				+ "\n==============================================================\n",
				Global.getProperty("server.port") + FileUtils.path("/"
				+ Global.getProperty("server.servlet.context-path")));
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(ApiApplication.class);
	}
	
}
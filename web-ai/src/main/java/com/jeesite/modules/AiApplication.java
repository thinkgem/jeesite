/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.StringUtils;
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
public class AiApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(AiApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
		String vuePath = Global.getProperty("vuePath");
		String ctxPath = Global.getProperty("server.servlet.context-path");
		if (StringUtils.isNoneBlank(vuePath) && !StringUtils.equals(ctxPath, "/js")) {
			logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   提示：您修改了 server.servlet.context-path 参数，需要您"
				+ "\r\n   同步修改 _app.config.js 中的 VITE_GLOB_API_URL_PREFIX 参数 "
				+ "\r\n   请修改为 VITE_GLOB_API_URL_PREFIX=\"" + ctxPath + "\""
				+ "\r\n\r\n==============================================================\r\n");
		}
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   启动完成，访问地址：http://127.0.0.1:"
				+ Global.getProperty("server.port") + FileUtils.path("/"
				+ Global.getProperty("server.servlet.context-path"))
				+ "\r\n\r\n   默认管理账号： system   密码： admin"
				+ "\r\n\r\n==============================================================\r\n");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(AiApplication.class);
	}
	
}
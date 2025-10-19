/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.UnknownHostException;

/**
 * Application
 * @author ThinkGem
 */
@SpringBootApplication
public class FastApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(FastApplication.class);

	public static void setJeeSiteInitDataProperty() {
		// 删除这个设置，启动系统时，将不会进行检测初始化数据库
		System.setProperty("jeesite.initdata", "true");
	}
	
	public static void main(String[] args) throws UnknownHostException {
		FastApplication.setJeeSiteInitDataProperty();
		SpringApplication.run(FastApplication.class, args);
		String vuePath = Global.getProperty("vuePath");
		String ctxPath = Global.getProperty("server.servlet.context-path");
		if (StringUtils.isNoneBlank(vuePath, ctxPath)) {
			logger.info(
				"\n\n==============================================================\n"
				+ "\n   提示：您修改了 server.servlet.context-path 参数，需要您"
				+ "\n   同步修改 _app.config.js 中的 VITE_GLOB_API_URL_PREFIX 参数 "
				+ "\n   请修改为 VITE_GLOB_API_URL_PREFIX=\"{}\" 并重新打包 Vue\n"
				+ "\n==============================================================\n",
				ctxPath);
		}
		logger.info(
				"\n\n==============================================================\n"
				+ "\n   启动完成，访问地址：http://127.0.0.1:{}\n"
				+ "\n   默认管理账号： system   密码： admin\n"
				+ "\n==============================================================\n",
				Global.getProperty("server.port") + FileUtils.path("/"
				+ Global.getProperty("server.servlet.context-path")));
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		FastApplication.setJeeSiteInitDataProperty();
		return builder.sources(FastApplication.class);
	}

	@Controller
	@ConditionalOnProperty(name = "server.servlet.context-path", havingValue = "")
	public static class JeeSiteController extends BaseController {

		@RequestMapping(value = "/js/**")
		public String login() {
			return REDIRECT + Global.getProperty("defaultPath");
		}

	}
	
}
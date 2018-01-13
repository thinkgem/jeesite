/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jeesite.common.config.Global;
import com.jeesite.modules.sys.interceptor.LogInterceptor;

/**
 * 后台管理日志记录拦截器
 * @author ThinkGem
 * @version 2018年1月10日
 */
@Configuration
@EnableWebMvc
@ConditionalOnProperty(name="web.interceptor.log.enabled", havingValue="true", matchIfMissing=true)
public class LogInterceptorConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
			.addPathPatterns(Global.getAdminPath() + "/**")
			.excludePathPatterns(Global.getAdminPath() + "/index")
			.excludePathPatterns(Global.getAdminPath() + "/login")
			.excludePathPatterns(Global.getAdminPath() + "/**/listData")
			.excludePathPatterns(Global.getAdminPath() + "/**/treeData")
			.excludePathPatterns(Global.getAdminPath() + "/file/**")
			.excludePathPatterns(Global.getAdminPath() + "/tags/**")
			.excludePathPatterns(Global.getAdminPath() + "/sys/log/**")
			.excludePathPatterns(Global.getAdminPath() + "/sys/online/count")
			;
	}

}
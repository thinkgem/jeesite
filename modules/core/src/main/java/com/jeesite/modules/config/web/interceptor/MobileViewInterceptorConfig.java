/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.interceptor.MobileInterceptor;

/**
 * 前台自动切换到手机视图拦截器
 * @author ThinkGem
 * @version 2018年1月10日
 */
@Configuration
@EnableWebMvc
@ConditionalOnProperty(name="web.interceptor.mobile.enabled", havingValue="true", matchIfMissing=false)
public class MobileViewInterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(new MobileInterceptor());
		String apps = Global.getProperty("web.interceptor.mobile.addPathPatterns");
		String epps = Global.getProperty("web.interceptor.mobile.excludePathPatterns");
		for (String uri : StringUtils.split(apps, ",")){
			if (StringUtils.isNotBlank(uri)){
				registration.addPathPatterns(StringUtils.trim(uri));
			}
		}
		for (String uri : StringUtils.split(epps, ",")){
			if (StringUtils.isNotBlank(uri)){
				registration.excludePathPatterns(StringUtils.trim(uri));
			}
		}
	}

}
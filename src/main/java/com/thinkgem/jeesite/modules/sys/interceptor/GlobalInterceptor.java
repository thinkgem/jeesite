/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统拦截器
 * @author ThinkGem
 * @version 2013-01-15
 */
public class GlobalInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);
			
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String uri = request.getRequestURI();
		String uriPrefix = request.getContextPath() + BaseController.ADMIN_PATH;
		// 拦截所有来自管理端的POST请求
		if ("POST".equals(request.getMethod()) && uri.length() > uriPrefix.length()){
			User user = UserUtils.getUser();
			if (user!=null){
				StringBuilder sb = new StringBuilder();
				sb.append("url: (" + request.getMethod() + ") " + uri);
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					sb.append((index++==0?"?":"&")+param+"=");
					sb.append(StringUtils.abbreviate(request.getParameter((String)param),100));
				}
				sb.append("; userId: " + user.getId());
				sb.append("; userName: " + user.getName());
				sb.append("; loginName: " + user.getLoginName());
				sb.append("; ipAddr: " + request.getLocalAddr());
				sb.append("; datetime: " + DateUtils.getDateTime());
				sb.append("; handler: " + handler.toString());
				logger.info(sb.toString());
			}
		}
	}

}

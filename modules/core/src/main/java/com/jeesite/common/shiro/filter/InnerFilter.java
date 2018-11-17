/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;

/**
 * 内部系统访问过滤器
 * @author ThinkGem
 * @version 2018-11-10
 */
public class InnerFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean result = false;
		String[] prefixes = (String[])mappedValue;
		if (prefixes == null){
			prefixes = StringUtils.split(Global.getProperty(
					"shiro.innerFilterAllowRemoteAddrs", "127.0.0.1"), ",");
		}
		if (prefixes != null && request instanceof HttpServletRequest){
			String ip = ((HttpServletRequest)request).getRemoteAddr();
			for (String prefix : prefixes){
				result = StringUtils.startsWithIgnoreCase(ip, StringUtils.trim(prefix));
				if (result){
					break;
				}
			}
		}
		return result;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return PermissionsAuthorizationFilter.redirectTo403Page(request, response);
	}
	
}

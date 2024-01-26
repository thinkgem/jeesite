/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 内部系统访问过滤器
 * @author ThinkGem
 * @version 2018-11-10
 */
public class InnerFilter extends AccessControlFilter {

	private static final String[] prefixes = Global.getPropertyToArray("shiro.innerFilterAllowRemoteAddrs", "127.0.0.1");

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean result = false;
		String[] prefixes = (String[])mappedValue;
		if (prefixes == null){
			prefixes = InnerFilter.prefixes;
		}
		if (prefixes != null && request instanceof HttpServletRequest){
			String ip = request.getRemoteAddr() + "]";
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
		return PermissionsFilter.redirectTo403Page(request, response);
	}
	
}

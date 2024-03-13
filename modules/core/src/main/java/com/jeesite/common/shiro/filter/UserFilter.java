/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 用户权限过滤器
 * @author ThinkGem
 * @version 2017-03-22
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

    private String sessionIdHeaderName;

	public UserFilter () {
        this.setSessionIdHeaderName(Global.getProperty("session.sessionIdHeaderName", "x-token"));
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		String sessionId = (String)request.getAttribute(getSessionIdHeaderName());
		if (StringUtils.isNotBlank(sessionId)) {
			((HttpServletResponse)response).setHeader(getSessionIdHeaderName(), sessionId);
		}
		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return PermissionsFilter.redirectTo403Page(request, response);
    }

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsFilter.redirectToDefaultPath(request, response);
	}

	public String getSessionIdHeaderName() {
		return sessionIdHeaderName;
	}

	public void setSessionIdHeaderName(String sessionIdHeaderName) {
		this.sessionIdHeaderName = sessionIdHeaderName;
	}
}

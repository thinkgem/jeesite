/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 用户权限过滤器
 * @author ThinkGem
 * @version 2017-03-22
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsAuthorizationFilter.redirectToDefaultPath(request, response);
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return PermissionsAuthorizationFilter.redirectTo403Page(request, response);
    }
	
}

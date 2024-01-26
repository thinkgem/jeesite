/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 用户权限过滤器
 * @author ThinkGem
 * @version 2017-03-22
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return PermissionsFilter.redirectTo403Page(request, response);
    }

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsFilter.redirectToDefaultPath(request, response);
	}
	
}

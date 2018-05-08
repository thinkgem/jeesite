/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.common.web.http.wrapper.GetHttpServletRequestWrapper;

/**
 * 权限字符串过滤器
 * @author ThinkGem
 * @version 2017-03-22
 */
public class PermissionsAuthorizationFilter extends org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter {

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsAuthorizationFilter.redirectToDefaultPath(request, response);
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return PermissionsAuthorizationFilter.redirectTo403Page(request, response);
    }
	
	/**
	 * 无访问权限时，跳转到403页面
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @author ThinkGem
	 */
	public static boolean redirectTo403Page(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
        	redirectToDefaultPath(request, response);
        } else {
        	try {
        		// 如果访问的是未授权页面，则直接转到403页面（2016-11-3）
				request.getRequestDispatcher("/error/403").forward(request, response);
			} catch (ServletException e) {
				throw new UnauthorizedException(e);
			}
        }
        return false;
    }
	
	/**
	 * 跳转登录页时，跳转到默认首页
	 */
	public static void redirectToDefaultPath(ServletRequest request, ServletResponse response) throws IOException {
		// AJAX不支持Redirect改用Forward
		String loginUrl = Global.getProperty("shiro.defaultPath");
		HttpServletRequest req = ((HttpServletRequest) request);
		if (StringUtils.equals(req.getContextPath()+loginUrl, req.getRequestURI())){
			loginUrl = Global.getProperty("shiro.loginUrl");
		}
		if (ServletUtils.isAjaxRequest(req)) {
			try {
				request.getRequestDispatcher(loginUrl).forward(
						new GetHttpServletRequestWrapper(request), response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
    	}else{
    		WebUtils.issueRedirect(request, response, loginUrl);
    	}
	}
	
}

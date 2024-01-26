/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.common.web.http.wrapper.GetHttpServletRequestWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import java.io.IOException;

/**
 * 权限字符串过滤器
 * @author ThinkGem
 * @version 2017-03-22
 */
public class PermissionsFilter extends org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return PermissionsFilter.redirectTo403Page(request, response);
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
//				String uri = req.getRequestURI();
//				if (StringUtils.endsWithIgnoreCase(uri, ".json")
//						&& !StringUtils.endsWithIgnoreCase(loginUrl, ".json")){
//					loginUrl += ".json";
//				}else if (StringUtils.endsWithIgnoreCase(uri, ".xml")
//						&& !StringUtils.endsWithIgnoreCase(loginUrl, ".xml")){
//					loginUrl += ".xml";
//				}
				loginUrl = Global.getAdminPath() + "/login";
				request.getRequestDispatcher(loginUrl).forward(
						new GetHttpServletRequestWrapper(request), response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
    	}else{
    		loginUrl += StringUtils.contains(loginUrl, "?") ? "&" : "?";
    		StringBuilder requestUrl = new StringBuilder(req.getRequestURL());
            if (req.getQueryString() != null) {
                requestUrl.append("?").append(req.getQueryString());
            }
    		loginUrl += "__url=" + EncodeUtils.encodeUrl(requestUrl.toString());
    		WebUtils.issueRedirect(request, response, loginUrl);
    	}
	}

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsFilter.redirectToDefaultPath(request, response);
	}
	
}

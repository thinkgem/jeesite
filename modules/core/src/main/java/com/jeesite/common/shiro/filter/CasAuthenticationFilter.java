/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.realm.CasAuthorizingRealm;
import com.jeesite.common.shiro.realm.LoginInfo;

/**
 * CAS过滤器
 * @author ThinkGem
 * @version 2018-7-11
 */
@SuppressWarnings("deprecation")
public class CasAuthenticationFilter extends org.apache.shiro.cas.CasFilter {

	private CasAuthorizingRealm authorizingRealm; // 安全认证类
	
	/**
	 * 登录成功调用事件
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		
		// 登录成功后初始化授权信息并处理登录后的操作
		authorizingRealm.onLoginSuccess((LoginInfo)subject.getPrincipal(), (HttpServletRequest)request);
		
		String url = request.getParameter("__url");
		if (StringUtils.isNotBlank(url)) {
			WebUtils.issueRedirect(request, response, url, null, true);
		} else {
			WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
		}
		return false;
	}
	
	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated() || subject.isRemembered()) {
			try {
				String url = request.getParameter("__url");
				if (StringUtils.isNotBlank(url)) {
					WebUtils.issueRedirect(request, response, url, null, true);
				} else {
					WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else {
			try {
				if (ae != null && StringUtils.startsWith(ae.getMessage(), "msg:")){
					request.setAttribute("exception", ae);
					request.getRequestDispatcher("/error/403").forward(request, response);
				}else{
	                WebUtils.issueRedirect(request, response, getLoginUrl());
				}
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return false;
		}
	}

	public void setAuthorizingRealm(CasAuthorizingRealm authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}

}

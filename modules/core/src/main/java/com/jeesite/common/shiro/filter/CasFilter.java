/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;

/**
 * CAS过滤器
 * @author ThinkGem
 * @version 2020-9-19
 */
@SuppressWarnings("deprecation")
public class CasFilter extends org.apache.shiro.cas.CasFilter {
	
	/**
	 * 登录成功调用事件
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		return FormFilter.onLoginSuccess((HttpServletRequest)request, (HttpServletResponse)response);
	}
	
	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated() || subject.isRemembered()) {
			try {
				// AJAX不支持Redirect改用Forward
				request.getRequestDispatcher(getSuccessUrl()).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else {
			try {
				String message = ExceptionUtils.getExceptionMessage(ae);
				if (StringUtils.isNotBlank(message)){
					request.setAttribute("exception", ae);
					request.setAttribute("message", message);
					request.getRequestDispatcher("/error/403").forward(request, response);
				}else{
	                WebUtils.issueRedirect(request, response, getLoginUrl());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return false;
		}
	}

	public void setAuthorizingRealm(BaseAuthorizingRealm authorizingRealm) {
		
	}

}

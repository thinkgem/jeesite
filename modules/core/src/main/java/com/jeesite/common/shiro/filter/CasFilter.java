/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.cas.CasBaseFilter;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CAS过滤器
 * @author ThinkGem
 * @version 2020-9-19
 */
@SuppressWarnings("deprecation")
public class CasFilter extends CasBaseFilter {

	private BaseAuthorizingRealm authorizingRealm;

	public CasFilter() {
		this.setSuccessUrl(Global.getProperty("shiro.successUrl"));
	}
	
	/**
	 * 登录成功调用事件
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		authorizingRealm.onLoginSuccess(UserUtils.getLoginInfo(), (HttpServletRequest)request);
		ServletUtils.redirectUrl((HttpServletRequest)request, (HttpServletResponse)response, getSuccessUrl());
		return false;
	}
	
	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated() || subject.isRemembered()) {
			ServletUtils.redirectUrl((HttpServletRequest)request, (HttpServletResponse)response, getSuccessUrl());
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
		this.authorizingRealm = authorizingRealm;
	}

}

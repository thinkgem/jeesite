/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import java.util.Map;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;

import com.jeesite.common.shiro.authc.LdapToken;
import com.jeesite.common.web.http.ServletUtils;

/**
 * LDAP过滤器
 * @author ThinkGem
 * @version 2021-7-6
 */
public class LdapFilter extends FormFilter {
	
	@Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request, response);	// 用户名
		String password = getPassword(request);				// 登录密码
		boolean rememberMe = isRememberMe(request);			// 记住我（自动登录）
		String host = getHost(request);						// 登录主机
		Map<String, Object> paramMap = ServletUtils.getExtParams(request);	// 登录附加参数
		return new LdapToken(username, password.toCharArray(), rememberMe, host, paramMap);
    }

	@Override
	protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
		return true;
	}
	
}

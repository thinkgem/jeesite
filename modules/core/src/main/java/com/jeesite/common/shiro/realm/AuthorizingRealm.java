/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.realm;

import javax.servlet.http.HttpServletRequest;

import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.LogUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2018-7-11
 */
public class AuthorizingRealm extends BaseAuthorizingRealm  {

	private UserService userService;
	
	public AuthorizingRealm() {
		super();
	}
	
	@Override
	public void onLoginSuccess(LoginInfo loginInfo, HttpServletRequest request) {
		super.onLoginSuccess(loginInfo, request);
		
		// 更新登录IP、时间、会话ID等
		User user = UserUtils.get(loginInfo.getId());
		getUserService().updateUserLoginInfo(user);
		
		// 记录用户登录日志
		LogUtils.saveLog(user, request, "系统登录", Log.TYPE_LOGIN_LOGOUT);
	}
	
	@Override
	public void onLogoutSuccess(LoginInfo loginInfo, HttpServletRequest request) {
		super.onLogoutSuccess(loginInfo, request);
		
		// 记录用户退出日志
		User user = UserUtils.get(loginInfo.getId());
		LogUtils.saveLog(user, request, "系统退出", Log.TYPE_LOGIN_LOGOUT);
	}

	public UserService getUserService() {
		if (userService == null){
			userService = SpringUtils.getBean(UserService.class);
		}
		return userService;
	}
	
}

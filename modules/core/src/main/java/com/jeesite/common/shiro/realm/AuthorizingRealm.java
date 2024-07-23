/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.realm;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.SM3Utils;
import com.jeesite.common.codec.Sha1Utils;
import com.jeesite.common.config.Global;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.LogUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 系统认证授权实现类
 * @author ThinkGem
 * @version 2018-7-11
 */
public class AuthorizingRealm extends BaseAuthorizingRealm  {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_ITERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private UserService userService;
	
	public AuthorizingRealm() {
		super();
	}
	
	/**
	 * 获取登录凭证，将 authcToken 转换为 FormToken，参考 CAS 实现
	 */
	@Override
	protected FormToken getFormToken(AuthenticationToken authcToken) {
		return super.getFormToken(authcToken);
	}
	
	/**
	 * 用于用户根据登录信息获取用户信息<br>
	 * 1、默认根据登录账号登录信息，如：UserUtils.getByLoginCode(formToken.getUsername(), formToken.getParam("corpCode"));<br>
	 * 2、中断操作，可抛出异常提示用户 throw new AuthenticationException("msg:登录失败");<br>
	 * 3、如果增加其它登录方式，请重写此方法，如根据手机号或邮箱登录返回用户信息。
	 */
	@Override
	protected User getUserInfo(FormToken formToken) {
		return super.getUserInfo(formToken);
	}
	
	/**
	 * 校验登录凭证，如密码验证，token验证，验证失败抛出 AuthenticationException 异常
	 */
	@Override
	protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo authcInfo) throws AuthenticationException {
		super.assertCredentialsMatch(authcToken, authcInfo);
	}

	/**
	 * 获取用户授权信息，默认返回类型 SimpleAuthorizationInfo
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(LoginInfo loginInfo, Subject subject, Session session, User user) {
		return super.doGetAuthorizationInfo(loginInfo, subject, session, user);
	}
	
	/**
	 * 生成密文密码，生成随机的16位salt并经过1024次 sha-1 hash
	 * @param plainPassword 明文密码
	 * @return 16位salt密钥  + 40位hash密码
	 */
	@Override
	public String encryptPassword(String plainPassword) {
		String plain = EncodeUtils.decodeHtml(plainPassword);
		String salt = SM3Utils.genSaltString(SALT_SIZE);
		if (Global.isSmAlgorithm()) {
			String data = SM3Utils.sm3(plain, salt, HASH_ITERATIONS);
			return salt + data;
		}
		String data = Sha1Utils.sha1(plain, salt, HASH_ITERATIONS);
		return salt + data;
	}

	/**
	 * 验证密码正确性
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	@Override
	public boolean validatePassword(String plainPassword, String password) {
		try{
			String plain = EncodeUtils.decodeHtml(plainPassword);
			String salt = password.substring(0, SALT_SIZE * 2);
			if (Global.isSmAlgorithm()) {
				String data = SM3Utils.sm3(plain, salt, HASH_ITERATIONS);
				return password.equals(salt + data);
			}
			String data = Sha1Utils.sha1(plain, salt, HASH_ITERATIONS);
			return password.equals(salt + data);
		}catch(Exception e){
			return false;
		}
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

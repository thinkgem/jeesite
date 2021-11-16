/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.realm;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.Sha1Utils;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.LogUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统认证授权实现类
 * @author ThinkGem
 * @version 2018-7-11
 */
public class AuthorizingRealm extends BaseAuthorizingRealm  {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	private UserService userService;
	
	public AuthorizingRealm() {
		super();
//		// 设定密码校验的Hash算法与迭代次数（V4.1.4及以上版本不需要了，统一使用validatePassword验证密码）
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
//		matcher.setHashIterations(HASH_INTERATIONS);
//		this.setCredentialsMatcher(matcher);
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
	 * 1、默认根据登录账号登录信息，如：UserUtils.getByLoginCode(token.getUsername(), token.getParam("corpCode"));<br>
	 * 2、如果增加其它登录，请重写此方法，如根据手机号或邮箱登录返回用户信息。
	 */
	@Override
	protected User getUserInfo(FormToken token) {
		return super.getUserInfo(token);
	}
	
	/**
	 * 校验登录凭证，如密码验证，token验证，验证失败抛出 AuthenticationException 异常
	 */
	@Override
	protected void assertCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo authcInfo) throws AuthenticationException {
		super.assertCredentialsMatch(authcToken, authcInfo);
	}
	
	/**
	 * 生成密文密码，生成随机的16位salt并经过1024次 sha-1 hash
	 * @param plainPassword 明文密码
	 * @return 16位salt密钥  + 40位hash密码
	 */
	public String encryptPassword(String plainPassword) {
		String plain = EncodeUtils.decodeHtml(plainPassword);
		byte[] salt = Sha1Utils.genSalt(SALT_SIZE);
		byte[] hashPassword = Sha1Utils.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return EncodeUtils.encodeHex(salt) + EncodeUtils.encodeHex(hashPassword);
	}

	/**
	 * 验证密码正确性
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public boolean validatePassword(String plainPassword, String password) {
		try{
			String plain = EncodeUtils.decodeHtml(plainPassword);
			byte[] salt = EncodeUtils.decodeHex(password.substring(0, 16));
			byte[] hashPassword = Sha1Utils.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
			return password.equals(EncodeUtils.encodeHex(salt) + EncodeUtils.encodeHex(hashPassword));
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

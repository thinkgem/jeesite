/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.shiro.filter.FormAuthenticationFilter;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 单点登录Controller
 * @author ThinkGem
 * @version 2020-9-19
 */
@Controller
public class SsoController extends BaseController{
	
	/**
	 * 单点登录（如已经登录，则直接跳转）
	 * @param username 	登录用户名（loginCode）
	 * @param token 	单点登录令牌，令牌组成：sso密钥+用户名+日期，进行md5加密，举例： 
	 * 		// 注意如果 shiro.sso.encryptKey 为 true，则 secretKey 会自动加密。
	 * 		String secretKey = Global.getConfig("shiro.sso.secretKey");
	 * 		String token = Md5Utils.md5(secretKey + username + DateUtils.getDate("yyyyMMdd"));
	 * @param params 	登录附加参数（JSON格式），或 param_ 前缀的请求参数。
	 * @param url 		登录成功后跳转的url地址。
	 * @param relogin 	是否强制重新登录，需要强制重新登录传递true
	 * @see 调用示例：
	 * 	http://localhost/project/sso/{username}/{token}?url=/sys/user/list?p1=v1%26p2=v2&relogin=true
	 * 	如果url中携带参数，请使用转义字符，如“&”号，使用“%26”转义。
	 */
	@RequestMapping(value = "sso/{username}/{token}")
	public String sso(@PathVariable String username, @PathVariable String token,
			@RequestParam(defaultValue="${adminPath}/index") String url, String relogin,
			HttpServletRequest request, HttpServletResponse response, Model model){
		// 如果已经登录，并且是同一个人，并且不强制重新登录，则直接跳转到目标页
		User user = UserUtils.getUser();
		if(StringUtils.isNotBlank(user.getUserCode())
				&& StringUtils.equals(user.getLoginCode(), username)
				&& !ObjectUtils.toBoolean(relogin)){
			if (ServletUtils.isAjaxRequest(request)){
				return ServletUtils.renderResult(response, Global.TRUE, text("账号已登录"));
			}else{
				return REDIRECT + EncodeUtils.decodeUrl2(url);
			}
		}
		// 通过令牌登录系统
		if (token != null){
			try {
				// FormToken 构造方法的三个参数：登录名、单点登录的令牌秘钥、请求对象
				UserUtils.getSubject().login(new FormToken(username, token, request));
				request.setAttribute("__url", EncodeUtils.decodeUrl2(url));
				FormAuthenticationFilter.onLoginSuccess(request, response);
	        } catch (AuthenticationException e) {
	        	FormAuthenticationFilter.onLoginFailure(e, request, response);
	        }
			return null;
		}
		return "error/403";
	}
	
//	public static void main(String[] args) {
//		System.out.println(UserUtils.getSsoToken("system"));
//	}
	
}

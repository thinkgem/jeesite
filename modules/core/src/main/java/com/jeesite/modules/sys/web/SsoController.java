/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 单点登录Controller
 * @author ThinkGem
 * @version 2017-03-25
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
			@RequestParam(defaultValue="${adminPath}") String url, String relogin,
			HttpServletRequest request, Model model){
		User user = UserUtils.getUser();
		// 如果已经登录，并且是同一个人，并且不强制重新登录，则直接跳转到目标页
		if(StringUtils.isNotBlank(user.getUserCode())
				&& StringUtils.equals(user.getLoginCode(), username)
				&& !ObjectUtils.toBoolean(relogin)){
			return REDIRECT + EncodeUtils.decodeUrl2(url);
		}
		// 通过令牌登录系统
		if (token != null){
			try {
				FormToken upToken = new FormToken();
				upToken.setUsername(username);	// 登录用户名
				upToken.setSsoToken(token); 	// 单点登录令牌
				upToken.setParams(ServletUtils.getExtParams(request)); // 登录附加参数
				UserUtils.getSubject().login(upToken);
				return REDIRECT + EncodeUtils.decodeUrl2(url);
	        } catch (AuthenticationException e) {
	        	if (!e.getMessage().startsWith("msg:")){
	        		throw new AuthenticationException("msg:登录失败，请联系管理员。", e);
	        	}
	        	throw e;
	        }
		}
		return "error/403";
	}
	
}

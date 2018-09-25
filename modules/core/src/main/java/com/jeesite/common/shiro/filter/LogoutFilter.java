/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeesite.common.config.Global;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.http.ServletUtils;

/**
 * 登出过滤器
 * @author ThinkGem
 * @version 2017-03-22
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {
	
	private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);
	private BaseAuthorizingRealm authorizingRealm; // 安全认证类
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		try{
			Subject subject = getSubject(request, response);
	        String redirectUrl = getRedirectUrl(request, response, subject);
	        //try/catch added for SHIRO-298:
	        try {
	        	Object principal = subject.getPrincipal();
	        	if (principal != null){
//	        		// 记录用户退出日志（@Deprecated v4.0.5支持setAuthorizingRealm，之后版本可删除此if子句）
//		        	if (authorizingRealm == null){
//			    		LogUtils.saveLog(UserUtils.getUser(), ServletUtils.getRequest(),
//			    				"系统退出", Log.TYPE_LOGIN_LOGOUT);
//		        	}
//		        	else{
		        		// 退出成功之前初始化授权信息并处理登录后的操作
		        		authorizingRealm.onLogoutSuccess((LoginInfo)subject.getPrincipal(),
		        				(HttpServletRequest)request);
//		        	}
	        	}
	    		// 退出登录	
	    		subject.logout();
	        } catch (SessionException ise) {
	            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
	        }
	        
	        // 如果是Ajax请求，返回Json字符串。
	 		if (ServletUtils.isAjaxRequest((HttpServletRequest)request)){
	 			ServletUtils.renderResult((HttpServletResponse)response,
	 					Global.TRUE, Global.getText("sys.logout.success"));
	 			return false;
	 		}
	     	
	        issueRedirect(request, response, redirectUrl);
		}catch(Exception e){
			log.debug("Encountered session exception during logout.  This can generally safely be ignored.", e);
		}
		return false;
	}
	
	/**
	 * 登出跳转URL
	 */
	@Override
	protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
		String url = Global.getProperty("shiro.logoutUrl");
		// 如果配置了登出之后跳转的url，并且url不能为 ${adminPath}/logout 否则会造成死循环。
		if (StringUtils.isNoneBlank(url) && !url.equals((Global.getAdminPath()+"/logout"))){
			return url;
		}
		return super.getRedirectUrl(request, response, subject);
	}

	public void setAuthorizingRealm(BaseAuthorizingRealm authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}
	
}

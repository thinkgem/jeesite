/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.network.IpUtils;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.LogUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2020-4-13
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String CAPTCHA_PARAM = "validCode"; // 验证码
	public static final String MESSAGE_PARAM = "message"; // 登录返回消息
	public static final String REMEMBER_USERCODE_PARAM = "rememberUserCode"; // 记住用户名
	public static final String EXCEPTION_ATTRIBUTE_NAME = "exception"; // 异常类属性名
	
	private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationFilter.class);
	
	private BaseAuthorizingRealm authorizingRealm; // 安全认证类

	private Cookie rememberUserCodeCookie; // 记住用户名Cookie
	
	/**
	 * 构造方法
	 */
	public FormAuthenticationFilter() {
		super();
		rememberUserCodeCookie = new SimpleCookie(REMEMBER_USERCODE_PARAM);
		rememberUserCodeCookie.setHttpOnly(true);
        rememberUserCodeCookie.setMaxAge(Cookie.ONE_YEAR);
	}
	
	/**
	 * 创建登录授权令牌
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request, response);	// 用户名
		String password = getPassword(request);				// 登录密码
		boolean rememberMe = isRememberMe(request);			// 记住我（自动登录）
		String host = getHost(request);						// 登录主机
		String captcha = getCaptcha(request);				// 登录验证码
		Map<String, Object> paramMap = ServletUtils.getExtParams(request);	// 登录附加参数
		return new FormToken(username, password.toCharArray(), rememberMe, host, captcha, paramMap);
	}
	
	/**
	 * 获取登录用户名
	 */
	protected String getUsername(ServletRequest request, ServletResponse response) {
		String username = super.getUsername(request);
		if (StringUtils.isBlank(username)){
			username = ObjectUtils.toString(request.getAttribute(getUsernameParam()), StringUtils.EMPTY);
		}
		// 登录用户名解密（解决登录用户名明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			username = DesUtils.decode(username, secretKey);
			if (StringUtils.isBlank(username)){
				logger.info("登录账号为空或解码错误.");
			}
		}
		// 登录成功后，判断是否需要记住用户名
		if (WebUtils.isTrue(request, REMEMBER_USERCODE_PARAM)) {
			rememberUserCodeCookie.setValue(EncodeUtils.encodeUrl(EncodeUtils.xssFilter(username)));
			rememberUserCodeCookie.saveTo((HttpServletRequest)request, (HttpServletResponse)response);
		} else {
			rememberUserCodeCookie.removeFrom((HttpServletRequest)request, (HttpServletResponse)response);
		}
		return username;
	}
	
	/**
	 * 获取登录密码
	 */
	@Override
	protected String getPassword(ServletRequest request) {
		String password = super.getPassword(request);
		if (StringUtils.isBlank(password)){
			password = ObjectUtils.toString(request.getAttribute(getPasswordParam()), StringUtils.EMPTY);
		}
		// 登录密码解密（解决登录密码明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			password = DesUtils.decode(password, secretKey);
			if (StringUtils.isBlank(password)){
				logger.info("登录密码为空或解码错误.");
			}
		}
		return password;
	}
	
	/**
	 * 获取记住我
	 */
	@Override
	protected boolean isRememberMe(ServletRequest request) {
		String isRememberMe = WebUtils.getCleanParam(request, getRememberMeParam());
		if (StringUtils.isBlank(isRememberMe)){
			isRememberMe = ObjectUtils.toString(request.getAttribute(getRememberMeParam()), StringUtils.EMPTY);
		}
		return ObjectUtils.toBoolean(isRememberMe);
	}
	
	/**
	 * 获取请求的客户端主机
	 */
	@Override
	protected String getHost(ServletRequest request) {
		return IpUtils.getRemoteAddr((HttpServletRequest)request);
	}
	
	/**
	 * 获取登录验证码
	 */
	protected String getCaptcha(ServletRequest request) {
		String captcha = WebUtils.getCleanParam(request, CAPTCHA_PARAM);
		if (StringUtils.isBlank(captcha)){
			captcha = ObjectUtils.toString(request.getAttribute(CAPTCHA_PARAM), StringUtils.EMPTY);
		}
		// 登录用户名解密（解决登录用户名明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			captcha = DesUtils.decode(captcha, secretKey);
		}
		return captcha;
	}
	
	/**
	 * 多次调用登录接口，允许改变登录身份，无需退出再登录
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return (!isLoginRequest(request, response) && isPermissive(mappedValue)); // 不验证登录状态，只验证登录请求
	}
	
	/**
	 * 跳转登录页时，跳转到默认首页
	 */
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsAuthorizationFilter.redirectToDefaultPath(request, response);
	}

	/**
	 * 地址访问接入验证
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (logger.isTraceEnabled()) {
					logger.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Login page view.");
				}
				//allow them to see the login page ;)
				return true;
			}
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " + "Authentication url ["
						+ getLoginUrl() + "]");
			}
			redirectToLogin(request, response); // 此过滤器优先级较高，未登录，则跳转登录页，方便 CAS 登录
//			saveRequestAndRedirectToLogin(request, response);  // 去掉保存登录前的跳转地址  ThinkGem
			return false;
		}
	}

	/**
	 * 是否为登录操作（支持GET或CAS登录时传递__login=true参数）
	 */
	@Override
	protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
		boolean isLogin = WebUtils.isTrue(request, "__login");
		return super.isLoginRequest(request, response) || isLogin;
	}

	/**
	 * 是否为登录操作（支持GET或CAS登录时传递__login=true参数）
	 */
	@Override
	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		boolean isLogin = WebUtils.isTrue(request, "__login");
		return super.isLoginSubmission(request, response) || isLogin;
	}
	
	/**
	 * 执行登录方法
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		// 是否在登录后生成新的Session（默认false）
		if (Global.getPropertyToBoolean("shiro.isGenerateNewSessionAfterLogin", "false")){
			UserUtils.getSubject().logout();
		}
		return super.executeLogin(request, response);
	}

	/**
	 * 登录成功调用事件
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {

		// 登录成功后初始化授权信息并处理登录后的操作
		authorizingRealm.onLoginSuccess((LoginInfo)subject.getPrincipal(), (HttpServletRequest) request);
		
		// AJAX不支持Redirect改用Forward
		request.getRequestDispatcher(getSuccessUrl()).forward(request, response);
		return false;
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		String message = StringUtils.EMPTY;
		if (e instanceof IncorrectCredentialsException || e instanceof UnknownAccountException) {
			message = Global.getText("sys.login.failure");
		} else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		} else {
			message = Global.getText("sys.login.error");
			logger.error(message, e); // 输出到日志文件
		}
		request.setAttribute(EXCEPTION_ATTRIBUTE_NAME, e);
		request.setAttribute(MESSAGE_PARAM, message);
		
		// 登录操作如果是Ajax操作，直接返回登录信息字符串。
		if (ServletUtils.isAjaxRequest(((HttpServletRequest) request))){
			Map<String, Object> data = getLoginFailureData(((HttpServletRequest) request), ((HttpServletResponse) response));
			ServletUtils.renderResult(((HttpServletResponse) response), Global.TRUE, message, data);
			return false;
		}
				
		return true;
	}

	public void setAuthorizingRealm(BaseAuthorizingRealm authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}
	
	public static Map<String, Object> getLoginData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = MapUtils.newHashMap();
		
		// 获取登录参数
		Map<String, Object> paramMap = ServletUtils.getExtParams(request);
		for (Entry<String, Object> entry : paramMap.entrySet()){
			data.put(ServletUtils.EXT_PARAMS_PREFIX + entry.getKey(), entry.getValue());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (!Global.TRUE.equals(Global.getConfig("shiro.isAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 是否显示验证码
		data.put("isValidCodeLogin", Global.getConfigToInteger("sys.login.failedNumAfterValidCode", "200") == 0);

		//获取当前会话对象
		Session session = UserUtils.getSession();
		data.put("sessionid", (String)session.getId());
		
		// 如果登录设置了语言，则切换语言
		if (paramMap.get("lang") != null){
			Global.setLang((String)paramMap.get("lang"), request, response);
		}
		
		data.put("result", "login");
		return data;
	}

	public static Map<String, Object> getLoginFailureData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = MapUtils.newHashMap();
		
		String username = WebUtils.getCleanParam(request, DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, DEFAULT_REMEMBER_ME_PARAM);
		boolean rememberUserCode = WebUtils.isTrue(request, REMEMBER_USERCODE_PARAM);
		Exception exception = (Exception)request.getAttribute(EXCEPTION_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(MESSAGE_PARAM);

		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			username = DesUtils.decode(username, secretKey);
		}
		
		data.put(DEFAULT_USERNAME_PARAM, username);
		data.put(DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		data.put(REMEMBER_USERCODE_PARAM, rememberUserCode);
		Map<String, Object> paramMap = ServletUtils.getExtParams(request);
		for (Entry<String, Object> entry : paramMap.entrySet()){
			data.put(ServletUtils.EXT_PARAMS_PREFIX + entry.getKey(), entry.getValue());
		}
		data.put(MESSAGE_PARAM, message);
		
		// 非授权异常，登录失败，验证码加 1。
		if (!(exception instanceof UnauthorizedException)){
			data.put("isValidCodeLogin", BaseAuthorizingRealm.isValidCodeLogin(username,
					(String)paramMap.get("corpCode"), (String)paramMap.get("deviceType"), "failed"));
		}

		// 记录用户登录失败日志
		String corpCode = (String)paramMap.get("corpCode");
		User user = UserUtils.getByLoginCode(username, corpCode);
		LogUtils.saveLog(user, request, "登录失败", Log.TYPE_LOGIN_LOGOUT);
		
		//获取当前会话对象
		Session session = UserUtils.getSession();
		data.put("sessionid", (String)session.getId());
		
		// 如果登录设置了语言，则切换语言
		if (paramMap.get("lang") != null){
			Global.setLang((String)paramMap.get("lang"), request, response);
		}
		
		data.put("result", Global.FALSE);
		return data;
	}
}
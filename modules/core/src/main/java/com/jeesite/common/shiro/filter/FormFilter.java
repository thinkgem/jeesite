/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.network.IpUtils;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.modules.sys.utils.*;
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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2020-9-19
 */
public class FormFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String CAPTCHA_PARAM = "validCode"; 					// 验证码
	public static final String MESSAGE_PARAM = "message"; 						// 登录返回消息
	public static final String REMEMBER_USERCODE_PARAM = "rememberUserCode"; 	// 记住用户名
	public static final String EXCEPTION_ATTRIBUTE_NAME = "exception"; 			// 异常类属性名
	public static final String LOGIN_PARAM = "__login";							// 支持GET方式登录的参数

	public static final Boolean POST_ROLE_PERMI = Global.getConfigToBoolean("user.postRolePermi", "false");
	public static final Boolean SWITCH_OFFICE = Global.getConfigToBoolean("user.switchOffice", "false");
	public static final Boolean LOGIN_AFTER_ACTIVE_MAIN_OFFICE = Global.getConfigToBoolean("user.loginAfterActiveMainOffice", "false");

	private static final Logger logger = LoggerFactory.getLogger(FormFilter.class);

	private static FormFilter instance;
	private static Cookie sessionIdCookie;
	private static Cookie rememberUserCodeCookie;

	private BaseAuthorizingRealm authorizingRealm;

	/**
	 * 构造方法
	 */
	public FormFilter() {
		super();
		sessionIdCookie = SpringUtils.getBean("sessionIdCookie");
		rememberUserCodeCookie = new SimpleCookie(sessionIdCookie);
		rememberUserCodeCookie.setName(REMEMBER_USERCODE_PARAM);
		rememberUserCodeCookie.setMaxAge(Cookie.ONE_YEAR);
        instance = this;
	}
	
	/**
	 * 创建登录授权令牌
	 */
	public static FormToken newToken(HttpServletRequest request, HttpServletResponse response) {
		return (FormToken)instance.createToken(request, response);
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
		if (StringUtils.length(username) > 1000) {
    		throw new AuthenticationException("msg:"+Global.getText("sys.login.error"));
		}
		// 登录用户名解密（解决登录用户名明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(secretKey)){
			username = DesUtils.decode(username, secretKey);
			if (StringUtils.isBlank(username)){
				logger.info("登录账号为空或解码错误.");
			}
		}
		// 登录时判断是否需要记住用户名
		if (WebUtils.isTrue(request, REMEMBER_USERCODE_PARAM)) {
			rememberUserCodeCookie.setValue(EncodeUtils.encodeUrl(EncodeUtils.xssFilter(username, (HttpServletRequest)request)));
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
		if (StringUtils.length(password) > 1000) {
    		throw new AuthenticationException("msg:"+Global.getText("sys.login.error"));
		}
		// 登录密码解密（解决登录密码明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(secretKey)){
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
		if (StringUtils.length(captcha) > 100) {
    		throw new AuthenticationException("msg:"+Global.getText("sys.login.error"));
		}
		// 登录用户名解密（解决登录用户名明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(captcha) && StringUtils.isNotBlank(secretKey)){
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
		boolean isLogin = WebUtils.isTrue(request, LOGIN_PARAM);
		return super.isLoginRequest(request, response) || isLogin;
	}

	/**
	 * 是否为登录操作（支持GET或CAS登录时传递__login=true参数）
	 */
	@Override
	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		boolean isLogin = WebUtils.isTrue(request, LOGIN_PARAM);
		return super.isLoginSubmission(request, response) || isLogin;
	}

	/**
	 * 跳转登录页时，跳转到默认首页
	 */
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		PermissionsFilter.redirectToDefaultPath(request, response);
	}
	
	/**
	 * 执行登录方法
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		// 是否在登录后生成新的Session（默认false）
		if (Global.getPropertyToBoolean("shiro.isGenerateNewSessionAfterLogin", "false")){
			String[] keys = new String[] { ValidCodeUtils.VALID_CODE };
			Map<String, Object> attrMap = MapUtils.newHashMap();
			final Session sessionOld = UserUtils.getSession();
			for (String key : keys) {
				Object value = sessionOld.getAttribute(key);
				if (value != null) {
					attrMap.put(key, value);
				}
			}
			UserUtils.getSubject().logout();
			// 恢复生成新的Session之前的Session数据
			final Session sessionNew = UserUtils.getSession();
			attrMap.forEach(sessionNew::setAttribute);
		}
		return super.executeLogin(request, response);
	}

	/**
	 * 登录成功调用事件（静态方便其他位置调用）
	 */
	public static boolean onLoginSuccess(HttpServletRequest request, HttpServletResponse response) {
		try {
			return instance.onLoginSuccess(null, null, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 登录成功调用事件
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse response) throws Exception {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		// 登录成功后初始化授权信息并处理登录后的操作
		authorizingRealm.onLoginSuccess(UserUtils.getLoginInfo(), request);
		// 跳转到登录成功页面，若未指定则获取默认 shiro.successUrl in application.yml
		String successUrl = (String)request.getAttribute("__url");
		if (StringUtils.isBlank(successUrl)) {
			successUrl = getSuccessUrl();
		}
		ServletUtils.redirectUrl(request, (HttpServletResponse)response, successUrl);
		return false;
	}

	/**
	 * 登录失败调用事件（静态方便其他位置调用）
	 */
	public static boolean onLoginFailure(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
		return instance.onLoginFailure(null, e, request, response);
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		String message = StringUtils.EMPTY;
		if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		} else if (e instanceof IncorrectCredentialsException || e instanceof UnknownAccountException) {
			message = Global.getText("sys.login.failure");
		} else {
			message = Global.getText("sys.login.error");
			logger.error(message, e); // 输出到日志文件
		}
		request.setAttribute(EXCEPTION_ATTRIBUTE_NAME, e);
		request.setAttribute(MESSAGE_PARAM, message);
		// 跳转到登录失败页面
		String loginFailureUrl = Global.getProperty("adminPath") + "/loginFailure";
		ServletUtils.redirectUrl((HttpServletRequest)request, (HttpServletResponse)response, loginFailureUrl);
		return false;
	}
	
	/**
	 * 获取登录页面数据
	 * @author ThinkGem
	 */
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

		// 设置公共结果数据
		setCommonData(request, response, data, paramMap);
		data.put("result", "login");
		return data;
	}

	/**
	 * 获取登录失败数据
	 * @author ThinkGem
	 */
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

		// 设置公共结果数据
		setCommonData(request, response, data, paramMap);
		data.put("result", Global.FALSE);
		return data;
	}

	/**
	 * 设置公共数据
	 * @author ThinkGem
	 */
	private static void setCommonData(HttpServletRequest request, HttpServletResponse response,
									  Map<String, Object> data, Map<String, Object> paramMap) {
		if (ServletUtils.isAjaxRequest(request)) {
			Session session = UserUtils.getSession();
			data.put("sessionid", session.getId());
			Cookie cookie = new SimpleCookie(sessionIdCookie);
			cookie.setValue((String)session.getId());
			cookie.saveTo(request, response);
		}
		if (paramMap.get("lang") != null){
			Global.setLang((String)paramMap.get("lang"), request, response);
		}
		data.put("demoMode", Global.isDemoMode());
		data.put("useCorpModel", Global.isUseCorpModel());
		data.put("loginCodeCorpUnique", Global.getConfigToBoolean("user.loginCodeCorpUnique", "false"));
		data.put("title", Global.getProperty("productName"));
		data.put("company", Global.getProperty("companyName"));
		data.put("version", Global.getProperty("productVersion"));
		data.put("year", Global.getProperty("copyrightYear"));
	}

	/**
	 * 获取登录页面数据
	 * @author ThinkGem
	 */
	public static Map<String, Object> getLoginSuccessData(HttpServletRequest request, HttpServletResponse response,
														  User user, Session session) {
		Map<String, Object> data = MapUtils.newHashMap();
		if (ServletUtils.isAjaxRequest(request)) {
			Cookie cookie = new SimpleCookie(sessionIdCookie);
			cookie.setValue((String)session.getId());
			cookie.saveTo(request, response);
		}
		data.put("user", user); // 设置当前用户信息
		data.put("demoMode", Global.isDemoMode());
		data.put("useCorpModel", Global.isUseCorpModel());
		data.put("currentCorpCode", CorpUtils.getCurrentCorpCode());
		data.put("currentCorpName", CorpUtils.getCurrentCorpName());
		data.put("msgEnabled", Global.getPropertyToBoolean("msg.enabled", "false"));
		data.put("sysCode", session.getAttribute("sysCode"));
		data.put("roleCode", session.getAttribute("roleCode"));
		data.put("postCode", session.getAttribute("postCode"));
		data.put("title", Global.getProperty("productName"));
		data.put("company", Global.getProperty("companyName"));
		data.put("version", Global.getProperty("productVersion"));
		data.put("year", Global.getProperty("copyrightYear"));
		data.put("lang", Global.getLang());
		List<Map<String, Object>> roleList = ListUtils.newArrayList();
		String desktopUrl = null; String roleCode = (String)session.getAttribute("roleCode");
		Set<String> roleCodes = roleCode != null ? SetUtils.newHashSet(StringUtils.splitComma(roleCode)) : null;
		for (Role role : user.getRoleList()){
			Map<String, Object> roleMap = MapUtils.newHashMap();
			roleMap.put("roleCode", role.getRoleCode());
			roleMap.put("roleName", role.getRoleName());
			roleMap.put("isShow", role.getIsShow());
			roleMap.put("sysCodes", role.getSysCodes());
			roleList.add(roleMap);
			// 根据当前身份，选择桌面地址（先得到先受用）
			if (desktopUrl == null) {
				if (roleCodes != null){
					if (roleCodes.contains(role.getRoleCode()) && StringUtils.isNotBlank(role.getDesktopUrl())) {
						desktopUrl = role.getDesktopUrl();
					}
				}else if (StringUtils.isNotBlank(role.getDesktopUrl())) {
					desktopUrl = role.getDesktopUrl();
				}
			}
		}
		data.put("roleList", roleList);
		if (POST_ROLE_PERMI && User.USER_TYPE_EMPLOYEE.equals(user.getUserType())) {
			List<Map<String, Object>> postList = ListUtils.newArrayList();
			data.put("postRolePermi", "true");
			Employee employee = user.getRefObj();
			for (EmployeePost ep : EmpUtils.getEmployeePostList(employee.getEmpCode())){
				Post post = ep.getPost();
				if (post != null) {
					Map<String, Object> postMap = MapUtils.newHashMap();
					postMap.put("postCode", post.getPostCode());
					postMap.put("postName", post.getPostName());
					postList.add(postMap);
				}
			}
			data.put("postList", postList);
		}
		if (SWITCH_OFFICE && User.USER_TYPE_EMPLOYEE.equals(user.getUserType())) {
			data.put("switchOffice", "true");
			// 登录后指定当前部门，即当前部门权限，否则为混合权限
			data.put("officeCode", LOGIN_AFTER_ACTIVE_MAIN_OFFICE ? EmpUtils.getCurrentOfficeCode()
					: session.getAttribute("officeCode"));
			data.put("officeName", EmpUtils.getCurrentOfficeName());
		}
		data.put("desktopUrl", desktopUrl != null ? desktopUrl : Global.getConfig("sys.index.desktopUrl"));
		return data;
	}
	
	public void setAuthorizingRealm(BaseAuthorizingRealm authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}
	
}
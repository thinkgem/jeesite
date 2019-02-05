/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.filter.FormAuthenticationFilter;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.PwdUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2017-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}")
public class LoginController extends BaseController{
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		// 地址中如果包含JSESSIONID，则跳转一次，去掉JSESSIONID信息。
		if (StringUtils.containsIgnoreCase(request.getRequestURI(), ";JSESSIONID=")){
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/login" + queryString);
			return null;
		}

		LoginInfo loginInfo = UserUtils.getLoginInfo();
		
		// 如果已经登录，则跳转到管理首页
		if(loginInfo != null){
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/index" + queryString);
			return null;
		}
		
		// 如果是登录操作，跳转到此，则认为是登录失败（支持GET登录时传递__login=true参数）
		if (WebUtils.isTrue(request, "__login")){
			return loginFailure(request, response, model);
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (!Global.TRUE.equals(Global.getConfig("shiro.isAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 是否显示验证码
		model.addAttribute("isValidCodeLogin", Global.getConfigToInteger("sys.login.failedNumAfterValidCode", "200") == 0);

		//获取当前会话对象
		Session session = UserUtils.getSession();
		model.addAttribute("sessionid", (String)session.getId());
		
		// 获取登录参数
		Map<String, Object> paramMap = ServletUtils.getExtParams(request);
		
		// 如果登录设置了语言，则切换语言
		if (paramMap.get("lang") != null){
			Global.setLang((String)paramMap.get("lang"), request, response);
		}
		
		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest((HttpServletRequest)request)){
			model.addAttribute("result", "login");
			model.addAttribute("message", text("sys.login.notLongIn"));
			return ServletUtils.renderObject(response, model);
		}
		
		// 返回指定用户类型的登录页视图
		String userType = (String)paramMap.get("userType");
		if (StringUtils.isNotBlank(userType)){
			String view = UserUtils.getUserTypeValue(userType, "loginView");
			if(StringUtils.isNotBlank(view)){
				return view;
			}
		}
		
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Model model) {
		LoginInfo loginInfo = UserUtils.getLoginInfo();
		
		// 如果已经登录，则跳转到管理首页
		if(loginInfo != null){
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/index" + queryString);
			return null;
		}
		
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean rememberUserCode = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_USERCODE_PARAM);
		String params = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PARAMS_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			username = DesUtils.decode(username, secretKey);
		}
		
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_USERCODE_PARAM, rememberUserCode);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAMS_PARAM, params);
		Map<String, Object> paramMap = ServletUtils.getExtParams(request);
		for (Entry<String, Object> entry : paramMap.entrySet()){
			model.addAttribute(FormAuthenticationFilter.DEFAULT_PARAM_PREFIX_PARAM + entry.getKey(), entry.getValue());
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		
		// 如果登录设置了语言，则切换语言
		if (paramMap.get("lang") != null){
			Global.setLang((String)paramMap.get("lang"), request, response);
		}
		
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, text(message));

		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			model.addAttribute("isValidCodeLogin", BaseAuthorizingRealm.isValidCodeLogin(username, (String)paramMap.get("deviceType"), "failed"));
		}
		
		//获取当前会话对象
		Session session = UserUtils.getSession();
		model.addAttribute("sessionid", (String)session.getId());

		// 登录操作如果是Ajax操作，直接返回登录信息字符串。
		if (ServletUtils.isAjaxRequest(request)){
			model.addAttribute("result", Global.FALSE);
			return ServletUtils.renderObject(response, model);
		}
		
		// 返回指定用户类型的登录页视图
		String userType = (String)paramMap.get("userType");
		if (StringUtils.isNotBlank(userType)){
			String view = UserUtils.getUserTypeValue(userType, "loginView");
			if(StringUtils.isNotBlank(view)){
				return view;
			}
		}
		
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		// 地址中如果包含JSESSIONID，则跳转一次，去掉JSESSIONID信息。
		if (StringUtils.containsIgnoreCase(request.getRequestURI(), ";JSESSIONID=")){
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/index" + queryString);
			return null;
		}

		// 验证下用户权限，以便调用doGetAuthorizationInfo方法，保存单点登录登出句柄
		Subject subject = SecurityUtils.getSubject();
		if (subject == null || !subject.isPermitted("user")){
			if (subject != null){
				subject.logout();
			}
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/login" + queryString);
			return null;
		}

		//获取登录用户信息
		LoginInfo loginInfo = UserUtils.getLoginInfo();
		
		// 未加载shiro模块时会为空，直接访问则提示操作权限不足。
		if(loginInfo == null){
			UserUtils.getSubject().logout();
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/login" + queryString);
			return null;
		}
		
		// 当前用户对象信息
		User user = UserUtils.get(loginInfo.getId());
		if (user == null){
			UserUtils.getSubject().logout();
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/login" + queryString);
			return null;
		}
		model.addAttribute("user", user); // 设置当前用户信息

		//获取当前会话对象
		Session session = UserUtils.getSession();
		
		// 是否是登录操作
		boolean isLogin = "true".equals(session.getAttribute("__login"));
		if (isLogin){
			// 获取后接着清除，防止下次获取仍然认为是登录状态
			session.removeAttribute("__login");
			// 设置共享SessionId的Cookie值（第三方系统使用）
			String cookieName = Global.getProperty("session.shareSessionIdCookieName");
			if (StringUtils.isNotBlank(cookieName)){
				CookieUtils.setCookie((HttpServletResponse)response, cookieName, (String)session.getId());
			}
			// 如果登录设置了语言，则切换语言
			if (loginInfo.getParam("lang") != null){
				Global.setLang(loginInfo.getParam("lang"), request, response);
			}
		}

		// 获取登录成功页面
		String successUrl = Global.getProperty("shiro.successUrl");
		if (!StringUtils.contains(successUrl, "://")){
			successUrl = request.getContextPath() + successUrl;
		}
		
		// 登录操作如果是Ajax操作，直接返回登录信息字符串。
		if (ServletUtils.isAjaxRequest(request)){
			model.addAttribute("result", Global.TRUE);
			// 如果是登录，则返回登录成功信息，否则返回获取成功信息
			if (isLogin){
				model.addAttribute("message", text("sys.login.success"));
			}else{
				model.addAttribute("message", text("sys.login.getInfo"));
			}
			model.addAttribute("sessionid", (String)session.getId());
			model.addAttribute("__url", successUrl); // 告诉浏览器登录后跳转的页面
			return ServletUtils.renderObject(response, model);
		}
		// 如果是登录操作，则跳转到登录成功页
		else if (isLogin){
			return REDIRECT + successUrl;
		}
		
		// 是否允许刷新主页，如果已登录，再次访问主页，则退出原账号。
		if (!Global.getConfigToBoolean("shiro.isAllowRefreshIndex", "true")){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				CookieUtils.setCookie(response, "LOGINED", "false");
				String queryString = request.getQueryString();
				queryString = queryString == null ? "" : "?" + queryString;
				return REDIRECT + adminPath + "/login" + queryString;
			}
		}
		
		// 初始密码策略和密码修改策略验证（0：关闭；1：提醒用户；2：强制修改初始或旧密码）
		String passwordModifyUrl = PwdUtils.passwordModifyValid(user, model);
		if (passwordModifyUrl != null){
			try {
				request.getRequestDispatcher(passwordModifyUrl).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		// 返回指定用户类型的首页视图
		String view = UserUtils.getUserTypeValue(user.getUserType(), "indexView");
		if(StringUtils.isNotBlank(view)){
			return view;
		}
		
		// 返回主页面视图
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 获取侧边栏菜单数据
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "index/menuTree")
	public String indexMenuTree(String parentCode) {
		return "modules/sys/sysIndex/menuTree";
	}
	
	/**
	 * 获取当前用户权限字符串数据（移动端用）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "authInfo")
	@ResponseBody
	public AuthorizationInfo authInfo() {
		return UserUtils.getAuthInfo();
	}

	/**
	 * 获取当前用户菜单数据（移动端用）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "menuTree")
	@ResponseBody
	public List<Menu> menuTree(String parentCode) {
		if (StringUtils.isNotBlank(parentCode)){
			return UserUtils.getMenuListByParentCode(parentCode);
		}
		return UserUtils.getMenuTree();
	}

	/**
	 * 切换系统菜单（仅超级管理员有权限）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "switch/{sysCode}")
	public String switchSys(@PathVariable String sysCode) {
		User user = UserUtils.getUser();
		if (user.isSuperAdmin() && StringUtils.isNotBlank(sysCode)){
			Session session = UserUtils.getSession();
			session.setAttribute("sysCode", sysCode);
			UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		}
		return REDIRECT + adminPath + "/index";
	}
	
	/**
	 * 切换主题
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "switchSkin/{skinName}")
	public String switchSkin(@PathVariable String skinName, HttpServletRequest request, HttpServletResponse response) {
		LoginInfo loginInfo = UserUtils.getLoginInfo();
		if (StringUtils.isNotBlank(skinName) && !"select".equals(skinName)){
			CookieUtils.setCookie(response, "skinName_" + loginInfo.getId(), skinName);
			return REDIRECT + adminPath + "/index";
		}
		return "modules/sys/sysSwitchSkin";
	}
	
	/**
	 * 个人桌面页面
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "desktop")
	public String desktop(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/sysDesktop";
	}
	
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.shiro.filter.FormFilter;
import com.jeesite.common.shiro.realm.BaseAuthorizingRealm;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.PwdUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2020-9-19
 */
@Controller
@Tag(name = "Login - 登录公共")
@RequestMapping(value = "${adminPath}")
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public class LoginController extends BaseController{
	
	/**
	 * 登录页面
	 */
	@RequestMapping(value = "login")
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
		if (WebUtils.isTrue(request, BaseAuthorizingRealm.IS_LOGIN_OPER)){
			return loginFailure(request, response, model);
		}

		// 获取登录数据
		model.addAllAttributes(FormFilter.getLoginData(request, response));

		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest((HttpServletRequest)request)){
			model.addAttribute("message", text("sys.login.notLongIn"));
			return ServletUtils.renderObject(response, model);
		}
		
		// API 模式不返回视图页面
		if (Global.isApiMode()) {
			return null;
		}
		
		// 返回指定用户类型的登录页视图
		String userType = (String)model.asMap().get(ServletUtils.EXT_PARAMS_PREFIX + "userType");
		if (StringUtils.isBlank(userType)){
			userType = User.USER_TYPE_EMPLOYEE;
		}
		String view = UserUtils.getUserTypeValue(userType, "loginView");
		if(StringUtils.isNotBlank(view)){
			return view;
		}
		
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，返回错误信息
	 */
	@RequestMapping(value = "loginFailure")
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Model model) {
//		// 如果已经登录，则跳转到管理首页
//		LoginInfo loginInfo = UserUtils.getLoginInfo();
//		if(loginInfo != null){ // 注释掉，已经登录的账号，正常返回登录失败信息，方便前端判断。
//			String queryString = request.getQueryString();
//			queryString = queryString == null ? "" : "?" + queryString;
//			ServletUtils.redirectUrl(request, response, adminPath + "/index" + queryString);
//			return null;
//		}
		
		// 获取登录失败数据
		model.addAllAttributes(FormFilter.getLoginFailureData(request, response));
		
		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest(request)){
			return ServletUtils.renderObject(response, model);
		}
		
		// API 模式不返回视图页面
		if (Global.isApiMode()) {
			return null;
		}
		
		// 返回指定用户类型的登录页视图
		String userType = (String)model.asMap().get(ServletUtils.EXT_PARAMS_PREFIX + "userType");
		if (StringUtils.isBlank(userType)){
			userType = User.USER_TYPE_EMPLOYEE;
		}
		String view = UserUtils.getUserTypeValue(userType, "loginView");
		if(StringUtils.isNotBlank(view)){
			return view;
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
		if (!subject.isPermitted("user")){
			subject.logout();
			String queryString = request.getQueryString();
			queryString = queryString == null ? "" : "?" + queryString;
			ServletUtils.redirectUrl(request, response, adminPath + "/login" + queryString);
			return null;
		}

		// 获取登录用户信息，未加载shiro模块时会为空，直接访问则提示操作权限不足。
		LoginInfo loginInfo = UserUtils.getLoginInfo();
		if(loginInfo == null){
			subject.logout();
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

		// 获取当前会话对象，并返回一些数据
		Session session = UserUtils.getSession();
		model.addAllAttributes(FormFilter.getLoginSuccessData(request, response, user, session));

		// 是否是登录操作
		boolean isLogin = Global.TRUE.equals(session.getAttribute(BaseAuthorizingRealm.IS_LOGIN_OPER));
		if (isLogin){
			// 获取后接着清除，防止下次获取仍然认为是登录状态
			session.removeAttribute(BaseAuthorizingRealm.IS_LOGIN_OPER);
			// 设置共享SessionId的Cookie值（第三方系统使用）
			String cookieName = Global.getProperty("session.shareSessionIdCookieName");
			if (StringUtils.isNotBlank(cookieName)){
				CookieUtils.setCookie(response, cookieName, (String)session.getId(), "/");
			}
			// 如果登录设置了语言，则切换语言
			if (loginInfo.getParam("lang") != null){
				Global.setLang(loginInfo.getParam("lang"), request, response);
			}
			// 根据当前用户子系统，切换到默认系统下
			for(Role role : user.getRoleList()) {
				if (role.getSysCodes() != null) {
					String sysCode = null;
					for (String code : StringUtils.splitComma(role.getSysCodes())) {
						if (StringUtils.isNotBlank(code)) {
							sysCode = code;
							break;
						}
					}
					if (sysCode != null) {
						session.setAttribute("sysCode", sysCode);
						UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
						break;
					}
				}
			}
		}

		// 获取登录成功后跳转的页面
		String successUrl = request.getParameter("__url");
		if (StringUtils.isBlank(successUrl)){
			successUrl = (String)request.getAttribute("__url");
		}
		if (StringUtils.contains(successUrl, "://")){
			String domain = ServletUtils.getRequestDomain(successUrl);
			successUrl = StringUtils.substring(successUrl, domain.length());
			if (StringUtils.startsWith(successUrl, request.getContextPath())) {
				successUrl = StringUtils.substringAfter(successUrl, request.getContextPath());
			}
		}
		if (StringUtils.isBlank(successUrl)){
			successUrl = Global.getProperty("shiro.successUrl");
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
			if (!StringUtils.contains(successUrl, "://")){
				successUrl = request.getContextPath() + successUrl;
			}
			model.addAttribute("__url", successUrl); // 告诉浏览器登录后跳转的页面
			// 初始密码策略和密码修改策略验证（0：关闭；1：提醒用户；2：强制修改初始或旧密码）
			String modifyPasswordMsg = PwdUtils.getModifyPasswordMsg(user, model);
			model.addAttribute("modifyPasswordMsg", modifyPasswordMsg);
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
		
		// 非无类型用户，自动根据用户类型设置默认菜单的归属系统（个性化示例）
		//if (!User.USER_TYPE_NONE.equals(user.getUserType())){
		//	session.setAttribute("sysCode", user.getUserType());
		//	UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		//}
		
		// 登录切换角色身份（个性化示例）
		//String roleCode = "dept";
		//session.setAttribute("roleCode", roleCode);
		//UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		
		// API 模式不返回视图页面
		if (Global.isApiMode()) {
			return null;
		}
		
		// 返回指定用户类型的首页视图
		String userType = user.getUserType();
		if (User.USER_TYPE_NONE.equals(userType)){
			userType = User.USER_TYPE_EMPLOYEE;
		}
		String view = UserUtils.getUserTypeValue(userType, "indexView");
		if(StringUtils.isNotBlank(view)){
			return view;
		}
		
		// 返回主页面视图
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 侧边栏菜单数据
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "index/menuTree")
	public String indexMenuTree(String parentCode) {
		return "modules/sys/menuTree";
	}
	
	/**
	 * 当前用户权限字符串数据
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "authInfo")
	@ResponseBody
	public AuthorizationInfo authInfo() {
		return UserUtils.getAuthInfo();
	}

	/**
	 * 当前用户菜单列表数据
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "menuTree")
	@ResponseBody
	@JsonView(Menu.SimpleView.class)
	public List<Menu> menuTree(String parentCode) {
		return UserUtils.getMenuTreeByParentCode(parentCode);
	}

	/**
	 * 当前用户菜单路由数据
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "menuRoute")
	@ResponseBody
	public List<Map<String, Object>> menuRoute(String parentCode) {
		return UserUtils.getMenuRouteByParentCode(parentCode);
	}

	/**
	 * 切换系统菜单（仅超级管理员有权限）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "switch/{sysCode}")
	public String switchSys(@PathVariable String sysCode) {
		Session session = UserUtils.getSession();
		if (StringUtils.isNotBlank(sysCode)){
			session.setAttribute("sysCode", sysCode);
		}else{
			session.removeAttribute("sysCode");
		}
		UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		return REDIRECT + adminPath + "/index";
	}

	/**
	 * 切换角色菜单（仅超级管理员有权限）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"switchRole","switchRole/{roleCode}"})
	public String switchRole(@PathVariable(required=false) String roleCode) {
		Session session = UserUtils.getSession();
		if (StringUtils.isNotBlank(roleCode)){
			session.setAttribute("roleCode", roleCode);
		}else{
			session.removeAttribute("roleCode");
		}
		UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		return REDIRECT + adminPath + "/index";
	}
	
	/**
	 * 切换主题风格
	 */
	//@RequiresPermissions("user")
	@RequestMapping(value = "switchSkin/{skinName}")
	public String switchSkin(@PathVariable String skinName, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(skinName) && !"select".equals(skinName)){
			CookieUtils.setCookie(response, "skinName", skinName);
			if (ServletUtils.isAjaxRequest(request)) {
				return renderResult(response, Global.TRUE, text("主题切换成功"));
			}
			return REDIRECT + adminPath + "/index";
		}
		return "modules/sys/switchSkin";
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

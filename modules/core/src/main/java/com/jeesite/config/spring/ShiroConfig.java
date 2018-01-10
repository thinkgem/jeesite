/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.config.spring;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.jeesite.common.config.Global;
import com.jeesite.common.shiro.cas.CasOutHandler;
import com.jeesite.common.shiro.config.FilterChainDefinitionMap;
import com.jeesite.common.shiro.filter.CasAuthenticationFilter;
import com.jeesite.common.shiro.filter.FormAuthenticationFilter;
import com.jeesite.common.shiro.filter.PermissionsAuthorizationFilter;
import com.jeesite.common.shiro.filter.UserFilter;
import com.jeesite.common.shiro.realm.AuthorizingRealm;
import com.jeesite.common.shiro.session.SessionDAO;
import com.jeesite.common.shiro.session.SessionManager;
import com.jeesite.common.shiro.web.ShiroFilterFactoryBean;
import com.jeesite.common.shiro.web.WebSecurityManager;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.UserService;

/**
 * Shiro配置
 * @author ThinkGem
 * @version 2017年11月30日
 */
@SuppressWarnings("deprecation")
@Configuration
public class ShiroConfig {
	
	/**
	 * 单点登录信息句柄，单点退出用
	 */
	@Bean
	public CasOutHandler casOutHandler() {
		return new CasOutHandler();
	}

	/**
	 * 系统安全认证实现类
	 */
	@Bean
	public AuthorizingRealm authorizingRealm(SessionDAO sessionDAO, UserService userService,
			EmpUserService empUserService, CasOutHandler casOutHandler) {
		AuthorizingRealm bean = new AuthorizingRealm();
		bean.setCachingEnabled(false);
		bean.setSessionDAO(sessionDAO);
		bean.setUserService(userService);
		bean.setEmpUserService(empUserService);
		bean.setCasOutHandler(casOutHandler);
		bean.setCasServerUrl(Global.getProperty("shiro.casServerUrl"));
		bean.setCasServerCallbackUrl(Global.getProperty("shiro.casClientUrl") + Global.getAdminPath() + "/login-cas");
		return bean;
	}

	/**
	 * CAS登录过滤器
	 */
	@Bean
	public CasAuthenticationFilter shiroCasFilter(AuthorizingRealm authorizingRealm) {
		CasAuthenticationFilter bean = new CasAuthenticationFilter();
		bean.setAuthorizingRealm(authorizingRealm);
		return bean;
	}

	/**
	 * Form登录过滤器
	 */
	@Bean
	public FormAuthenticationFilter shiroAuthcFilter(AuthorizingRealm authorizingRealm) {
		FormAuthenticationFilter bean = new FormAuthenticationFilter();
		bean.setAuthorizingRealm(authorizingRealm);
		return bean;
	}

//	/**
//	 * 登出过滤器
//	 */
//	@Bean
//	public LogoutFilter shiroLogoutFilter() {
//		return new LogoutFilter();
//	}

	/**
	 * 权限字符串过滤器
	 */
	@Bean
	public PermissionsAuthorizationFilter shiroPermsFilter() {
		return new PermissionsAuthorizationFilter();
	}

//	/**
//	 * 角色权限过滤器
//	 */
//	@Bean
//	public RolesAuthorizationFilter shiroRolesFilter() {
//		return new RolesAuthorizationFilter();
//	}

	/**
	 * 用户权限过滤器
	 */
	@Bean
	public UserFilter shiroUserFilter() {
		return new UserFilter();
	}

	/**
	 * URL过滤定义
	 */
	@Bean
	public FilterChainDefinitionMap shiroFilterChainDefinitionMap() {
		FilterChainDefinitionMap bean = new FilterChainDefinitionMap();
		bean.setFilterChainDefinitions(Global.getProperty("shiro.filterChainDefinitions"));
		bean.setDefaultFilterChainDefinitions(Global.getProperty("shiro.defaultFilterChainDefinitions"));
		return bean;
	}

	/**
	 * Shiro认证过滤器
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(WebSecurityManager securityManager, CasAuthenticationFilter shiroCasFilter,
			FormAuthenticationFilter shiroAuthcFilter,
//			LogoutFilter shiroLogoutFilter,
			PermissionsAuthorizationFilter shiroPermsFilter,
//			RolesAuthorizationFilter shiroRolesFilter,
			UserFilter shiroUserFilter, FilterChainDefinitionMap shiroFilterChainDefinitionMap) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl(Global.getProperty("shiro.loginUrl"));
		bean.setSuccessUrl(Global.getProperty("shiro.successUrl"));
		Map<String, Filter> filters = bean.getFilters();
		filters.put("cas", shiroCasFilter);
		filters.put("authc", shiroAuthcFilter);
//		filters.put("logout", shiroLogoutFilter);
		filters.put("perms", shiroPermsFilter);
//		filters.put("roles", shiroRolesFilter);
		filters.put("user", shiroUserFilter);
		bean.setFilterChainDefinitionMap(shiroFilterChainDefinitionMap.getObject());
		return bean;
	}

	/**
	 * 定义Shiro安全管理配置
	 */
	@Bean
	public WebSecurityManager securityManager(AuthorizingRealm authorizingRealm, SessionManager sessionManager, CacheManager shiroCacheManager) {
		WebSecurityManager bean = new WebSecurityManager();
		bean.setRealm(authorizingRealm);
		bean.setSessionManager(sessionManager);
		bean.setCacheManager(shiroCacheManager);
		// 设置支持CAS的subjectFactory
		bean.setSubjectFactory(new CasSubjectFactory());
		return bean;
	}

}

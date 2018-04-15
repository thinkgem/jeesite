/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config;

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
import com.jeesite.common.shiro.filter.LogoutFilter;
import com.jeesite.common.shiro.filter.PermissionsAuthorizationFilter;
import com.jeesite.common.shiro.filter.RolesAuthorizationFilter;
import com.jeesite.common.shiro.filter.UserFilter;
import com.jeesite.common.shiro.realm.AuthorizingRealm;
import com.jeesite.common.shiro.session.SessionDAO;
import com.jeesite.common.shiro.session.SessionManager;
import com.jeesite.common.shiro.web.ShiroFilterFactoryBean;
import com.jeesite.common.shiro.web.WebSecurityManager;

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
	public AuthorizingRealm authorizingRealm(SessionDAO sessionDAO, CasOutHandler casOutHandler) {
		AuthorizingRealm bean = new AuthorizingRealm();
		bean.setCachingEnabled(false);
		bean.setSessionDAO(sessionDAO);
		bean.setCasOutHandler(casOutHandler);
		bean.setCasServerUrl(Global.getProperty("shiro.casServerUrl"));
		bean.setCasServerCallbackUrl(Global.getProperty("shiro.casClientUrl") + Global.getAdminPath() + "/login-cas");
		return bean;
	}

	/**
	 * CAS登录过滤器
	 */
	private CasAuthenticationFilter shiroCasFilter(AuthorizingRealm authorizingRealm) {
		CasAuthenticationFilter bean = new CasAuthenticationFilter();
		bean.setAuthorizingRealm(authorizingRealm);
		return bean;
	}

	/**
	 * Form登录过滤器
	 */
	private FormAuthenticationFilter shiroAuthcFilter(AuthorizingRealm authorizingRealm) {
		FormAuthenticationFilter bean = new FormAuthenticationFilter();
		bean.setAuthorizingRealm(authorizingRealm);
		return bean;
	}

	/**
	 * 登出过滤器
	 */
	private LogoutFilter shiroLogoutFilter() {
		return new LogoutFilter();
	}

	/**
	 * 权限字符串过滤器
	 */
	private PermissionsAuthorizationFilter shiroPermsFilter() {
		return new PermissionsAuthorizationFilter();
	}

	/**
	 * 角色权限过滤器
	 */
	private RolesAuthorizationFilter shiroRolesFilter() {
		return new RolesAuthorizationFilter();
	}

	/**
	 * 用户权限过滤器
	 */
	private UserFilter shiroUserFilter() {
		return new UserFilter();
	}

	/**
	 * Shiro认证过滤器
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(WebSecurityManager securityManager,
			AuthorizingRealm authorizingRealm) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl(Global.getProperty("shiro.loginUrl"));
		bean.setSuccessUrl(Global.getProperty("shiro.successUrl"));
		Map<String, Filter> filters = bean.getFilters();
		filters.put("cas", shiroCasFilter(authorizingRealm));
		filters.put("authc", shiroAuthcFilter(authorizingRealm));
		filters.put("logout", shiroLogoutFilter());
		filters.put("perms", shiroPermsFilter());
		filters.put("roles", shiroRolesFilter());
		filters.put("user", shiroUserFilter());
		FilterChainDefinitionMap chains = new FilterChainDefinitionMap();
		chains.setFilterChainDefinitions(Global.getProperty("shiro.filterChainDefinitions"));
		chains.setDefaultFilterChainDefinitions(Global.getProperty("shiro.defaultFilterChainDefinitions"));
		bean.setFilterChainDefinitionMap(chains.getObject());
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

	/**
	 * Shiro 生命周期处理器，实现初始化和销毁回调
	 */
	@Bean(name="lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * Shiro 过滤器代理配置
	 */
	@Bean
	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator bean = new DefaultAdvisorAutoProxyCreator();
		bean.setProxyTargetClass(true);
		return bean;
	}

	/**
	 * 启用Shrio授权注解拦截方式，AOP式方法级权限检查
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(WebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor bean = new AuthorizationAttributeSourceAdvisor();
		bean.setSecurityManager(securityManager);
		return bean;
	}
	
//	/**
//	 * 在方法中 注入 securityManager 进行代理控制
//	 */
//	@Bean
//	public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultWebSecurityManager securityManager) {
//		MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
//		bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
//		bean.setArguments(new Object[] { securityManager });
//		return bean;
//	}
	
}

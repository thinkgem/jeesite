/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config;

import java.util.Collection;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.shiro.cas.CasOutHandler;
import com.jeesite.common.shiro.config.FilterChainDefinitionMap;
import com.jeesite.common.shiro.filter.CasAuthenticationFilter;
import com.jeesite.common.shiro.filter.FormAuthenticationFilter;
import com.jeesite.common.shiro.filter.InnerFilter;
import com.jeesite.common.shiro.filter.LogoutFilter;
import com.jeesite.common.shiro.filter.PermissionsAuthorizationFilter;
import com.jeesite.common.shiro.filter.RolesAuthorizationFilter;
import com.jeesite.common.shiro.filter.UserFilter;
import com.jeesite.common.shiro.realm.AuthorizingRealm;
import com.jeesite.common.shiro.realm.CasAuthorizingRealm;
import com.jeesite.common.shiro.session.SessionDAO;
import com.jeesite.common.shiro.session.SessionManager;
import com.jeesite.common.shiro.web.ShiroFilterFactoryBean;
import com.jeesite.common.shiro.web.WebSecurityManager;

/**
 * Shiro配置
 * @author ThinkGem
 * @version 2018-7-11
 */
@SuppressWarnings("deprecation")
@Configuration
public class ShiroConfig {
	
	/**
	 * Apache Shiro Filter
	 * @throws Exception 
	 */
	@Bean
	@Order(3000)
	@ConditionalOnMissingBean(name="shiroFilterProxy")
	public FilterRegistrationBean<Filter> shiroFilterProxy(ShiroFilterFactoryBean shiroFilter) throws Exception {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setFilter((Filter) shiroFilter.getInstance());
		bean.addUrlPatterns("/*");
		return bean;
	}

	/**
	 * 内部系统访问过滤器
	 */
	private InnerFilter shiroInnerFilter() {
		return new InnerFilter();
	}
	
	/**
	 * CAS登录过滤器
	 */
	private CasAuthenticationFilter shiroCasFilter(CasAuthorizingRealm casAuthorizingRealm) {
		CasAuthenticationFilter bean = new CasAuthenticationFilter();
		bean.setAuthorizingRealm(casAuthorizingRealm);
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
	private LogoutFilter shiroLogoutFilter(AuthorizingRealm authorizingRealm) {
		LogoutFilter bean = new LogoutFilter();
		bean.setAuthorizingRealm(authorizingRealm);
		return bean;
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
			AuthorizingRealm authorizingRealm, CasAuthorizingRealm casAuthorizingRealm) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl(Global.getProperty("shiro.loginUrl"));
		bean.setSuccessUrl(Global.getProperty("adminPath")+"/index");
		Map<String, Filter> filters = bean.getFilters();
		filters.put("inner", shiroInnerFilter());
		filters.put("cas", shiroCasFilter(casAuthorizingRealm));
		filters.put("authc", shiroAuthcFilter(authorizingRealm));
		filters.put("logout", shiroLogoutFilter(authorizingRealm));
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
	 * 系统安全认证实现类
	 */
	@Bean
	public AuthorizingRealm authorizingRealm(SessionDAO sessionDAO) {
		AuthorizingRealm bean = new AuthorizingRealm();
		bean.setSessionDAO(sessionDAO);
		return bean;
	}
	
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
	public CasAuthorizingRealm casAuthorizingRealm(SessionDAO sessionDAO, CasOutHandler casOutHandler) {
		CasAuthorizingRealm bean = new CasAuthorizingRealm();
		bean.setSessionDAO(sessionDAO);
		bean.setCasOutHandler(casOutHandler);
		bean.setCasServerUrl(Global.getProperty("shiro.casServerUrl"));
		bean.setCasServerCallbackUrl(Global.getProperty("shiro.casClientUrl") + Global.getAdminPath() + "/login-cas");
		return bean;
	}

	/**
	 * 定义Shiro安全管理配置
	 */
	@Bean
	public WebSecurityManager securityManager(AuthorizingRealm authorizingRealm,
			CasAuthorizingRealm casAuthorizingRealm, SessionManager sessionManager,
			CacheManager shiroCacheManager) {
		WebSecurityManager bean = new WebSecurityManager();
		Collection<Realm> realms = ListUtils.newArrayList();
		realms.add(authorizingRealm); // 第一个为权限授权控制类
		realms.add(casAuthorizingRealm);
		bean.setRealms(realms);
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

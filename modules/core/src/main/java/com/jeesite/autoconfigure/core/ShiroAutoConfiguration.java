/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.autoconfigure.core;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.shiro.cas.CasOutHandler;
import com.jeesite.common.shiro.cas.CasSubjectFactory;
import com.jeesite.common.shiro.config.FilterChainDefinitionMap;
import com.jeesite.common.shiro.filter.*;
import com.jeesite.common.shiro.realm.AuthorizingRealm;
import com.jeesite.common.shiro.realm.CasAuthorizingRealm;
import com.jeesite.common.shiro.realm.LdapAuthorizingRealm;
import com.jeesite.common.shiro.session.SessionDAO;
import com.jeesite.common.shiro.session.SessionManager;
import com.jeesite.common.shiro.web.ShiroFilterFactoryBean;
import com.jeesite.common.shiro.web.WebSecurityManager;
import jakarta.servlet.Filter;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.filter.InvalidRequestFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Collection;
import java.util.Map;

/**
 * Shiro配置
 * @author ThinkGem
 * @version 2023-12-20
 */
@AutoConfiguration(before = SessionAutoConfiguration.class)
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public class ShiroAutoConfiguration {

	/**
	 * Apache Shiro Filter
	 */
	@Bean("shiroFilterProxy")
	@Order(Ordered.HIGHEST_PRECEDENCE + 5000)
	@ConditionalOnMissingBean(name="shiroFilterProxy")
	public FilterRegistrationBean<Filter> shiroFilterProxy(ShiroFilterFactoryBean shiroFilter) throws Exception {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setFilter(shiroFilter.getObject());
		bean.addUrlPatterns("/*");
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 5000);
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
	private CasFilter shiroCasFilter(CasAuthorizingRealm casAuthorizingRealm) {
		CasFilter bean = new CasFilter();
		bean.setAuthorizingRealm(casAuthorizingRealm);
		return bean;
	}

	/**
	 * LDAP登录过滤器
	 */
	private LdapFilter shiroLdapFilter(LdapAuthorizingRealm ldapAuthorizingRealm) {
		LdapFilter bean = new LdapFilter();
		bean.setAuthorizingRealm(ldapAuthorizingRealm);
		return bean;
	}

	/**
	 * Form登录过滤器
	 */
	private FormFilter shiroAuthcFilter(AuthorizingRealm authorizingRealm) {
		FormFilter bean = new FormFilter();
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
	private PermissionsFilter shiroPermsFilter() {
		return new PermissionsFilter();
	}

	/**
	 * 角色权限过滤器
	 */
	private RolesFilter shiroRolesFilter() {
		return new RolesFilter();
	}

	/**
	 * 用户权限过滤器
	 */
	private UserFilter shiroUserFilter() {
		return new UserFilter();
	}

	/**
	 * 非法请求过滤器
	 */
	private InvalidRequestFilter invalidRequestFilter() {
		InvalidRequestFilter bean = new InvalidRequestFilter();
		bean.setBlockNonAscii(false);
		return bean;
	}

	/**
	 * Shiro认证过滤器
	 */
	@Bean("shiroFilter")
	@ConditionalOnMissingBean(name="shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(WebSecurityManager webSecurityManager, AuthorizingRealm authorizingRealm,
			CasAuthorizingRealm casAuthorizingRealm, LdapAuthorizingRealm ldapAuthorizingRealm) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(webSecurityManager);
		bean.setLoginUrl(Global.getProperty("shiro.loginUrl"));
		bean.setSuccessUrl(Global.getProperty("adminPath")+"/index");
		Map<String, Filter> filters = bean.getFilters();
		filters.put("inner", shiroInnerFilter());
		filters.put("cas", shiroCasFilter(casAuthorizingRealm));
		filters.put("ldap", shiroLdapFilter(ldapAuthorizingRealm));
		filters.put("authc", shiroAuthcFilter(authorizingRealm));
		filters.put("logout", shiroLogoutFilter(authorizingRealm));
		filters.put("perms", shiroPermsFilter());
		filters.put("roles", shiroRolesFilter());
		filters.put("user", shiroUserFilter());
		filters.put("invalidRequest", invalidRequestFilter());
		FilterChainDefinitionMap chains = new FilterChainDefinitionMap();
		chains.setFilterChainDefinitions(Global.getProperty("shiro.filterChainDefinitions"));
		chains.setDefaultFilterChainDefinitions(Global.getProperty("shiro.defaultFilterChainDefinitions"));
		bean.setFilterChainDefinitionMap(chains.getObject());
		return bean;
	}

	/**
	 * 系统安全认证实现类
	 */
	@Bean("authorizingRealm")
	@ConditionalOnMissingBean(name="authorizingRealm")
	public AuthorizingRealm authorizingRealm(@Qualifier("sessionDAO") SessionDAO sessionDAO) {
		AuthorizingRealm bean = new AuthorizingRealm();
		bean.setSessionDAO(sessionDAO);
		return bean;
	}

	/**
	 * 单点登录信息句柄，单点退出用
	 */
	@Bean("casOutHandler")
	@ConditionalOnMissingBean(name="casOutHandler")
	public CasOutHandler casOutHandler() {
		return new CasOutHandler();
	}

	/**
	 * CAS安全认证实现类
	 */
	@Bean("casAuthorizingRealm")
	@ConditionalOnMissingBean(name="casAuthorizingRealm")
	public CasAuthorizingRealm casAuthorizingRealm(@Qualifier("sessionDAO") SessionDAO sessionDAO, CasOutHandler casOutHandler) {
		CasAuthorizingRealm bean = new CasAuthorizingRealm();
		bean.setSessionDAO(sessionDAO);
		bean.setCasOutHandler(casOutHandler);
		bean.setCasServerUrl(Global.getProperty("shiro.casServerUrl"));
		bean.setCasServerCallbackUrl(Global.getProperty("shiro.casClientUrl") + Global.getAdminPath() + "/login-cas");
		return bean;
	}

	/**
	 * LDAP安全认证实现类
	 */
	@Bean("ldapAuthorizingRealm")
	@ConditionalOnMissingBean(name="ldapAuthorizingRealm")
	public LdapAuthorizingRealm ldapAuthorizingRealm(@Qualifier("sessionDAO") SessionDAO sessionDAO, CasOutHandler casOutHandler) {
		LdapAuthorizingRealm bean = new LdapAuthorizingRealm();
		JndiLdapContextFactory contextFactory = (JndiLdapContextFactory) bean.getContextFactory();
		contextFactory.setUrl(Global.getProperty("shiro.ldapUrl"/*, "ldap://127.0.0.1:389"*/));
		bean.setUserDnTemplate(Global.getProperty("shiro.ldapUserDn"/*, "uid={0},ou=users,dc=mycompany,dc=com"*/));
		bean.setSessionDAO(sessionDAO);
		return bean;
	}

	/**
	 * 定义Shiro安全管理配置
	 */
	@Bean("webSecurityManager")
	@ConditionalOnMissingBean(name="webSecurityManager")
	public WebSecurityManager webSecurityManager(AuthorizingRealm authorizingRealm, CasAuthorizingRealm casAuthorizingRealm,
			LdapAuthorizingRealm ldapAuthorizingRealm, SessionManager sessionManager, @Qualifier("shiroCacheManager") CacheManager shiroCacheManager) {
		WebSecurityManager bean = new WebSecurityManager();
		Collection<Realm> realms = ListUtils.newArrayList();
		realms.add(authorizingRealm); // 第一个为权限授权控制类
		realms.add(casAuthorizingRealm);
		realms.add(ldapAuthorizingRealm);
		bean.setRealms(realms);
		bean.setSessionManager(sessionManager);
		bean.setCacheManager(shiroCacheManager);
		bean.setSubjectFactory(new CasSubjectFactory());
		//bean.setRememberMeManager(null); // 关闭 RememberMe
		return bean;
	}

	/**
	 * Shiro 生命周期处理器，实现初始化和销毁回调
	 */
	@Bean("lifecycleBeanPostProcessor")
	@ConditionalOnMissingBean(name="lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * Shiro 过滤器代理配置
	 */
	@Bean("defaultAdvisorAutoProxyCreator")
	@DependsOn({ "lifecycleBeanPostProcessor" })
	@ConditionalOnMissingBean(name="defaultAdvisorAutoProxyCreator")
	public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator bean = new DefaultAdvisorAutoProxyCreator();
		bean.setProxyTargetClass(true);
		return bean;
	}

	/**
	 * 启用Shrio授权注解拦截方式，AOP式方法级权限检查
	 */
	@Bean("authorizationAttributeSourceAdvisor")
	@ConditionalOnMissingBean(name="authorizationAttributeSourceAdvisor")
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(WebSecurityManager webSecurityManager) {
		AuthorizationAttributeSourceAdvisor bean = new AuthorizationAttributeSourceAdvisor();
		bean.setSecurityManager(webSecurityManager);
		return bean;
	}
}

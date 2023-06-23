/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.realm;

import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.realm.ldap.DefaultLdapRealm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.shiro.authc.LdapToken;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.LogUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统认证授权实现类
 * @author ThinkGem
 * @version 2021-7-6
 */
public class LdapAuthorizingRealm extends BaseAuthorizingRealm  {

    private static final Logger log = LoggerFactory.getLogger(DefaultLdapRealm.class);

    //The zero index currently means nothing, but could be utilized in the future for other substitution techniques.
    private static final String USERDN_SUBSTITUTION_TOKEN = "{0}";

    private String userDnPrefix;
    private String userDnSuffix;

    /**
     * The LdapContextFactory instance used to acquire {@link javax.naming.ldap.LdapContext LdapContext}'s at runtime
     * to acquire connections to the LDAP directory to perform authentication attempts and authorizatino queries.
     */
    private LdapContextFactory contextFactory;

	private UserService userService;
	private EmpUserService empUserService;

    /**
     * Default no-argument constructor that defaults the internal {@link LdapContextFactory} instance to a
     * {@link JndiLdapContextFactory}.
     */
    public LdapAuthorizingRealm() {
    	super();
        //Credentials Matching is not necessary - the LDAP directory will do it automatically:
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        //Any Object principal and Object credentials may be passed to the LDAP provider, so accept any token:
        setAuthenticationTokenClass(LdapToken.class);
        this.contextFactory = new JndiLdapContextFactory();
    }
	
	@Override
	protected FormToken getFormToken(AuthenticationToken authcToken) {
		HttpServletRequest request = ServletUtils.getRequest();
		if (authcToken == null){
			return null;
		}
        LdapToken ldapToken = (LdapToken) authcToken;
		
        // LDAP 身份认证
        LdapContext ctx = null;
        try {
        	Object principal = getUserDn(ldapToken.getUsername());
			Object credentials = String.valueOf(ldapToken.getPassword());
            log.debug("Authenticating user '{}' through LDAP", principal);
            ctx = getContextFactory().getLdapContext(principal, credentials);
        } catch (AuthenticationNotSupportedException e) {
            throw new UnsupportedAuthenticationMechanismException("msg:LDAP 不支持的授权类型", e);
        } catch (javax.naming.AuthenticationException e) {
            throw new AuthenticationException("msg:LDAP 授权失败："+e.getMessage(), e);
        } catch (NamingException e) {
            throw new AuthenticationException("msg:LDAP 连接失败："+e.getMessage(), e);
        } catch (Exception e) {
            throw new AuthenticationException("msg:LDAP 登录失败："+e.getMessage(), e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
		
		// 生成登录信息对象
		FormToken token = new FormToken(request);
        token.setUsername(ldapToken.getUsername());
        token.setPassword(ldapToken.getPassword());
        token.setParams(ldapToken.getParams());
        return token;
	}
	
	@Override
	protected User getUserInfo(FormToken token) {
		User user = super.getUserInfo(token);
		if (user == null){
			throw new AuthenticationException("msg:用户 “" + token.getUsername() + "” 在本系统中不存在, 请联系管理员.");
		}
		return user;
	}
	
	@Override
	protected void assertCredentialsMatch(AuthenticationToken authcToken,
			AuthenticationInfo info) throws AuthenticationException {
		// 已经在 getFormToken 认证过了，这里就不验证身份了
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(LoginInfo loginInfo, Subject subject, Session session, User user) {
		return super.doGetAuthorizationInfo(loginInfo, subject, session, user);
	}
	
	@Override
	public void onLoginSuccess(LoginInfo loginInfo, HttpServletRequest request) {
		super.onLoginSuccess(loginInfo, request);

		//System.out.print("__sid: "+request.getSession().getId());
		//System.out.println(" == "+UserUtils.getSession().getId());
		
		// 更新登录IP、时间、会话ID等
		User user = UserUtils.get(loginInfo.getId());
		getUserService().updateUserLoginInfo(user);
		
		// 记录用户登录日志
		LogUtils.saveLog(user, ServletUtils.getRequest(), "系统登录", Log.TYPE_LOGIN_LOGOUT);
	}
	
	@Override
	public void onLogoutSuccess(LoginInfo loginInfo, HttpServletRequest request) {
		super.onLogoutSuccess(loginInfo, request);
		
		// 记录用户退出日志
		User user = UserUtils.get(loginInfo.getId());
		LogUtils.saveLog(user, request, "系统退出", Log.TYPE_LOGIN_LOGOUT);
	}

	public UserService getUserService() {
		if (userService == null){
			userService = SpringUtils.getBean(UserService.class);
		}
		return userService;
	}

	public EmpUserService getEmpUserService() {
		if (empUserService == null){
			empUserService = SpringUtils.getBean(EmpUserService.class);
		}
		return empUserService;
	}

    /**
     * Returns the User DN prefix to use when building a runtime User DN value or {@code null} if no
     * {@link #getUserDnTemplate() userDnTemplate} has been configured.  If configured, this value is the text that
     * occurs before the {@link #USERDN_SUBSTITUTION_TOKEN} in the {@link #getUserDnTemplate() userDnTemplate} value.
     *
     * @return the the User DN prefix to use when building a runtime User DN value or {@code null} if no
     *         {@link #getUserDnTemplate() userDnTemplate} has been configured.
     */
    protected String getUserDnPrefix() {
        return userDnPrefix;
    }

    /**
     * Returns the User DN suffix to use when building a runtime User DN value.  or {@code null} if no
     * {@link #getUserDnTemplate() userDnTemplate} has been configured.  If configured, this value is the text that
     * occurs after the {@link #USERDN_SUBSTITUTION_TOKEN} in the {@link #getUserDnTemplate() userDnTemplate} value.
     *
     * @return the User DN suffix to use when building a runtime User DN value or {@code null} if no
     *         {@link #getUserDnTemplate() userDnTemplate} has been configured.
     */
    protected String getUserDnSuffix() {
        return userDnSuffix;
    }

    /**
     * Sets the User Distinguished Name (DN) template to use when creating User DNs at runtime.  A User DN is an LDAP
     * fully-qualified unique user identifier which is required to establish a connection with the LDAP
     * directory to authenticate users and query for authorization information.
     * <h2>Usage</h2>
     * User DN formats are unique to the LDAP directory's schema, and each environment differs - you will need to
     * specify the format corresponding to your directory.  You do this by specifying the full User DN as normal, but
     * but you use a <b>{@code {0}}</b> placeholder token in the string representing the location where the
     * user's submitted principal (usually a username or uid) will be substituted at runtime.
     * <p/>
     * For example,  if your directory
     * uses an LDAP {@code uid} attribute to represent usernames, the User DN for the {@code jsmith} user may look like
     * this:
     * <p/>
     * <pre>uid=jsmith,ou=users,dc=mycompany,dc=com</pre>
     * <p/>
     * in which case you would set this property with the following template value:
     * <p/>
     * <pre>uid=<b>{0}</b>,ou=users,dc=mycompany,dc=com</pre>
     * <p/>
     * If no template is configured, the raw {@code AuthenticationToken}
     * {@link AuthenticationToken#getPrincipal() principal} will be used as the LDAP principal.  This is likely
     * incorrect as most LDAP directories expect a fully-qualified User DN as opposed to the raw uid or username.  So,
     * ensure you set this property to match your environment!
     *
     * @param template the User Distinguished Name template to use for runtime substitution
     * @throws IllegalArgumentException if the template is null, empty, or does not contain the
     *                                  {@code {0}} substitution token.
     * @see LdapContextFactory#getLdapContext(Object,Object)
     */
    public void setUserDnTemplate(String template) throws IllegalArgumentException {
        if (!StringUtils.hasText(template)) {
            return;
        }
        int index = template.indexOf(USERDN_SUBSTITUTION_TOKEN);
        if (index < 0) {
            String msg = "User DN template must contain the '" +
                    USERDN_SUBSTITUTION_TOKEN + "' replacement token to understand where to " +
                    "insert the runtime authentication principal.";
            throw new IllegalArgumentException(msg);
        }
        String prefix = template.substring(0, index);
        String suffix = template.substring(prefix.length() + USERDN_SUBSTITUTION_TOKEN.length());
        if (log.isDebugEnabled()) {
            log.debug("Determined user DN prefix [{}] and suffix [{}]", prefix, suffix);
        }
        this.userDnPrefix = prefix;
        this.userDnSuffix = suffix;
    }

    /**
     * Returns the User Distinguished Name (DN) template to use when creating User DNs at runtime - see the
     * {@link #setUserDnTemplate(String) setUserDnTemplate} JavaDoc for a full explanation.
     *
     * @return the User Distinguished Name (DN) template to use when creating User DNs at runtime.
     */
    public String getUserDnTemplate() {
        return getUserDn(USERDN_SUBSTITUTION_TOKEN);
    }

    /**
     * Returns the LDAP User Distinguished Name (DN) to use when acquiring an
     * {@link javax.naming.ldap.LdapContext LdapContext} from the {@link LdapContextFactory}.
     * <p/>
     * If the the {@link #getUserDnTemplate() userDnTemplate} property has been set, this implementation will construct
     * the User DN by substituting the specified {@code principal} into the configured template.  If the
     * {@link #getUserDnTemplate() userDnTemplate} has not been set, the method argument will be returned directly
     * (indicating that the submitted authentication token principal <em>is</em> the User DN).
     *
     * @param principal the principal to substitute into the configured {@link #getUserDnTemplate() userDnTemplate}.
     * @return the constructed User DN to use at runtime when acquiring an {@link javax.naming.ldap.LdapContext}.
     * @throws IllegalArgumentException if the method argument is null or empty
     * @throws IllegalStateException    if the {@link #getUserDnTemplate userDnTemplate} has not been set.
     * @see LdapContextFactory#getLdapContext(Object, Object)
     */
    protected String getUserDn(String principal) throws IllegalArgumentException, IllegalStateException {
        if (!StringUtils.hasText(principal)) {
            throw new IllegalArgumentException("User principal cannot be null or empty for User DN construction.");
        }
        String prefix = getUserDnPrefix();
        String suffix = getUserDnSuffix();
        if (prefix == null && suffix == null) {
            log.debug("userDnTemplate property has not been configured, indicating the submitted " +
                    "AuthenticationToken's principal is the same as the User DN.  Returning the method argument " +
                    "as is.");
            return principal;
        }

        int prefixLength = prefix != null ? prefix.length() : 0;
        int suffixLength = suffix != null ? suffix.length() : 0;
        StringBuilder sb = new StringBuilder(prefixLength + principal.length() + suffixLength);
        if (prefixLength > 0) {
            sb.append(prefix);
        }
        sb.append(principal);
        if (suffixLength > 0) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * Sets the LdapContextFactory instance used to acquire connections to the LDAP directory during authentication
     * attempts and authorization queries.  Unless specified otherwise, the default is a {@link JndiLdapContextFactory}
     * instance.
     *
     * @param contextFactory the LdapContextFactory instance used to acquire connections to the LDAP directory during
     *                       authentication attempts and authorization queries
     */
    public void setContextFactory(LdapContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    /**
     * Returns the LdapContextFactory instance used to acquire connections to the LDAP directory during authentication
     * attempts and authorization queries.  Unless specified otherwise, the default is a {@link JndiLdapContextFactory}
     * instance.
     *
     * @return the LdapContextFactory instance used to acquire connections to the LDAP directory during
     *         authentication attempts and authorization queries
     */
    public LdapContextFactory getContextFactory() {
        return this.contextFactory;
    }

}

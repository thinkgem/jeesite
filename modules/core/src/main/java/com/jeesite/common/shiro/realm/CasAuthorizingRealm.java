/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.shiro.realm;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasToken;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.shiro.cas.CasCreateUser;
import com.jeesite.common.shiro.cas.CasOutHandler;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.LogUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2018-7-11
 */
@SuppressWarnings("deprecation")
public class CasAuthorizingRealm extends BaseAuthorizingRealm  {

	private UserService userService;
	private EmpUserService empUserService;
	
	private CasOutHandler casOutHandler;
	private String casServerUrl; 			// CAS 服务器地址
    private String casServerCallbackUrl; 	// CAS 服务器回调地址
    private TicketValidator ticketValidator;// CAS 令牌验证类
	
	public CasAuthorizingRealm() {
		super();
		this.setAuthenticationTokenClass(CasToken.class);
	}
	
	@Override
	protected FormToken getFormToken(AuthenticationToken authcToken) {
		
		// 单点登录登出句柄（登出时注销session）有CAS中央服务器调用
		HttpServletRequest request = ServletUtils.getRequest();
		if (casOutHandler.isLogoutRequest(request)) {
			LoginInfo loginInfo = casOutHandler.destroySession(request);
			if (loginInfo != null){
				this.onLogoutSuccess(loginInfo, request);
			}
			return null;
		}
		
		if (authcToken == null){
			return null;
		}
		
		CasToken casToken = (CasToken) authcToken;
		String ticket = (String) casToken.getCredentials();
		if (ticketValidator == null) {
            ticketValidator = new Cas20ServiceTicketValidator(casServerUrl);
            ((Cas20ServiceTicketValidator)ticketValidator).setEncoding("UTF-8");
        }
		
		// 进行登录身份验证
		Assertion casAssertion = null;
		try {
			casAssertion = ticketValidator.validate(ticket, casServerCallbackUrl);
		} catch (TicketValidationException e) {
			// 令牌失效，在LogoutFilter会自动跳转到登录页
			return null;
		}
		AttributePrincipal casPrincipal = casAssertion.getPrincipal();
		casToken.setUserId(casPrincipal.getName());
		
		// 生成登录信息对象
		FormToken token = new FormToken();
        token.setUsername(casPrincipal.getName());
        Map<String, Object> params = MapUtils.newHashMap();
        params.putAll(casPrincipal.getAttributes());
        params.put("ticket", ticket);
        token.setParams(params);
        return token;
	}
	
	@Override
	protected User getUserInfo(FormToken token) {
		
		User user = super.getUserInfo(token);
		if (user == null){
			Map<String, Object> attrs = token.getParams();
			
			// 如果允许客户端创建账号，则创建账号
			if (ObjectUtils.toBoolean(attrs.get("isAllowClientCreateUser"))){
				
				// 获取CAS传递过来的用户属性信息
				user = new User(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("userCode"))));
				user.setLoginCode(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("loginCode"))));
				user.setPassword(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("password"))));
				user.setUserName(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("userName"))));
				user.setEmail(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("email"))));
				user.setMobile(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("mobile"))));
				user.setPhone(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("phone"))));
				user.setUserType(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("userType"))));
				user.setRefCode(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("refCode"))));
				user.setRefName(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("refName"))));
				user.setMgrType(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("mgrType"))));
				user.setStatus(EncodeUtils.decodeUrl(ObjectUtils.toString(attrs.get("status"))));
				
				// 如果是员工类型，则平台自动创建
				if (User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
					
					// 保存员工和用户
					try{
						EmpUser empUser = new EmpUser();
						empUser.setIsNewRecord(true);
						empUser.setMobile(user.getMobile());
						empUser.setEmail(user.getEmail());
						empUser.setPhone(user.getPhone());
						empUser.getEmployee().getCompany().setCompanyCode(EncodeUtils
								.decodeUrl(ObjectUtils.toString(attrs.get("companyCode"))));
						empUser.getEmployee().getOffice().setOfficeCode(EncodeUtils
								.decodeUrl(ObjectUtils.toString(attrs.get("officeCode"))));
						getEmpUserService().save(empUser);
					}catch(ValidationException ve){
						throw new AuthenticationException("msg:" + ve.getMessage());
					}
					
					// 重新获取用户登录
					user = UserUtils.getByLoginCode(token.getUsername()/*, corpCode*/);
					if (user != null) {
						return user;
					}
					
				}
				
				// 其它类型，根据项目需要自行创建
				else{
					try{
						CasCreateUser casCreateUser = SpringUtils.getBean(CasCreateUser.class);
						if(casCreateUser != null){
							casCreateUser.createUser(user, attrs);
						}
					}catch(NoSuchBeanDefinitionException e){
						throw new AuthenticationException("msg:用户 “" + token.getUsername()
								+ "”, 类型 “" + user.getUserType() + "” 在本系统中不存在, 请联系管理员.");
					}
				}
			}else{
				throw new AuthenticationException("msg:用户 “" + token.getUsername() + "” 在本系统中不存在, 请联系管理员.");
			}
		}
		return user;
	}
	
	@Override
	protected void assertCredentialsMatch(AuthenticationToken authcToken,
			AuthenticationInfo info) throws AuthenticationException {
		// CAS的Ticket已经在doGetAuthenticationInfo()认证过了，这里就不验证身份了
	}
	
	@Override
	public void onLoginSuccess(LoginInfo loginInfo, HttpServletRequest request) {
		super.onLoginSuccess(loginInfo, request);

		// 单点登录登出句柄（登录时注入session），在这之前必须获取下授权信息
		String ticket = loginInfo.getParam("ticket");
		casOutHandler.recordSession(request, ticket);
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
	
    public void setCasOutHandler(CasOutHandler casOutHandler) {
		this.casOutHandler = casOutHandler;
	}

	public void setCasServerUrl(String casServerUrl) {
		this.casServerUrl = casServerUrl;
	}

	public void setCasServerCallbackUrl(String casServerCallbackUrl) {
		this.casServerCallbackUrl = casServerCallbackUrl;
	}
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.utils.ValidCodeUtils;

/**
 * 账号自助服务Controller
 * @author ThinkGem
 * @version 2017-12-7
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseController{

	@Autowired
	private UserService userService;

	/**
	 * 忘记密码页面
	 */
	@RequestMapping(value = "forgetPwd")
	public String forgetPwd(Model model) {
		return "modules/sys/account/forgetPwd";
	}
	
	/**
	 * 获取短信、邮件验证码
	 * @param validCode 图片验证码，防止重复机器人。
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getValidCode")
	@ResponseBody
	public String getValidCode(User user, String validCode, String validType, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, "验证码不正确或已失效！");
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, "非法操作。");
		}
		User u = userService.getByLoginCode(user);
		if(u == null){
			return renderResult(Global.FALSE, "登录账号不正确！");
		}
		if("mobile".equals(validType) && StringUtils.isBlank(u.getMobile())){
			return renderResult(Global.FALSE, "该账号未设置手机号码！");
		}else if("email".equals(validType) && StringUtils.isBlank(u.getEmail())){
			return renderResult(Global.FALSE, "该账号未设置邮件地址！");
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("fpLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, "您当前操作太频繁，请稍等一会再操作！");
		}else{
			UserUtils.putCache("fpLastDate", new Date());
		}
		// 生成验证码，并缓存。
		String fpValidCode = StringUtils.getRandomNum(6);
		UserUtils.putCache("fpUserCode", u.getUserCode());
		UserUtils.putCache("fpLoginCode", u.getLoginCode());
		UserUtils.putCache("fpValidCode", fpValidCode);
		// 发送短信消息。
		if("mobile".equals(validType)){
			return sendSmsValidCode(u, fpValidCode, "找回密码");
		}
		// 发送邮件消息。
		else if("email".equals(validType)){
			return sendEmailValidCode(u, fpValidCode, "找回密码");
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码保存密码
	 */
	@PostMapping(value = "savePwdByValidCode")
	@ResponseBody
	public String savePwdByValidCode(User user, String fpValidCode, HttpServletRequest request) {
		String userCode = (String)UserUtils.getCache("fpUserCode");
		String loginCode = (String)UserUtils.getCache("fpLoginCode");
		String validCode = (String)UserUtils.getCache("fpValidCode");
		Date date = (Date)UserUtils.getCache("fpLastDate");
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}

		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(userCode != null && loginCode != null && loginCode.equals(user.getLoginCode())
				&& validCode != null && validCode.equals(fpValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, "验证码不正确或已失效！");
		}
		
		// 更新为新密码。
		try{
			userService.updatePassword(userCode, user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		
		// 修改密码成功后清理验证码，验证码只允许使用一次。
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");
		UserUtils.removeCache("fpValidCode");
		UserUtils.removeCache("fpLastDate");
		return renderResult(Global.TRUE, "恭喜你，您的账号 "+loginCode+" 密码修改成功！");
	}

	/**
	 * 获取保密问题
	 * @param validCode 图片验证码，防止重复机器人。
	 */
	@PostMapping(value = "getPwdQuestion")
	@ResponseBody
	public String getPwdQuestion(User user, String validCode, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, "验证码不正确或已失效！");
		}
		// 账号是否存在验证
		User u = userService.getByLoginCode(user);
		if (u == null){
			return renderResult(Global.FALSE, "登录账号不正确！");
		}
		// 获取保密问题，并缓存
		Map<String, String> data = MapUtils.newHashMap();
		data.put("pwdQuestion", u.getPwdQuestion());
		data.put("pwdQuestion2", u.getPwdQuestion2());
		data.put("pwdQuestion3", u.getPwdQuestion3());
		UserUtils.putCache("fpUserCode", u.getUserCode());
		UserUtils.putCache("fpLoginCode", u.getLoginCode());
		return renderResult(Global.TRUE, "获取密保问题成功！", data);
	}

	/**
	 * 校验密保问题答案
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping(value = "savePwdByPwdQuestion")
	@ResponseBody
	public String savePwdByPwdQuestion(User user, HttpServletRequest request) {
		String userCode = (String)UserUtils.getCache("fpUserCode");
		String loginCode = (String)UserUtils.getCache("fpLoginCode");
		User u = userService.getByLoginCode(user);
		
		// 验证三个密保问题是否正确。
		if (!(u != null && loginCode.equals(user.getLoginCode())
				&& UserService.validatePassword(user.getPwdQuestionAnswer(), u.getPwdQuestionAnswer())
				&& UserService.validatePassword(user.getPwdQuestionAnswer2(), u.getPwdQuestionAnswer2())
				&& UserService.validatePassword(user.getPwdQuestionAnswer3(), u.getPwdQuestionAnswer3()))) {
			return renderResult(Global.FALSE, "您填写的密保问题答案不正确！");
		}
		
		// 更新为新密码。
		try{
			userService.updatePassword(userCode, user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		
		// 验证成功后清理缓存。
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");
		return renderResult(Global.TRUE, "验证通过");
	}
	
	/**
	 * 用户注册页面
	 * @param user 用户信息参数
	 */
	@RequestMapping(value = "registerUser")
	@ResponseBody
	public String registerUser(User user, HttpServletRequest request) {
		return "modules/sys/account/registerUser";
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getRegisterUserValidCode")
	@ResponseBody
	public String getRegisterUserValidCode(User user, String validCode, String validType, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, "验证码不正确或已失效！");
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, "非法操作。");
		}
		// 非空数据校验。
		if (!(StringUtils.isNotBlank(user.getLoginCode()) && StringUtils.isNotBlank(user.getUserName()))){
			return renderResult(Global.FALSE, "登录账号和用户姓名不能为空！");
		}
		// 邮箱、手机号码是否填写验证
		if("email".equals(validType) && !StringUtils.isNotBlank(user.getEmail())){
			return renderResult(Global.FALSE, "电子邮箱不能为空！");
		}
		else if("mobile".equals(validType) && !StringUtils.isNotBlank(user.getMobile())){
			return renderResult(Global.FALSE, "手机号码不能为空！");
		}
		// 操作是否频繁验证，如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("regLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, "您当前操作太频繁，请稍等一会再操作！");
		}else{
			UserUtils.putCache("regLastDate", new Date());
		}
		// 验证用户编码是否存在。
		if (userService.getByLoginCode(user) != null){
			return renderResult(Global.FALSE, "登录账号已存在！");
		}
		// 生成验证码，并缓存。
		String code = StringUtils.getRandomNum(6);
		UserUtils.putCache("regCorpCode", user.getCorpCode());
		UserUtils.putCache("regCorpName", user.getCorpName());
		UserUtils.putCache("regLoginCode", user.getLoginCode());
		// 账号注册类型
		String[] userTypes = StringUtils.split(Global.getConfig(
				"sys.account.registerUser.userTypes.userTypes", "-1"), ",");
		if (StringUtils.inString(user.getUserType(), userTypes)){
			UserUtils.putCache("regUserType", user.getUserType());
		}else{
			return renderResult(Global.FALSE, "非法的用户类型！");
		}
		UserUtils.putCache("regEmail", user.getEmail());
		UserUtils.putCache("regMobile", user.getMobile());
		UserUtils.putCache("regValidCode", code);
		// 发送邮箱或短信验证码
		if("send_email".equals(validType)){
			return sendEmailValidCode(user, code, "注册账号");
		}else if("send_mobile".equals(validType)){
			return sendSmsValidCode(user, code, "注册账号");
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "saveRegisterUserByValidCode")
	@ResponseBody
	public String saveRegisterUserByValidCode(User user, String regValidCode, HttpServletRequest request) {
		if (!"true".equals(Global.getConfig("sys.account.registerUser"))){
			return renderResult(Global.FALSE, "当前系统没有开启注册功能！");
		}
		String corpCode = (String)UserUtils.getCache("regCorpCode");
		String corpName = (String)UserUtils.getCache("regCorpName");
		String userType = (String)UserUtils.getCache("regUserType");
		String loginCode = (String)UserUtils.getCache("regLoginCode");
		String email = (String)UserUtils.getCache("regEmail");
		String mobile = (String)UserUtils.getCache("regMobile");
		String validCode = (String)UserUtils.getCache("regValidCode");
		Date date = (Date)UserUtils.getCache("regLastDate");
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}

		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(loginCode != null && loginCode.equals(user.getLoginCode())
				&& validCode != null && validCode.equals(regValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, "验证码不正确或已失效！");
		}

		// 非空数据校验。
		if (!(StringUtils.isNotBlank(user.getLoginCode()) && StringUtils.isNotBlank(user.getUserName()))){
			return renderResult(Global.FALSE, "登录账号和用户姓名不能为空！");
		}
		
		// 新增并保存用户。
		User u = new User();
		u.setIsNewRecord(true);
		if (StringUtils.isNotBlank(corpCode)){
			u.setCorpCode(corpCode);
			u.setCorpName(corpName);
		}
		u.setLoginCode(loginCode);
		u.setUserName(user.getUserName());
		u.setEmail(email);
		u.setMobile(mobile);
		u.setUserType(userType);
		u.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		userService.save(u);
		try{
			userService.updatePassword(u.getUserCode(), user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		
		// 验证成功后清理验证码，验证码只允许使用一次。
		UserUtils.removeCache("regUserType");
		UserUtils.removeCache("regLoginCode");
		UserUtils.removeCache("regValidCode");
		UserUtils.removeCache("regLastDate");
	
		return renderResult(Global.TRUE, "恭喜你，您的账号 "+u.getLoginCode()+" 注册成功！");
	}
	
	/**
	 * 发送邮件验证码
	 */
	private String sendEmailValidCode(User user, String code, String title){
		try {
			Class<?> message = Class.forName("com.jeesite.modules.msg.entity.MsgPushEntity");
			Class<?> messageUtils = Class.forName("com.jeesite.modules.msg.utils.MessageUtils");
			Method method = messageUtils.getMethod("sendEmail", String.class, String.class, String.class, String.class, String.class);
			String contentTitle = user.getUserName() + "（" + user.getLoginCode() + "）"+title+"验证码";
			String contentText = "尊敬的用户，您好!\n\n您的验证码是：" + code +"（请勿透露给其他人）\n\n"
						+ "请复制后，填写在你的验证码窗口完成验证。\n\n本邮件由系统自动发出，请勿回复。\n\n感谢您的使用。";
			String receiverType = (String)message.getField("RECEIVER_TYPE_NONE").get(null);
			String receiverCodes = user.getEmail(), receiverNames = user.getUserName();
			method.invoke(null, contentTitle, contentText, receiverType, receiverCodes, receiverNames);
		} catch (ClassNotFoundException e) {
			return renderResult(Global.FALSE, "消息模块未安装，请联系管理员！");
		} catch (Exception e) {
			logger.error(title+"发送邮件错误。", e);
			return renderResult(Global.FALSE, "系统出现了点问题，错误信息：" + e.getMessage());
		}
		return renderResult(Global.TRUE, "邮件已发送，请接收并填写验证码！");
	}

	/**
	 * 发送短信验证码
	 */
	private String sendSmsValidCode(User user, String code, String title){
		try {
			Class<?> message = Class.forName("com.jeesite.modules.msg.entity.MsgPushEntity");
			Class<?> messageUtils = Class.forName("com.jeesite.modules.msg.utils.MessageUtils");
			Method method = messageUtils.getMethod("sendSms", String.class, String.class, String.class, String.class, String.class);
			String contentTitle = user.getUserName() + "（" + user.getLoginCode() + "）"+title+"验证码";
			String contentText = "您好，您的验证码是：" + code +"（请勿透露给其他人）感谢您的使用。";
			String receiverType = (String)message.getField("RECEIVER_TYPE_NONE").get(null);
			String receiverCodes = user.getMobile(), receiverNames = user.getUserName();
			method.invoke(null, contentTitle, contentText, receiverType, receiverCodes, receiverNames);
		} catch (ClassNotFoundException e) {
			return renderResult(Global.FALSE, "消息模块未安装，请联系管理员！");
		} catch (Exception e) {
			logger.error(title+"发送短信错误。", e);
			return renderResult(Global.FALSE, "系统出现了点问题，错误信息：" + e.getMessage());
		}
		return renderResult(Global.TRUE, "短信已发送，请接收并填写验证码！");
	}
	
}

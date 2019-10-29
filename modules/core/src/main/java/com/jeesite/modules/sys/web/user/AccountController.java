/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.msg.EmailUtils;
import com.jeesite.common.msg.SmsUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.PwdUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.utils.ValidCodeUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 账号自助服务Controller
 * @author ThinkGem
 * @version 2017-12-7
 */
@Controller
@RequestMapping(value = "/account")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
@Api(tags = "Account / 账号自助服务、找回密码、账号注册")
public class AccountController extends BaseController{

	@Autowired
	private UserService userService;

	/**
	 * 忘记密码页面
	 */
	@GetMapping(value = "forgetPwd")
	@ApiOperation(value = "忘记密码页面")
	public String forgetPwd(Model model) {
		return "modules/sys/account/forgetPwd";
	}
	
	/**
	 * 获取找回密码短信、邮件验证码
	 * @param validCode 图片验证码，防止重复机器人。
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getFpValidCode")
	@ResponseBody
	@ApiOperation(value = "获取找回密码的短信或邮件验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "validCode", value = "图片验证码，防止重复机器人", required = true),
		@ApiImplicitParam(name = "validType", value = "验证方式（mobile、email）", required = true),
	})
	public String getFpValidCode(User user, String validCode, String validType, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, text("图片验证码不正确或已失效，请点击图片刷新！"));
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, text("非法操作。"));
		}
		User u = UserUtils.getByLoginCode(user.getLoginCode());
		if(u == null){
			return renderResult(Global.FALSE, text("登录账号不正确！"));
		}
		if("mobile".equals(validType) && StringUtils.isBlank(u.getMobile())){
			return renderResult(Global.FALSE, text("该账号未设置手机号码！"));
		}else if("email".equals(validType) && StringUtils.isBlank(u.getEmail())){
			return renderResult(Global.FALSE, text("该账号未设置邮件地址！"));
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("fpLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, text("您当前操作太频繁，请稍等一会再操作！"));
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
			return sendSmsValidCode(u, fpValidCode, text("找回密码"));
		}
		// 发送邮件消息。
		else if("email".equals(validType)){
			return sendEmailValidCode(u, fpValidCode, text("找回密码"));
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码重置密码
	 */
	@PostMapping(value = "savePwdByValidCode")
	@ResponseBody
	@ApiOperation(value = "根据短信或邮件验证码重置密码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "fpValidCode", value = "手机或邮箱接受的验证码", required = true),
		@ApiImplicitParam(name = "password", value = "新密码", required = true, paramType="query", type="String"),
	})
	public String savePwdByValidCode(User user, String fpValidCode, HttpServletRequest request) {
		String userCode = (String)UserUtils.getCache("fpUserCode");
		String loginCode = (String)UserUtils.getCache("fpLoginCode");
		String validCode = (String)UserUtils.getCache("fpValidCode");
		Date date = (Date)UserUtils.getCache("fpLastDate");
		
		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(userCode != null && loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, text("请重新获取验证码！"));
		}
		
		// 清理验证码，验证码只允许使用一次。
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");
		UserUtils.removeCache("fpValidCode");
		UserUtils.removeCache("fpLastDate");
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}
		if (!(validCode != null && validCode.equals(fpValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, text("验证码不正确或已失效，请重新获取验证码！"));
		}
		
		// 更新为新密码。
		try{
			userService.updatePassword(userCode, user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		return renderResult(Global.TRUE, text("恭喜你，您的账号 {0} 密码修改成功！", loginCode));
	}

	/**
	 * 获取找回密码的保密问题
	 * @param validCode 图片验证码，防止重复机器人。
	 */
	@PostMapping(value = "getPwdQuestion")
	@ResponseBody
	@ApiOperation(value = "获取找回密码的保密问题")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "validCode", value = "图片验证码，防止重复机器人", required = true),
	})
	public String getPwdQuestion(User user, String validCode, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, text("图片验证码不正确或已失效，请点击图片刷新！"));
		}
		// 账号是否存在验证
		User u = UserUtils.getByLoginCode(user.getLoginCode());
		if (u == null){
			return renderResult(Global.FALSE, text("登录账号不正确！"));
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("fpLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, text("您当前操作太频繁，请稍等一会再操作！"));
		}else{
			UserUtils.putCache("fpLastDate", new Date());
		}

		// 未设置密保
		if (StringUtils.isAnyBlank(u.getPwdQuestion(), u.getPwdQuestion2(), u.getPwdQuestion3())){
			return renderResult(Global.FALSE, text("该账号未设置密保问题！"));
		}
		
		// 获取保密问题，并缓存
		Map<String, String> data = MapUtils.newHashMap();
		data.put("pwdQuestion", u.getPwdQuestion());
		data.put("pwdQuestion2", u.getPwdQuestion2());
		data.put("pwdQuestion3", u.getPwdQuestion3());
		UserUtils.putCache("fpUserCode", u.getUserCode());
		UserUtils.putCache("fpLoginCode", u.getLoginCode());
		return renderResult(Global.TRUE, text("获取密保问题成功！"), data);
	}

	/**
	 * 校验密保问题答案
	 * @param user
	 * @param request
	 * @return
	 */
	@PostMapping(value = "savePwdByPwdQuestion")
	@ResponseBody
	@ApiOperation(value = "根据保密问题重置密码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "pwdQuestionAnswer", value = "保密问题答案（1）", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "pwdQuestionAnswer2", value = "保密问题答案（2）", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "pwdQuestionAnswer3", value = "保密问题答案（3）", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "password", value = "新密码", required = true, paramType="query", type="String"),
	})
	public String savePwdByPwdQuestion(User user, HttpServletRequest request) {
		String userCode = (String)UserUtils.getCache("fpUserCode");
		String loginCode = (String)UserUtils.getCache("fpLoginCode");
		
		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(userCode != null && loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, text("请重新获取保密问题！"));
		}
		
		// 清理保密问题，每次获取只允许使用一次。
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");
		
		// 验证三个密保问题是否正确。
		User u = UserUtils.getByLoginCode(user.getLoginCode());
		if (!(u != null && loginCode.equals(user.getLoginCode())
				&& PwdUtils.validatePassword(user.getPwdQuestionAnswer(), u.getPwdQuestionAnswer())
				&& PwdUtils.validatePassword(user.getPwdQuestionAnswer2(), u.getPwdQuestionAnswer2())
				&& PwdUtils.validatePassword(user.getPwdQuestionAnswer3(), u.getPwdQuestionAnswer3()))) {
			return renderResult(Global.FALSE, text("您填写的密保问题答案不正确！"));
		}
		
		// 更新为新密码。
		try{
			userService.updatePassword(userCode, user.getPassword());
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		return renderResult(Global.TRUE, text("验证通过"));
	}
	
	/**
	 * 用户注册页面
	 * @param user 用户信息参数
	 */
	@GetMapping(value = "registerUser")
	@ApiOperation(value = "用户注册页面")
	public String registerUser(User user, HttpServletRequest request) {
		return "modules/sys/account/registerUser";
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getRegValidCode")
	@ResponseBody
	@ApiOperation(value = "获取注册用户短信或邮件验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "userName", value = "用户姓名", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "email", value = "电子邮箱", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "corpCode", value = "租户编号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "corpName", value = "租户名称", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "userType", value = "用户类型（employee）", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "validCode", value = "图片验证码，防止重复机器人", required = true),
		@ApiImplicitParam(name = "validType", value = "验证方式（mobile、email）", required = true),
	})
	public String getRegValidCode(User user, String validCode, String validType, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, text("图片验证码不正确或已失效，请点击图片刷新！"));
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, text("非法操作。"));
		}
		// 非空数据校验。
		if (!(StringUtils.isNotBlank(user.getLoginCode()) && StringUtils.isNotBlank(user.getUserName()))){
			return renderResult(Global.FALSE, text("登录账号和用户姓名不能为空！"));
		}
		// 邮箱、手机号码是否填写验证
		if("email".equals(validType) && !StringUtils.isNotBlank(user.getEmail())){
			return renderResult(Global.FALSE, text("电子邮箱不能为空！"));
		}
		else if("mobile".equals(validType) && !StringUtils.isNotBlank(user.getMobile())){
			return renderResult(Global.FALSE, text("手机号码不能为空！"));
		}
		// 操作是否频繁验证，如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = (Date)UserUtils.getCache("regLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, text("您当前操作太频繁，请稍等一会再操作！"));
		}else{
			UserUtils.putCache("regLastDate", new Date());
		}
		// 验证用户编码是否存在。
		if (UserUtils.getByLoginCode(user.getLoginCode()) != null){
			return renderResult(Global.FALSE, text("登录账号已存在！"));
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
			return renderResult(Global.FALSE, text("非法的用户类型！"));
		}
		UserUtils.putCache("regEmail", user.getEmail());
		UserUtils.putCache("regMobile", user.getMobile());
		UserUtils.putCache("regValidCode", code);
		// 发送邮箱或短信验证码
		if("send_email".equals(validType)){
			return sendEmailValidCode(user, code, text("注册账号"));
		}else if("send_mobile".equals(validType)){
			return sendSmsValidCode(user, code, text("注册账号"));
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "saveRegByValidCode")
	@ResponseBody
	@ApiOperation(value = "根据短信或邮件验证码注册用户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "loginCode", value = "登录账号", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "userName", value = "用户姓名", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "password", value = "登录密码", required = true, paramType="query", type="String"),
		@ApiImplicitParam(name = "regValidCode", value = "手机或邮箱接受的验证码", required = true),
	})
	public String saveRegByValidCode(User user, String regValidCode, HttpServletRequest request) {
		if (!"true".equals(Global.getConfig("sys.account.registerUser"))){
			return renderResult(Global.FALSE, text("当前系统没有开启注册功能！"));
		}
		String corpCode = (String)UserUtils.getCache("regCorpCode");
		String corpName = (String)UserUtils.getCache("regCorpName");
		String userType = (String)UserUtils.getCache("regUserType");
		String loginCode = (String)UserUtils.getCache("regLoginCode");
		String email = (String)UserUtils.getCache("regEmail");
		String mobile = (String)UserUtils.getCache("regMobile");
		String validCode = (String)UserUtils.getCache("regValidCode");
		Date date = (Date)UserUtils.getCache("regLastDate");

		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, text("非法操作。"));
		}
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}
		if (!(validCode != null && validCode.equals(regValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, text("验证码不正确或已失效，请重新获取验证码！"));
		}

		// 非空数据校验。
		if (!(StringUtils.isNotBlank(user.getLoginCode()) && StringUtils.isNotBlank(user.getUserName()))){
			return renderResult(Global.FALSE, text("登录账号和用户姓名不能为空！"));
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
		u.setPassword(user.getPassword());
		u.setEmail(email);
		u.setMobile(mobile);
		u.setUserType(userType);
		u.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		userService.save(u);
		
		// 验证成功后清理验证码，验证码只允许使用一次。
		UserUtils.removeCache("regUserType");
		UserUtils.removeCache("regLoginCode");
		UserUtils.removeCache("regValidCode");
		UserUtils.removeCache("regLastDate");
	
		return renderResult(Global.TRUE, text("恭喜你，您的账号 "+u.getLoginCode()+" 注册成功！"));
	}
	
	/**
	 * 发送邮件验证码
	 */
	private String sendEmailValidCode(User user, String code, String title){
		String account = user.getEmail();
		try {
			title = text("{0}（{1}）{2}验证码", user.getUserName(), user.getLoginCode(), title);
			String content = text("尊敬的用户，您好!\n\n您的验证码是：{0}（请勿透露给其他人）\n\n"
					+ "请复制后，填写在你的验证码窗口完成验证。\n\n本邮件由系统自动发出，请勿回复。\n\n感谢您的使用！", code);
//			String receiveUserCode = "[CODE]"+account;
//			MsgPushUtils.push(MsgPush.TYPE_EMAIL, title, content, null, null, receiveUserCode);
			EmailUtils.send(account, title, content);
		} catch (Exception e) {
			logger.error(title + "发送邮件错误。", e);
			return renderResult(Global.FALSE, text("系统出现了点问题，错误信息：{0}", e.getMessage()));
		}
		account = account.replaceAll("([\\w\\W]?)([\\w\\W]+)([\\w\\W])(@[\\w\\W]+)", "$1****$3$4");
		return renderResult(Global.TRUE, text("验证码已发送到“{0}”邮箱账号，请尽快查收！", account));
	}
	
	/**
	 * 发送短信验证码
	 */
	private String sendSmsValidCode(User user, String code, String title){
		String account = user.getMobile();
		try {
			title = text("{0}（{1}）{2}验证码", user.getUserName(), user.getLoginCode(), title);
			String content = text("您好，您的验证码是：{0}（请勿透露给其他人）感谢您的使用。", code);
//			String receiveUserCode = "[CODE]"+account;
//			MsgPushUtils.push(MsgPush.TYPE_SMS, title, content, null, null, receiveUserCode);
			SmsUtils.send(content, account);
		} catch (Exception e) {
			logger.error(title + "发送短信错误。", e);
			return renderResult(Global.FALSE, text("系统出现了点问题，错误信息：{0}", e.getMessage()));
		}
		account = account.replaceAll("(\\d{3})(\\d+)(\\d{3})","$1****$3");
		return renderResult(Global.TRUE, text("验证码已发送到“{0}”的手机号码，请尽快查收！", account));
	}
	
}

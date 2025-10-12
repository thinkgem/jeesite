/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.msg.EmailUtils;
import com.jeesite.common.msg.SmsUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.shiro.authc.FormToken;
import com.jeesite.common.shiro.filter.FormFilter;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.PwdUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.utils.ValidCodeUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 账号自助服务Controller
 * @author ThinkGem
 * @version 2020-9-20
 */
@Controller
@Tag(name = "Account - 账号服务")
@RequestMapping(value = "/account")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class AccountController extends BaseController{

	private final UserService userService;

	public AccountController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 获取登录短信或邮件验证码
	 * @param mobile 手机号码
	 * @param validCode 图片验证码，防止重复机器人。
	 * @author ThinkGem
	 */
	@PostMapping(value = "getLoginValidCode")
	@ResponseBody
	@Operation(summary = "获取登录的短信或邮件验证码")
	@Parameters({
		@Parameter(name = "mobile", description = "手机号码", required = true),
		@Parameter(name = "validCode", description = "图片验证码，防止重复机器人", required = true),
		@Parameter(name = "corpCode", description = "所属租户"),
	})
	public String getLoginValidCode(String mobile, String validCode, String corpCode, HttpServletRequest request) {
		return getValidCode("login", mobile, validCode, "mobile", corpCode, request, "登录验证码");
	}
	
	/**
	 * 根据短信或邮件验证码登录系统
	 * @author ThinkGem
	 */
	@PostMapping(value = "loginByValidCode")
	@ResponseBody
	@Operation(summary = "根据短信或邮件验证码登录系统")
	@Parameters({
		@Parameter(name = "loginValidCode", description = "手机接受的验证码", required = true),
		@Parameter(name = "selectLoginCode", description = "手机号绑定多个账号时的登录账号", required = true),
	})
	public String loginByValidCode(String selectLoginCode, String loginValidCode, HttpServletRequest request, HttpServletResponse response) {
		if (!Global.getConfigToBoolean("user.loginByValidCode", "true")) {
			return renderResult(Global.FALSE, "验证码登录未开启，请设置：user.loginByValidCode=true");
		}
		String type = "login";
		String userCode = UserUtils.getCache(type + "UserCode");
		String loginCode = UserUtils.getCache(type + "LoginCode");
		List<User> userList = UserUtils.getCache(type + "UserList");
		String s = validValidCode(type, userCode, loginCode, loginValidCode, request);
		if (s != null) {
			return s;
		}
		// 登录系统
		try {
			if (StringUtils.isNotBlank(selectLoginCode) && ListUtils.isNotEmpty(userList)) {
				Optional<User> userOptional = userList.stream().filter(user -> selectLoginCode
						.equals(user.getLoginCode())).findFirst();
				if (userOptional.isPresent()) {
					User user = userOptional.get();
					loginCode = user.getLoginCode();
				}
			}
			FormToken formToken = FormFilter.newToken(request, response);
			formToken.setUsername(loginCode);
			formToken.setInnerLogin(true);
			UserUtils.getSubject().login(formToken);
			FormFilter.onLoginSuccess(request, response);
        } catch (AuthenticationException e) {
        	FormFilter.onLoginFailure(e, request, response);
        }
		return null;
	}
	
	/**
	 * 忘记密码页面
	 */
	@GetMapping(value = "forgetPwd")
	@Hidden
	public String forgetPwd(Model model) {
		return "modules/sys/forgetPwd";
	}
	
	/**
	 * 获取找回密码短信或邮件验证码
	 * @param validCode 图片验证码，防止重复机器人。
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getFpValidCode")
	@ResponseBody
	@Operation(summary = "获取找回密码的短信或邮件验证码")
	@Parameters({
		@Parameter(name = "loginCode", description = "登录账号", required = true),
		@Parameter(name = "validCode", description = "图片验证码，防止重复机器人", required = true),
		@Parameter(name = "validType", description = "验证方式（mobile、email）", required = true),
		@Parameter(name = "corpCode", description = "所属租户"),
	})
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "响应对象", content = @Content( schemaProperties = {
		@SchemaProperty(name = "result", schema = @Schema(description = "结果状态")),
		@SchemaProperty(name = "message", schema = @Schema(description = "返回消息")),
	}))})
	public String getFpValidCode(User user, String validCode, String validType, String corpCode, HttpServletRequest request) {
		return getValidCode("fp", user.getLoginCode(), validCode, validType, corpCode, request, "找回密码");
	}
	
	/**
	 * 根据短信或邮件验证码重置密码
	 */
	@PostMapping(value = "savePwdByValidCode")
	@ResponseBody
	@Operation(summary = "根据短信或邮件验证码重置密码")
	@Parameters({
		@Parameter(name = "loginCode", description = "登录账号", required = true),
		@Parameter(name = "fpValidCode", description = "手机或邮箱接受的验证码", required = true),
		@Parameter(name = "password", description = "新密码", required = true),
	})
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "响应对象", content = @Content( schemaProperties = {
		@SchemaProperty(name = "result", schema = @Schema(description = "结果状态")),
		@SchemaProperty(name = "message", schema = @Schema(description = "返回消息")),
	}))})
	public String savePwdByValidCode(String password, String fpValidCode, HttpServletRequest request) {
		String type = "fp";
		String userCode = UserUtils.getCache(type + "UserCode");
		String loginCode = UserUtils.getCache(type + "LoginCode");
		String s = validValidCode(type, userCode, loginCode, fpValidCode, request);
		if (s != null) {
			return s;
		}
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			password = DesUtils.decode(password, secretKey);
		}
		// 更新为新密码
		try{
			userService.updatePassword(userCode, password);
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
		return renderResult(Global.TRUE, text("恭喜你，您的账号 {0} 密码找回成功！", loginCode));
	}

	/**
	 * 获取验证码
	 * @author ThinkGem
	 */
	private String getValidCode(String type, String loginCode, String validCode, String validType, String corpCode, HttpServletRequest request, String msgTitle) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, text("图片验证码不正确或已失效，请点击图片刷新！"));
		}
		if (!"mobile".equals(validType) && !"email".equals(validType)){
			return renderResult(Global.FALSE, text("非法操作。"));
		}
		Map<String, Object> data = MapUtils.newHashMap();
		User u = null;
		if ("login".equals(type)){
			User where = new User();
			where.setMobile(loginCode);
			where.setCorpCode_(corpCode);
			where.setStatus(User.STATUS_NORMAL);
			List<User> userList = userService.findListByMobile(where);
			if (!userList.isEmpty()){
				u = userList.get(0);
				if (userList.size() > 1) {
					data.put("extMessage", text("该手机号绑定了多个账号，请选择一个账号登录"));
					data.put("userList", userList.stream().map(user -> {
						Map<String, Object> map = MapUtils.newHashMap();
						map.put("loginCode", user.getLoginCode());
						map.put("userName", user.getUserName());
						return map;
					}).collect(Collectors.toList()));
				}
				UserUtils.putCache(type + "UserList", userList);
			}
			if(u == null){
				return renderResult(Global.FALSE, text("手机号不正确！"));
			}
		} else {
			u = UserUtils.getByLoginCode(loginCode, corpCode);
			if(u == null){
				return renderResult(Global.FALSE, text("登录账号不正确！"));
			}
		}
		if("mobile".equals(validType) && StringUtils.isBlank(u.getMobile())){
			return renderResult(Global.FALSE, text("该账号未设置手机号码！"));
		}else if("email".equals(validType) && StringUtils.isBlank(u.getEmail())){
			return renderResult(Global.FALSE, text("该账号未设置邮件地址！"));
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = UserUtils.getCache(type + "LastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, text("您当前操作太频繁，请稍等一会再操作！"));
		}else{
			UserUtils.putCache(type + "LastDate", new Date());
		}
		// 生成验证码，并缓存。
		String loginValidCode = StringUtils.getRandomNum(6);
		UserUtils.putCache(type + "UserCode", u.getUserCode());
		UserUtils.putCache(type + "LoginCode", u.getLoginCode());
		UserUtils.putCache(type + "ValidCode", loginValidCode);
		// 发送邮箱或短信验证码
		if("mobile".equals(validType)){
			return sendSmsValidCode(u, loginValidCode, text(msgTitle), data);
		}else if("email".equals(validType)){
			return sendEmailValidCode(u, loginValidCode, text(msgTitle), data);
		}
		return null;
	}

	/**
	 * 验证验证码
	 * @author ThinkGem
	 */
	private String validValidCode(String type, String userCode, String loginCode, String loginValidCode, HttpServletRequest request) {
		// 验证类型、用户名和登录账号，如果为空，则要求重新获取验证码
		if (StringUtils.isAnyBlank(type, userCode, loginCode)){
			return renderResult(Global.FALSE, text("请重新获取验证码！"));
		}

		// 获取验证码和验证时间
		String validCode = UserUtils.getCache(type + "ValidCode");
		Date date = UserUtils.getCache(type + "LastDate");
		
		// 验证码是否超时
		boolean isTimeout = true;
		String validTime = Global.getConfig("sys.account.validCodeTimeout", "10"); //验证码有效时间（单位分钟，0表示不限制，默认值10）
		if("0".equals(validTime) || (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 60*Long.parseLong(validTime))){
			isTimeout = false;
		}
		if (!(validCode != null && validCode.equals(loginValidCode) && !isTimeout)){
			return renderResult(Global.FALSE, text("验证码不正确或已失效，请重新获取验证码！"));
		}
		
		// 验证成功后，清理缓存
		UserUtils.removeCache(type + "UserCode");
		UserUtils.removeCache(type + "LoginCode");
		UserUtils.removeCache(type + "ValidCode");
		UserUtils.removeCache(type + "LastDate");
		UserUtils.removeCache(type + "UserList");
		
		return null;
	}

	/**
	 * 获取找回密码的保密问题
	 * @param validCode 图片验证码，防止重复机器人。
	 */
	@PostMapping(value = "getPwdQuestion")
	@ResponseBody
	@Operation(summary = "获取找回密码的保密问题")
	@Parameters({
		@Parameter(name = "loginCode", description = "登录账号", required = true),
		@Parameter(name = "validCode", description = "图片验证码，防止重复机器人", required = true),
		@Parameter(name = "corpCode", description = "所属租户"),
	})
	public String getPwdQuestion(String loginCode, String validCode, String corpCode, HttpServletRequest request) {
		// 校验图片验证码，防止重复机器人。
		if (!ValidCodeUtils.validate(request, validCode)){
			return renderResult(Global.FALSE, text("图片验证码不正确或已失效，请点击图片刷新！"));
		}
		// 账号是否存在验证
		User u = UserUtils.getByLoginCode(loginCode, corpCode);
		if (u == null){
			return renderResult(Global.FALSE, text("登录账号不正确！"));
		}
		// 操作是否频繁验证， 如果离上次获取验证码小于20秒，则提示操作频繁。
		Date date = UserUtils.getCache("fpLastDate");
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
	 */
	@PostMapping(value = "savePwdByPwdQuestion")
	@ResponseBody
	@Operation(summary = "根据保密问题重置密码")
	@Parameters({
		@Parameter(name = "loginCode", description = "登录账号", required = true),
		@Parameter(name = "pwdQuestionAnswer", description = "保密问题答案（1）", required = true),
		@Parameter(name = "pwdQuestionAnswer2", description = "保密问题答案（2）", required = true),
		@Parameter(name = "pwdQuestionAnswer3", description = "保密问题答案（3）", required = true),
		@Parameter(name = "password", description = "新密码", required = true),
	})
	public String savePwdByPwdQuestion(User user, HttpServletRequest request) {
		String userCode = UserUtils.getCache("fpUserCode");
		String loginCode = UserUtils.getCache("fpLoginCode");

		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(userCode != null && loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, text("请重新获取保密问题！"));
		}
		
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			user.setPwdQuestionAnswer(DesUtils.decode(user.getPwdQuestionAnswer(), secretKey));
			user.setPwdQuestionAnswer2(DesUtils.decode(user.getPwdQuestionAnswer2(), secretKey));
			user.setPwdQuestionAnswer3(DesUtils.decode(user.getPwdQuestionAnswer3(), secretKey));
			user.setPassword(DesUtils.decode(user.getPassword(), secretKey));
		}
		
		// 验证三个密保问题是否正确。
		User u = UserUtils.get(userCode);
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
		
		// 更新密码后，清理缓存
		UserUtils.removeCache("fpUserCode");
		UserUtils.removeCache("fpLoginCode");

		return renderResult(Global.TRUE, text("恭喜你，您的账号 {0} 密码找回成功！", user.getLoginCode()));
	}
	
	/**
	 * 用户注册页面
	 * @param user 用户信息参数
	 */
	@GetMapping(value = "registerUser")
	@Hidden
	public String registerUser(User user, HttpServletRequest request) {
		return "modules/sys/registerUser";
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param validType 验证方式：mobile、email
	 */
	@PostMapping(value = "getRegValidCode")
	@ResponseBody
	@Operation(summary = "获取注册用户短信或邮件验证码")
	@Parameters({
		@Parameter(name = "loginCode", description = "登录账号", required = true),
		@Parameter(name = "userName", description = "用户姓名", required = true),
		@Parameter(name = "email", description = "电子邮箱", required = true),
		@Parameter(name = "mobile", description = "手机号码", required = true),
		@Parameter(name = "corpCode_", description = "租户编号", required = true),
		@Parameter(name = "corpName_", description = "租户名称", required = true),
		@Parameter(name = "userType", description = "用户类型（employee）", required = true),
		@Parameter(name = "validCode", description = "图片验证码，防止重复机器人", required = true),
		@Parameter(name = "validType", description = "验证方式（mobile、email）", required = true),
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
		if (Global.isUseCorpModel() && StringUtils.isBlank(user.getCorpCode_())) {
			return renderResult(Global.FALSE, text("请选择注册到的租户！"));
		}
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
		Date date = UserUtils.getCache("regLastDate");
		if (date != null && (System.currentTimeMillis()-date.getTime())/(1000L) < 20L){
			return renderResult(Global.FALSE, text("您当前操作太频繁，请稍等一会再操作！"));
		}else{
			UserUtils.putCache("regLastDate", new Date());
		}
		// 验证用户编码是否存在。
		if (UserUtils.getByLoginCode(user.getLoginCode(), user.getCorpCode_()) != null){
			return renderResult(Global.FALSE, text("登录账号已存在"));
		}
		// 生成验证码，并缓存。
		String regValidCode = StringUtils.getRandomNum(6);
		UserUtils.putCache("regCorpCode", user.getCorpCode_());
		UserUtils.putCache("regCorpName", user.getCorpName_());
		UserUtils.putCache("regLoginCode", user.getLoginCode());
		UserUtils.putCache("regUserName", user.getUserName());
		// 账号注册类型
		String[] userTypes = Global.getConfigToArray("sys.account.registerUser.userTypes", "-1");
		if (StringUtils.inString(user.getUserType(), userTypes)){
			UserUtils.putCache("regUserType", user.getUserType());
		}else{
			return renderResult(Global.FALSE, text("请在参数设置里设定允许自助注册的用户类型")+"："+user.getUserType());
		}
		UserUtils.putCache("regEmail", user.getEmail());
		UserUtils.putCache("regMobile", user.getMobile());
		UserUtils.putCache("regValidCode", regValidCode);
		// 发送邮箱或短信验证码
		if("email".equals(validType)){
			return sendEmailValidCode(user, regValidCode, text("注册账号"), null);
		}else if("mobile".equals(validType)){
			return sendSmsValidCode(user, regValidCode, text("注册账号"), null);
		}
		return null;
	}
	
	/**
	 * 根据短信或邮件验证码注册用户（通过邮箱、手机号）
	 * @param user 用户信息参数
	 * @param regValidCode 注册验证码
	 */
	@PostMapping(value = "saveRegByValidCode")
	@ResponseBody
	@Operation(summary = "根据短信或邮件验证码注册用户")
	@Parameters({
		@Parameter(name = "loginCode", description = "登录账号", required = true),
		@Parameter(name = "password", description = "登录密码", required = true),
		@Parameter(name = "regValidCode", description = "手机或邮箱接受的验证码", required = true),
	})
	public String saveRegByValidCode(User user, String regValidCode, HttpServletRequest request) {
		if (!"true".equals(Global.getConfig("sys.account.registerUser"))){
			return renderResult(Global.FALSE, text("当前系统没有开启注册功能！"));
		}
		String corpCode = UserUtils.getCache("regCorpCode");
		String corpName = UserUtils.getCache("regCorpName");
		String userType = UserUtils.getCache("regUserType");
		String loginCode = UserUtils.getCache("regLoginCode");
		String userName = UserUtils.getCache("regUserName");
		String email = UserUtils.getCache("regEmail");
		String mobile = UserUtils.getCache("regMobile");
		String validCode = UserUtils.getCache("regValidCode");
		Date date = UserUtils.getCache("regLastDate");

		// 一同验证保存的用户名和验证码是否正确（如果只校验验证码，不验证用户名，则会有获取验证码后修改用户名的漏洞）
		if (!(loginCode != null && loginCode.equals(user.getLoginCode()))){
			return renderResult(Global.FALSE, text("请先获取验证码。"));
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
			u.setCorpCode_(corpCode);
			u.setCorpName_(corpName);
		}
		u.setLoginCode(loginCode);
		u.setUserName(userName);
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			user.setPassword(DesUtils.decode(user.getPassword(), secretKey));
		}
		u.setPassword(user.getPassword());
		u.setEmail(email);
		u.setMobile(mobile);
		u.setUserType(userType);
		u.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		u.setStatus(User.STATUS_AUDIT); // 待审核状态
		userService.save(u);
		
		// 注册用户后，清理缓存
		UserUtils.removeCache("regCorpCode");
		UserUtils.removeCache("regCorpName");
		UserUtils.removeCache("regUserType");
		UserUtils.removeCache("regLoginCode");
		UserUtils.removeCache("regUserName");
		UserUtils.removeCache("regValidCode");
		UserUtils.removeCache("regLastDate");
	
		return renderResult(Global.TRUE, text("恭喜你，您的账号 {0} 注册成功！", u.getLoginCode()));
	}
	
	/**
	 * 发送邮件验证码
	 */
	private String sendEmailValidCode(User user, String code, String title, Map<String, Object> data){
		String email = user.getEmail();
		try {
			title = text("{0}（{1}）{2}验证码", user.getUserName(), user.getLoginCode(), title);
			String content = text("尊敬的用户，您好!\n\n您的验证码是：{0}（请勿透露给其他人）\n\n"
					+ "请复制后，填写在你的验证码窗口完成验证。\n\n本邮件由系统自动发出，请勿回复。\n\n感谢您的使用！", code);
//			String receiveUserCode = "[CODE]"+email;
//			MsgPushUtils.push(MsgPush.TYPE_EMAIL, title, content, null, null, receiveUserCode);
			EmailUtils.send(email, title, content);
		} catch (Exception e) {
			logger.error(title + "发送邮件错误。", e);
			return renderResult(Global.FALSE, text("系统出现了点问题，错误信息：{0}", e.getMessage()));
		}
		email = EncodeUtils.emailMask(email);
		return renderResult(Global.TRUE, text("验证码已发送到“{0}”邮箱账号，请尽快查收！", email), data);
	}
	
	/**
	 * 发送短信验证码
	 */
	private String sendSmsValidCode(User user, String code, String title, Map<String, Object> data){
		String mobile = user.getMobile();
		try {
			title = text("{0}（{1}）{2}验证码", user.getUserName(), user.getLoginCode(), title);
			String content = text("您好，您的验证码是：{0}（请勿透露给其他人）感谢您的使用。", code);
//			String receiveUserCode = "[CODE]"+mobile;
//			MsgPushUtils.push(MsgPush.TYPE_SMS, title, content, null, null, receiveUserCode);
			SmsUtils.send(content, mobile);
		} catch (Exception e) {
			logger.error(title + "发送短信错误。", e);
			return renderResult(Global.FALSE, text("系统出现了点问题，错误信息：{0}", e.getMessage()));
		}
		mobile = EncodeUtils.mobileMask(mobile);
		return renderResult(Global.TRUE, text("验证码已发送到“{0}”的手机号码，请尽快查收！", mobile), data);
	}
	
}

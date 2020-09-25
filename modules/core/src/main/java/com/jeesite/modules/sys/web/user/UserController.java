/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONValidator;
import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.PwdUtils;
import com.jeesite.modules.sys.utils.UserUtils;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2017-3-21
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
@ApiIgnore
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	
	/**
	 * 获取用户列表
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response) {
//		if (User.USER_TYPE_NONE.equals(user.getUserType())){
//			return new Page<User>(request, response);
//		}
		if (Global.isStrictMode() && !user.getCurrentUser().isAdmin()){
			return new Page<User>(request, response);
		}
		user.setPage(new Page<>(request, response));
		Page<User> page = userService.findPage(user);
		return page;
	}
	
	/**
	 * 验证登录名是否有效
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "checkLoginCode")
	@ResponseBody
	public String checkLoginCode(String oldLoginCode, String loginCode) {
		return userService.checkLoginCode(oldLoginCode, loginCode);
	}

	/**
	 * 用户信息 - 显示
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(User user, String op, Model model) {
		if (StringUtils.isBlank(op)){
			op = "base";
		}
		model.addAttribute("op", op);
		model.addAttribute("user", user.getCurrentUser());
		return "modules/sys/user/userInfo";
	}

	/**
	 * 用户信息 - 保存基础信息
	 */
	@RequiresPermissions("user")
	@PostMapping(value = "infoSaveBase")
	@ResponseBody
	public String infoSaveBase(User user, HttpServletRequest request) {
		if (StringUtils.isBlank(user.getUserName())){
			return renderResult(Global.TRUE, text("sys.user.userNameNotBlank"));
		}
		Global.assertDemoMode();
		User currentUser = UserUtils.getUser();
		currentUser.setAvatarBase64(user.getAvatarBase64());
		currentUser.setUserName(user.getUserName());
		currentUser.setEmail(user.getEmail());
		currentUser.setMobile(user.getMobile());
		currentUser.setPhone(user.getPhone());
		currentUser.setSex(user.getSex());
		currentUser.setSign(user.getSign());
		userService.updateUserInfo(currentUser);
		return renderResult(Global.TRUE, text("sys.user.infoSaveSuccess"));
	}
	
	/**
	 * 用户信息 - 保存用户密码
	 */
	@RequiresPermissions("user")
	@PostMapping(value = "infoSavePwd")
	@ResponseBody
	public String infoSavePwd(User user, String oldPassword, String newPassword,
			String confirmNewPassword) {
		User currentUser = UserUtils.getUser();
		// 登录密码解密（解决密码明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			oldPassword = DesUtils.decode(oldPassword, secretKey);
			newPassword = DesUtils.decode(newPassword, secretKey);
			confirmNewPassword = DesUtils.decode(confirmNewPassword, secretKey);
		}
		// 验证旧密码
		if(!PwdUtils.validatePassword(oldPassword, currentUser.getPassword())){
			return renderResult(Global.FALSE, text("sys.user.oldPasswordError"));
		}
		// 验证新密码和确认密码
		if(!StringUtils.equals(newPassword, confirmNewPassword)){
			return renderResult(Global.FALSE, text("sys.user.confirmPasswrodError"));
		}
		// 更新密码
		try{
			userService.updatePassword(currentUser.getUserCode(), confirmNewPassword);
			return renderResult(Global.TRUE, text("sys.user.passwordModifySuccess"));
		}catch(ServiceException se){
			return renderResult(Global.FALSE, se.getMessage());
		}
	}
	
	/**
	 * 用户信息 - 保存保密问题
	 */
	@RequiresPermissions("user")
	@PostMapping(value = "infoSavePqa")
	@ResponseBody
	public String infoSavePqa(User user, String validPassword, String oldPwdQuestionAnswer,
			String oldPwdQuestionAnswer2, String oldPwdQuestionAnswer3) {
		User currentUser = UserUtils.getUser();
		// 安全问题密码解密（解决密码明文传输安全问题）
		String secretKey = Global.getProperty("shiro.loginSubmit.secretKey");
		if (StringUtils.isNotBlank(secretKey)){
			validPassword = DesUtils.decode(validPassword, secretKey);
			oldPwdQuestionAnswer = DesUtils.decode(oldPwdQuestionAnswer, secretKey);
			oldPwdQuestionAnswer2 = DesUtils.decode(oldPwdQuestionAnswer2, secretKey);
			oldPwdQuestionAnswer3 = DesUtils.decode(oldPwdQuestionAnswer3, secretKey);
			user.setPwdQuestionAnswer(DesUtils.decode(user.getPwdQuestionAnswer(), secretKey));
			user.setPwdQuestionAnswer2(DesUtils.decode(user.getPwdQuestionAnswer2(), secretKey));
			user.setPwdQuestionAnswer3(DesUtils.decode(user.getPwdQuestionAnswer3(), secretKey));
		}
		boolean updateQuesstion = false;
		// 如果从未设置过
		if (StringUtils.isBlank(currentUser.getPwdQuestion()) 
				&& StringUtils.isBlank(currentUser.getPwdQuestion2()) 
				&& StringUtils.isBlank(currentUser.getPwdQuestion3())){
			if(!PwdUtils.validatePassword(validPassword, currentUser.getPassword())){
				return renderResult(Global.FALSE, text("sys.user.passwordError"));
			}
			updateQuesstion = true;
		}
		// 验证密保答案
		else if (PwdUtils.validatePassword(oldPwdQuestionAnswer, currentUser.getPwdQuestionAnswer())
				&& PwdUtils.validatePassword(oldPwdQuestionAnswer2, currentUser.getPwdQuestionAnswer2())
				&& PwdUtils.validatePassword(oldPwdQuestionAnswer3, currentUser.getPwdQuestionAnswer3())) {
			updateQuesstion = true;
		}
		// 保存密保答案
		if (updateQuesstion){
			currentUser.setPwdQuestion(user.getPwdQuestion());
			currentUser.setPwdQuestionAnswer(PwdUtils.encryptPassword(user.getPwdQuestionAnswer()));
			currentUser.setPwdQuestion2(user.getPwdQuestion2());
			currentUser.setPwdQuestionAnswer2(PwdUtils.encryptPassword(user.getPwdQuestionAnswer2()));
			currentUser.setPwdQuestion3(user.getPwdQuestion3());
			currentUser.setPwdQuestionAnswer3(PwdUtils.encryptPassword(user.getPwdQuestionAnswer3()));
			userService.updateQuestion(currentUser);
			return renderResult(Global.TRUE, text("sys.user.pwdQuestionModifySuccess"));
		}else{
			return renderResult(Global.FALSE, text("sys.user.pwdQuestionAnswerError"));
		}
	}
	
	/**
	 * 选择用户对话框
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "userSelect")
	public String userSelect(User user, String selectData, Model model) {
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (selectDataJson != null && JSONValidator.from(selectDataJson).validate()){
			model.addAttribute("selectData", selectDataJson);
		}
		model.addAttribute("user", user);
		return "modules/sys/user/userSelect";
	}
	
}

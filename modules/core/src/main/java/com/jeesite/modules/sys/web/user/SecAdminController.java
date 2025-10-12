/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.user;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.UserDataScope;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.ModuleUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 二级管理员Controller
 * @author ThinkGem
 * @version 2017-12-12
 */
@Controller
@Tag(name = "SecAdmin - 二级管理员管理")
@RequestMapping(value = "${adminPath}/sys/secAdmin")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class SecAdminController extends BaseController {

	private final UserService userService;

	public SecAdminController(UserService userService) {
		this.userService = userService;
	}

	@ModelAttribute
	public User get(String userCode, boolean isNewRecord) {
		return userService.get(userCode, isNewRecord);
	}

	@RequiresPermissions("sys:secAdmin:view")
	@RequestMapping(value = "list")
	public String list(User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/user/secAdminList";
	}

	@RequiresPermissions("sys:secAdmin:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response) {
		user.setMgrType(User.MGR_TYPE_SEC_ADMIN);	// 二级管理员
		user.setPage(new Page<>(request, response));
		Page<User> page = userService.findPage(user);
		return page;
	}

	@RequiresPermissions("sys:secAdmin:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		UserDataScope userDataScope = new UserDataScope();
		userDataScope.setUserCode(user.getUserCode());
		userDataScope.setCtrlPermi(UserDataScope.CTRL_PERMI_MANAGE);
		List<UserDataScope> userDataScopeList = userService.findDataScopeList(userDataScope);
		model.addAttribute("userDataScopeList", userDataScopeList);
		model.addAttribute("user", user);
		model.addAttribute("moduleCodes", ModuleUtils.getEnableModuleCodes());
		model.addAttribute("dataScopes", JsonMapper.fromJson(Global.getConfig("user.dataScopes", "[]"), List.class));
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/user/secAdminForm";
	}

	/**
	 * 设置为二级管理员身份
	 */
	@RequiresPermissions("sys:secAdmin:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated User user) {
		if (!User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		// 设置为二级管理员身份
		user.setMgrType(User.MGR_TYPE_SEC_ADMIN);
		userService.updateMgrType(user);
		// 保存用户管理数据权限
		userService.saveAuthDataScope(user);
		return renderResult(Global.TRUE, text("保存二级管理员''{0}''成功", user.getUserName()));
	}
	
	/**
	 * 取消二级管理员身份
	 */
	@RequiresPermissions("sys:secAdmin:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(User user) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		if (!User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		// 取消用户管理数据权限
		user.setMgrType(User.MGR_TYPE_SEC_ADMIN);
		user.setUserDataScopeListJson("[]");
		userService.saveAuthDataScope(user);
		// 取消二级管理员身份
		user.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		userService.updateMgrType(user);
		return renderResult(Global.TRUE, text("取消二级管理员''{0}''身份成功", user.getUserName()));
	}
	
}

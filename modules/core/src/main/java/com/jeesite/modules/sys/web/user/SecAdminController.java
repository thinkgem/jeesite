/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.UserDataScope;
import com.jeesite.modules.sys.service.UserService;

/**
 * 二级管理员Controller
 * @author ThinkGem
 * @version 2017-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/secAdmin")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
public class SecAdminController extends BaseController {

	@Autowired
	private UserService userService;

	@ModelAttribute
	public User get(String userCode, boolean isNewRecord) {
		return userService.get(userCode, isNewRecord);
	}

	@RequiresPermissions("sys:secAdmin:view")
	@RequestMapping(value = "list")
	public String list(User user, Model model) {
		return "modules/sys/user/secAdminList";
	}

	@RequiresPermissions("user")
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
	public String form(User user, String op, Model model) {
		UserDataScope userDataScope = new UserDataScope();
		userDataScope.setUserCode(user.getUserCode());
		userDataScope.setCtrlPermi(UserDataScope.CTRL_PERMI_MANAGE);
		List<UserDataScope> userDataScopeList = userService.findDataScopeList(userDataScope);
		model.addAttribute("userDataScopeList", userDataScopeList);
		model.addAttribute("user", user);
		return "modules/sys/user/secAdminForm";
	}

	/**
	 * 设置为二级管理员身份
	 * @param user
	 * @return
	 */
	@RequiresPermissions("sys:secAdmin:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated User user, String op) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		// 设置为二级管理员身份
		user.setMgrType(User.MGR_TYPE_SEC_ADMIN);
		userService.updateMgrType(user);
		// 保存用户管理数据权限
		userService.saveAuthDataScope(user);
		return renderResult(Global.TRUE, "保存用户'" + user.getUserName() + "'成功");
	}
	
	/**
	 * 取消二级管理员身份
	 * @param user
	 * @return
	 */
	@RequiresPermissions("sys:secAdmin:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(User user) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		// 取消用户管理数据权限
		user.setMgrType(User.MGR_TYPE_SEC_ADMIN);
		user.setUserDataScopeListJson("[]");
		userService.saveAuthDataScope(user);
		// 取消二级管理员身份
		user.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		userService.updateMgrType(user);
		return renderResult(Global.TRUE, "取消用户'" + user.getUserName() + "'管理员身份成功！");
	}
	
}

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.role;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.service.RoleService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 角色 Controller
 * @author ThinkGem
 * @version 2026-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class RoleUserController extends BaseController {

	private final RoleService roleService;

	public RoleUserController(RoleService roleService) {
		this.roleService = roleService;
	}

	@ModelAttribute
	public Role get(String roleCode, boolean isNewRecord) {
		return roleService.get(roleCode, isNewRecord);
	}

	/** 
	 * 角色授权给用户
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "formAuthUser")
	public String formAuthUser(Role role, Model model) {
		model.addAttribute("role", role);
		return "modules/sys/roleFormAuthUser";
	}
	
	/** 
	 * 保存角色授权给用户
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveAuthUser")
	@ResponseBody
	public String saveAuthUser(Role role) {
//		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
//		Role old = super.getWebDataBinderSource(request);
//		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
//			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
//		}
		roleService.saveAuthUser(role);
		return renderResult(Global.TRUE, text("角色授权给用户成功"));
	}
	
	/** 
	 * 删除角色授权给用户
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "deleteAuthUser")
	@ResponseBody
	public String deleteAuthUser(Role role) {
//		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
//		Role old = super.getWebDataBinderSource(request);
//		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
//			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
//		}
		roleService.deleteAuthUser(role);
		return renderResult(Global.TRUE, text("取消用户角色授权成功"));
	}
	
}
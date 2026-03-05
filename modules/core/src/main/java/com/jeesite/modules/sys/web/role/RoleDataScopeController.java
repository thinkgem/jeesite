/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.role;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.MenuDataScope;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.RoleDataScope;
import com.jeesite.modules.sys.service.DataScopeService;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.utils.ModuleUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色 Controller
 * @author ThinkGem
 * @version 2026-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@ApiIgnore
public class RoleDataScopeController extends BaseController {

	private final RoleService roleService;
	private final DataScopeService dataScopeService;

	public RoleDataScopeController(RoleService roleService, DataScopeService dataScopeService) {
		this.roleService = roleService;
		this.dataScopeService = dataScopeService;
	}

	@ModelAttribute
	public Role get(String roleCode, boolean isNewRecord) {
		return roleService.get(roleCode, isNewRecord);
	}

	/** 
	 * 角色授权数据权限
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "formAuthDataScope")
	public String formAuthDataScope(Role role, Model model) {
		// 查询角色数据权限，包括菜单数据权限
		if (Global.TRUE.equals(role.getMenuCode())){
			// 拥有的角色数据权限
			RoleDataScope roleDataScope = new RoleDataScope();
			roleDataScope.setRoleCode(role.getRoleCode());
			List<RoleDataScope> roleDataScopeList = dataScopeService.findRoleDataScopeList(roleDataScope);
			model.addAttribute("roleDataScopeList", roleDataScopeList);
			// 拥有的菜单数据权限
			MenuDataScope menuDataScope = new MenuDataScope();
			menuDataScope.setRoleCode(role.getRoleCode());
			List<MenuDataScope> menuDataScopeList = dataScopeService.findMenuDataScopeList(menuDataScope);
			model.addAttribute("menuDataScopeList", menuDataScopeList);
		}
		// 全栈版本的数据权限（只查不关联菜单的权限）
		else {
			// 拥有的角色数据权限
			RoleDataScope roleDataScope = new RoleDataScope();
			roleDataScope.setRoleCode(role.getRoleCode());
			roleDataScope.setMenuCode(RoleDataScope.DEFAULT_MENU_CODE);
			List<RoleDataScope> roleDataScopeList = dataScopeService.findRoleDataScopeList(roleDataScope);
			model.addAttribute("roleDataScopeList", roleDataScopeList);
		}
		// 设置其它对象
		model.addAttribute("role", role);
		model.addAttribute("moduleCodes", ModuleUtils.getEnableModuleCodes());
		model.addAttribute("dataScopes", JsonMapper.fromJson(Global.getConfig("user.dataScopes", "[]"), List.class));
		return "modules/sys/roleFormAuthDataScope";
	}
	
	/** 
	 * 保存角色授权数据权限
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveAuthDataScope")
	@ResponseBody
	public String saveAuthDataScope(Role role, HttpServletRequest request) {
		if (StringUtils.isBlank(role.getRoleCode())){
			return renderResult(Global.FALSE, text("角色编码不能为空"));
		}
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		dataScopeService.saveAuthDataScope(role);
		return renderResult(Global.TRUE, text("角色授权数据权限成功"));
	}

}
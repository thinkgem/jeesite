/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 角色Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) Long id) {
		if (id != null){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role) {
		List<Role> list = systemService.findAllRole();
		addModelAttribute("list", list);
		return "modules/sys/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form() {
		addModelAttribute("menuList", systemService.findAllMenu());
		addModelAttribute("categoryList", categoryService.findByUser(false));
		return "modules/sys/roleForm";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, String oldName) {
		if (!beanValidator(role)){
			return form();
		}
		if (!"true".equals(checkName(oldName, role.getName()))){
			addModelMessage("保存角色'" + role.getName() + "'失败, 角色名已存在");
			return form();
		}
		systemService.saveRole(role);
		addFlashMessage("保存角色'" + role.getName() + "'成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam Long id) {
		if (Role.isAdmin(id)){
			addFlashMessage("删除角色失败, 不允许内置角色或编号空");
		}else if (UserUtils.getUser(true).getRoleIdList().contains(id)){
			addFlashMessage("删除角色失败, 不能删除当前用户所在角色");
		}else{
			systemService.deleteRole(id);
			addFlashMessage("删除角色成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/role/?repage";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && systemService.findRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

}

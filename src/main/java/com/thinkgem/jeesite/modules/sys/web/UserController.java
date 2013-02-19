/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-01-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) Long id) {
		if (id != null){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(User user, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user); 
        model.addAttribute("page", page);
		return "modules/sys/userList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getArea()==null){
			user.setArea(UserUtils.getUser().getArea());
		}
		if (user.getOffice()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, String oldLoginName, String newPassword,
			RedirectAttributes redirectAttributes) {
		if (beanValidators(redirectAttributes, user)){
			if ("true".equals(checkLoginName(oldLoginName, user.getLoginName()))){
				systemService.saveUser(user, newPassword);
				addFlashMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
			}else{
				addFlashMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
			}
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/user/?repage";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		if (User.isAdmin(id)){
			addFlashMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户或编号空");
		}else{
			systemService.deleteUser(id);
			addFlashMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/user/?repage";
	}

	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	@RequiresUser
	@RequestMapping(value = "info")
	public String info(User user, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			systemService.saveUser(currentUser, "");
			model.addAttribute("message","保存用户信息成功");
		}
		model.addAttribute("user", currentUser);
		return "modules/sys/userInfo";
	}

	@RequiresUser
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(@RequestParam(required=false) String oldPassword, 
			@RequestParam(required=false) String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword)){
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
			}else{
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}
	
//	@InitBinder
//	public void initBinder(WebDataBinder b) {
//	    
//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//			
//			@Autowired
//			private SystemService systemService;
//			
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				String[] ids = StringUtils.split(text, ",");
//				List<Role> roles = new ArrayList<Role>();
//				for (String id : ids) {
//					Role role = systemService.getRole(Long.valueOf(id));
//					roles.add(role);
//				}
//				setValue(roles);
//			}
//			
//			@Override
//			public String getAsText() {
//				return Collections3.extractToString((List) getValue(), "id", ",");
//			}
//		});
//		
//	}
}

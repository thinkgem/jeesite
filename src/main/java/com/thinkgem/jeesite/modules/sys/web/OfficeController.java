/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 部门Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) Long id) {
		if (id != null){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"list", ""})
	public String list(Office office, Model model) {
		User user = UserUtils.getUser();
		if(user.isAdmin()){
			office.setId(1L);
		}else{
			office.setId(user.getOffice().getId());
		}
		model.addAttribute("office", office);
		List<Office> list = Lists.newArrayList();
		List<Office> sourcelist = officeService.findAll();
		Office.sortList(list, sourcelist, office.getId());
        model.addAttribute("list", list);
		return "modules/sys/officeList";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		if (office.getParent()==null||office.getParent().getId()==null){
			office.setParent(UserUtils.getUser().getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea()==null){
			office.setArea(UserUtils.getUser().getArea());
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		officeService.save(office);
		addMessage(redirectAttributes, "保存部门'" + office.getName() + "'成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/office/";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		if (Office.isRoot(id)){
			addMessage(redirectAttributes, "删除部门失败, 不允许删除顶级部门或编号空");
		}else{
			officeService.delete(id);
			addMessage(redirectAttributes, "删除部门成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/office/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public String treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) String checkedIds, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		StringBuilder sb = new StringBuilder("var data={};");
		User user = UserUtils.getUser();
		List<Office> list = officeService.findAll();
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				sb.append("data['"+(!user.isAdmin() && e.getId().equals(user.getOffice().getId())?"-1":
					e.getParent()!=null?e.getParent().getId():"-1")+"_"+e.getId()+"']='text: "+e.getName());
				if (StringUtils.isNotBlank(checkedIds)){
					sb.append("; checked: ").append(checkedIds.indexOf(","+e.getId()+",")==-1?"false":"true");
				}
				sb.append("';\n");
			}
		}
		return sb.toString();
	}
}

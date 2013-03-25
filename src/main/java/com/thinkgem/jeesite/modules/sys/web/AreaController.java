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
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = Global.ADMIN_PATH+"/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) Long id) {
		if (id != null){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model) {
		User user = UserUtils.getUser();
		if(user.isAdmin()){
			area.setId(1L);
		}else{
			area.setId(user.getArea().getId());
		}
		model.addAttribute("area", area);
		List<Area> list = Lists.newArrayList();
		List<Area> sourcelist = areaService.findAll();
		Area.sortList(list, sourcelist, area.getId());
        model.addAttribute("list", list);
		return "modules/sys/areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent()==null||area.getParent().getId()==null){
			area.setParent(UserUtils.getUser().getArea());
		}
		area.setParent(areaService.get(area.getParent().getId()));
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, area)){
			return form(area, model);
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:"+Global.ADMIN_PATH+"/sys/area/";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		if (Area.isAdmin(id)){
			addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
		}else{
			areaService.delete(id);
			addMessage(redirectAttributes, "删除区域成功");
		}
		return "redirect:"+Global.ADMIN_PATH+"/sys/area/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public String treeData(@RequestParam(required=false) Long extId, @RequestParam(required=false) String checkedIds, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		StringBuilder sb = new StringBuilder("var data={};");
		User user = UserUtils.getUser();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				sb.append("data['"+(!user.isAdmin() && e.getId().equals(user.getArea().getId())?"-1":
						e.getParent()!=null?e.getParent().getId():"-1")+"_"+e.getId()+"']='text: "+e.getName());
				if (StringUtils.isNotBlank(checkedIds)){
					sb.append(";checked: ").append(checkedIds.indexOf(","+e.getId()+",")==-1?"false":"true");
				}
				sb.append("';\n");
			}
		}
		return sb.toString();
	}
}

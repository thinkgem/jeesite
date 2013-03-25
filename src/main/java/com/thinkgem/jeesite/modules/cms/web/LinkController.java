/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Link;
import com.thinkgem.jeesite.modules.cms.service.LinkService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 链接Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = Global.ADMIN_PATH+"/cms/link")
public class LinkController extends BaseController {

	@Autowired
	private LinkService linkService;
	
	@ModelAttribute
	public Link get(@RequestParam(required=false) Long id) {
		if (id != null){
			return linkService.get(id);
		}else{
			return new Link();
		}
	}
	
	@RequiresPermissions("cms:link:view")
	@RequestMapping(value = {"list", ""})
	public String list(Link link, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin() && !SecurityUtils.getSubject().isPermitted("cms:link:audit")){
			link.setUser(user);
		}
        Page<Link> page = linkService.find(new Page<Link>(request, response), link); 
        model.addAttribute("page", page);
		return "modules/cms/linkList";
	}

	@RequiresPermissions("cms:link:view")
	@RequestMapping(value = "form")
	public String form(Link link, Model model) {
		model.addAttribute("link", link);
		return "modules/cms/linkForm";
	}

	@RequiresPermissions("cms:link:edit")
	@RequestMapping(value = "save")
	public String save(Link link, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, link)){
			return form(link, model);
		}
		linkService.save(link);
		addMessage(redirectAttributes, "保存链接'" + StringUtils.abbreviate(link.getTitle(),20) + "'成功");
		return "redirect:"+Global.ADMIN_PATH+"/cms/link/?repage&category.id="+link.getCategory().getId();
	}
	
	@RequiresPermissions("cms:link:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, Long categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		linkService.delete(id, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"恢复":"")+"删除链接成功");
		return "redirect:"+Global.ADMIN_PATH+"/cms/link/?repage&category.id="+categoryId;
	}

	/**
	 * 链接选择列表
	 */
	@RequiresPermissions("cms:link:view")
	@RequestMapping(value = "selectList")
	public String selectList(Link link, HttpServletRequest request, HttpServletResponse response, Model model) {
        list(link, request, response, model);
		return "modules/cms/linkSelectList";
	}
	
	/**
	 * 通过编号获取链接名称
	 */
	@RequiresPermissions("cms:link:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = linkService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}
}

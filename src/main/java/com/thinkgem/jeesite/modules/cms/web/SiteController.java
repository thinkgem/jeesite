/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 站点Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = Global.ADMIN_PATH+"/cms/site")
public class SiteController extends BaseController {

	@Autowired
	private SiteService siteService;
	
	@ModelAttribute
	public Site get(@RequestParam(required=false) Long id) {
		if (id != null){
			return siteService.get(id);
		}else{
			return new Site();
		}
	}
	
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = {"list", ""})
	public String list(Site site, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Site> page = siteService.find(new Page<Site>(request, response), site); 
        model.addAttribute("page", page);
		return "modules/cms/siteList";
	}

	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = "form")
	public String form(Site site, Model model) {
		model.addAttribute("site", site);
		return "modules/cms/siteForm";
	}

	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "save")
	public String save(Site site, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, site)){
			return form(site, model);
		}
		siteService.save(site);
		addMessage(redirectAttributes, "保存站点'" + site.getName() + "'成功");
		return "redirect:"+Global.ADMIN_PATH+"/cms/site/?repage";
	}
	
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		if (Site.isDefault(id)){
			addMessage(redirectAttributes, "删除站点失败, 不允许删除默认站点");
		}else{
			siteService.delete(id, isRe);
			addMessage(redirectAttributes, (isRe!=null&&isRe?"恢复":"")+"删除站点成功");
		}
		return "redirect:"+Global.ADMIN_PATH+"/cms/site/?repage";
	}
	
	/**
	 * 选择站点
	 * @param siteId
	 * @return
	 */
	@RequiresPermissions("cms:site:select")
	@RequiresUser
	@RequestMapping(value = "select")
	public String select(Long id, boolean flag){
		if (id!=null){
			UserUtils.putCache("siteId", id);
		}
		if (flag){
			return "redirect:"+Global.ADMIN_PATH;
		}
		return "modules/cms/siteSelect";
	}
}

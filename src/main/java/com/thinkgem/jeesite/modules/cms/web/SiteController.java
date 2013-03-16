/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 站点Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/cms/site")
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
	public String list(Site site) {
        Page<Site> page = siteService.find(new Page<Site>(request, response), site); 
        addModelAttribute("page", page);
		return "modules/cms/siteList";
	}

	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = "form")
	public String form(Site site) {
		addModelAttribute("site", site);
		return "modules/cms/siteForm";
	}

	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "save")
	public String save(Site site) {
		if (!beanValidator(site)){
			return form(site);
		}
		siteService.save(site);
		addFlashMessage("保存站点'" + site.getName() + "'成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/site/?repage";
	}
	
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, @RequestParam(required=false) Boolean isRe) {
		if (Site.isDefault(id)){
			addFlashMessage("删除站点失败, 不允许删除默认站点");
		}else{
			siteService.delete(id, isRe);
			addFlashMessage((isRe!=null&&isRe?"恢复":"")+"删除站点成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/site/?repage";
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
			return "redirect:"+BaseController.ADMIN_PATH;
		}
		return "modules/cms/siteSelect";
	}
}

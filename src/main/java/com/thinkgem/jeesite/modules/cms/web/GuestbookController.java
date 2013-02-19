/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.service.GuestbookService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 留言Controller
 * @author ThinkGem
 * @version 2013-01-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/cms/guestbook")
public class GuestbookController extends BaseController {

	@Autowired
	private GuestbookService guestbookService;
	
	@ModelAttribute
	public Guestbook get(@RequestParam(required=false) Long id) {
		if (id != null){
			return guestbookService.get(id);
		}else{
			return new Guestbook();
		}
	}
	
	@RequiresPermissions("cms:guestbook:view")
	@RequestMapping(value = {"list", ""})
	public String list(Guestbook guestbook, Model model) {
        Page<Guestbook> page = guestbookService.find(new Page<Guestbook>(request, response), guestbook); 
        model.addAttribute("page", page);
		return "modules/cms/guestbookList";
	}

	@RequiresPermissions("cms:guestbook:view")
	@RequestMapping(value = "form")
	public String form(Guestbook guestbook, Model model) {
		model.addAttribute("guestbook", guestbook);
		return "modules/cms/guestbookForm";
	}

	@RequiresPermissions("cms:guestbook:edit")
	@RequestMapping(value = "save")
	public String save(Guestbook guestbook, RedirectAttributes redirectAttributes) {
		if (beanValidators(redirectAttributes, guestbook)){
			if (guestbook.getReUser() == null){
				guestbook.setReUser(UserUtils.getUser());
			}
			if (guestbook.getReDate() == null){
				guestbook.setReDate(new Date());
			}
			guestbookService.save(guestbook);
			addFlashMessage(redirectAttributes, DictUtils.getDictLabel(guestbook.getStatus(), "cms_status", "保存")
					+"留言'" + guestbook.getName() + "'成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/guestbook/?repage&status=2";
	}
	
	@RequiresPermissions("cms:guestbook:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		guestbookService.delete(id, isRe);
		addFlashMessage(redirectAttributes, (isRe!=null&&isRe?"恢复审核":"删除")+"留言成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/guestbook/?repage&status=2";
	}

}

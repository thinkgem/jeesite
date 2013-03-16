/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web.front;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.GuestbookService;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;

/**
 * 留言板Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.FRONT_PATH)
public class FrontGuestbookController extends BaseController{
	
	@Autowired
	private GuestbookService guestbookService;

	/**
	 * 留言板
	 */
	@RequestMapping(value = "guestbook", method=RequestMethod.GET)
	public String guestbook(@RequestParam(required=false, defaultValue="1") Integer pageNo,
			@RequestParam(required=false, defaultValue="30") Integer pageSize) {
		Site site = CmsUtils.getSite(1L);
		addModelAttribute("site", site);
		Page<Guestbook> page = new Page<Guestbook>(pageNo, pageSize);
		Guestbook guestbook = new Guestbook();
		guestbook.setStatus(Guestbook.STATUS_RELEASE);
		page = guestbookService.find(page, guestbook);
		addModelAttribute("page", page);
		return "modules/cms/front/themes/"+site.getTheme()+"/frontGuestbook";
	}
	
	/**
	 * 留言板-保存留言信息
	 */
	@RequestMapping(value = "guestbook", method=RequestMethod.POST)
	public String guestbookSave(Guestbook guestbook, String validateCode) {
		if (StringUtils.isNotBlank(validateCode)){
			if (ValidateCodeServlet.validate(request, validateCode)){
				guestbook.setIp(request.getRemoteAddr());
				guestbook.setCreateDate(new Date());
				guestbook.setStatus(Guestbook.STATUS_AUDIT);
				guestbookService.save(guestbook);
				addFlashMessage("提交成功，请等待管理员审核。");
			}else{
				addFlashMessage("验证码不正确。");
			}
		}else{
			addFlashMessage("验证码不能为空。");
		}
		return "redirect:"+BaseController.FRONT_PATH+"/guestbook";
	}
	
}

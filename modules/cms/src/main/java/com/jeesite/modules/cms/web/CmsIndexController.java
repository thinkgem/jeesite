/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.utils.CmsUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 内容管理Controller
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsIndexController extends BaseController {

	/**
	 * 管理内容页
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "index")
	public String index(Model model) {
		model.addAttribute("currentSite", CmsUtils.getCurrentSite());
		model.addAttribute("siteList", CmsUtils.getSiteList());
		return "modules/cms/cmsIndex";
	}

	/**
	 * 查看内容页
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "view")
	public String view(Model model) {
		return "modules/cms/cmsView";
	}

	/**
	 * 空内容页面
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return "modules/cms/cmsNone";
	}
	
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.web.BaseController;

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
	public String index() {
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

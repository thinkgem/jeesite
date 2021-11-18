/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.web.BaseController;

/**
 * 测试树表Controller
 * @author ThinkGem
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testTree")
public class TestTreeController extends BaseController {

	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testTree:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return REDIRECT + adminPath + "/test2/testTree";
	}
	
}
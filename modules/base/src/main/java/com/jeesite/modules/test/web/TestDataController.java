/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.test.entity.TestData;

/**
 * 测试数据Controller
 * @author ThinkGem
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testData")
public class TestDataController extends BaseController {

	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestData testData, Model model) {
		return REDIRECT + adminPath + "/test1/testData";
	}
	
}
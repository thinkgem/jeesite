/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.test.entity.TestDataChild;
import com.jeesite.modules.test.service.TestDataChildService;

/**
 * 测试子表Controller
 * @author ThinkGem
 * @version 2018-01-31
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testDataChild")
public class TestDataChildController extends BaseController {

	@Autowired
	private TestDataChildService testDataChildService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestDataChild get(String id, boolean isNewRecord) {
		return testDataChildService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testDataChild:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestDataChild testDataChild, Model model) {
		model.addAttribute("testDataChild", testDataChild);
		return "modules/test/testDataChildList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("test:testDataChild:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<TestDataChild> listData(TestDataChild testDataChild, HttpServletRequest request, HttpServletResponse response) {
		Page<TestDataChild> page = testDataChildService.findPage(new Page<TestDataChild>(request, response), testDataChild); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("test:testDataChild:view")
	@RequestMapping(value = "form")
	public String form(TestDataChild testDataChild, Model model) {
		model.addAttribute("testDataChild", testDataChild);
		return "modules/test/testDataChildForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("test:testDataChild:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated TestDataChild testDataChild) {
		testDataChildService.save(testDataChild);
		return renderResult(Global.TRUE, "保存数据成功！");
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("test:testDataChild:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestDataChild testDataChild) {
		testDataChildService.delete(testDataChild);
		return renderResult(Global.TRUE, "删除数据成功！");
	}
	
}
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
import com.jeesite.modules.test.client.TestDataServiceClient;
import com.jeesite.modules.test.entity.TestData;

/**
 * 测试数据Controller，调用 test1 服务
 * @author ThinkGem
 * @version 2018-10-20
 */
@Controller
@RequestMapping(value = "${adminPath}/test2/testData")
public class TestData2Controller extends BaseController {

	@Autowired
	private TestDataServiceClient testDataServiceClient;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestData get(String id, boolean isNewRecord) {
		//System.out.println("s1: "+UserUtils.getSession().getId());
		return testDataServiceClient.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestData testData, Model model) {
		model.addAttribute("testData", testData);
		return "modules/test/testDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<TestData> listData(TestData testData, HttpServletRequest request, HttpServletResponse response) {
		testData.setPage(new Page<>(request, response));
		Page<TestData> page = testDataServiceClient.findPage(testData); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = "form")
	public String form(TestData testData, Model model) {
		model.addAttribute("testData", testData);
		return "modules/test/testDataForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "save")
	@ResponseBody
//	@GlobalTransactional
	public String save(@Validated TestData testData) {
		testDataServiceClient.save(testData);
		return renderResult(Global.TRUE, text("保存数据成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("test:testData:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(TestData testData) {
		testData.setStatus(TestData.STATUS_DISABLE);
		testDataServiceClient.updateStatus(testData);
		return renderResult(Global.TRUE, text("停用数据成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("test:testData:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(TestData testData) {
		testData.setStatus(TestData.STATUS_NORMAL);
		testDataServiceClient.updateStatus(testData);
		return renderResult(Global.TRUE, text("启用数据成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("test:testData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestData testData) {
		testDataServiceClient.delete(testData);
		return renderResult(Global.TRUE, text("删除数据成功！"));
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.web;

import java.util.List;

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
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.entity.TestDataChild;
import com.jeesite.modules.test.service.TestDataService;

/**
 * 测试数据Controller
 * @author ThinkGem
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testData")
public class TestDataController extends BaseController {

	@Autowired
	private TestDataService testDataService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestData get(String id, boolean isNewRecord) {
		return testDataService.get(id, isNewRecord);
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
		Page<TestData> page = testDataService.findPage(testData);
		return page;
	}
	
	/**
	 * 查询子表列表数据
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = "subListData")
	@ResponseBody
	public List<TestDataChild> subListData(TestDataChild testDataChild) {
		return testDataService.findSubList(testDataChild);
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
	public String save(@Validated TestData testData) {
		testDataService.save(testData);
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
		testDataService.updateStatus(testData);
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
		testDataService.updateStatus(testData);
		return renderResult(Global.TRUE, text("启用数据成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("test:testData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestData testData) {
		testDataService.delete(testData);
		return renderResult(Global.TRUE, text("删除数据成功！"));
	}
	
	/**
	 * 事务测试
	 */
	@RequiresPermissions("test:testData:edit")
	@RequestMapping(value = "transTest")
	@ResponseBody
	public String transTest(TestData testData) {
		try{
			testDataService.transTest(testData);
		}catch (Exception e) {
			logger.debug("事务测试信息，报错回滚：" + e.getMessage());
		}
		boolean bl = testDataService.transValid(testData);
		return renderResult(Global.TRUE, "事务测试"+(bl?"成功，数据已":"失败，数据未")+"回滚！");
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.api;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jeesite.common.entity.Page;
import com.jeesite.modules.test.entity.TestData;

/**
 * 测试数据Service
 * @author ThinkGem
 * @version 2018-10-18
 */
@RequestMapping(value = "/api/test1/testData")
public interface TestDataServiceApi {
	
	/**
	 * 获取单条数据
	 * @param testData
	 * @return
	 */
	@RequiresPermissions("test:testData:view")
	@GetMapping(value = "get")
	public TestData get(@RequestParam("id") String id, @RequestParam("isNewRecord") boolean isNewRecord);
	
	/**
	 * 获取单条数据
	 * @param testData
	 * @return
	 */
	@RequiresPermissions("test:testData:view")
	@GetMapping(value = "getByPk")
	public TestData get(@RequestParam("id") String id);
	
	/**
	 * 查询分页数据
	 * @param testData
	 * @return
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "findList")
	public List<TestData> findList(TestData testData);
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param testData
	 * @return
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "findPage")
	public Page<TestData> findPage(TestData testData);
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "save")
	public void save(TestData testData);
	
	/**
	 * 更新状态
	 * @param testData
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "updateStatus")
	public void updateStatus(TestData testData);
	
	/**
	 * 删除数据
	 * @param testData
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "delete")
	public void delete(TestData testData);
	
}
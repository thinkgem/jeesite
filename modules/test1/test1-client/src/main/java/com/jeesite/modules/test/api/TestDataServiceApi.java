/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.api;

import com.jeesite.common.entity.Page;
import com.jeesite.modules.test.entity.TestData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 测试数据API
 * @author ThinkGem
 * @version 2018-10-18
 */
@RequestMapping(value = "/api/test1/testData")
public interface TestDataServiceApi {
	
	/**
	 * 获取单条数据
	 * @param id 主键编号
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "getByPk")
	TestData get(@RequestParam(name = "id", required = false) String id);
	
	/**
	 * 获取单条数据
	 * @param id 主键编号
	 * @param isNewRecord 是否新记录
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "getByPkAndIsNewRecord")
	TestData get(@RequestParam(name = "id", required = false) String id, @RequestParam("isNewRecord") boolean isNewRecord);
	
	/**
	 * 查询分页数据
	 * @param testData 查询条件
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "findList")
	List<TestData> findList(TestData testData);
	
	/**
	 * 查询分页数据
	 * @param testData 查询条件
	 * @param testData page 分页对象
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "findPage")
	Page<TestData> findPage(TestData testData);
	
	/**
	 * 查询数据总数
	 * @param testData 查询条件
	 */
	@RequiresPermissions("test:testData:view")
	@PostMapping(value = "findCount")
	long findCount(TestData testData);
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData 数据对象
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "save")
	void save(TestData testData);
	
	/**
	 * 更新状态
	 * @param testData 数据对象
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "updateStatus")
	void updateStatus(TestData testData);
	
	/**
	 * 删除数据
	 * @param testData 数据对象
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "delete")
	void delete(TestData testData);
	
}
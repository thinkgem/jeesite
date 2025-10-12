/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.api;

import com.jeesite.common.entity.Page;
import com.jeesite.modules.test.entity.TestTree;
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
@RequestMapping(value = "/api/test2/testTree")
public interface TestTreeServiceApi {
	
	/**
	 * 获取单条数据
	 * @param id 主键编号
	 */
	@RequiresPermissions("test:testTree:view")
	@PostMapping(value = "getByPk")
	TestTree get(@RequestParam(name = "id", required = false) String id);
	
	/**
	 * 获取单条数据
	 * @param id 主键编号
	 * @param isNewRecord 是否新记录
	 */
	@RequiresPermissions("test:testTree:view")
	@PostMapping(value = "getByPkAndIsNewRecord")
	TestTree get(@RequestParam(name = "id", required = false) String id, @RequestParam("isNewRecord") boolean isNewRecord);
	
	/**
	 * 根据父节点单条数据
	 * @param testTree 查询条件
	 */
	@RequiresPermissions("test:testTree:view")
	@PostMapping(value = "getLastByParentCode")
	TestTree getLastByParentCode(TestTree testTree);
	
	/**
	 * 查询分页数据
	 * @param testTree 查询条件
	 */
	@RequiresPermissions("test:testTree:view")
	@PostMapping(value = "findList")
	List<TestTree> findList(TestTree testTree);
	
	/**
	 * 查询分页数据
	 * @param testTree 查询条件
	 * @param testTree page 分页对象
	 */
	@RequiresPermissions("test:testTree:view")
	@PostMapping(value = "findPage")
	Page<TestTree> findPage(TestTree testTree);
	
	/**
	 * 查询数据总数
	 * @param testTree 查询条件
	 */
	@RequiresPermissions("test:testTree:view")
	@PostMapping(value = "findCount")
	long findCount(TestTree testTree);
	
	/**
	 * 保存数据（插入或更新）
	 * @param testTree 数据对象
	 */
	@RequiresPermissions("test:testTree:edit")
	@PostMapping(value = "save")
	void save(TestTree testTree);
	
	/**
	 * 更新状态
	 * @param testTree 数据对象
	 */
	@RequiresPermissions("test:testTree:edit")
	@PostMapping(value = "updateStatus")
	void updateStatus(TestTree testTree);
	
	/**
	 * 删除数据
	 * @param testTree 数据对象
	 */
	@RequiresPermissions("test:testTree:edit")
	@PostMapping(value = "delete")
	void delete(TestTree testTree);
	
	/**
	 * 修复树结构数据
	 */
	@RequiresPermissions("test:testTree:edit")
	@PostMapping(value = "fixTreeData")
	void fixTreeData();
	
}
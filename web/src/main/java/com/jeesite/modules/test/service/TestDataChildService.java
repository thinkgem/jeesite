/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.test.entity.TestDataChild;
import com.jeesite.modules.test.dao.TestDataChildDao;

/**
 * 测试子表Service
 * @author ThinkGem
 * @version 2018-01-31
 */
@Service
@Transactional(readOnly=true)
public class TestDataChildService extends CrudService<TestDataChildDao, TestDataChild> {
	
	/**
	 * 获取单条数据
	 * @param testDataChild
	 * @return
	 */
	public TestDataChild get(TestDataChild testDataChild) {
		return super.get(testDataChild);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param testDataChild
	 * @return
	 */
	public Page<TestDataChild> findPage(Page<TestDataChild> page, TestDataChild testDataChild) {
		return super.findPage(page, testDataChild);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testDataChild
	 */
	@Transactional(readOnly=false)
	public void save(TestDataChild testDataChild) {
		super.save(testDataChild);
	}
	
	/**
	 * 更新状态
	 * @param testDataChild
	 */
	@Transactional(readOnly=false)
	public void updateStatus(TestDataChild testDataChild) {
		super.updateStatus(testDataChild);
	}
	
	/**
	 * 删除数据
	 * @param testDataChild
	 */
	@Transactional(readOnly=false)
	public void delete(TestDataChild testDataChild) {
		super.delete(testDataChild);
	}
	
}
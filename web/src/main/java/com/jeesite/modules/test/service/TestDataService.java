/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.dao.TestDataDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 测试数据Service
 * @author ThinkGem
 * @version 2018-01-28
 */
@Service
@Transactional(readOnly=true)
public class TestDataService extends CrudService<TestDataDao, TestData> {
	
	/**
	 * 获取单条数据
	 * @param testData
	 * @return
	 */
	public TestData get(TestData testData) {
		return super.get(testData);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param testData
	 * @return
	 */
	public Page<TestData> findPage(Page<TestData> page, TestData testData) {
		return super.findPage(page, testData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData
	 */
	@Transactional(readOnly=false)
	public void save(TestData testData) {
		super.save(testData);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testData.getId(), "testData_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testData.getId(), "testData_file");
	}
	
	/**
	 * 更新状态
	 * @param testData
	 */
	@Transactional(readOnly=false)
	public void updateStatus(TestData testData) {
		super.updateStatus(testData);
	}
	
	/**
	 * 删除数据
	 * @param testData
	 */
	@Transactional(readOnly=false)
	public void delete(TestData testData) {
		super.delete(testData);
	}
	
}
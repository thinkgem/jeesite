/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.test.dao.TestDataChildDao;
import com.jeesite.modules.test.dao.TestDataDao;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.entity.TestDataChild;

/**
 * 测试数据Service
 * @author ThinkGem
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly=true)
public class TestDataService extends CrudService<TestDataDao, TestData> {
	
	@Autowired
	private TestDataChildDao testDataChildDao;
	
	/**
	 * 获取单条数据
	 * @param testData
	 * @return
	 */
	@Override
	public TestData get(TestData testData) {
		TestData entity = super.get(testData);
		if (entity != null){
			TestDataChild testDataChild = new TestDataChild(entity);
			testDataChild.setStatus(TestDataChild.STATUS_NORMAL);
			entity.setTestDataChildList(testDataChildDao.findList(testDataChild));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param testData
	 * @return
	 */
	@Override
	public Page<TestData> findPage(Page<TestData> page, TestData testData) {
		return super.findPage(page, testData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TestData testData) {
		super.save(testData);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testData.getId(), "testData_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testData.getId(), "testData_file");
		// 保存 TestData子表
		for (TestDataChild testDataChild : testData.getTestDataChildList()){
			if (!TestDataChild.STATUS_DELETE.equals(testDataChild.getStatus())){
				testDataChild.setTestData(testData);
				if (testDataChild.getIsNewRecord()){
					testDataChild.preInsert();
					testDataChildDao.insert(testDataChild);
				}else{
					testDataChild.preUpdate();
					testDataChildDao.update(testDataChild);
				}
			}else{
				testDataChildDao.delete(testDataChild);
			}
		}
	}
	
	/**
	 * 更新状态
	 * @param testData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TestData testData) {
		super.updateStatus(testData);
	}
	
	/**
	 * 删除数据
	 * @param testData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TestData testData) {
		super.delete(testData);
		TestDataChild testDataChild = new TestDataChild();
		testDataChild.setTestData(testData);
		testDataChildDao.delete(testDataChild);
	}
	
}
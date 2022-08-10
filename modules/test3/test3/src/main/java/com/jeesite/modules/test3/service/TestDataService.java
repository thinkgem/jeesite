/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.test3.api.TestDataServiceApi;
import com.jeesite.modules.test3.dao.TestDataChildDao;
import com.jeesite.modules.test3.dao.TestDataDao;
import com.jeesite.modules.test3.entity.TestData;
import com.jeesite.modules.test3.entity.TestDataChild;

import org.springframework.transaction.annotation.Transactional;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 测试数据Service
 * @author ThinkGem
 * @version 2021-06-22
 */
@Service
@RestController
public class TestDataService extends CrudService<TestDataDao, TestData>
		implements TestDataServiceApi {
	
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
	 * @param testData 查询条件
	 * @param testData.page 分页对象
	 * @return
	 */
	@Override
	public Page<TestData> findPage(TestData testData) {
		return super.findPage(testData);
	}
	
	/**
	 * 查询子表分页数据
	 * @param testDataChild
	 * @param testDataChild.page 分页对象
	 * @return
	 */
	public Page<TestDataChild> findSubPage(TestDataChild testDataChild) {
		Page<TestDataChild> page = testDataChild.getPage();
		page.setList(testDataChildDao.findList(testDataChild));
		return page;
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData
	 */
	@Override
	@GlobalTransactional
	@Transactional
	public void save(TestData testData) {
		super.save(testData);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testData, testData.getId(), "testData_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testData, testData.getId(), "testData_file");
		// 保存 TestData子表
		for (TestDataChild testDataChild : testData.getTestDataChildList()){
			if (!TestDataChild.STATUS_DELETE.equals(testDataChild.getStatus())){
				testDataChild.setTestData(testData);
				if (testDataChild.getIsNewRecord()){
					testDataChildDao.insert(testDataChild);
				}else{
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
	@GlobalTransactional
	@Transactional
	public void updateStatus(TestData testData) {
		super.updateStatus(testData);
	}
	
	/**
	 * 删除数据
	 * @param testData
	 */
	@Override
	@GlobalTransactional
	@Transactional
	public void delete(TestData testData) {
		super.delete(testData);
		TestDataChild testDataChild = new TestDataChild();
		testDataChild.setTestData(testData);
		testDataChildDao.deleteByEntity(testDataChild);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.test.dao.TestDataChildDao;
import com.jeesite.modules.test.dao.TestDataDao;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.entity.TestDataChild;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试数据Service
 * @author ThinkGem
 * @version 2018-04-22
 */
@Service
public class TestDataService extends CrudService<TestDataDao, TestData> {
	
	private final TestDataChildDao testDataChildDao;

	public TestDataService(TestDataChildDao testDataChildDao) {
		this.testDataChildDao = testDataChildDao;
	}

	/**
	 * 获取单条数据
	 * @param testData 主键
	 */
	@Override
	public TestData get(TestData testData) {
		return super.get(testData);
	}

	/**
	 * 加载子表数据
	 */
	public TestData loadChildData(TestData testData) {
		if (testData != null && !testData.getIsNewRecord()){
			TestDataChild testDataChild = new TestDataChild(testData);
			testDataChild.setStatus(TestDataChild.STATUS_NORMAL);
			testData.setTestDataChildList(testDataChildDao.findList(testDataChild));
		}
		return testData;
	}
	
	/**
	 * 查询分页数据
	 * @param testData 查询条件
	 * @param testData page 分页对象
	 */
	@Override
	public Page<TestData> findPage(TestData testData) {
		
//		// 演示Map参数和返回值，支持分页
//		Page<Map<String, Object>> pageMap = new Page<>();
//		Map<String, Object> params = MapUtils.newHashMap();
//		params.put("testInput", "123");
//		params.put("page", pageMap);
//		pageMap.setList(dao.findListForMap(params));
//		System.out.println(pageMap.getList());
//		System.out.println(pageMap.getCount());
		
		return super.findPage(testData);
	}
	
	/**
	 * 查询子表分页数据
	 * @param testData 查询条件
	 * @param testData page 分页对象
	 */
	public List<TestDataChild> findSubList(TestDataChild testData) {
		return testDataChildDao.findList(testData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData 数据对象
	 */
	@Override
	@Transactional
	public void save(TestData testData) {
		super.save(testData);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testData, testData.getId(), "testData_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testData, testData.getId(), "testData_file");
		// 保存 TestData子表
		int index = 0;
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
			// 保存上传图片
			FileUploadUtils.saveFileUpload(testDataChild, testDataChild.getId(),
					"testDataChildList["+index+"].testDataChild_image");
			// 保存上传附件
			FileUploadUtils.saveFileUpload(testDataChild, testDataChild.getId(),
					"testDataChildList["+index+"].testDataChild_file");
			index++;
		}
	}
	
	/**
	 * 更新状态
	 * @param testData 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(TestData testData) {
		super.updateStatus(testData);
	}
	
	/**
	 * 删除数据
	 * @param testData 数据对象
	 */
	@Override
	@Transactional
	public void delete(TestData testData) {
		super.delete(testData);
		TestDataChild testDataChild = new TestDataChild();
		testDataChild.setTestData(testData);
		testDataChildDao.deleteByEntity(testDataChild);
	}
	
	/**
	 * 任务调度测试：testDataService.executeTestTask(userService, 1, 2L, 3F, 4D, 'abc')
	 */
	public void executeTestTask(UserService userService, Integer i, Long l, Float f, Double d, String s){
		System.out.println(DateUtils.getTime() + " 任务执行了~~~  bean: " + userService + ", i: " + i
				+ ", l: " + l + ", f: " + f + ", d: " + d + ", s: " + s);
	}
	
	/**
	 * 事务测试，若 Child 报错，则回滚
	 */
	@Transactional//(propagation=Propagation.NOT_SUPPORTED)
	public void transTest(TestData testData) {
		testData.setTestInput("transTest");
		testData.setTestTextarea(IdGen.randomBase62(5));
		dao.insert(testData);
		TestDataChild testDataChild = new TestDataChild();
		testDataChild.setTestData(testData);
		// 设置一个超出数据库范围的值，抛出数据库异常
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<500; i++){
			sb.append("transTest" + i);
		}
		testDataChild.setTestInput(sb.toString());
		testDataChildDao.insert(testDataChild);
	}
	
	/**
	 * 事务验证，返回空，则事务回滚成功
	 */
	public boolean transValid(TestData testData) {
		return dao.get(testData) == null;
	}
	
}
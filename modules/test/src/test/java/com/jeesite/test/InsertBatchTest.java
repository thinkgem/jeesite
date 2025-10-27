/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.test.dao.TestDataDao;
import com.jeesite.modules.test.entity.TestData;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * 批量插入测试
 * @author ThinkGem
 * @version 2019年10月28日
 */
@ActiveProfiles("test")
@SpringBootTest(classes = ApplicationTest.class)
public class InsertBatchTest extends BaseSpringContextTests {

	private final TestDataDao testDataDao;

	public InsertBatchTest(TestDataDao testDataDao) {
		this.testDataDao = testDataDao;
	}

	@Test
	public void testData1() throws Exception{
		List<TestData> list = ListUtils.newArrayList();
		for(int i=0; i<5000; i++){
			TestData testData = new TestData();
			testData.setTestInput("test"+i);
			list.add(testData);
		}
		testDataDao.insertBatch(list);
		list = testDataDao.findList(new TestData());
		System.out.println("insert: " + list.size());
		long count = testDataDao.updateBatch(list);
		System.out.println("update: " + count);
	}
	
}

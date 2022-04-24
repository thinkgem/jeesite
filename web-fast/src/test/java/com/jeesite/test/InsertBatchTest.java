/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.callback.MethodCallback;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.FastApplication;
import com.jeesite.modules.test.dao.TestDataDao;
import com.jeesite.modules.test.entity.TestData;

/**
 * 批量插入测试
 * @author ThinkGem
 * @version 2019年10月28日
 */
@ActiveProfiles("test")
@SpringBootTest(classes = FastApplication.class)
public class InsertBatchTest extends BaseSpringContextTests {
	
	@Autowired
	private TestDataDao testDataDao;
	
	@Test
	public void testData() throws Exception{
		List<TestData> list = ListUtils.newArrayList();
		for(int i=0; i<5000; i++){
			TestData testData = new TestData();
			testData.setTestInput("test"+i);
			list.add(testData);
		}
		ListUtils.pageList(list, 100, new MethodCallback() {
			@SuppressWarnings("unchecked")
			public Object execute(Object... objs) {
				return testDataDao.insertBatch((List<TestData>)objs[0]);
			}
		});
		list = testDataDao.findList(new TestData());
		System.out.println("size: " + list.size());
	}
	
}

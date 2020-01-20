/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.service.BaseService;
import com.jeesite.modules.test.client.TestDataServiceClient;
import com.jeesite.modules.test.client.TestTreeServiceClient;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.entity.TestTree;

/**
 * 分布式事务测试
 * @author ThinkGem
 * @version 2020-1-9
 */
@Service
@Transactional(readOnly=true)
public class TransTestService extends BaseService{
	
	@Autowired
	private TestDataServiceClient testDataService;
	@Autowired
	private TestTreeServiceClient testTreeService;
	
	/**
	 * 事务测试，第二个接口调用故意抛出异常
	 */
//	@LcnTransaction
	@Transactional(readOnly=false)
	public void transTest(TestData testData) {
		
		// 正常保存 testData 数据
		testData.setIsNewRecord(true);
		testData.setId(IdGen.randomBase62(5));
		testData.setTestInput(testData.getId());
		testData.setTestTextarea(testData.getId());
		testDataService.save(testData);
		
		// 保存 testTree 失败，抛出异常，testData 应回滚
		TestTree testTree = new TestTree();
		testTree.setIsNewRecord(true);
		testTree.setTreeCode(testData.getId());
		testTree.setTreeName(testData.getId());
		TestTree where = new TestTree();
		where.setParentCode(TestTree.ROOT_CODE);
		TestTree last = testTreeService.getLastByParentCode(where);
		if (last != null){
			testTree.setTreeSort(last.getTreeSort()+30);
		}
		// 设置一个超出数据库范围的值，抛出数据库异常
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<500; i++){
			sb.append("transTest" + i);
		}
		testTree.setTreeName(sb.toString());
		testTreeService.save(testTree);
		
		// 有些情况可能需要手动回滚事务，调用该方法即可
		//DTXUserControls.rollbackCurrentGroup();
	}
	
	/**
	 * 事务验证，返回空，则事务回滚成功
	 */
	public boolean transValid(TestData testData) {
		if (StringUtils.isBlank(testData.getId())){
			return true;
		}
		return testDataService.get(testData.getId()) == null;
	}
	
}
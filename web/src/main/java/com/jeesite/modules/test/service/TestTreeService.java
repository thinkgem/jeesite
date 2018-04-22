/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.test.dao.TestTreeDao;
import com.jeesite.modules.test.entity.TestTree;

/**
 * 测试树表Service
 * @author ThinkGem
 * @version 2018-04-22
 */
@Service
@Transactional(readOnly=true)
public class TestTreeService extends TreeService<TestTreeDao, TestTree> {
	
	/**
	 * 获取单条数据
	 * @param testTree
	 * @return
	 */
	@Override
	public TestTree get(TestTree testTree) {
		return super.get(testTree);
	}
	
	/**
	 * 查询列表数据
	 * @param testTree
	 * @return
	 */
	@Override
	public List<TestTree> findList(TestTree testTree) {
		return super.findList(testTree);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TestTree testTree) {
		super.save(testTree);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testTree.getId(), "testTree_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testTree.getId(), "testTree_file");
	}
	
	/**
	 * 更新状态
	 * @param testTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TestTree testTree) {
		super.updateStatus(testTree);
	}
	
	/**
	 * 删除数据
	 * @param testTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TestTree testTree) {
		super.delete(testTree);
	}
	
}
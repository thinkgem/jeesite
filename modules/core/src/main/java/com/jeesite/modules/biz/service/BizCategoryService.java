/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.biz.service;

import com.jeesite.common.service.api.TreeServiceApi;
import com.jeesite.modules.biz.entity.BizCategory;

import java.util.List;

/**
 * 业务分类Service
 * @author ThinkGem
 * @version 2019-08-12
 */
public interface BizCategoryService extends TreeServiceApi<BizCategory> {
	
	/**
	 * 获取单条数据
	 * @param bpmCategory
	 * @return
	 */
	@Override
	BizCategory get(BizCategory bpmCategory);
	
	/**
	 * 查询列表数据
	 * @param bpmCategory
	 * @return
	 */
	@Override
	List<BizCategory> findList(BizCategory bpmCategory);
	
	/**
	 * 保存数据（插入或更新）
	 * @param bpmCategory
	 */
	@Override
	void save(BizCategory bpmCategory);
	
	/**
	 * 更新状态
	 * @param bpmCategory
	 */
	@Override
	void updateStatus(BizCategory bpmCategory);
	
	/**
	 * 删除数据
	 * @param bpmCategory
	 */
	@Override
	void delete(BizCategory bpmCategory);
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.biz.service.support;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.biz.dao.BizCategoryDao;
import com.jeesite.modules.biz.entity.BizCategory;
import com.jeesite.modules.biz.service.BizCategoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务分类Service
 * @author ThinkGem
 * @version 2019-08-12
 */
public class BizCategoryServiceSupport extends TreeService<BizCategoryDao, BizCategory>
		implements BizCategoryService {
	
	/**
	 * 获取单条数据
	 * @param bizCategory
	 * @return
	 */
	@Override
	public BizCategory get(BizCategory bizCategory) {
		return super.get(bizCategory);
	}
	
	/**
	 * 查询列表数据
	 * @param bizCategory
	 * @return
	 */
	@Override
	public List<BizCategory> findList(BizCategory bizCategory) {
		return super.findList(bizCategory);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bizCategory
	 */
	@Override
	@Transactional
	public void save(BizCategory bizCategory) {
		if (bizCategory.getIsNewRecord()){
			// 生成主键，并验证改主键是否存在，如存在则抛出验证信息
			genIdAndValid(bizCategory, bizCategory.getViewCode());
		}
		super.save(bizCategory);
	}
	
	/**
	 * 更新状态
	 * @param bizCategory
	 */
	@Override
	@Transactional
	public void updateStatus(BizCategory bizCategory) {
		super.updateStatus(bizCategory);
	}
	
	/**
	 * 删除数据
	 * @param bizCategory
	 */
	@Override
	@Transactional
	public void delete(BizCategory bizCategory) {
		bizCategory.sqlMap().markIdDelete();
		super.delete(bizCategory);
	}
	
}
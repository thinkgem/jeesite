/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppUpgrade;
import com.jeesite.modules.app.dao.AppUpgradeDao;

/**
 * APP版本管理Service
 * @author ThinkGem
 * @version 2021-04-09
 */
@Service
public class AppUpgradeService extends CrudService<AppUpgradeDao, AppUpgrade> {
	
	/**
	 * 获取单条数据
	 * @param appUpgrade
	 * @return
	 */
	@Override
	public AppUpgrade get(AppUpgrade appUpgrade) {
		return super.get(appUpgrade);
	}
	
	/**
	 * 查询分页数据
	 * @param appUpgrade 查询条件
	 * @param appUpgrade page 分页对象
	 * @return
	 */
	@Override
	public Page<AppUpgrade> findPage(AppUpgrade appUpgrade) {
		return super.findPage(appUpgrade);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appUpgrade
	 */
	@Override
	@Transactional
	public void save(AppUpgrade appUpgrade) {
		super.save(appUpgrade);
	}
	
	/**
	 * 更新状态
	 * @param appUpgrade
	 */
	@Override
	@Transactional
	public void updateStatus(AppUpgrade appUpgrade) {
		super.updateStatus(appUpgrade);
	}
	
	/**
	 * 删除数据
	 * @param appUpgrade
	 */
	@Override
	@Transactional
	public void delete(AppUpgrade appUpgrade) {
		super.delete(appUpgrade);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import com.jeesite.common.service.api.TreeServiceApi;
import com.jeesite.modules.sys.entity.Company;

/**
 * 公司管理Service
 * @author ThinkGem
 * @version 2016-4-23
 */
public interface CompanyService extends TreeServiceApi<Company> {

	/**
	 * 获取单条数据
	 */
	@Override
	public Company get(Company company);
	
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(Company company, String ctrlPermi);

	/**
	 * 查询公司列表
	 */
	@Override
	public List<Company> findList(Company company);

	/**
	 * 保存公司
	 */
	@Override
	public void save(Company company);
	
	/**
	 * 删除公司
	 */
	@Override
	public void delete(Company company);

	/**
	 * 停用当前节点
	 */
	@Override
	public void updateStatus(Company company);

}
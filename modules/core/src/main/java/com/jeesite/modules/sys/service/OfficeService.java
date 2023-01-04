/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.service.api.TreeServiceApi;
import com.jeesite.modules.sys.entity.Office;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2016-4-23
 */
public interface OfficeService extends TreeServiceApi<Office> {

	/**
	 * 获取单条数据
	 */
	@Override
	Office get(Office office);
	
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	void addDataScopeFilter(Office office, String ctrlPermi);

	/**
	 * 查询组织机构列表
	 */
	@Override
	List<Office> findList(Office office);

	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	void save(Office office);

	/**
	 * 导入机构数据
	 * @param file 导入的用户数据文件
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 */
	String importData(MultipartFile file, Boolean isUpdateSupport);

	/**
	 * 更新部门状态
	 */
	@Override
	void updateStatus(Office office);

	/**
	 * 删除数据
	 */
	@Override
	void delete(Office office);

}

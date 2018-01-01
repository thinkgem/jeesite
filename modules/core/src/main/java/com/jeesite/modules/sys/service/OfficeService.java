/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.sys.dao.OfficeDao;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2016-4-23
 */
@Service
@Transactional(readOnly=true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	@Autowired
	private DataScopeService dataScopeService;
	
	/**
	 * 获取单条数据
	 */
	@Override
	public Office get(Office office) {
		return super.get(office);
	}
	
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(Office office, String ctrlPermi) {
		office.getSqlMap().getDataScope().addFilter("dsf", "Office", "a.office_code", ctrlPermi);
	}

	/**
	 * 查询组织机构列表
	 */
	@Override
	public List<Office> findList(Office office) {
		return super.findList(office);
	}

	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Office office) {
		if (office.getIsNewRecord()){
			genIdAndValid(office, office.getViewCode());
			// 当前新数据授权，如果用户有上级数据权限，则当前数据也有相应的数据权限
			dataScopeService.insertIfParentExists(office, "Office");
		}
		super.save(office);
		// 清理部门相关缓存
		clearOfficeCache();
	}

	/**
	 * 更新部门状态
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Office office) {
		super.updateStatus(office);
		// 清理部门相关缓存
		clearOfficeCache();
	}

	/**
	 * 删除数据
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Office office) {
		super.delete(office);
		// 清理部门相关缓存
		clearOfficeCache();
	}
	
	/**
	 * 清理部门相关缓存
	 */
	private void clearOfficeCache(){
//		EmpUtils.removeCache(EmpUtils.CACHE_OFFICE_LIST);
		EmpUtils.removeCache(EmpUtils.CACHE_OFFICE_ALL_LIST);
	}

}

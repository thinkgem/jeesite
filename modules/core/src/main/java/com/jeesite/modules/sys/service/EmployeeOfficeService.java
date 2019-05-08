/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.dao.EmployeeOfficeDao;
import com.jeesite.modules.sys.entity.EmployeeOffice;

/**
 * 附属机构Service
 * @author ThinkGem
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly=true)
public class EmployeeOfficeService extends CrudService<EmployeeOfficeDao, EmployeeOffice> {
	
	/**
	 * 获取单条数据
	 * @param employeeOffice
	 * @return
	 */
	@Override
	public EmployeeOffice get(EmployeeOffice employeeOffice) {
		return super.get(employeeOffice);
	}
	
	/**
	 * 查询分页数据
	 * @param employeeOffice 查询条件
	 * @param employeeOffice.page 分页对象
	 * @return
	 */
	@Override
	public Page<EmployeeOffice> findPage(EmployeeOffice employeeOffice) {
		return super.findPage(employeeOffice);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param employeeOffice
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmployeeOffice employeeOffice) {
		super.save(employeeOffice);
	}
	
	/**
	 * 更新状态
	 * @param employeeOffice
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmployeeOffice employeeOffice) {
		super.updateStatus(employeeOffice);
	}
	
	/**
	 * 删除数据
	 * @param employeeOffice
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmployeeOffice employeeOffice) {
		super.delete(employeeOffice);
	}
	
}
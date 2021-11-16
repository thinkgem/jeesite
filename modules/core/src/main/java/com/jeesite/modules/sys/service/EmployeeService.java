/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.EmployeeOffice;
import com.jeesite.modules.sys.entity.EmployeePost;

/**
 * 员工管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
public interface EmployeeService extends CrudServiceApi<Employee> {
	
	/**
	 * 获取单条数据
	 */
	@Override
	public Employee get(Employee employee);
	
	/**
	 * 根据工号获取数据
	 */
	public Employee getByEmpNo(Employee employee);
	
	/**
	 * 查询分页数据
	 */
	@Override
	public Page<Employee> findPage(Employee employee);
	
	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	public void save(Employee employee);
	
	/**
	 * 删除数据
	 */
	@Override
	public void delete(Employee employee);
	
	/**
	 * 查询当前员工关联的岗位信息
	 */
	public List<EmployeePost> findEmployeePostList(Employee employee);
	
	/**
	 * 查询当前员工关联的附属机构信息
	 */
	public List<EmployeeOffice> findEmployeeOfficeList(Employee employee);

}
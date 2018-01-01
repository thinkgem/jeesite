/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.dao.EmployeeDao;
import com.jeesite.modules.sys.dao.EmployeePostDao;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.EmployeePost;

/**
 * 员工管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
@Service
@Transactional(readOnly=true)
public class EmployeeService extends CrudService<EmployeeDao, Employee> {
	
	@Autowired
	private EmployeePostDao employeePostDao;
	
	/**
	 * 获取单条数据
	 */
	@Override
	public Employee get(Employee employee) {
		return super.get(employee);
	}
	
	/**
	 * 查询分页数据
	 */
	@Override
	public Page<Employee> findPage(Page<Employee> page, Employee employee) {
		return super.findPage(page, employee);
	}
	
	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Employee employee) {
		if (employee.getIsNewRecord()){
			if (dao.get(employee) != null){
				throw newValidationException("员工编码已存在");
			}
		}
		super.save(employee);
		// 保存员工岗位
		EmployeePost where = new EmployeePost();
		where.setEmpCode(employee.getEmpCode());
		employeePostDao.deleteByEntity(where);
		if (ListUtils.isNotEmpty(employee.getEmployeePostList())){
			for (EmployeePost e : employee.getEmployeePostList()){
				e.setEmpCode(employee.getEmpCode());
			}
			employeePostDao.insertBatch(employee.getEmployeePostList());
		}
	}
	
	/**
	 * 删除数据
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Employee employee) {
		super.delete(employee);
	}
	
	/**
	 * 查询当前员工关联的岗位信息
	 */
	public List<EmployeePost> findEmployeePostList(Employee employee){
		EmployeePost employeePost = new EmployeePost();
		employeePost.setEmpCode(employee.getEmpCode());
		return employeePostDao.findList(employeePost);
	}
}
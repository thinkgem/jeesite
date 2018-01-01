/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.dao.EmpUserDao;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 员工管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
@Service
@Transactional(readOnly=true)
public class EmpUserService extends CrudService<EmpUserDao, EmpUser> {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 获取单条数据
	 */
	@Override
	public EmpUser get(EmpUser empUser) {
		return super.get(empUser);
	}
	
	/**
	 * 添加数据权限过滤条件
	 * @param entity 控制对象
	 * @param ctrlPermi 控制权限类型（拥有的数据权限：DataScope.CTRL_PERMI_HAVE、可管理的数据权限：DataScope.CTRL_PERMI_HAVE）
	 */
	@Override
	public void addDataScopeFilter(EmpUser empUser, String ctrlPermi) {
		empUser.getSqlMap().getDataScope().addFilter("dsfOffice",
				"Office", "e.office_code", "a.create_by", ctrlPermi);
		if (StringUtils.isNotBlank(EmpUtils.getCompany().getCompanyCode())){
			empUser.getSqlMap().getDataScope().addFilter("dsfCompany",
					"Company", "e.company_code", "a.create_by", ctrlPermi);
		}
	}

	/**
	 * 分页查询数据
	 */
	@Override
	public Page<EmpUser> findPage(Page<EmpUser> page, EmpUser empUser) {
		return super.findPage(page, empUser);
	}

	/**
	 * 保存用户员工
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmpUser user) {
		// 1、初始化用户信息
		if (user.getIsNewRecord()){
			userService.genId(user, user.getLoginCode());
			user.setUserCode(user.getUserCode()+"_"+IdGen.randomBase62(4).toLowerCase());
			user.setUserType(EmpUser.USER_TYPE_EMPLOYEE);
			user.setMgrType(EmpUser.MGR_TYPE_NOT_ADMIN);
		}
		Employee employee = user.getEmployee();
		// 如果员工编码为空，则使用用户编码
		if (StringUtils.isBlank(employee.getEmpCode())){
			employee.setEmpCode(user.getUserCode());
		}
		// 如果员工姓名为空，则使用昵称名
		if (StringUtils.isBlank(employee.getEmpName())){
			employee.setEmpName(user.getUserName());
		}
		// 2、保存用户
		user.setRefCode(employee.getEmpCode());
		user.setRefName(employee.getEmpName());
		userService.save(user);
		// 3、保存员工
		employee.setIsNewRecord(user.getIsNewRecord());
		employeeService.save(employee);
	}

	/**
	 * 更新状态
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmpUser empUser) {
		userService.delete(empUser);
		employeeService.delete(empUser.getEmployee());
	}
	
	/**
	 * 删除用户
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmpUser empUser) {
		userService.delete(empUser);
		employeeService.delete(empUser.getEmployee());
	}
	
}
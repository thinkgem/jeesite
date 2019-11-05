/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.dao;

import java.util.List;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.EmpUser;

/**
 * 员工管理DAO接口
 * @author ThinkGem
 * @version 2017-03-25
 */
@MyBatisDao
public interface EmpUserDao extends CrudDao<EmpUser> {

	/**
	 * 查询全部用户，仅返回基本信息
	 */
	public List<EmpUser> findUserList(EmpUser empUser);
	
	/**
	 * 根据部门编码查询用户，仅返回基本信息
	 */
	public List<EmpUser> findUserListByOfficeCodes(EmpUser empUser);
	
	/**
	 * 根据角色编码查询用户，仅返回基本信息
	 */
	public List<EmpUser> findUserListByRoleCodes(EmpUser empUser);
	
	/**
	 * 根据岗位编码查询用户，仅返回基本信息
	 */
	public List<EmpUser> findUserListByPostCodes(EmpUser empUser);
	
}
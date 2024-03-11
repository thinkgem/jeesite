/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.EmpUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;

/**
 * 员工管理DAO接口
 * @author ThinkGem
 * @version 2017-03-25
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface EmpUserDao extends CrudDao<EmpUser> {

//	@Override
//	@Results({
//		@Result(column = "mobile", property = "mobile",
//				javaType = String.class, typeHandler = AesTypeHandler.class)
//	})
//	@SelectProvider(type = SelectSqlProvider.class, method = "get")
//	EmpUser get(EmpUser entity);

	/**
	 * 查询全部用户，仅返回基本信息
	 */
	List<EmpUser> findUserList(EmpUser empUser);
	
	/**
	 * 根据部门编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByOfficeCodes(EmpUser empUser);
	
	/**
	 * 根据角色编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByRoleCodes(EmpUser empUser);
	
	/**
	 * 根据岗位编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByPostCodes(EmpUser empUser);
	
}
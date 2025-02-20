/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.sys.entity.EmpUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 员工管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
public interface EmpUserService extends CrudServiceApi<EmpUser> {
	
	/**
	 * 获取单条数据
	 */
	@Override
	EmpUser get(EmpUser empUser);
	
	/**
	 * 添加数据权限过滤条件
	 * @param empUser 控制对象
	 * @param ctrlPermi 控制权限类型（拥有的数据权限：DataScope.CTRL_PERMI_HAVE、可管理的数据权限：DataScope.CTRL_PERMI_HAVE）
	 */
	@Override
	void addDataScopeFilter(EmpUser empUser, String ctrlPermi);

	/**
	 * 分页查询数据
	 */
	@Override
	Page<EmpUser> findPage(EmpUser empUser);
	
	/**
	 * 查询全部用户，仅返回基本信息
	 */
	List<EmpUser> findUserList(EmpUser empUser);
	
	/**
	 * 根据部门编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByOfficeCodes(EmpUser empUser);

	/**
	 * 根据公司编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByCompanyCodes(EmpUser empUser);
	
	/**
	 * 根据角色编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByRoleCodes(EmpUser empUser);
	
	/**
	 * 根据岗位编码查询用户，仅返回基本信息
	 */
	List<EmpUser> findUserListByPostCodes(EmpUser empUser);

	/**
	 * 保存用户员工
	 */
	@Override
	void save(EmpUser user);

	/**
	 * 导入用户数据
	 * @param file 导入的用户数据文件
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 */
	String importData(MultipartFile file, Boolean isUpdateSupport);

	/**
	 * 更新状态
	 */
	@Override
	void updateStatus(EmpUser empUser);
	
	/**
	 * 删除用户
	 */
	@Override
	void delete(EmpUser empUser);
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.EmployeePost;

/**
 * 员工岗位DAO接口
 * @author ThinkGem
 * @version 2017-03-25
 */
@MyBatisDao
public interface EmployeePostDao extends CrudDao<EmployeePost> {
	
}
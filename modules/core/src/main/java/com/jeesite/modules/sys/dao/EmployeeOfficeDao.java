/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.EmployeeOffice;

/**
 * 员工附属机构关系表DAO接口
 * @author ThinkGem
 * @version 2019-04-29
 */
@MyBatisDao
public interface EmployeeOfficeDao extends CrudDao<EmployeeOffice> {
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.Company;

/**
 * 公司管理DAO接口
 * @author ThinkGem
 * @version 2017-03-12
 */
@MyBatisDao(dataSourceName=DataSourceHolder.DEFAULT)
public interface CompanyDao extends TreeDao<Company> {
	
}
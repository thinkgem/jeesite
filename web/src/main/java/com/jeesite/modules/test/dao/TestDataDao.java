/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test.entity.TestData;

/**
 * 测试数据DAO接口
 * @author ThinkGem
 * @version 2018-02-07
 */
@MyBatisDao
public interface TestDataDao extends CrudDao<TestData> {
	
}
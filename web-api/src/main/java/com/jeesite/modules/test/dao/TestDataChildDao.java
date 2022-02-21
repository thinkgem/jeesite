/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test.entity.TestDataChild;

/**
 * 测试数据DAO接口
 * @author ThinkGem
 * @version 2018-04-22
 */
@MyBatisDao//(dataSourceName="ds2")
public interface TestDataChildDao extends CrudDao<TestDataChild> {
	
}
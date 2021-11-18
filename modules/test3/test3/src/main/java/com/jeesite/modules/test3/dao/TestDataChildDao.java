/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test3.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test3.entity.TestDataChild;

/**
 * 测试数据DAO接口
 * @author ThinkGem
 * @version 2021-06-22
 */
@MyBatisDao
public interface TestDataChildDao extends CrudDao<TestDataChild> {
	
}
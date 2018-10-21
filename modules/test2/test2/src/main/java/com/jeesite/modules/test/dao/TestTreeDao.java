/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test.entity.TestTree;

/**
 * 测试树表DAO接口
 * @author ThinkGem
 * @version 2018-04-22
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}
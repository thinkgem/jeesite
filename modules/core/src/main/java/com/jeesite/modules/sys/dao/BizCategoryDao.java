/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.BizCategory;

/**
 * 业务分类DAO接口
 * @author ThinkGem
 * @version 2019-08-12
 */
@MyBatisDao
public interface BizCategoryDao extends TreeDao<BizCategory> {
	
}
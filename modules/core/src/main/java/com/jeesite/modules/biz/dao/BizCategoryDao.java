/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.biz.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.biz.entity.BizCategory;

/**
 * 业务分类 DAO 接口
 * @author ThinkGem
 * @version 2019-08-12
 */
@MyBatisDao
public interface BizCategoryDao extends TreeDao<BizCategory> {
	
}
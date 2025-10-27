/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.cms.entity.ArticleData;

/**
 * 文章详情表DAO接口
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@MyBatisDao
public interface ArticleDataDao extends CrudDao<ArticleData> {
	
}
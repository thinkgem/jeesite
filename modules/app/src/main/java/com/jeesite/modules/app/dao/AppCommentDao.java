/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppComment;

/**
 * APP意见反馈DAO接口
 * @author ThinkGem
 * @version 2021-04-09
 */
@MyBatisDao
public interface AppCommentDao extends CrudDao<AppComment> {
	
}
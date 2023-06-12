/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.PostRole;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 岗位角色DAO接口
 * @author ThinkGem
 * @version 2023-6-8
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface PostRoleDao extends CrudDao<PostRole> {
	
}
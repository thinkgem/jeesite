/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.OfficeRole;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 部门角色 DAO 接口
 * @author ThinkGem
 * @version 2026-7-10
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface OfficeRoleDao extends CrudDao<OfficeRole> {
	
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.EmployeePost;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 员工岗位DAO接口
 * @author ThinkGem
 * @version 2017-03-25
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface EmployeePostDao extends CrudDao<EmployeePost> {
	
}
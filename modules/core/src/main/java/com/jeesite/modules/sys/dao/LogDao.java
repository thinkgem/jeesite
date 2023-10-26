/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 日志DAO接口
 * @author ThinkGem
 * @version 2017-03-19
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface LogDao extends CrudDao<Log> {
	
	/**
	 * 删除某个日期之前创建的日志
	 * @param log .createDate
	 */
	int deleteLogBefore(Log log);
	
}

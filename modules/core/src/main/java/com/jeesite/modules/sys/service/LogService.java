/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.sys.entity.Log;

/**
 * 日志Service
 * @author ThinkGem
 * @version 2014-05-16
 */
public interface LogService extends CrudServiceApi<Log> {
	
	/**
	 * 查询日志记录
	 */
	@Override
	public Page<Log> findPage(Log log);
	
	/**
	 * 不使用数据库事务，执行插入日志
	 */
	public void insertLog(Log entity);
	
}

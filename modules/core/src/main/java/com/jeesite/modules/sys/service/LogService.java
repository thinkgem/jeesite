/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
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
	Page<Log> findPage(Log log);
	
	/**
	 * 不使用数据库事务，执行插入日志
	 */
	void insertLog(Log entity);

	/**
	 * 清理指定日期之前的日志（可新建job定时调用）
	 * 1、清理1年前的所有日志：logService.deleteLogBefore(1, 0, 0);
	 * 2、清理6个月前的所有日志：logService.deleteLogBefore(0, 6, 0);
	 * 3、清理7天前的所有日志：logService.deleteLogBefore(0, 0, 7);
	 * 4、清理1年6个月前的所有日志：logService.deleteLogBefore(1, 6, 0);
	 */
	void deleteLogBefore(Integer year, Integer months, Integer days);
}

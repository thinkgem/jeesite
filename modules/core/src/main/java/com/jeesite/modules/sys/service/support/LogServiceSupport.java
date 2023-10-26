/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service.support;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.dao.LogDao;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.service.LogService;

/**
 * 日志Service
 * @author ThinkGem
 * @version 2014-05-16
 */
public class LogServiceSupport extends CrudService<LogDao, Log>
		implements LogService{
	
	/**
	 * 查询日志记录
	 */
	@Override
	public Page<Log> findPage(Log log) {
//		// 设置默认时间范围，默认当前月
//		if (log.getCreateDate_gte() == null){
//			log.setCreateDate_gte(DateUtils.setDays(new Date(), 1));
//		}
//		if (log.getCreateDate_lte() == null){
//			log.setCreateDate_lte(DateUtils.addDays(DateUtils.addMonths(log.getCreateDate_gte(), 1), -1));
//		}
		// 普通用户看自己的，管理员看全部的。
		if (!log.currentUser().isAdmin()){
			log.setCreateBy(log.currentUser().getUserCode());
		}
		return super.findPage(log);
	}
	
	/**
	 * 不使用数据库事务，执行插入日志
	 */
	@Override
	@Transactional//(propagation = Propagation.NOT_SUPPORTED)
	public void insertLog(Log entity) {
		DataSourceHolder.setJdbcTransaction(false);
		dao.insert(entity);
	}
	
	/**
	 * 清理指定日期之前的日志（可新建job定时调用）
	 * 1、清理1年前的所有日志：logService.deleteLogBefore(1, 0, 0);
	 * 2、清理6个月前的所有日志：logService.deleteLogBefore(0, 6, 0);
	 * 3、清理7天前的所有日志：logService.deleteLogBefore(0, 0, 7);
	 * 4、清理1年6个月前的所有日志：logService.deleteLogBefore(1, 6, 0);
	 */
	@Override
	@Transactional
	public void deleteLogBefore(Integer year, Integer months, Integer days) {
		Date date = DateUtils.getOfDayLast(new Date());
		if (year != null && year != 0) {
			date = DateUtils.addYears(date, -year);
		}
		if (months != null && months != 0) {
			date = DateUtils.addMonths(date, -months);
		}
		if (days != null && days != 0) {
			date = DateUtils.addDays(date, -days);
		}
		Log log = new Log();
		log.setCreateDate(date);
		dao.deleteLogBefore(log);
	}
	
	
}

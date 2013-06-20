/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.BaseDaoImpl;
import com.thinkgem.jeesite.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * @author ThinkGem
 * @version 2013-06-02
 */
public interface LogDao extends LogDaoCustom, CrudRepository<Log, Long> {
	
}

/**
 * DAO自定义接口
 * @author ThinkGem
 */
interface LogDaoCustom extends BaseDao<Log> {

}

/**
 * DAO自定义接口实现
 * @author ThinkGem
 */
@Repository
class LogDaoImpl extends BaseDaoImpl<Log> implements LogDaoCustom {

}

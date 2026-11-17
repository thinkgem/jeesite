/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.common.mybatis.mapper.provider.SelectSqlProvider;
import com.jeesite.common.mybatis.type.JsonTypeHandler;
import com.jeesite.modules.ai.entity.AiAgent;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * AI智能体 DAO 接口
 * @author ThinkGem
 * @version 2025-07-04
 */
@MyBatisDao
public interface AiAgentDao extends CrudDao<AiAgent> {

	@Override
	@Results({
		@Result(column = "optionMap", property = "optionMap",
				javaType = Map.class, typeHandler = JsonTypeHandler.class)
	})
	@SelectProvider(type = SelectSqlProvider.class, method = "get")
	AiAgent get(AiAgent entity);

	@Override
	@Results({
		@Result(column = "optionMap", property = "optionMap",
				javaType = Map.class, typeHandler = JsonTypeHandler.class)
	})
	@SelectProvider(type = SelectSqlProvider.class, method = "findList")
	List<AiAgent> findList(AiAgent entity);

}

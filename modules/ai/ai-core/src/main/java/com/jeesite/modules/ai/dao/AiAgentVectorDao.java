/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.ai.entity.AiAgentVector;

/**
 * 智能体与向量库绑定关联表 DAO 接口
 * @author ThinkGem
 * @version 2026-11-17
 */
@MyBatisDao
public interface AiAgentVectorDao extends CrudDao<AiAgentVector> {

}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.msg.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.msg.entity.MsgInner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 内部消息DAO接口
 * @author ThinkGem
 * @version 2019-03-12
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface MsgInnerDao extends CrudDao<MsgInner> {
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.msg.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.msg.entity.MsgInner;
import com.jeesite.modules.msg.entity.MsgInnerRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 内部消息Service
 * @author ThinkGem
 * @version 2019-03-12
 */
public interface MsgInnerService extends CrudServiceApi<MsgInner> {
	
	/**
	 * 获取单条数据
	 * @param msgInner
	 * @return
	 */
	@Override
	MsgInner get(MsgInner msgInner);
	
	/**
	 * 查询分页数据
	 * @param msgInner 查询条件； page 分页对象
	 * @return
	 */
	@Override
	Page<MsgInner> findPage(MsgInner msgInner);

	/**
	 * 查询消息记录数据
	 */
	List<MsgInnerRecord> findRecordList(MsgInnerRecord msgInnerRecord);
	
	/**
	 * 保存数据（插入或更新）
	 * @param msgInner
	 */
	@Override
	@Transactional
	void save(MsgInner msgInner);
	
	/**
	 * 根据消息编号和接受者用户名读取内部消息
	 */
	@Transactional
	void readMsgInnerRecord(MsgInner msgInner);
	
	/**
	 * 更新状态
	 * @param msgInner
	 */
	@Override
	@Transactional
	void updateStatus(MsgInner msgInner);
	
	/**
	 * 删除数据
	 * @param msgInner
	 */
	@Override
	@Transactional
	void delete(MsgInner msgInner);
	
}
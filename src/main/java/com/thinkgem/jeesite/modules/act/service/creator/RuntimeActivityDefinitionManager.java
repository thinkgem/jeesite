package com.thinkgem.jeesite.modules.act.service.creator;

import java.util.List;

public interface RuntimeActivityDefinitionManager
{
	/**
	 * 获取所有的活动定义信息，引擎会在启动的时候加载这些活动定义并进行注册
	 */
	List<RuntimeActivityDefinitionEntity> list() throws Exception;

	/**
	 * 删除所有活动定义
	 */
	void removeAll() throws Exception;

	/**
	 * 新增一条活动定义的信息
	 */
	void save(RuntimeActivityDefinitionEntity entity) throws Exception;

}
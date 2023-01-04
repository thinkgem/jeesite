/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service;

import com.jeesite.common.service.api.TreeServiceApi;
import com.jeesite.modules.sys.entity.Area;

import java.util.List;

/**
 * 行政区划Service
 * @author ThinkGem
 * @version 2014-8-19
 */
public interface AreaService extends TreeServiceApi<Area> {

	/**
	 * 获取区划
	 */
	@Override
	Area get(Area area);

	/**
	 * 查询区划
	 */
	@Override
	List<Area> findList(Area area);

	/**
	 * 保存区划
	 */
	@Override
	void save(Area area);

	/**
	 * 删除区划
	 */
	@Override
	void delete(Area area);

}

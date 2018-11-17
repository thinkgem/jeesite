/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import com.jeesite.common.service.api.TreeServiceApi;
import com.jeesite.modules.sys.entity.Area;

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
	public Area get(Area area);

	/**
	 * 查询区划
	 */
	@Override
	public List<Area> findList(Area area);

	/**
	 * 保存区划
	 */
	@Override
	public void save(Area area);

	/**
	 * 删除区划
	 */
	@Override
	public void delete(Area area);

}

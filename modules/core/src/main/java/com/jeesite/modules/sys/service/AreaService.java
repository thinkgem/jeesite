/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.sys.dao.AreaDao;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.utils.AreaUtils;

/**
 * 行政区划Service
 * @author ThinkGem
 * @version 2014-8-19
 */
@Service
@Transactional(readOnly=true)
public class AreaService extends TreeService<AreaDao, Area> {

	/**
	 * 获取区划
	 */
	@Override
	public Area get(Area area) {
		return super.get(area);
	}

	/**
	 * 查询区划
	 */
	@Override
	public List<Area> findList(Area area) {
		return super.findList(area);
	}

	/**
	 * 保存区划
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Area area) {
		super.save(area);
		AreaUtils.clearCache();
	}

	/**
	 * 删除区划
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Area area) {
		super.delete(area);
		AreaUtils.clearCache();
	}

}

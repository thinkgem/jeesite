/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.utils;

import java.util.List;

import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.service.AreaService;

/**
 * 
 * @author ThinkGem
 * @version 2017年3月1日
 */
public class AreaUtils {

	// 系统缓存常量
	public static final String CACHE_AREA_ALL_LIST = "areaAllList";
	
	/**
	 * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
	 */
	private static final class Static {
		private static AreaService areaService = SpringUtils.getBean(AreaService.class);
	}
	
	/**
	 * 获取所有区域列表（系统级别缓存）
	 * @return
	 */
	public static List<Area> getAreaAllList(){
		List<Area> areaList = SysCacheUtils.get(CACHE_AREA_ALL_LIST);
		if (areaList == null){
			areaList = Static.areaService.findList(new Area());
			SysCacheUtils.put(CACHE_AREA_ALL_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 清理区域缓存
	 */
	public static void clearCache(){
		SysCacheUtils.remove(CACHE_AREA_ALL_LIST);
	}
	
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.utils.AreaUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2018-08-11
 * @example fieldType = AreaType.class
 */
public class AreaType {

	private static ThreadLocal<List<Area>> cache = new NamedThreadLocal<>("AreaType");
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Area> cacheList = cache.get();
		if (cacheList == null){
			cacheList = AreaUtils.getAreaAllList();
			cache.set(cacheList);
		}
		for (Area e : cacheList){
			if (StringUtils.trimToEmpty(val).equals(e.getAreaName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 获取对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Area)val).getAreaName() != null){
			return ((Area)val).getAreaName();
		}
		return "";
	}
	
	/**
	 * 清理缓存
	 */
	public static void clearCache(){
		cache.remove();
	}
	
}

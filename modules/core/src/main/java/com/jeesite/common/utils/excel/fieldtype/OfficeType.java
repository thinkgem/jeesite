/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2013-03-10
 * @example fieldType = OfficeType.class
 */
public class OfficeType {

	private static ThreadLocal<List<Office>> cache = new ThreadLocal<>();
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Office> cacheList = cache.get();
		if (cacheList == null){
			cacheList = EmpUtils.getOfficeAllList();
			cache.set(cacheList);
		}
		for (Office e : cacheList){
			if (StringUtils.trimToEmpty(val).equals(e.getOfficeName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Office)val).getOfficeName() != null){
			return ((Office)val).getOfficeName();
		}
		return "";
	}
}

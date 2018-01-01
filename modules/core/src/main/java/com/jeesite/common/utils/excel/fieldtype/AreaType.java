/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.utils.AreaUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2013-03-10
 * @example fieldType = AreaType.class
 */
public class AreaType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (Area e : AreaUtils.getAreaAllList()){
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
}

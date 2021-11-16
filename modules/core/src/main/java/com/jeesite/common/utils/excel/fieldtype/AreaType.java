/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.utils.AreaUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2020-3-5
 * @example fieldType = AreaType.class
 */
public class AreaType implements FieldType {

	private List<Area> list;
	
	public AreaType() {
		list = AreaUtils.getAreaAllList();
	}
	
	/**
	 * 获取对象值（导入）
	 */
	public Object getValue(String val) {
		for (Area e : list){
			if (StringUtils.trimToEmpty(val).equals(e.getAreaName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 获取对象值（导出）
	 */
	public String setValue(Object val) {
		if (val != null && ((Area)val).getAreaName() != null){
			return ((Area)val).getAreaName();
		}
		return StringUtils.EMPTY;
	}
	
}

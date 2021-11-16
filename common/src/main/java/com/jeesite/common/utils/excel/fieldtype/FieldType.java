/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.fieldtype;

/**
 * Excel字段类型转换
 * @author ThinkGem
 * @version 2020-3-5
 */
public interface FieldType {

	/**
	 * 获取对象值（导入）
	 */
	default public Object getValue(String val) {
		return null;
	}

	/**
	 * 获取对象值（导出）
	 */
	default public String setValue(Object val) {
		return null;
	}
	
	/**
	 * 获取对象值格式（导出）
	 */
	default public String getDataFormat() {
		return null;
	}
	
}

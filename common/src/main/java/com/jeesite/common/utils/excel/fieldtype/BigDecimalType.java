/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.fieldtype;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;


/**
 * BigDecimal类型转换
 * @author ThinkGem
 * @version 2020-3-5
 * @example fieldType = BigDecimalType.class
 */
public class BigDecimalType implements FieldType {
	
	/**
	 * 获取对象值（导入）
	 */
	@Override
	public Object getValue(String val) {
		return new BigDecimal(val);
	}

	/**
	 * 获取对象值（导出）
	 */
	@Override
	public String setValue(Object val) {
		return val == null ? StringUtils.EMPTY : val.toString();
	}
	
	/**
	 * 获取对象值格式（导出）
	 */
	@Override
	public String getDataFormat() {
		return "0.00";
	}
	
}

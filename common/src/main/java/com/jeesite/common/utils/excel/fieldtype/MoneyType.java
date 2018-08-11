/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * 金额类型转换（保留两位）
 * @author ThinkGem
 * @version 2015-9-29
 * @example fieldType = MoneyType.class
 */
public class MoneyType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		return val == null ? "" : val.replaceAll(",", "");
	}

	/**
	 * 获取对象值（导出）
	 */
	public static String setValue(Object val) {
		NumberFormat nf = new DecimalFormat(",##0.00"); 
		return val == null ? "" : nf.format(val);
	}
	
	/**
	 * 获取对象值格式（导出）
	 */
	public static String getDataFormat() {
		return "0.00";
	}
	
	/**
	 * 清理缓存
	 */
	public static void clearCache(){
		
	}
	
}

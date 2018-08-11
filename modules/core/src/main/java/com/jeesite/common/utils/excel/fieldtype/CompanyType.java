/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import org.springframework.core.NamedThreadLocal;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2018-08-11
 * @example fieldType = CompanyType.class
 */
public class CompanyType {

	private static ThreadLocal<List<Company>> cache = new NamedThreadLocal<>("CompanyType");

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Company> cacheList = cache.get();
		if (cacheList == null){
			cacheList = EmpUtils.getCompanyAllList();
			cache.set(cacheList);
		}
		for (Company e : cacheList){
			if (StringUtils.trimToEmpty(val).equals(e.getCompanyName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Company)val).getCompanyName() != null){
			return ((Company)val).getCompanyName();
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

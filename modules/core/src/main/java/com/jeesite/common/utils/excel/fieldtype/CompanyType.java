/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2020-3-5
 * @example fieldType = CompanyType.class
 */
public class CompanyType implements FieldType {

	private List<Company> list;
	
	public CompanyType() {
		list = EmpUtils.getCompanyAllList();
	}

	/**
	 * 获取对象值（导入）
	 */
	public Object getValue(String val) {
		for (Company e : list){
			if (StringUtils.trimToEmpty(val).equals(e.getCompanyName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public String setValue(Object val) {
		if (val != null && ((Company)val).getCompanyName() != null){
			return ((Company)val).getCompanyName();
		}
		return StringUtils.EMPTY;
	}
	
}

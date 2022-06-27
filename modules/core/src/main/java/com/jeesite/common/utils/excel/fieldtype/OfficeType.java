/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2020-3-5
 * @example fieldType = OfficeType.class
 */
public class OfficeType implements FieldType {

	private List<Office> list;
	
	public OfficeType() {
		list = EmpUtils.getOfficeAllList();
	}
	
	/**
	 * 获取对象值（导入）
	 */
	@Override
	public Object getValue(String val) {
		for (Office e : list){
			if (StringUtils.trimToEmpty(val).equals(e.getOfficeName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	@Override
	public String setValue(Object val) {
		if (val != null && ((Office)val).getOfficeName() != null){
			return ((Office)val).getOfficeName();
		}
		return StringUtils.EMPTY;
	}
	
}

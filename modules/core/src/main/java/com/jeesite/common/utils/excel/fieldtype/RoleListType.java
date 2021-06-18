/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.service.RoleService;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2018-08-11
 * @example fieldType = RoleListType.class
 */
public class RoleListType implements FieldType {

	private List<Role> roleList;
	
	public RoleListType() {
		RoleService roleService = SpringUtils.getBean(RoleService.class);
		roleList = roleService.findList(new Role());
	}
	
	/**
	 * 获取对象值（导入）
	 */
	public Object getValue(String val) {
		for (String s : StringUtils.split(val, ",")) {
			for (Role e : roleList) {
				if (StringUtils.trimToEmpty(s).equals(e.getRoleName())) {
					roleList.add(e);
				}
			}
		}
		return roleList.size() > 0 ? roleList : null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public String setValue(Object val) {
		if (val != null) {
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>) val;
			return ListUtils.extractToString(roleList, "roleName", ", ");
		}
		return "";
	}
	
}

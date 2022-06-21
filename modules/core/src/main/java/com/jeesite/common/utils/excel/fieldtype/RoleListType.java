/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.ArrayList;
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
	@Override
	public Object getValue(String val) {
		List<String> list = new ArrayList<String>();
		for (String s : StringUtils.split(val, ",")) {
			for (Role e : roleList) {
				if (StringUtils.trimToEmpty(s).equals(e.getRoleName())) {
					list.add(e.getRoleCode());
				}
			}
		}
		return list.size() > 0 ? list.toArray(new String[list.size()]) : null;
	}

	/**
	 * 设置对象值（导出）
	 */
	@Override
	public String setValue(Object val) {
		if (val != null) {
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>) val;
			return ListUtils.extractToString(roleList, "roleName", ", ");
		}
		return StringUtils.EMPTY;
	}
	
}

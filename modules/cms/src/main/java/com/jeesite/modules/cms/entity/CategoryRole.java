/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 栏目与角色关联表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_category_role", alias = "a", columns = {
		@Column(name = "category_code", attrName = "categoryCode", label = "栏目编码", isPK = true),
		@Column(name = "role_code", attrName = "roleCode", label = "角色编码", isPK = true),
		@Column(name = "ctrl_type", attrName = "ctrlType", label = "控制类型", comment = "控制类型（view查看、edit编辑）"),
	}, orderBy = "a.category_code DESC, a.role_code DESC"
)
public class CategoryRole extends DataEntity<CategoryRole> {

	private static final long serialVersionUID = 1L;
	private String categoryCode; 	// 栏目编码
	private String roleCode; 		// 角色编码
	private String ctrlType; 		// 控制类型（view查看、edit编辑）

	public CategoryRole() {

	}

	public CategoryRole(String categoryCode, String roleCode) {
		this.categoryCode = categoryCode;
		this.roleCode = roleCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	@Size(min = 0, max = 32, message = "控制类型长度不能超过 32 个字符")
	public String getCtrlType() {
		return ctrlType;
	}

	public void setCtrlType(String ctrlType) {
		this.ctrlType = ctrlType;
	}

}
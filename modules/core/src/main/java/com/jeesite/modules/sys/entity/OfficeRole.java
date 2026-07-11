/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.io.Serial;

/**
 * 部门角色 Entity
 * @author ThinkGem
 * @version 2026-7-10
 */
@Table(name = "${_prefix}sys_office_role", alias = "a", label = "部门角色关系", columns = {
		@Column(name = "office_code", attrName = "officeCode", label = "机构编码", isPK = true),
		@Column(name = "role_code", attrName = "roleCode", label = "角色编码", isPK = true),
	},
	joinTable = {
		@JoinTable(type=JoinTable.Type.LEFT_JOIN, entity=Role.class, alias = "r", lazy = true,
			on = "a.role_code = r.role_code", attrName = "role",
			columns = {
				@Column(name = "role_code", attrName = "roleCode", label = "角色编码", isPK = true),
				@Column(name = "role_name", attrName = "roleName", label = "角色名称"),
				@Column(name = "status", attrName = "status", label = "角色状态"),
			})
	}, orderBy = ""
)
public class OfficeRole extends DataEntity<OfficeRole> {

	@Serial
	private static final long serialVersionUID = 1L;
	protected String officeCode;	// 机构编码
	protected String roleCode;	// 角色编码

	protected Role role; // sqlMap().loadJoinTableAlias("r") 的时候返回数据

	public OfficeRole() {
		this(null, null);
	}

	public OfficeRole(String officeCode, String roleCode){
		this.officeCode = officeCode;
		this.roleCode = roleCode;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String[] getOfficeCode_in(){
		return sqlMap.getWhere().getValue("office_code", QueryType.IN);
	}

	public void setOfficeCode_in(String[] officeCodes){
		sqlMap.getWhere().and("office_code", QueryType.IN, officeCodes);
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}

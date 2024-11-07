/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 岗位角色Entity
 * @author ThinkGem
 * @version 2023-6-8
 */
@Table(name="${_prefix}sys_post_role", alias="a", columns={
		@Column(name="role_code", attrName="roleCode", label="角色编码", isPK=true),
		@Column(name="post_code", attrName="postCode", label="岗位编码", isPK=true),
	},
	joinTable={
		@JoinTable(type=JoinTable.Type.LEFT_JOIN, entity=Role.class, alias="r", lazy = true,
			on="a.role_code = r.role_code AND r.status = ${STATUS_NORMAL}", attrName="role",
			columns={
				@Column(name="role_code", attrName="roleCode", label="角色编码", isPK=true),
				@Column(name="role_name", attrName="roleName", label="角色名称"),
			})
	}, orderBy=""
)
public class PostRole extends DataEntity<PostRole> {

	private static final long serialVersionUID = 1L;
	private String postCode;	// 岗位编码
	private String roleCode;	// 角色编码

	private Role role; // sqlMap().loadJoinTableAlias("r") 的时候返回数据

	public PostRole() {
		this(null, null);
	}

	public PostRole(String postCode, String roleCode){
		this.postCode = postCode;
		this.roleCode = roleCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
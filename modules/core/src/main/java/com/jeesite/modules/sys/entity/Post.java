/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Table(name="${_prefix}sys_post", alias="a", label="岗位信息", columns={
		@Column(includeEntity=BaseEntity.class),
		@Column(includeEntity=DataEntity.class),
		@Column(name="post_code", attrName="postCode", label="岗位编码", isPK=true),
		@Column(name="view_code", attrName="viewCode", label="岗位代码"),
		@Column(name="post_name", attrName="postName", label="岗位名称"),
		@Column(name="post_type", attrName="postType", label="岗位分类", comment="岗位分类（高管、中层、基层）"),
		@Column(name="post_sort", attrName="postSort", label="岗位排序", comment="岗位排序（升序）"),
	}, orderBy="a.post_sort ASC"
)
public class Post extends DataEntity<Post> {
	
	private static final long serialVersionUID = 1L;
	private String postCode;		// 岗位编码
	private String viewCode;		// 岗位代码（作为显示用，多租户内唯一）
	private String postName;		// 岗位名称
	private String postType;		// 岗位分类（高管、中层、基层）
	private Integer postSort;		// 岗位排序（升序）
	
	private String empCode; 		// 根据用户查询岗位

	private String roleCodes;		// 关联的角色编号
	private String roleNames;		// 关联的角色名称

	public Post() {
		this(null);
	}

	public Post(String id){
		super(id);
	}
	
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	@NotBlank(message="岗位代码不能为空")
	@Pattern(regexp="[a-zA-Z0-9_]{0,30}", message="代码长度不能大于 30 个字符，并且只能包含字母、数字、下划线")
	public String getViewCode() {
		return viewCode;
	}

	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	
	@NotBlank(message="岗位名称不能为空")
	@Size(min=0, max=100, message="岗位名称长度不能超过 100 个字符")
	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	@Schema(description = "模糊查询岗位名称")
	public String getPostName_like() {
		return sqlMap().getWhere().getValue("post_name", QueryType.LIKE);
	}

	public void setPostName_like(String roleName) {
		sqlMap().getWhere().and("post_name", QueryType.LIKE, roleName);
	}
	
	@Size(min=0, max=100, message="岗位分类长度不能超过 100 个字符")
	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}
	
	public Integer getPostSort() {
		return postSort;
	}

	public void setPostSort(Integer postSort) {
		this.postSort = postSort;
	}

	@Schema(description = "根据员工编码查询")
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
}
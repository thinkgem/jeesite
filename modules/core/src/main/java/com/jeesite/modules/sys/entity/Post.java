/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

@Table(name="${_prefix}sys_post", alias="a", columns={
		@Column(includeEntity=BaseEntity.class),
		@Column(includeEntity=DataEntity.class),
		@Column(name="post_code", attrName="postCode", label="岗位编码", isPK=true),
		@Column(name="post_name", attrName="postName", label="岗位名称", queryType=QueryType.LIKE),
		@Column(name="post_type", attrName="postType", label="岗位分类", comment="岗位分类（高管、中层、基层）"),
		@Column(name="post_sort", attrName="postSort", label="岗位排序", comment="岗位排序（升序）"),
	}, orderBy="a.update_date DESC"
)
public class Post extends DataEntity<Post> {
	
	private static final long serialVersionUID = 1L;
	private String postCode;		// 岗位编码
	private String postName;		// 岗位名称
	private String postType;		// 岗位分类（高管、中层、基层）
	private Integer postSort;		// 岗位排序（升序）
	
	private String empCode; 		// 根据用户查询岗位
	
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
	
	@NotBlank(message="岗位名称不能为空")
	@Length(min=0, max=100, message="岗位名称长度不能超过 100 个字符")
	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}
	
	@Length(min=0, max=100, message="岗位分类长度不能超过 100 个字符")
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

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;

import java.io.Serial;

/**
 * 员工岗位Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}sys_employee_post", alias="a", columns={
		@Column(name="emp_code",  attrName="empCode",  label="员工编码", isPK=true),
		@Column(name="post_code", attrName="postCode", label="岗位编码", isPK=true),
	},
	joinTable={
		@JoinTable(type=JoinTable.Type.LEFT_JOIN, entity=Post.class, alias="p", lazy = true,
			on="a.post_code = p.post_code", attrName="post",
			columns={
				@Column(name="post_code", attrName="postCode", label="岗位编码", isPK=true),
				@Column(name="post_name", attrName="postName", label="岗位名称"),
			}),
		@JoinTable(type=JoinTable.Type.JOIN, entity=User.class, alias="u", lazy = true,
			on="a.emp_code = u.ref_code AND u.user_type='employee'", attrName="this",
			columns={
				@Column(name="user_code", attrName="userCode", label="用户编码", isPK=true),
			})
	}, orderBy=""
)
public class EmployeePost extends DataEntity<EmployeePost> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String empCode;		// 员工编码
	private String postCode;	// 岗位编码

	private Post post; // sqlMap().loadJoinTableAlias("p") 的时候返回数据

	private String userCode; // 根据用户编码查询 sqlMap().loadJoinTableAlias("p,u") 的时候有效

	public EmployeePost() {
		this(null, null);
	}

	public EmployeePost(String empCode, String postCode){
		this.empCode = empCode;
		this.postCode = postCode;
	}
	
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 员工岗位Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}sys_employee_post", alias="a", columns={
		@Column(name="emp_code",  attrName="empCode",  label="员工编码", isPK=true),
		@Column(name="post_code", attrName="postCode", label="岗位编码", isPK=true),
	}, orderBy=""
)
public class EmployeePost extends DataEntity<EmployeePost> {
	
	private static final long serialVersionUID = 1L;
	private String empCode;		// 员工编码
	private String postCode;	// 岗位编码
	
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
	
}
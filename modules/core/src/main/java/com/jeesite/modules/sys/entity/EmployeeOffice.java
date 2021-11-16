/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 附属机构Entity
 * @author ThinkGem
 * @version 2019-04-29
 */
@Table(name="${_prefix}sys_employee_office", alias="a", columns={
		@Column(name="id", attrName="id", label="编码", isPK=true),
		@Column(name="emp_code", attrName="empCode", label="员工编码"),
		@Column(name="office_code", attrName="officeCode", label="机构编码"),
		@Column(name="post_code", attrName="postCode", label="岗位编码"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
			on="o.office_code=a.office_code", attrName="this",
			columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="parent_codes",label="所有父级编码", queryType=QueryType.LIKE),
				@Column(name="office_name", label="机构名称", isQuery=false),
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Post.class, alias="p", 
			on="p.post_code=a.post_code", attrName="this",
			columns={
				@Column(name="post_code", label="岗位编码", isPK=true),
				@Column(name="post_name", label="岗位名称", isQuery=false),
			}),
	}, orderBy="a.id ASC"
)
public class EmployeeOffice extends DataEntity<EmployeeOffice> {
	
	private static final long serialVersionUID = 1L;
	private String empCode;		// 员工编码
	private String officeCode;	// 机构编码
	private String postCode;	// 岗位编码

	private String parentCodes;	// 机构所有上级编码（数据权限用）
	private String officeName;	// 机构名称（联合查询项）
	private String postName;	// 岗位名称（联合查询项）
	
	public EmployeeOffice() {
		this(null, null);
	}
	
	public EmployeeOffice(String empCode, String officeCode){
		super(null);
		this.empCode = empCode;
		this.officeCode = officeCode;
	}
	
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	
	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	@Size(min=0, max=64, message="岗位编码长度不能超过 64 个字符")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getParentCodes() {
		return parentCodes;
	}

	public void setParentCodes(String parentCodes) {
		this.parentCodes = parentCodes;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}
	
}
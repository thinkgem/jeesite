/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Extend;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 组织机构Entity
 * @author ThinkGem
 * @version 2017-03-23
 */
@Table(name="${_prefix}sys_office", alias="a", columns={
		@Column(includeEntity=BaseEntity.class),
		@Column(includeEntity=DataEntity.class),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="office_code", attrName="officeCode", 	label="机构编码", isPK=true),
		@Column(name="view_code", 	attrName="viewCode", 	label="机构代码"),
		@Column(name="office_name", attrName="officeName", 	label="机构名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(name="full_name", 	attrName="fullName", 	label="机构全称", queryType=QueryType.LIKE),
		@Column(name="office_type", attrName="officeType", 	label="机构类型"),
		@Column(name="leader", 		attrName="leader", 		label="负责人", queryType=QueryType.LIKE),
		@Column(name="phone", 		attrName="phone", 		label="电话", queryType=QueryType.LIKE),
		@Column(name="address", 	attrName="address", 	label="联系地址", queryType=QueryType.LIKE),
		@Column(name="zip_code", 	attrName="zipCode", 	label="邮政编码", queryType=QueryType.LIKE),
		@Column(name="email", 		attrName="email", 		label="邮箱", queryType=QueryType.LIKE),
		@Column(includeEntity=Extend.class, attrName="extend"),
	}, extWhereKeys="dsf", orderBy="a.tree_sort, a.office_code"
)
public class Office extends TreeEntity<Office> {
	
	private static final long serialVersionUID = 1L;
	private String officeCode;		// 机构编码
	private String viewCode;		// 机构代码（作为显示用，多租户内唯一）
	private String officeName;		// 机构名称
	private String fullName;		// 机构全称
	private String officeType;		// 机构类型（1：省级公司；2：市级公司；3：部门）
	private String leader;		// 负责人
	private String phone;		// 电话
	private String address;		// 联系地址
	private String zipCode;		// 邮政编码
	private String email;		// 邮箱
	private Extend extend;		// 扩展字段

	private String companyCode; // 根据公司查询机构，组织机构所属公司
	
	public Office() {
		this(null);
	}

	public Office(String id){
		super(id);
	}
	
	@Override
	public Office getParent() {
		return parent;
	}

	@Override
	public void setParent(Office parent) {
		this.parent = parent;
	}
	
	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	@NotBlank(message="机构代码不能为空")
	@Pattern(regexp="[a-zA-Z0-9_]{0,30}", message="编码长度不能大于 30 个字符，并且只能包含字母、数字、下划线")
	public String getViewCode() {
		return viewCode;
	}

	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	
	@NotBlank(message="机构名称不能为空")
	@Length(min=0, max=100, message="机构名称长度不能超过 100 个字符")
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	@NotBlank(message="机构全称不能为空")
	@Length(min=0, max=200, message="机构全称长度不能超过 200 个字符")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@NotBlank(message="机构类型不能为空")
	@Length(min=0, max=1, message="机构类型长度不能超过 1 个字符")
	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}
	
	@Length(min=0, max=100, message="负责人长度不能超过 100 个字符")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}
	
	@Length(min=0, max=100, message="电话长度不能超过 100 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=255, message="联系地址长度不能超过 255 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=100, message="邮政编码长度不能超过 100 个字符")
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@Length(min=0, max=200, message="邮箱长度不能超过 200 个字符")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Extend getExtend() {
		return extend;
	}

	public void setExtend(Extend extend) {
		this.extend = extend;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * 根据类型查找上级部门
	 * 1、例如当前机构类型为部门，你想获取部门所在的省公司名称
	 * 2、例如当前机构类型为部门的子部门，你想获取部门所在省公司名称
	 * 3、例如当前机构类型为小组，你想获取所在公司名称
	 * @param type 机构类型
	 * @return
	 */
	@JsonIgnore
	public Office getParentByType(String type){
		if (type == null){
			return null;
		}
		// 获取当前用户部门
		Office office = EmpUtils.getOffice(getOfficeCode());
		if (office == null){
			return null;
		}
		// 查找相同类型的上级部门
		do{
			office = EmpUtils.getOffice(office.getParentCode());
			if (office == null){
				return null;
			}
		}while(!type.equals(office.getOfficeType()));
		// 返回相同类型的上级部门对象
		return office;
	}

	@Override
	public String toString() {
		return officeCode;
	}
	
}
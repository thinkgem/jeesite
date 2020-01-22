/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import javax.validation.Valid;

import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.fieldtype.CompanyType;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;

/**
 * 员工用户管理Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}sys_user", alias="a", columns={
		@Column(includeEntity=User.class),
	}, joinTable={
		@JoinTable(type=Type.JOIN, entity=Employee.class, alias="e",
			on="e.emp_code=a.ref_code AND a.user_type=#{USER_TYPE_EMPLOYEE}",
			attrName="employee", columns={
				@Column(includeEntity=BaseEntity.class),
				@Column(includeEntity=DataEntity.class),
				@Column(name="emp_code", 	attrName="empCode", 			label="员工编码", isPK=true),
				@Column(name="emp_no", 		attrName="empNo", 				label="员工工号"),
				@Column(name="emp_name", 	attrName="empName", 			label="员工姓名", queryType=QueryType.LIKE),
				@Column(name="emp_name_en", attrName="empNameEn", 			label="英文名", queryType=QueryType.LIKE),
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
			on="o.office_code=e.office_code", attrName="employee.office",
			columns={
					@Column(includeEntity=DataEntity.class),
					@Column(includeEntity=TreeEntity.class),
					@Column(name="office_code", label="机构编码", isPK=true),
					@Column(name="view_code", 	label="机构代码"),
					@Column(name="office_name", label="机构名称", isQuery=false),
					@Column(name="full_name", 	label="机构全称"),
					@Column(name="office_type", label="机构类型"),
					@Column(name="leader", 		label="负责人"),
					@Column(name="phone", 		label="电话"),
					@Column(name="address", 	label="联系地址"),
					@Column(name="zip_code", 	label="邮政编码"),
					@Column(name="email", 		label="邮箱"),
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Company.class, alias="c", 
			on="c.company_code=e.company_code", attrName="employee.company",
			columns={
					@Column(includeEntity=DataEntity.class),
					@Column(includeEntity=TreeEntity.class),
					@Column(name="company_code", label="公司编码", isPK=true),
					@Column(name="view_code", 	label="公司代码"),
					@Column(name="company_name", label="公司名称", isQuery=false),
					@Column(name="full_name", 	label="公司全称"),
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="ar",
			on="ar.area_code = c.area_code", attrName="employee.company.area",
			columns={
					@Column(name="area_code", label="区域代码", isPK=true),
					@Column(name="area_name", label="区域名称", isQuery=false),
					@Column(name="area_type", label="区域类型"),
		}),
	},
	extWhereKeys="dsfOffice, dsfCompany",
	orderBy="a.user_weight DESC, a.update_date DESC"
)
public class EmpUser extends User {
	
	private static final long serialVersionUID = 1L;
	
	private String[] codes; // 查询用
	
	public EmpUser() {
		this(null);
	}

	public EmpUser(String id){
		super(id);
	}
	
	@Valid
	@ExcelFields({
		@ExcelField(title="归属机构", attrName="employee.office", align=Align.CENTER, sort=10, fieldType=OfficeType.class),
		@ExcelField(title="归属公司", attrName="employee.company", align = Align.CENTER, sort=20, fieldType=CompanyType.class),
		@ExcelField(title="登录账号", attrName="loginCode", align=Align.CENTER, sort=30),
		@ExcelField(title="用户昵称", attrName="userName", align=Align.CENTER, sort=40),
		@ExcelField(title="电子邮箱", attrName="email", align=Align.LEFT, sort=50),
		@ExcelField(title="手机号码", attrName="mobile", align=Align.CENTER, sort=60),
		@ExcelField(title="办公电话", attrName="phone", align=Align.CENTER, sort=70),
		@ExcelField(title="性别", attrName="sex", dictType="sys_user_sex", width=10*256, align=Align.CENTER, sort=75),
		@ExcelField(title="员工编码", attrName="employee.empCode", align=Align.CENTER, sort=80),
		@ExcelField(title="员工姓名", attrName="employee.empName", align=Align.CENTER, sort=95),
		@ExcelField(title="拥有角色编号", attrName="userRoleString", align=Align.LEFT, sort=800, type=ExcelField.Type.IMPORT),
		@ExcelField(title="最后登录日期", attrName="lastLoginDate", width=20*256, align=Align.CENTER, sort=900, type=ExcelField.Type.EXPORT, dataFormat="yyyy-MM-dd HH:mm"),
	})
	public Employee getEmployee(){
		Employee employee = (Employee)super.getRefObj();
		if (employee == null){
			employee = new Employee(getRefCode());
			super.setRefObj(employee);
		}
		return employee;
	}
	
	public void setEmployee(Employee employee){
		super.setRefObj(employee);
	}

	public String[] getCodes() {
		return codes;
	}

	public void setCodes(String[] codes) {
		this.codes = codes;
	}
	
}
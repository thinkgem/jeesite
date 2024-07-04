/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工管理Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}sys_employee", alias="a", label="员工信息", columns={
		@Column(includeEntity=BaseEntity.class),
		@Column(includeEntity=DataEntity.class),
		@Column(name="emp_code", 	attrName="empCode", 			label="员工编码", isPK=true),
		@Column(name="emp_no", 		attrName="empNo", 				label="员工工号"),
		@Column(name="emp_name", 	attrName="empName", 			label="员工姓名", queryType=QueryType.LIKE),
		@Column(name="emp_name_en", attrName="empNameEn", 			label="英文名", queryType=QueryType.LIKE),
		@Column(name="office_code", attrName="office.officeCode", 	label="机构编码", isQuery=false),
		@Column(name="office_name", attrName="office.officeName", 	label="机构名称", isQuery=false),
		@Column(name="company_code", attrName="company.companyCode", label="公司编码", isQuery=false),
		@Column(name="company_name", attrName="company.companyName", label="公司名称", isQuery=false),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
			on="o.office_code = a.office_code",
			columns={@Column(includeEntity=Office.class)}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Company.class, alias="c", 
			on="c.company_code = a.company_code",
			columns={@Column(includeEntity=Company.class)}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="ar",
			on="ar.area_code = c.area_code", attrName="company.area",
			columns={@Column(includeEntity=Area.class)}),
	}, orderBy="a.update_date DESC"
)
public class Employee extends DataEntity<Employee> {
	
	private static final long serialVersionUID = 1L;
	private String empCode;		// 员工编码
	private String empNo;		// 员工工号
	private String empName;		// 员工姓名
	private String empNameEn;	// 员工英文名
	private Office office;		// 机构对象
	private Company company;	// 公司对象
	
	private String postCode;	// 根据职位查询

	private List<EmployeePost> employeePostList = ListUtils.newArrayList(); // 关联岗位信息
	private List<EmployeeOffice> employeeOfficeList = ListUtils.newArrayList(); // 关联附属机构信息
	
	public Employee() {
		this(null);
	}

	public Employee(String id){
		super(id);
	}
	
	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	@Size(min=0, max=100, message="工号长度不能超过 100 个字符")
	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	@Size(min=0, max=100, message="名称长度不能超过 100 个字符")
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	@Size(min=0, max=100, message="英文名长度不能超过 100 个字符")
	public String getEmpNameEn() {
		return empNameEn;
	}

	public void setEmpNameEn(String empNameEn) {
		this.empNameEn = empNameEn;
	}

	@NotNull(message="归属机构不能为空")
	public Office getOffice() {
		if (office == null){
			office = new Office();
		}
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@NotNull(message="归属公司不能为空")
	public Company getCompany() {
		if (company == null){
			company = new Company();
		}
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Schema(description = "根据岗位编码查询")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public List<EmployeePost> getEmployeePostList() {
		return employeePostList;
	}
	
	public void setEmployeePostList(List<EmployeePost> employeePostList) {
		this.employeePostList = employeePostList;
	}

	@Schema(description = "员工岗位关系")
	public String getEmployeePosts() {
//		List<String> list = ListUtils.extractToList(employeePostList, "postCode");
//		return StringUtils.join(list, ",");
		return employeePostList.stream().map(EmployeePost::getPostCode).collect(Collectors.joining(","));
	}

	public void setEmployeePosts(String employeePosts) {
		String[] list = StringUtils.splitComma(employeePosts);
		for (String val : list){
			if (StringUtils.isNotBlank(val)){
				EmployeePost e = new EmployeePost();
				e.setPostCode(val);
				this.employeePostList.add(e);
			}
		}
	}

	public List<EmployeeOffice> getEmployeeOfficeList() {
		return employeeOfficeList;
	}

	public void setEmployeeOfficeList(List<EmployeeOffice> employeeOfficeList) {
		this.employeeOfficeList = employeeOfficeList;
	}
	
}
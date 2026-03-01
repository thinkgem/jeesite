/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;


import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;


/**
 * 公司机构 Entity
 * @author ThinkGem
 * @version 2017-03-23
 */
@Table(name = "${_prefix}sys_company_office", alias = "a", label = "公司部门关系", columns = {
		@Column(name = "company_code", attrName = "companyCode", label = "公司编码", isPK = true),
		@Column(name = "office_code",  attrName = "officeCode",  label = "机构编码", isPK = true),
	}, orderBy = ""
)
public class CompanyOffice extends DataEntity<CompanyOffice> {
	
	private static final long serialVersionUID = 1L;
	private String companyCode;		// 公司编码
	private String officeCode;		// 机构编码
	
	public CompanyOffice() {
		this(null);
	}

	public CompanyOffice(String id){
		super(id);
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
}
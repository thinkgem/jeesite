/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Extend;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 公司管理Entity
 * @author ThinkGem
 * @version 2017-03-23
 */
@Table(name="${_prefix}sys_company", alias="a", columns={
		@Column(includeEntity=BaseEntity.class),
		@Column(includeEntity=DataEntity.class),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="company_code", 	attrName="companyCode", 	label="公司编码", isPK=true),
		@Column(name="view_code", 		attrName="viewCode", 		label="公司代码"),
		@Column(name="company_name", 	attrName="companyName", 	label="公司名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(name="full_name", 		attrName="fullName", 		label="公司全称", queryType=QueryType.LIKE),
		@Column(name="area_code", 		attrName="area.areaCode", 	label="区域编码"),
		@Column(includeEntity=Extend.class, attrName="extend"),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="b",
			on="b.area_code = a.area_code",
			columns={
					@Column(name="area_code", label="区域代码", isPK=true),
					@Column(name="area_name", label="区域名称", isQuery=false),
					@Column(name="area_type", label="区域类型"),
					@Column(name="tree_names", label="区域全称"),
		}),
	}, extWhereKeys="dsf", orderBy="a.tree_sorts, a.company_code"
)
public class Company extends TreeEntity<Company> {
	
	private static final long serialVersionUID = 1L;
	private String companyCode;		// 公司编码
	private String viewCode;		// 公司代码
	private String companyName;		// 公司名称
	private String fullName;		// 公司全称
	private Area area;				// 区域编码
	private Extend extend;			// 扩展字段

	private List<CompanyOffice> companyOfficeList = ListUtils.newArrayList();	// 公司所包含的部门信息
	
	public Company() {
		this(null);
	}

	public Company(String id){
		super(id);
	}
	
	@Override
	public Company getParent() {
		return parent;
	}

	@Override
	public void setParent(Company parent) {
		this.parent = parent;
	}
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@NotBlank(message="公司代码不能为空")
	@Pattern(regexp="[a-zA-Z0-9_]{0,30}", message="代码长度不能大于 30 个字符，并且只能包含字母、数字、下划线")
	public String getViewCode() {
		return viewCode;
	}

	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	
	@NotBlank(message="公司名称不能为空")
	@Length(min=0, max=200, message="公司名称长度不能超过 200 个字符")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@NotBlank(message="公司全称不能为空")
	@Length(min=0, max=200, message="公司全称长度不能超过 200 个字符")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	public Extend getExtend() {
		return extend;
	}

	public void setExtend(Extend extend) {
		this.extend = extend;
	}

	public List<CompanyOffice> getCompanyOfficeList() {
		return companyOfficeList;
	}
	
	public void setCompanyOfficeListJson(String jsonString) {
		List<String> list = JsonMapper.fromJson(jsonString, List.class);
		if (list != null){
			for (String val : list){
				if (StringUtils.isNotBlank(val)){
					CompanyOffice e = new CompanyOffice();
					e.setCompanyCode(this.companyCode);
					e.setOfficeCode(val);
					e.setIsNewRecord(true);
					this.companyOfficeList.add(e);
				}
			}
		}
	}
	
}
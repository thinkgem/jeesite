/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 行政区划Entity
 * @author ThinkGem
 * @version 2017-03-22
 */
@Table(name="${_prefix}sys_area", alias="a", columns={
		@Column(includeEntity=DataEntity.class),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="area_code", attrName="areaCode", label="区域代码", isPK=true),
		@Column(name="area_name", attrName="areaName", label="区域名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(name="area_type", attrName="areaType", label="区域类型"),
	}, orderBy="a.tree_sorts, a.area_code"
)
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
	private String areaCode;		// 区域代码
	private String areaName;		// 区域名称
	private String areaType; 		// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
	public Area(){
		this(null);
	}

	public Area(String id){
		super(id);
	}
	
	@Override
	public Area getParent() {
		return parent;
	}

	@Override
	public void setParent(Area parent) {
		this.parent = parent;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@NotBlank(message="名称不能为空")
	@Length(min=0, max=100, message="名称长度不能超过 100 个字符")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@NotBlank(message="类型不能为空")
	@Length(min=0, max=1, message="类型长度不能超过 1 个字符")
	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	
	@Override
	public String toString() {
		return areaCode;
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.biz.entity;

import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 业务分类Entity
 * @author ThinkGem
 * @version 2019-08-12
 */
@Table(name="${_prefix}biz_category", alias="a", columns={
		@Column(name="category_code", attrName="categoryCode", label="业务分类", isPK=true),
		@Column(name="view_code", attrName="viewCode", label="业务分类"),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="category_name", attrName="categoryName", label="分类名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(includeEntity=DataEntity.class),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.tree_sorts, a.category_code"
)
public class BizCategory extends TreeEntity<BizCategory> {

	private static final long serialVersionUID = 1L;
	private String categoryCode;		// 分类编码
	private String viewCode;			// 分类代码（作为显示用，多租户内唯一）
	private String categoryName;		// 分类名称

	public BizCategory() {
		this(null);
	}

	public BizCategory(String id){
		super(id);
	}
	
	@Override
	public BizCategory getParent() {
		return parent;
	}

	@Override
	public void setParent(BizCategory parent) {
		this.parent = parent;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	@NotBlank(message="分类代码不能为空")
	@Pattern(regexp="[a-zA-Z0-9_]{0,30}", message="代码长度不能大于 30 个字符，并且只能包含字母、数字、下划线")
	public String getViewCode() {
		return viewCode;
	}

	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}

	public String getViewCode_like() {
		return sqlMap().getWhere().getValue("view_code", QueryType.LIKE);
	}

	public void setViewCode_like(String viewCode) {
		sqlMap().getWhere().and("view_code", QueryType.LIKE, viewCode);
	}

	@NotBlank(message="分类名称不能为空")
	@Size(min=0, max=64, message="分类名称长度不能超过 64 个字符")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
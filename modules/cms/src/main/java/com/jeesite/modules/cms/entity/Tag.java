/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import javax.validation.constraints.NotNull;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 内容标签Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name="${_prefix}cms_tag", alias="a", columns={
		@Column(name="tag_name", attrName="tagName", label="标签名称", isPK=true),
		@Column(name="clicknum", attrName="clicknum", label="点击次数"),
	}, orderBy="a.tag_name DESC"
)
public class Tag extends DataEntity<Tag> {
	
	private static final long serialVersionUID = 1L;
	private String tagName;		// 标签名称
	private Integer clicknum;	// 点击次数
	
	public Tag() {
		this(null);
	}

	public Tag(String id){
		super(id);
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	@NotNull(message="点击次数不能为空")
	public Integer getClicknum() {
		return clicknum;
	}

	public void setClicknum(Integer clicknum) {
		this.clicknum = clicknum;
	}
	
}
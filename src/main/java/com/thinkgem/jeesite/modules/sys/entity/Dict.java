/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.thinkgem.jeesite.common.persistence.BaseEntity;

/**
 * 字典Entity
 * @author ThinkGem
 * @version 2013-01-15
 */
@Entity
@Table(name = "sys_dict")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dict extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long id;		// 编号
	private String label;	// 值
	private String value;	// 键
	private String type;	// 类型
	private String desciption;// 描述
	private Integer sort;	// 排序
	private String delFlag;	// 删除标记（0：正常；1：删除）

	public Dict() {
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public Dict(Long id) {
		this();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sys_dict")
//	@SequenceGenerator(name = "seq_sys_dict", sequenceName = "seq_sys_dict")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotBlank
	@Size(min=0, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@NotBlank
	@Size(min=0, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@NotBlank
	@Size(min=0, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Size(min=0, max=100)
	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 站点Entity
 * @author ThinkGem
 * @version 2013-01-15
 */
@Entity
@Table(name = "cms_site")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Site extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id;		// 编号
	private String name;	// 站点名称
	private String title;	// 站点标题
	private String desciption;// 描述，填写有助于搜索引擎优化
	private String keywords;// 关键字，填写有助于搜索引擎优化
	private String theme;	// 主题
	private String copyright;// 版权信息
	private String delFlag; // 删除标记（0：正常；1：删除）

	public Site() {
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public Site(Long id){
		this();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cms_article")
	//@GenericGenerator(name = "seq_cms_article", strategy = "seq_cms_article")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Size(min=0, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@Size(min=0, max=100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Size(min=0, max=255)
	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	@Size(min=0, max=255)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Size(min=0, max=255)
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 判断是否为默认（主站）站点
	 */
	@Transient
	public static boolean isDefault(Long id){
		return id != null && id.equals(1L);
	}
	
	/**
	 * 获取当前编辑的站点编号
	 */
	@Transient
	public static long getCurrentSiteId(){
		Long siteId = (Long)UserUtils.getCache("siteId");
		return siteId!=null?siteId:1L;
	}
	
}
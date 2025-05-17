/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.entity;

import java.io.Serial;
import java.util.Date;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * APP版本管理Entity
 * @author ThinkGem
 * @version 2021-04-09
 */
@Table(name="${_prefix}app_upgrade", alias="a", label="版本信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="app_code", attrName="appCode", label="应用编号"),
		@Column(name="up_title", attrName="upTitle", label="升级标题", queryType=QueryType.LIKE),
		@Column(name="up_content", attrName="upContent", label="升级内容"),
		@Column(name="up_version", attrName="upVersion", label="升级版本"),
		@Column(name="up_type", attrName="upType", label="升级类型"),
		@Column(name="up_date", attrName="upDate", label="发布时间"),
		@Column(name="apk_url", attrName="apkUrl", label="APK下载地址"),
		@Column(name="res_url", attrName="resUrl", label="资源下载地址"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.up_version DESC"
)
public class AppUpgrade extends DataEntity<AppUpgrade> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String appCode;		// 应用编号
	private String upTitle;		// 升级标题
	private String upContent;		// 升级内容
	private Integer upVersion;		// 升级版本
	private String upType;		// 升级类型
	private Date upDate;		// 发布时间
	private String apkUrl;		// APK下载地址
	private String resUrl;		// 资源下载地址
	
	public AppUpgrade() {
		this(null);
	}

	public AppUpgrade(String id){
		super(id);
	}
	
	@Size(min=0, max=64, message="应用编号长度不能超过 64 个字符")
	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	@Size(min=0, max=200, message="升级标题长度不能超过 200 个字符")
	public String getUpTitle() {
		return upTitle;
	}

	public void setUpTitle(String upTitle) {
		this.upTitle = upTitle;
	}
	
	@Size(min=0, max=1000, message="升级内容长度不能超过 1000 个字符")
	public String getUpContent() {
		return upContent;
	}

	public void setUpContent(String upContent) {
		this.upContent = upContent;
	}
	
	public Integer getUpVersion() {
		return upVersion;
	}

	public void setUpVersion(Integer upVersion) {
		this.upVersion = upVersion;
	}
	
	public Integer getUpVersion_gt() {
		return sqlMap.getWhere().getValue("up_version", QueryType.GT);
	}

	public void setUpVersion_gt(Integer upVersion) {
		sqlMap.getWhere().and("up_version", QueryType.GT, upVersion);
	}
	
	@Size(min=0, max=1, message="升级类型长度不能超过 1 个字符")
	public String getUpType() {
		return upType;
	}

	public void setUpType(String upType) {
		this.upType = upType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getUpDate() {
		return upDate;
	}

	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}
	
	@Size(min=0, max=500, message="APK下载地址长度不能超过 500 个字符")
	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	
	@Size(min=0, max=500, message="资源下载地址长度不能超过 500 个字符")
	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}
	
}
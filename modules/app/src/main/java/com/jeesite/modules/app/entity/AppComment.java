/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * APP意见反馈Entity
 * @author ThinkGem
 * @version 2021-04-09
 */
@Table(name="${_prefix}app_comment", alias="a", label="意见信息", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="category", attrName="category", label="问题分类"),
		@Column(name="content", attrName="content", label="问题和意见"),
		@Column(name="contact", attrName="contact", label="联系方式"),
		@Column(name="create_by_name", attrName="createByName", label="提问人员姓名", queryType=QueryType.LIKE),
		@Column(name="device_info", attrName="deviceInfo", label="设备信息"),
		@Column(name="reply_date", attrName="replyDate", label="回复时间"),
		@Column(name="reply_content", attrName="replyContent", label="回复意见"),
		@Column(name="reply_user_code", attrName="replyUserCode", label="回复人员"),
		@Column(name="reply_user_name", attrName="replyUserName", label="回复人员姓名", queryType=QueryType.LIKE),
		@Column(name="status", attrName="status", label="状态", isUpdate=true), // save时，允许更新status字段
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.create_date DESC"
)
public class AppComment extends DataEntity<AppComment> {
	
	private static final long serialVersionUID = 1L;
	private String category;		// 问题分类
	private String content;		// 问题和意见
	private String contact;		// 联系方式
	private String deviceInfo;		// 设备信息
	private Date replyDate;		// 回复时间
	private String replyContent;		// 回复意见
	private String replyUserCode;		// 回复人员
	private String replyUserName;		// 回复人员姓名
	
	public AppComment() {
		this(null);
	}

	public AppComment(String id){
		super(id);
	}
	
	@NotBlank(message="问题分类不能为空")
	@Size(min=0, max=10, message="问题分类长度不能超过 10 个字符")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@NotBlank(message="问题和意见不能为空")
	@Size(min=0, max=500, message="问题和意见长度不能超过 500 个字符")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Size(min=0, max=200, message="联系方式长度不能超过 200 个字符")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Size(min=0, max=4000, message="设备信息长度不能超过 4000 个字符")
	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
	
	@Size(min=0, max=500, message="回复意见长度不能超过 500 个字符")
	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	@Size(min=0, max=64, message="回复人员长度不能超过 64 个字符")
	public String getReplyUserCode() {
		return replyUserCode;
	}

	public void setReplyUserCode(String replyUserCode) {
		this.replyUserCode = replyUserCode;
	}
	
	@Size(min=0, max=200, message="回复人员姓名长度不能超过 200 个字符")
	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}
	
}
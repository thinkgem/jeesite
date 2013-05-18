/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 评论Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "cms_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long id;		// 编号
	private String module; 	// 内容模型（article：文章；picture：图片；download：下载）
	private Long contentId;	// 归属分类内容的编号（Article.id、Photo.id、Download.id）
	private String title;	// 归属分类内容的标题（Article.title、Photo.title、Download.title）
	private String content; // 评论内容
	private String name; 	// 评论姓名
	private String ip; 		// 评论IP
	private Date createDate;// 评论时间
	private User auditUser; // 审核人
	private Date auditDate;	// 审核时间
	private String status;	// 删除标记（0：发布；1：作废；2：审核；）

	public Comment() {
		this.createDate = new Date();
		this.status = STATUS_RELEASE;
	}
	
	public Comment(Long id){
		this();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cms_comment")
//	@SequenceGenerator(name = "seq_cms_comment", sequenceName = "seq_cms_comment")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min=1, max=20)
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@NotNull
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Length(min=1, max=255)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name="audit_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@NotNull
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Length(min=1, max=1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
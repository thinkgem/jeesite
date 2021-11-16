/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 文章评论表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name="${_prefix}cms_comment", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="category_code", attrName="categoryCode", label="栏目编码"),
		@Column(name="article_id", attrName="articleId", label="内容编号"),
		@Column(name="parent_id", attrName="parentId", label="父级评论"),
		@Column(name="article_title", attrName="articleTitle", label="内容标题", queryType=QueryType.LIKE),
		@Column(name="content", attrName="content", label="评论内容"),
		@Column(name="name", attrName="name", label="评论姓名", queryType=QueryType.LIKE),
		@Column(name="ip", attrName="ip", label="评论IP"),
		@Column(includeEntity=DataEntity.class),
		@Column(name="audit_user_code", attrName="auditUserCode", label="审核人"),
		@Column(name="audit_date", attrName="auditDate", label="审核时间"),
		@Column(name="audit_comment", attrName="auditComment", label="审核意见"),
		@Column(name="hits_plus", attrName="hitsPlus", label="支持数"),
		@Column(name="hits_minus", attrName="hitsMinus", label="反对数"),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.id DESC"
)
public class Comment extends DataEntity<Comment> {
	
	private static final long serialVersionUID = 1L;
	
	private Category category;// 分类编号
	
	private String categoryCode;	// 栏目编码
	private String articleId;		// 内容编号 （Article.id、Photo.id、Download.id）
	private String parentId;		// 父级评论
	private String articleTitle;	// 内容标题（Article.title、Photo.title、Download.title）
	private String content;			// 评论内容
	private String name;			// 评论姓名
	private String ip;				// 评论IP
	private String auditUserCode;	// 审核人
	private Date auditDate;			// 审核时间
	private String auditComment;	// 审核意见
	private Integer hitsPlus;		// 支持数
	private Integer hitsMinus;		// 反对数
	
	public Comment() {
		this(null);
	}

	public Comment(String id){
		super(id);
	}
	
	@NotNull
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@NotBlank(message="栏目编码不能为空")
	@Size(min=0, max=64, message="栏目编码长度不能超过 64 个字符")
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	@NotBlank(message="内容编号不能为空")
	@Size(min=0, max=64, message="内容编号长度不能超过 64 个字符")
	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
	@Size(min=0, max=64, message="父级评论长度不能超过 64 个字符")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@NotBlank(message="内容标题不能为空")
	@Size(min=0, max=255, message="内容标题长度不能超过 255 个字符")
	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	
	@NotBlank(message="评论内容不能为空")
	@Size(min=0, max=255, message="评论内容长度不能超过 255 个字符")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Size(min=0, max=50, message="评论姓名长度不能超过 50 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Size(min=0, max=100, message="评论IP长度不能超过 100 个字符")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Size(min=0, max=64, message="审核人长度不能超过 64 个字符")
	public String getAuditUserCode() {
		return auditUserCode;
	}

	public void setAuditUserCode(String auditUserCode) {
		this.auditUserCode = auditUserCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	@Size(min=0, max=200, message="审核意见长度不能超过 200 个字符")
	public String getAuditComment() {
		return auditComment;
	}

	public void setAuditComment(String auditComment) {
		this.auditComment = auditComment;
	}
	
	public Integer getHitsPlus() {
		return hitsPlus;
	}

	public void setHitsPlus(Integer hitsPlus) {
		this.hitsPlus = hitsPlus;
	}
	
	public Integer getHitsMinus() {
		return hitsMinus;
	}

	public void setHitsMinus(Integer hitsMinus) {
		this.hitsMinus = hitsMinus;
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Extend;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 文章详情表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_article_data", alias = "a", columns = {
		@Column(name = "id", attrName = "id", label = "编号", isPK = true),
		@Column(name = "content", attrName = "content", label = "文章内容"),
		@Column(name = "relation", attrName = "relation", label = "相关文章"),
		@Column(name = "is_can_comment", attrName = "isCanComment", label = "是否允许评论"),
		@Column(includeEntity = Extend.class, attrName = "extend"),
	}, orderBy = "a.id DESC"
)
public class ArticleData extends DataEntity<ArticleData> {

	private static final long serialVersionUID = 1L;
	private String content; 		// 文章内容
	private String relation; 		// 相关文章
	private String isCanComment; 	// 是否允许评论
	private Extend extend; 			// 扩展字段

	public ArticleData() {
		this(null);
	}

	public ArticleData(String id) {
		super(id);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Size(min = 0, max = 1000, message = "相关文章长度不能超过 1000 个字符")
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Size(min = 0, max = 1, message = "是否允许评论长度不能超过 1 个字符")
	public String getIsCanComment() {
		return isCanComment;
	}

	public void setIsCanComment(String isCanComment) {
		this.isCanComment = isCanComment;
	}

	public Extend getExtend() {
		return extend;
	}

	public void setExtend(Extend extend) {
		this.extend = extend;
	}

}
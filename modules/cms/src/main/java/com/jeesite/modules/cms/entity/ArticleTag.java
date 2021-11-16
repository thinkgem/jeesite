/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 文章与标签关系Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_article_tag", alias = "a", columns = {
		@Column(name = "article_id", attrName = "articleId", label = "内容编号", isPK = true),
		@Column(name = "tag_name", attrName = "tagName", label = "标签名称", isPK = true),
	}, orderBy = "a.article_id DESC, a.tag_name DESC"
)
public class ArticleTag extends DataEntity<ArticleTag> {

	private static final long serialVersionUID = 1L;
	private String articleId; 	// 内容编号
	private String tagName; 	// 标签名称

	public ArticleTag() {

	}

	public ArticleTag(String articleId, String tagName) {
		this.articleId = articleId;
		this.tagName = tagName;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
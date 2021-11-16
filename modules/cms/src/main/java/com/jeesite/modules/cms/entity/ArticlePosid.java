/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 文章推荐位Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_article_posid", alias = "a", columns = {
		@Column(name = "article_id", attrName = "articleId", label = "内容编号", isPK = true),
		@Column(name = "postid", attrName = "postid", label = "推荐位置", comment = "推荐位置（1轮播图 2首页推荐 3栏目页面）", isPK = true),
	}, orderBy = "a.article_id DESC, a.postid DESC"
)
public class ArticlePosid extends DataEntity<ArticlePosid> {

	private static final long serialVersionUID = 1L;
	private String articleId; 	// 内容编号
	private String postid; 		// 推荐位置（1轮播图 2首页推荐 3栏目页面）

	public ArticlePosid() {

	}

	public ArticlePosid(String articleId, String postid) {
		this.articleId = articleId;
		this.postid = postid;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

}
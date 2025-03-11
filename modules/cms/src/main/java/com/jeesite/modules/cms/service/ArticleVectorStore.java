/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.entity.Page;
import com.jeesite.modules.cms.entity.Article;

import java.util.Map;

/**
 * 文章向量存储服务类
 * @author ThinkGem
 */
public interface ArticleVectorStore {

	/**
	 * 保存索引
	 * @author ThinkGem
	 */
	void save(Article article);

	/**
	 * 删除索引
	 * @author ThinkGem
	 */
	void delete(Article article);

	/**
	 * 重建向量库
	 * @author ThinkGem
	 */
	String rebuild(Article article);

}
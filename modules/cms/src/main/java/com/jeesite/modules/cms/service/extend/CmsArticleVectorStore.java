/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service.extend;

import com.jeesite.modules.cms.entity.CmsArticle;

/**
 * 文章向量存储服务类
 * @author ThinkGem
 */
public interface CmsArticleVectorStore {

	/**
	 * 保存索引
	 * @author ThinkGem
	 */
	void save(CmsArticle article);

	/**
	 * 删除索引
	 * @author ThinkGem
	 */
	void delete(CmsArticle article);

	/**
	 * 重建向量库
	 * @author ThinkGem
	 */
	String rebuild(CmsArticle article);

}

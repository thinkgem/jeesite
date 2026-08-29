/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service.extend;

import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.entity.CmsSite;

/**
 * 页面缓存服务接口
 * @author ThinkGem
 * @version 2023-4-7
 */
public interface CmsPageCacheService {

	/**
	 * 根据文章清理页面缓存
	 * @author ThinkGem
	 */
	void clearCache(CmsArticle article);

	/**
	 * 根据栏目清理页面缓存
	 * @author ThinkGem
	 */
	void clearCache(CmsCategory category);

	/**
	 * 根据栏目清理页面缓存
	 * @author ThinkGem
	 */
	void clearCache(CmsSite site);

}

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service.extend;

import com.jeesite.modules.cms.entity.CmsArticle;

import java.util.function.Consumer;

/**
 * 文章审核流程
 * @author ThinkGem
 * @version 2025-12-23
 */
public interface CmsArticleAuthService {

	/**
	 * 提交到流程
	 * @author ThinkGem
	 */
	void submit(CmsArticle article, Consumer<CmsArticle> consumer);

}

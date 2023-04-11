/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.entity.Page;
import com.jeesite.modules.cms.entity.Article;

import java.util.Map;

/**
 * 文章全文检索服务类
 * @author ThinkGem
 * @version 2023-4-10
 */
public interface ArticleIndexService {

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
	 * 重建索引
	 * @author ThinkGem
	 */
	String rebuild(Article article);

	/**
	 * 文章高级搜索
	 * @param page 分页对象
	 * @param qStr 搜索字符串
	 * @param qand 包含的字符串
	 * @param qnot 不包含的字符串
	 * @param bd 开始日期
	 * @param ed 结束日期
	 * @author ThinkGem
	 */
	Page<Map<String, Object>> searchPage(Page<Map<String, Object>> page, String qStr,
										 String qand, String qnot, String bd, String ed,
										 Map<String, String> params);
}
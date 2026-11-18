/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.article.service;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.utils.PageUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.ai.article.utils.ArticleTextUtils;
import com.jeesite.modules.ai.utils.AiVectorUtils;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.service.CmsArticleService;
import com.jeesite.modules.cms.service.extend.CmsArticleVectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 文章向量库存储实现
 * @author ThinkGem
 */
@Service
public class ArticleVectorStoreImpl implements CmsArticleVectorStore {

	/** 内容管理业务类型（对应「AI 向量库管理」中维护的绑定业务类型） */
	public static final String BIZ_TYPE = "cms-article";

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected CmsArticleService articleService;

	/**
	 * 获取向量存储：按 {@link #BIZ_TYPE} 从「AI 向量库管理」的绑定配置动态获取（配置变更后自动生效）
	 * @return 向量存储，未配置或不可用时返回 null
	 */
	protected VectorStore getVectorStore() {
		try {
			return AiVectorUtils.getBizVectorStore(BIZ_TYPE);
		} catch (Exception e) {
			logger.warn("获取向量库失败（{}）：{}", BIZ_TYPE, AiVectorUtils.getRootMessage(e));
			return null;
		}
	}

	/**
	 * 保存文章到向量库
	 * @author ThinkGem
	 */
	@Override
	public void save(CmsArticle article) {
		VectorStore vectorStore = getVectorStore();
		if (vectorStore == null) return;

		// 构建元数据
		Map<String, Object> metadata = MapUtils.newHashMap();
		metadata.put("bizType", BIZ_TYPE);
		metadata.put("id", article.getId());
		CmsCategory category = article.getCategory();
		if (category != null) {
			metadata.put("categoryCode", category.getCategoryCode());
			metadata.put("categoryName", category.getCategoryName());
			if (category.getSite() != null) {
				metadata.put("siteCode", category.getSite().getSiteCode());
			}
		}
		metadata.put("title", article.getTitle());
		metadata.put("href", article.getHref());
		metadata.put("keywords", article.getKeywords());
		metadata.put("description", article.getDescription());
		metadata.put("url", article.getUrl());
		metadata.put("status", article.getStatus());
		metadata.put("createBy", article.getCreateBy());
		metadata.put("createDate", article.getCreateDate());
		metadata.put("updateBy", article.getUpdateBy());
		metadata.put("updateDate", article.getUpdateDate());
		metadata.values().removeIf(Objects::isNull); // Spring AI Document 不允许 metadata 有 null 值

		// 提取文章内容
		String content = ArticleTextUtils.extractArticleContent(article);
		if (StringUtils.isBlank(content)) {
			logger.warn("文章内容为空，跳过向量存储。articleId: {}", article.getId());
			return;
		}

		// 分片写入向量库（删除原数据，增加新数据）
		AiVectorUtils.saveDocuments(vectorStore, new Document(article.getId(), content, metadata));

		logger.debug("文章已保存到向量库。articleId: {}, title: {}", article.getId(), article.getTitle());
	}

	/**
	 * 删除向量库文章
	 * @author ThinkGem
	 */
	@Override
	public void delete(CmsArticle article) {
		VectorStore vectorStore = getVectorStore();
		if (vectorStore == null) return;
		if (StringUtils.isNotBlank(article.getId())) {
			AiVectorUtils.deleteDocuments(vectorStore, "id", article.getId());
			logger.debug("已从向量库删除文章。articleId: {}", article.getId());
		}
	}

	/**
	 * 重建向量库文章
	 * @author ThinkGem
	 */
	public String rebuild(CmsArticle article) {
		if (getVectorStore() == null) return null;

		logger.debug("开始重建文章向量库。 siteCode: {}, categoryCode: {}",
				article.getCategory() != null && article.getCategory().getSite() != null
						? article.getCategory().getSite().getSiteCode() : null,
				article.getCategory() != null ? article.getCategory().getCategoryCode() : null);
		long start = System.currentTimeMillis();

		try {
			article.setIsQueryArticleData(true); // 查询文章内容
			if (articleService == null) {
				articleService = SpringUtils.getBean(CmsArticleService.class);
			}
			PageUtils.findList(article, null, e -> {
				List<CmsArticle> list = articleService.findList((CmsArticle) e);
				if (!list.isEmpty()) {
					list.forEach(this::save);
					return true;
				}
				return false;
			});
		} catch (Exception ex) {
			logger.error("重建文章向量库失败", ex);
			return "重建文章向量库失败：" + ex.getMessage();
		}

		String message = "重建文章向量库完成！ 用时" + TimeUtils.formatTime(System.currentTimeMillis() - start) + "。";
		logger.debug(message);
		return message;
	}

}

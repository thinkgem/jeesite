/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.config.Global;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.cms.dao.CmsCategoryDao;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.service.extend.CmsArticleIndexService;
import com.jeesite.modules.cms.service.extend.CmsArticleVectorStore;
import com.jeesite.modules.cms.service.extend.CmsPageCacheService;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 栏目 Service
 * @author ThinkGem
 * @version 2025-10-12
 */
@Service
public class CmsCategoryService extends TreeService<CmsCategoryDao, CmsCategory> {

	protected final CmsArticleIndexService articleIndexService;
	protected final CmsArticleVectorStore articleVectorStore;
	protected final CmsPageCacheService pageCacheService;

	public CmsCategoryService(ObjectProvider<CmsArticleIndexService> articleIndexService,
	                          ObjectProvider<CmsArticleVectorStore> articleVectorStore,
	                          ObjectProvider<CmsPageCacheService> pageCacheService) {
		this.articleIndexService = articleIndexService.getIfAvailable();
		this.articleVectorStore = articleVectorStore.getIfAvailable();
		this.pageCacheService = pageCacheService.getIfAvailable();
	}

	/**
	 * 获取单条数据
	 */
	@Override
	public CmsCategory get(CmsCategory category) {
		return super.get(category);
	}
	
	/**
	 * 添加数据权限
	 */
	@Override
	public void addDataScopeFilter(CmsCategory entity, String ctrlPermi) {
		entity.sqlMap().getDataScope().addFilter("dsfCategory",
				"Category", "a.category_code", "a.create_by", ctrlPermi);
	}
	
	/**
	 * 查询列表数据
	 */
	@Override
	public List<CmsCategory> findList(CmsCategory category) {
		return super.findList(category);
	}

	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	@Transactional
	public void save(CmsCategory category) {
		super.save(category);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(category, category.getId(), "category_image");
		// 清理栏目缓存
		clearCache(category);
	}
	
	/**
	 * 更新子节点，并设置子节点[sysCode]与父类相同
	 */
	@Override
	protected void updateChildNode(CmsCategory childEntity, CmsCategory parentEntity) {
		childEntity.setSite(parentEntity.getSite());
		childEntity.sqlMap().updateTreeDataExtSql("site_code = #{site.siteCode}");
		super.updateChildNode(childEntity, parentEntity);
	}

	/**
	 * 更新状态
	 */
	@Override
	@Transactional
	public void updateStatus(CmsCategory category) {
		super.updateStatus(category);
		// 清理栏目缓存
		clearCache(category);
	}

	/**
	 * 删除数据
	 */
	@Override
	@Transactional
	public void delete(CmsCategory category) {
		category.sqlMap().markIdDelete();
		super.delete(category);
		// 清理栏目缓存
		clearCache(category);
	}

	/**
	 * 清理栏目缓存
	 */
	public void clearCache(CmsCategory category) {
		// 清理栏目缓存
		CmsUtils.removeCache("category_" + category.getId());
		// 清理栏目列表缓存
		CmsUtils.removeCacheByKeyPrefix("categoryList_" + category.getSite().getId() + "_" + category.getParentCode() + "_");
		// 清理主导航缓存
		CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
		// 清理首页、栏目和文章页面缓存
		if (pageCacheService != null) {
			pageCacheService.clearCache(category);
		}
	}

	/**
	 * 重建索引
	 * @author ThinkGem
	 */
	public String rebuildIndex(CmsCategory category) {
		if (articleIndexService == null) {
			return text("您好，系统未安装全文检索模块");
		}
		Global.assertDemoMode();
		return articleIndexService.rebuild(new CmsArticle(category));
	}

	/**
	 * 重建向量数据库
	 * @author ThinkGem
	 */
	public String rebuildVectorStore(CmsCategory category) {
		if (articleVectorStore == null) {
			return text("您好，系统未配置向量数据库");
		}
		Global.assertDemoMode();
		return articleVectorStore.rebuild(new CmsArticle(category));
	}

}

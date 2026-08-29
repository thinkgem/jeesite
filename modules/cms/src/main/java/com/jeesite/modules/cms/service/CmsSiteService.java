/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.CmsSiteDao;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.entity.CmsSite;
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
 * 站点 Service
 * @author ThinkGem
 * @version 2025-10-12
 */
@Service
public class CmsSiteService extends CrudService<CmsSiteDao, CmsSite> {

	protected final CmsArticleIndexService articleIndexService;
	protected final CmsArticleVectorStore articleVectorStore;
	protected final CmsPageCacheService pageCacheService;

	public CmsSiteService(ObjectProvider<CmsArticleIndexService> articleIndexService,
	                      ObjectProvider<CmsArticleVectorStore> articleVectorStore,
	                      ObjectProvider<CmsPageCacheService> pageCacheService) {
		this.articleIndexService = articleIndexService.getIfAvailable();
		this.articleVectorStore = articleVectorStore.getIfAvailable();
		this.pageCacheService = pageCacheService.getIfAvailable();
	}

	/**
	 * 获取单条数据
	 * @param site 主键
	 */
	@Override
	public CmsSite get(CmsSite site) {
		return super.get(site);
	}

	/**
	 * 查询站点数据
	 * @param site 查询条件
	 */
	@Override
	public List<CmsSite> findList(CmsSite entity) {
		return super.findList(entity);
	}

	/**
	 * 查询分页数据
	 * @param site 查询条件
	 * @param site page 分页对象
	 */
	@Override
	public Page<CmsSite> findPage(CmsSite site) {
		return super.findPage(site);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param site 数据对象
	 */
	@Override
	@Transactional
	public void save(CmsSite site) {
		super.save(site);
		FileUploadUtils.saveFileUpload(site, site.getId(), "site_logo");
		// 清理站点缓存
		clearCache(site);
	}

	/**
	 * 更新状态
	 * @param site 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(CmsSite site) {
		super.updateStatus(site);
		// 清理站点缓存
		clearCache(site);
	}

	/**
	 * 删除数据
	 * @param site 数据对象
	 */
	@Override
	@Transactional
	public void delete(CmsSite site) {
		site.sqlMap().markIdDelete();
		super.delete(site);
		// 清理站点缓存
		clearCache(site);
	}

	/**
	 * 清理站点缓存
	 */
	public void clearCache(CmsSite site) {
		// 清理栏目缓存
		CmsUtils.removeCacheByKeyPrefix("category_");
		// 清理栏目列表缓存
		CmsUtils.removeCacheByKeyPrefix("categoryList_" + site.getId() + "_");
		// 清理主导航缓存
		CmsUtils.removeCache("mainNavList_" + site.getId());
		// 清理站点缓存
		CmsUtils.removeCache("site_" + site.getId());
		// 清理站点列表缓存
		CmsUtils.removeCache("siteList");
		// 清理首页、栏目和文章页面缓存
		if (pageCacheService != null) {
			pageCacheService.clearCache(site);
		}
	}

	/**
	 * 重建索引
	 * @author ThinkGem
	 */
	public String rebuildIndex(CmsSite site) {
		if (articleIndexService == null) {
			return text("您好，系统未安装全文检索模块");
		}
		Global.assertDemoMode();
		return articleIndexService.rebuild(new CmsArticle(new CmsCategory(site)));
	}

	/**
	 * 重建向量数据库
	 * @author ThinkGem
	 */
	public String rebuildVectorStore(CmsSite site) {
		if (articleVectorStore == null) {
			return text("您好，系统未配置向量数据库");
		}
		Global.assertDemoMode();
		return articleVectorStore.rebuild(new CmsArticle(new CmsCategory(site)));
	}
	
}

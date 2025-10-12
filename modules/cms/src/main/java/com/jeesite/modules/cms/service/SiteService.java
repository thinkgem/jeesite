/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.SiteDao;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 站点Service
 * @author ThinkGem
 * @version 2025-10-12
 */
@Service
public class SiteService extends CrudService<SiteDao, Site> {

	private final ArticleIndexService articleIndexService;
	private final ArticleVectorStore articleVectorStore;
	private final PageCacheService pageCacheService;

	public SiteService(ObjectProvider<ArticleIndexService> articleIndexService,
					   ObjectProvider<ArticleVectorStore> articleVectorStore,
					   ObjectProvider<PageCacheService> pageCacheService) {
		this.articleIndexService = articleIndexService.getIfAvailable();
		this.articleVectorStore = articleVectorStore.getIfAvailable();
		this.pageCacheService = pageCacheService.getIfAvailable();
	}

	/**
	 * 获取单条数据
	 * @param site 主键
	 */
	@Override
	public Site get(Site site) {
		return super.get(site);
	}

	/**
	 * 查询分页数据
	 * @param site 查询条件
	 * @param site page 分页对象
	 */
	@Override
	public Page<Site> findPage(Site site) {
		return super.findPage(site);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param site 数据对象
	 */
	@Override
	@Transactional
	public void save(Site site) {
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
	public void updateStatus(Site site) {
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
	public void delete(Site site) {
		site.sqlMap().markIdDelete();
		super.delete(site);
		// 清理站点缓存
		clearCache(site);
	}

	/**
	 * 清理站点缓存
	 */
	public void clearCache(Site site) {
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
	public String rebuildIndex(Site site) {
		if (articleIndexService == null) {
			return text("您好，系统未安装全文检索模块");
		}
		return articleIndexService.rebuild(new Article(new Category(site)));
	}

	/**
	 * 重建向量数据库
	 * @author ThinkGem
	 */
	public String rebuildVectorStore(Site site) {
		if (articleVectorStore == null) {
			return text("您好，系统未配置向量数据库");
		}
		return articleVectorStore.rebuild(new Article(new Category(site)));
	}
	
}
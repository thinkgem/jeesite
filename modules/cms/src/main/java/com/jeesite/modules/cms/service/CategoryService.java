/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.cms.dao.CategoryDao;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 栏目表Service
 * @author 长春叭哥、ThinkGem
 * @version 2023-4-10
 */
@Service
public class CategoryService extends TreeService<CategoryDao, Category> {

	@Autowired(required = false)
	private ArticleIndexService articleIndexService;
	@Autowired(required = false)
	private ArticleVectorStore articleVectorStore;
	@Autowired(required = false)
	private PageCacheService pageCacheService;

	/**
	 * 获取单条数据
	 */
	@Override
	public Category get(Category category) {
		return super.get(category);
	}
	
	/**
	 * 添加数据权限
	 */
	@Override
	public void addDataScopeFilter(Category entity, String ctrlPermi) {
		entity.sqlMap().getDataScope().addFilter("dsfCategory",
				"Category", "a.category_code", "a.create_by", ctrlPermi);
	}
	
	/**
	 * 查询列表数据
	 */
	@Override
	public List<Category> findList(Category category) {
		return super.findList(category);
	}

	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	@Transactional
	public void save(Category category) {
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
	protected void updateChildNode(Category childEntity, Category parentEntity) {
		childEntity.setSite(parentEntity.getSite());
		childEntity.sqlMap().updateTreeDataExtSql("site_code = #{site.siteCode}");
		super.updateChildNode(childEntity, parentEntity);
	}

	/**
	 * 更新状态
	 */
	@Override
	@Transactional
	public void updateStatus(Category category) {
		super.updateStatus(category);
		// 清理栏目缓存
		clearCache(category);
	}

	/**
	 * 删除数据
	 */
	@Override
	@Transactional
	public void delete(Category category) {
		category.sqlMap().markIdDelete();
		super.delete(category);
		// 清理栏目缓存
		clearCache(category);
	}

	/**
	 * 清理栏目缓存
	 */
	public void clearCache(Category category) {
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
	public String rebuildIndex(Category category) {
		if (articleIndexService == null) {
			return text("您好，系统未安装全文检索模块");
		}
		return articleIndexService.rebuild(new Article(category));
	}

	/**
	 * 重建向量数据库
	 * @author ThinkGem
	 */
	public String rebuildVectorStore(Category category) {
		if (articleVectorStore == null) {
			return text("您好，系统未配置向量数据库");
		}
		return articleVectorStore.rebuild(new Article(category));
	}

}
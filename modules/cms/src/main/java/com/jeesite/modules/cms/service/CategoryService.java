/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.service.ServiceException;
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
	private PageCacheService pageCacheService;

	/**
	 * 获取单条数据
	 * @param category
	 * @return
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
	 * @param category
	 * @return
	 */
	@Override
	public List<Category> findList(Category category) {
		return super.findList(category);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param category
	 */
	@Override
	@Transactional
	public void save(Category category) {
		super.save(category);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
		// 保存上传图片
		FileUploadUtils.saveFileUpload(category, category.getId(), "category_image");
		// 清理首页、栏目和文章页面缓存
		if (pageCacheService != null) {
			pageCacheService.clearCache(category);
		}
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
	 * @param category
	 */
	@Override
	@Transactional
	public void updateStatus(Category category) {
		super.updateStatus(category);
		// 清理首页、栏目和文章页面缓存
		if (pageCacheService != null) {
			pageCacheService.clearCache(category);
		}
	}

	/**
	 * 删除数据
	 * @param category
	 */
	@Override
	@Transactional
	public void delete(Category category) {
		super.delete(category);
		// 清理首页、栏目和文章页面缓存
		if (pageCacheService != null) {
			pageCacheService.clearCache(category);
		}
	}

	/**
	 * 重建索引
	 * @author ThinkGem
	 */
	public void rebuildIndex(Category category) {
		if (articleIndexService == null) {
			throw new ServiceException(text("未安装全文检索模块"));
		}
		articleIndexService.rebuild(new Article(category));
	}

}
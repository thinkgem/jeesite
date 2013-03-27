/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 栏目Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(CategoryService.class);
	
	@Autowired
	private CategoryDao categoryDao;
	
	public Category get(Long id) {
		return categoryDao.findOne(id);
	}
	
	public List<Category> findByUser(boolean isCurrentSite){
		List<Category> list = UserUtils.getCategoryList();
		if (isCurrentSite){
			List<Category> categoryList = Lists.newArrayList(); 
			for (Category e : list){
				if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
						&& e.getSite().getId().longValue() == Site.getCurrentSiteId())){
					categoryList.add(e);
				}
			}
			return categoryList;
		}else{
			return list;
		}
	}

	public List<Category> findByUserAndModule(String module){
		List<Category> categoryList = Lists.newArrayList(); 
		List<Category> list = UserUtils.getCategoryListByModule(module);
		for (Category e : list){
			if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
					&& e.getSite().getId().longValue() == Site.getCurrentSiteId())){
				categoryList.add(e);
			}
		}
		return categoryList;
	}

	public List<Category> findByParentId(Long parentId, Long siteId){
		return categoryDao.findByParentId(parentId, siteId);
	}
	
	public Page<Category> find(Page<Category> page, Category category) {
		DetachedCriteria dc = categoryDao.createDetachedCriteria();
		if (category.getSite()!=null && category.getSite().getId()!=null){
			dc.createAlias("site", "site");
			dc.add(Restrictions.eq("site.id", category.getSite().getId()));
		}
		if (category.getParent()!=null && category.getParent().getId()!=null){
			dc.createAlias("parent", "parent");
			dc.add(Restrictions.eq("parent.id", category.getParent().getId()));
		}
		if (StringUtils.isNotBlank(category.getInMenu())){
			dc.add(Restrictions.eq("inMenu", category.getInMenu()));
		}
		dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
		return categoryDao.find(page, dc);
//		page.setSpringPage(categoryDao.findByParentId(category.getParent().getId(), page.getSpringPage()));
//		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Category category) {
		category.setSite(new Site(Site.getCurrentSiteId()));
		category.setParent(this.get(category.getParent().getId()));
		String oldParentIds = category.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		category.setParentIds(category.getParent().getParentIds()+category.getParent().getId()+",");
		if (category.getDelFlag()==null){ category.setDelFlag(Category.DEL_FLAG_NORMAL);}
		categoryDao.clear();
		categoryDao.save(category);
		// 更新子节点 parentIds
		List<Category> list = categoryDao.findByParentIdsLike("%,"+category.getId()+",%");
		for (Category e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, category.getParentIds()));
		}
		categoryDao.save(list);
		UserUtils.removeCache("categoryList");
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		Category category = get(id);
		if (category!=null){
			categoryDao.deleteById(id, "%,"+id+",%");
			UserUtils.removeCache("categoryList");
			CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
		}
	}
	
}

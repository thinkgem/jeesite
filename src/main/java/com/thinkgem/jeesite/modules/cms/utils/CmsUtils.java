/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Link;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.LinkService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;

/**
 * 内容管理工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
public class CmsUtils implements ApplicationContextAware {

	private static final String CMS_CACHE = "cmsCache";
	
	private static SiteService siteService;
	private static CategoryService categoryService;
	private static ArticleService articleService;
	private static LinkService linkService;
	
	/**
	 * 获得站点信息
	 * @param id 站点编号
	 */
	public static Site getSite(long siteId){
		long id = 1L;
		if (siteId > 0){
			id = siteId;
		}
		Site site = (Site)CacheUtils.get(CMS_CACHE, "site_"+id);
		if (site==null){
			site = siteService.get(id);
			if (site!=null && Site.DEL_FLAG_NORMAL.equals(site.getDelFlag())){
				CacheUtils.put(CMS_CACHE, "site_"+id, site);
			}else{
				site = siteService.get(1L);
			}
		}
		return site;
	}
	
	/**
	 * 获得站点列表
	 */
	public static List<Site> getSiteList(){
		@SuppressWarnings("unchecked")
		List<Site> siteList = (List<Site>)CacheUtils.get(CMS_CACHE, "siteList");
		if (siteList == null){
			Page<Site> page = new Page<Site>(1, -1);
			page = siteService.find(page, new Site());
			siteList = page.getList();
			CacheUtils.put(CMS_CACHE, "siteList", siteList);
		}
		return siteList;
	}
	
	/**
	 * 获得主导航列表
	 * @param siteId 站点编号
	 */
	public static List<Category> getMainNavList(long siteId){
		@SuppressWarnings("unchecked")
		List<Category> mainNavList = (List<Category>)CacheUtils.get(CMS_CACHE, "mainNavList_"+siteId);
		if (mainNavList == null){
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category(1L));
			category.setInMenu(Category.SHOW);
			Page<Category> page = new Page<Category>(1, -1);
			page = categoryService.find(page, category);
			mainNavList = page.getList();
			CacheUtils.put(CMS_CACHE, "mainNavList_"+siteId, mainNavList);
		}
		return mainNavList;
	}
	
	/**
	 * 获得栏目列表
	 * @param siteId 站点编号
	 * @param parentId 分类父编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 */
	public static List<Category> getCategoryList(long siteId, long parentId, int number, String param){
		Page<Category> page = new Page<Category>(1, number, -1);
		Category category = new Category();
		category.setSite(new Site(siteId));
		category.setParent(new Category(parentId));
		if (StringUtils.isNotBlank(param)){
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
		}
		page = categoryService.find(page, category);
		return page.getList();
	}
	
	/**
	 * 获取文章
	 * @param id 文章编号
	 * @return
	 */
	public static Article getArticle(long articleId){
		return articleService.get(articleId);
	}
	
	/**
	 * 获取文章列表
	 * @param siteId 站点编号
	 * @param categoryId 分类编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 * 			posid	推荐位（1：首页焦点图；2：栏目页文章推荐；）
	 * 			thumb	缩略图（1：有缩略图的文章）
	 * @return
	 */
	public static List<Article> getArticleList(long siteId, long categoryId, int number, String param){
		Page<Article> page = new Page<Article>(1, number, -1);
		Article article = new Article(new Category(categoryId, new Site(siteId)));
		if (StringUtils.isNotBlank(param)){
			@SuppressWarnings({ "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
			if (new Integer(1).equals(map.get("posid")) || new Integer(2).equals(map.get("posid"))){
				article.setPosid(String.valueOf(map.get("posid")));
			}
			if (new Integer(1).equals(map.get("thumb"))){
				article.setThumb("1");
			}
		}
		article.setDelFlag(Article.DEL_FLAG_NORMAL);
		page = articleService.find(page, article, false);
		return page.getList();
	}
	
	/**
	 * 获取链接
	 * @param id 文章编号
	 * @return
	 */
	public static Link getLink(long linkId){
		return linkService.get(linkId);
	}
	
	/**
	 * 获取链接列表
	 * @param siteId 站点编号
	 * @param categoryId 分类编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 * @return
	 */
	public static List<Link> getLinkList(long siteId, long categoryId, int number, String param){
		Page<Link> page = new Page<Link>(1, number, -1);
		Link link = new Link(new Category(categoryId, new Site(siteId)));
		if (StringUtils.isNotBlank(param)){
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
		}
		link.setDelFlag(Link.DEL_FLAG_NORMAL);
		page = linkService.find(page, link, false);
		return page.getList();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
		siteService = (SiteService)applicationContext.getBean("siteService");
		categoryService = (CategoryService)applicationContext.getBean("categoryService");
		articleService = (ArticleService)applicationContext.getBean("articleService");
		linkService = (LinkService)applicationContext.getBean("linkService");
	}
	
	// ============== Cms Cache ==============
	
	public static Object getCache(String key) {
		return CacheUtils.get(CMS_CACHE, key);
	}

	public static void putCache(String key, Object value) {
		CacheUtils.put(CMS_CACHE, key, value);
	}

	public static void removeCache(String key) {
		CacheUtils.remove(CMS_CACHE, key);
	}
}
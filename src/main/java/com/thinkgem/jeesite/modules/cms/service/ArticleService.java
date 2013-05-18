/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.shiro.SecurityUtils;
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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDao;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文章Service
 * @author ThinkGem
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ArticleService.class);
	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CategoryDao categoryDao;
	
	public Article get(Long id) {
		return articleDao.findOne(id);
	}
	
	public Page<Article> find(Page<Article> page, Article article) {
		DetachedCriteria dc = articleDao.createDetachedCriteria();
		dc.createAlias("category", "category");
		dc.createAlias("category.site", "category.site");
		if (article.getCategory()!=null && article.getCategory().getId()!=null && !Category.isRoot(article.getCategory().getId())){
			Category category = categoryDao.findOne(article.getCategory().getId());
			if (category!=null){
				dc.add(Restrictions.or(
						Restrictions.eq("category.id", category.getId()),
						Restrictions.eq("category.parent.id", category.getId()),
						Restrictions.like("category.parentIds", "%,"+category.getId()+",%")));
				dc.add(Restrictions.eq("category.site.id", category.getSite().getId()));
				article.setCategory(category);
			}else{
				dc.add(Restrictions.eq("category.site.id", Site.getCurrentSiteId()));
			}
		}else{
			dc.add(Restrictions.eq("category.site.id", Site.getCurrentSiteId()));
		}
		if (StringUtils.isNotEmpty(article.getTitle())){
			dc.add(Restrictions.like("title", "%"+article.getTitle()+"%"));
		}
		if (StringUtils.isNotEmpty(article.getPosid())){
			dc.add(Restrictions.like("posid", "%,"+article.getPosid()+",%"));
		}
		if (StringUtils.isNotEmpty(article.getThumb())&&"1".equals(article.getThumb())){
			dc.add(Restrictions.and(Restrictions.isNotNull("thumb"),Restrictions.ne("thumb","")));
		}
		if (article.getUser()!=null && article.getUser().getId()>0){
			dc.add(Restrictions.eq("user.id", article.getUser().getId()));
		}
		dc.createAlias("category.office", "categoryOffice").createAlias("user", "user");
		dc.add(dataScopeFilter(UserUtils.getUser(), "categoryOffice", "user"));
		dc.add(Restrictions.eq("status", article.getStatus()));
		dc.addOrder(Order.desc("weight"));
		dc.addOrder(Order.desc("updateDate"));
		return articleDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Article article) {
		if (article.getArticleData().getContent()!=null){
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					article.getArticleData().getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!SecurityUtils.getSubject().isPermitted("cms:article:audit")){
			article.setStatus(Article.STATUS_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		if (article.getCategory()!=null&&article.getCategory().getId()!=null){
			Category category = categoryDao.findOne(article.getCategory().getId());
			if (!Article.YES.equals(category.getIsAudit())){
				article.setStatus(Article.STATUS_RELEASE);
			}
		}
		if (article.getId()==null){
			article.setUser(UserUtils.getUser());
		}
		article.setUpdateDate(new Date());
		articleDao.clear();
		articleDao.save(article);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id, Boolean isRe) {
//		articleDao.updateStatus(id, isRe!=null&&isRe?Article.STATUS_RELEASE:Article.STATUS_DELETE);
		// 使用下面方法，以便更新索引。
		Article article = articleDao.findOne(id);
		article.setStatus(isRe!=null&&isRe?Article.STATUS_RELEASE:Article.STATUS_DELETE);
		articleDao.save(article);
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		List<Object[]> list = Lists.newArrayList();
		Long[] idss = (Long[])ConvertUtils.convert(StringUtils.split(ids,","), Long.class);
		if (idss.length>0){
			List<Article> l = articleDao.findByIdIn(idss);
			for (Article e : l){
				list.add(new Object[]{e.getCategory().getId(),e.getId(),StringUtils.abbr(e.getTitle(),50)});
			}
		}
		return list;
	}
	
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(Long id) {
		articleDao.updateHitsAddOne(id);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex(){
		articleDao.createIndex();
	}
	
	/**
	 * 全文检索
	 */
	public Page<Article> search(Page<Article> page, String q){
		
		// 设置查询条件
		BooleanQuery query = articleDao.getFullTextQuery(q, "title","keywords","description","articleData.content");
		// 设置过滤条件
		BooleanQuery queryFilter = articleDao.getFullTextQuery(new BooleanClause(
				new TermQuery(new Term("status", Article.STATUS_RELEASE)), Occur.MUST));
		// 设置排序
		Sort sort = new Sort(new SortField("updateDate", SortField.DOC, true));
		// 全文检索
		articleDao.search(page, query, queryFilter, sort);
		// 关键字高亮
		articleDao.keywordsHighlight(query, page.getList(), "description","articleData.content");
		
		return page;
	}
	
}

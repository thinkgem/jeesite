/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
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
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.dao.LinkDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Link;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 链接Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class LinkService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(LinkService.class);
	
	@Autowired
	private LinkDao linkDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	public Link get(Long id) {
		return linkDao.findOne(id);
	}
	
	public Page<Link> find(Page<Link> page, Link link) {
		DetachedCriteria dc = linkDao.createDetachedCriteria();
		dc.createAlias("category", "category");
		dc.createAlias("category.site", "category.site");
		if (link.getCategory()!=null && link.getCategory().getId()!=null && !Category.isRoot(link.getCategory().getId())){
			Category category = categoryDao.findOne(link.getCategory().getId());
			if (category!=null){
				dc.add(Restrictions.or(
						Restrictions.eq("category.id", category.getId()),
						Restrictions.eq("category.parent.id", category.getId()),
						Restrictions.like("category.parentIds", "%,"+category.getId()+",%")));
				dc.add(Restrictions.eq("category.site.id", category.getSite().getId()));
				link.setCategory(category);
			}else{
				dc.add(Restrictions.eq("category.site.id", Site.getCurrentSiteId()));
			}
		}else{
			dc.add(Restrictions.eq("category.site.id", Site.getCurrentSiteId()));
		}
		if (StringUtils.isNotEmpty(link.getTitle())){
			dc.add(Restrictions.like("title", "%"+link.getTitle()+"%"));
		}
		if (link.getUser()!=null && link.getUser().getId()>0){
			dc.add(Restrictions.eq("user.id", link.getUser().getId()));
		}
		dc.createAlias("category.office", "categoryOffice").createAlias("user", "user");
		dc.add(dataScopeFilter(UserUtils.getUser(), "categoryOffice", "user"));
		dc.add(Restrictions.eq("status", link.getStatus()));
		dc.addOrder(Order.desc("weight"));
		dc.addOrder(Order.desc("updateDate"));
		return linkDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Link link) {
		if (link.getId()==null){
			link.setUser(UserUtils.getUser());
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!SecurityUtils.getSubject().isPermitted("cms:link:audit")){
			link.setStatus(Link.STATUS_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		if (link.getCategory()!=null&&link.getCategory().getId()!=null){
			Category category = categoryDao.findOne(link.getCategory().getId());
			if (!Article.YES.equals(category.getIsAudit())){
				link.setStatus(Article.STATUS_RELEASE);
			}
		}
		link.setUpdateDate(new Date());
		linkDao.clear();
		linkDao.save(link);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id, Boolean isRe) {
		linkDao.updateStatus(id, isRe!=null&&isRe?Link.STATUS_RELEASE:Link.STATUS_DELETE);
	}
	
	/**
	 * 通过编号获取内容标题
	 */
	public List<Object[]> findByIds(String ids) {
		List<Object[]> list = Lists.newArrayList();
		Long[] idss = (Long[])ConvertUtils.convert(StringUtils.split(ids,","), Long.class);
		if (idss.length>0){
			List<Link> l = linkDao.findByIdIn(idss);
			for (Link e : l){
				list.add(new Object[]{e.getId(),StringUtils.abbr(e.getTitle(),50)});
			}
		}
		return list;
	}

}

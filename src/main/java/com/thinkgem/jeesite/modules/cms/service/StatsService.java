/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.ArticleDao;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Site;

/**
 * 统计Service
 * @author ThinkGem
 * @version 2013-05-21
 */
@Service
@Transactional(readOnly = true)
public class StatsService extends BaseService {

	@Autowired
	private ArticleDao articleDao;
	
	public List<Map<String, Object>> article(Map<String, Object> paramMap) {
		
		StringBuilder ql = new StringBuilder();
		Parameter pm = new Parameter();
		
		ql.append("select new map(max(c.id) as categoryId, max(c.name) as categoryName, max(cp.id) as categoryParentId, max(cp.name) as categoryParentName,");
		ql.append("   count(*) as cnt, sum(a.hits) as hits, max(a.updateDate) as updateDate, max(o.id) as officeId, max(o.name) as officeName) ");
		ql.append(" from Article a join a.category c join c.office o join c.parent cp where c.site.id = ");
		ql.append(Site.getCurrentSiteId());
		
		Date beginDate = DateUtils.parseDate(paramMap.get("beginDate"));
		if (beginDate == null){
			beginDate = DateUtils.setDays(new Date(), 1);
			paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
		}
		Date endDate = DateUtils.parseDate(paramMap.get("endDate"));
		if (endDate == null){
			endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
		}
		
		String categoryId = ObjectUtils.toString(paramMap.get("categoryId"));
		if (StringUtils.isNotBlank(categoryId)){
			ql.append(" and (c.id = :id or c.parentIds like :parentIds)");
			pm.put("id", categoryId);
			pm.put("parentIds", "%,"+categoryId+",%");
		}
		
		String officeId = ObjectUtils.toString(paramMap.get("officeId"));
		if (StringUtils.isNotBlank(officeId)){
			ql.append(" and (o.id = :officeId or o.parentIds like :officeParentIds)");
			pm.put("officeId", officeId);
			pm.put("officeParentIds", "%,"+officeId+",%");
		}
		
		ql.append(" and a.updateDate between :beginDate and :endDate");
		pm.put("beginDate", beginDate);
		pm.put("endDate", DateUtils.addDays(endDate, 1));

		ql.append(" and c.delFlag = :delFlag");
		pm.put("delFlag", Article.DEL_FLAG_NORMAL);
		
		ql.append(" group by cp.sort, cp.id, c.sort, c.id");
		ql.append(" order by cp.sort, cp.id, c.sort, c.id");
		List<Map<String, Object>> list = articleDao.find(ql.toString(), pm);
		return list;
	}

}

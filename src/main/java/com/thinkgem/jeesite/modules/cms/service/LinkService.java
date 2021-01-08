/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.dao.LinkDao;
import com.thinkgem.jeesite.modules.cms.entity.Link;

/**
 * 链接Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class LinkService extends CrudService<LinkDao, Link> {

	@Transactional(readOnly = false)
	public Page<Link> findPage(Page<Link> page, Link link, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByLink");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
				&& updateExpiredWeightDate.getTime() < new Date().getTime())){
			dao.updateExpiredWeight(link);
			CacheUtils.put("updateExpiredWeightDateByLink", DateUtils.addHours(new Date(), 6));
		}
		link.getSqlMap().put("dsf", dataScopeFilter(link.getCurrentUser(), "o", "u"));
		
		return super.findPage(page, link);
	}
	
	@Transactional(readOnly = false)
	public void delete(Link link, Boolean isRe) {
		//dao.updateDelFlag(id, isRe!=null&&isRe?Link.DEL_FLAG_NORMAL:Link.DEL_FLAG_DELETE);
		link.setDelFlag(isRe!=null&&isRe?Link.DEL_FLAG_NORMAL:Link.DEL_FLAG_DELETE);
		dao.delete(link);
	}
	
	/**
	 * 通过编号获取内容标题
	 */
	public List<Object[]> findByIds(String ids) {
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		if (idss.length>0){
			List<Link> l = dao.findByIdIn(idss);
			for (Link e : l){
				list.add(new Object[]{e.getId(),StringUtils.abbr(e.getTitle(),50)});
			}
		}
		return list;
	}

}

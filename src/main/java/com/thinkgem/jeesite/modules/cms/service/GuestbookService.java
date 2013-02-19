/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.cms.dao.GuestbookDao;
import com.thinkgem.jeesite.modules.cms.entity.Guestbook;

/**
 * 留言Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Component
@Transactional(readOnly = true)
public class GuestbookService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(GuestbookService.class);
	
	@Autowired
	private GuestbookDao guestbookDao;
	
	public Guestbook get(Long id) {
		return guestbookDao.findOne(id);
	}
	
	public Page<Guestbook> find(Page<Guestbook> page, Guestbook guestbook) {
		DetachedCriteria dc = guestbookDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(guestbook.getType())){
			dc.add(Restrictions.eq("type", guestbook.getType()));
		}
		if (StringUtils.isNotEmpty(guestbook.getContent())){
			dc.add(Restrictions.like("content", "%"+guestbook.getContent()+"%"));
		}
		dc.add(Restrictions.eq("status", guestbook.getStatus()));
		dc.addOrder(Order.desc("id"));
		return guestbookDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(Guestbook guestbook) {
		guestbookDao.save(guestbook);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id, Boolean isRe) {
		guestbookDao.updateStatus(id, isRe!=null&&isRe?Guestbook.STATUS_AUDIT:Guestbook.STATUS_DELETE);
	}
	
}

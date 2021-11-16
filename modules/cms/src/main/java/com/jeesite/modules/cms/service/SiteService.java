/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.SiteDao;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 站点表Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Service
@Transactional(readOnly = true)
public class SiteService extends CrudService<SiteDao, Site> {
	
	/**
	 * 获取单条数据
	 * @param site
	 * @return
	 */
	@Override
	public Site get(Site site) {
		return super.get(site);
	}

	/**
	 * 查询分页数据
	 * @param site 查询条件
	 * @param site.page 分页对象
	 * @return
	 */
	@Override
	public Page<Site> findPage(Site site) {
		return super.findPage(site);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param site
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(Site site) {
		super.save(site);
		CmsUtils.removeCache("siteList");
		// 保存logo
		FileUploadUtils.saveFileUpload(site, site.getId(), "site_logo");
	}

	/**
	 * 更新状态
	 * @param site
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(Site site) {
		super.updateStatus(site);
	}

	/**
	 * 删除数据
	 * @param site
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Site site) {
		super.delete(site);
	}

	/**
	 * 删除站点
	 * @param site
	 * @param isRe
	 */
	@Transactional(readOnly = false)
	public void delete(Site site, Boolean isRe) {
		site.setStatus(isRe != null && isRe ? Site.STATUS_NORMAL : Site.STATUS_DELETE);
		super.delete(site);
		CmsUtils.removeCache("siteList");
	}
	
}
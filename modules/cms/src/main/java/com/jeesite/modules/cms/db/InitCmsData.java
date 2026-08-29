/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.db;

import com.jeesite.common.config.Global;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.cms.dao.CmsArticleDao;
import com.jeesite.modules.cms.dao.CmsArticleDataDao;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsArticleData;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.entity.CmsSite;
import com.jeesite.modules.cms.service.CmsCategoryService;
import com.jeesite.modules.cms.service.CmsSiteService;
import com.jeesite.modules.gen.utils.GenUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 初始化 CMS 表及数据
 * @author ThinkGem
 * @version 2020-5-26
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitCmsData extends BaseInitDataTests {

	public InitCmsData(CmsSiteService siteService, CmsCategoryService categoryService,
	                   CmsArticleDao articleDao, CmsArticleDataDao articleDataDao) {
		this.siteService = siteService;
		this.categoryService = categoryService;
		this.articleDao = articleDao;
		this.articleDataDao = articleDataDao;
	}

	@Override
	public boolean initData() throws Exception {
		if (GenUtils.isTableExists(Global.getTablePrefix() + "cms_article")) {
			return true; // 如果表已存在，则无需初始化
		}
		this.runCreateScript("cms.sql");
		this.initModuleInfo("cms");
		this.initModuleMenu("/cms/index");
		this.initModuleDict("cms_theme");
		this.initSite();
		this.initCategory();
		this.initArticle();
		this.initArticleData();
		return true;
	}

	private final CmsSiteService siteService;
	public void initSite() throws Exception {
		initExcelData(CmsSite.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				CmsSite entity = (CmsSite)params[1];
				entity.setIsNewRecord(true);
				siteService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	private final CmsCategoryService categoryService;
	public void initCategory() throws Exception {
		initExcelData(CmsCategory.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				CmsCategory entity = (CmsCategory)params[1];
				entity.setIsNewRecord(true);
				categoryService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	private final CmsArticleDao articleDao;
	public void initArticle() throws Exception {
		initExcelData(CmsArticle.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				CmsArticle entity = (CmsArticle)params[1];
				entity.setIsNewRecord(true);
				articleDao.insert(entity);
				return null;
			}
			return null;
		});
	}
	
	private final CmsArticleDataDao articleDataDao;
	public void initArticleData() throws Exception {
		initExcelData(CmsArticleData.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				CmsArticleData entity = (CmsArticleData)params[1];
				entity.setIsNewRecord(true);
				articleDataDao.insert(entity);
				return null;
			}
			return null;
		});
	}
	
}

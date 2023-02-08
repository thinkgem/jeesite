/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.db;

import com.jeesite.common.config.Global;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.cms.dao.ArticleDao;
import com.jeesite.modules.cms.dao.ArticleDataDao;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.SiteService;
import com.jeesite.modules.gen.utils.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 初始化CMS表及数据
 * @author ThinkGem
 * @version 2020-5-26
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitCmsData extends BaseInitDataTests {

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

	@Autowired
	private SiteService siteService;
	public void initSite() throws Exception{
		initExcelData(Site.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Site entity = (Site)params[1];
				entity.setIsNewRecord(true);
				siteService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	@Autowired
	private CategoryService categoryService;
	public void initCategory() throws Exception{
		initExcelData(Category.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Category entity = (Category)params[1];
				entity.setIsNewRecord(true);
				categoryService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	@Autowired
	private ArticleDao articleDao;
	public void initArticle() throws Exception{
		initExcelData(Article.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Article entity = (Article)params[1];
				entity.setIsNewRecord(true);
				articleDao.insert(entity);
				return null;
			}
			return null;
		});
	}
	
	@Autowired
	private ArticleDataDao articleDataDao;
	public void initArticleData() throws Exception{
		initExcelData(ArticleData.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				ArticleData entity = (ArticleData)params[1];
				entity.setIsNewRecord(true);
				articleDataDao.insert(entity);
				return null;
			}
			return null;
		});
	}
	
}

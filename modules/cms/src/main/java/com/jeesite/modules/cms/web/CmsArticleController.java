/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsArticleData;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.entity.CmsSite;
import com.jeesite.modules.cms.service.CmsArticleService;
import com.jeesite.modules.cms.service.CmsCategoryService;
import com.jeesite.modules.cms.service.CmsTemplateService;
import com.jeesite.modules.cms.utils.CmsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * 文章表 Controller
 * @author ThinkGem、长春叭哥、一往无前
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class CmsArticleController extends BaseController {

	private final CmsArticleService articleService;
	private final CmsCategoryService categoryService;
	private final CmsTemplateService templateService;

	public CmsArticleController(CmsArticleService articleService, CmsCategoryService categoryService, CmsTemplateService templateService) {
		this.articleService = articleService;
		this.categoryService = categoryService;
		this.templateService = templateService;
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute("article")
	public CmsArticle get(String id, boolean isNewRecord) {
		return articleService.get(id, isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "list", "" })
	public String list(@ModelAttribute("article") CmsArticle article, Boolean isAll, Model model) throws IOException {
		if (StringUtils.isNotBlank(article.getCategory().getCategoryCode())
				&& !CmsCategory.ROOT_CODE.equals(article.getCategory().getCategoryCode())) {
			article.setCategory(CmsUtils.getCategory(article.getCategory().getCategoryCode()));
		}
		// 栏目展现模式，当为（3：简介类栏目，栏目第一条内容）时，自动维护第一条内容
		if (CmsCategory.SHOW_MODES_FIRST_CONTENT.equals(article.getCategory().getShowModes())) {
			// 获取文章内容
			Page<CmsArticle> page = new Page<>(1, 1, -1);
			article.setPage(page);
			page = articleService.findPage(article);
			if (!page.getList().isEmpty()) {
				article = page.getList().get(0);
				article.setArticleData(articleService.get(new CmsArticleData(article.getId())));
			}
			return form(article, model);
		}
		model.addAttribute("isCanUseAuth", CmsArticleService.isCanUseAuth);
		model.addAttribute("isAll", isAll);
		return "modules/cms/articleList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CmsArticle> listData(@ModelAttribute("article") CmsArticle article, Boolean isAll, HttpServletRequest request, HttpServletResponse response) {
		article.setPage(new Page<>(request, response));
		if (StringUtils.isBlank(article.getCategory().getSite().getSiteCode())) {
			article.getCategory().setSite(new CmsSite(CmsSite.getCurrentSiteCode()));
		}
		// 查询指定栏目以及下级栏目的文章（如果不需要，可以注释掉）
		if (StringUtils.isNotBlank(article.getCategory().getCategoryCode())) {
			article.getCategory().setIsQueryChildren(true);
		}
		// 是否查询全部，不过滤权限
		if (!(isAll != null && isAll) || Global.isStrictMode()){
			articleService.addDataScopeFilter(article);
		}
//		if (!article.currentUser().isAdmin()) {
//			// 如果没有审核权限，或者 草稿状态的文章 则只查看自己创建的文章。
//			if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
//				article.setCreateBy(article.currentUser().getUserCode());
//			} else if (Article.STATUS_DRAFT.equals(article.getStatus())) {
//				article.setCreateBy(article.currentUser().getUserCode());
//			}
//		}
		Page<CmsArticle> page = articleService.findPage(article);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("article") CmsArticle article, Model model) throws IOException {
		if (StringUtils.isNotBlank(article.getCategory().getCategoryCode())) {
			CmsCategory categoryParam = new CmsCategory();
			categoryParam.setSite(new CmsSite(CmsSite.getCurrentSiteCode()));
			categoryParam.setParentCode(article.getCategory().getCategoryCode());
			List<CmsCategory> list = categoryService.findList(categoryParam);
			if (!list.isEmpty()) {
				article.setCategory(null); // 不允许在父节点上添加文章
			} else {
				article.setCategory(CmsUtils.getCategory(article.getCategory().getCategoryCode()));
			}
		}
//		if (StringUtils.isBlank(article.getId())) {
//			article.setStatus(Article.STATUS_DRAFT);
//		}
		model.addAttribute("isCanUseAuth", CmsArticleService.isCanUseAuth);
		model.addAttribute("article_DEFAULT_TEMPLATE", CmsArticle.DEFAULT_TEMPLATE);
		model.addAttribute("contentViewList", templateService.getTemplateContentDict(CmsArticle.DEFAULT_TEMPLATE));
		model.addAttribute("currentSite", CmsUtils.getCurrentSite());
		model.addAttribute("article", article);
		CmsUtils.addViewConfigAttribute(model, article.getCategory());
		return "modules/cms/articleForm";
	}
	
	/**
	 * 保存文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated @ModelAttribute("article") CmsArticle article) {
		articleService.save(article);
		return renderResult(Global.TRUE, text("保存文章表成功！"));
	}

	/**
	 * 停用文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(@ModelAttribute("article") CmsArticle article) {
		article.setStatus(CmsArticle.STATUS_DISABLE);
		articleService.updateStatus(article);
		return renderResult(Global.TRUE, text("停用文章表成功"));
	}

	/**
	 * 启用文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(@ModelAttribute("article") CmsArticle article) {
		article.setStatus(CmsArticle.STATUS_NORMAL);
		articleService.updateStatus(article);
		return renderResult(Global.TRUE, text("启用文章表成功"));
	}

	/**
	 * 删除文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(@ModelAttribute("article") CmsArticle article) {
		articleService.delete(article);
		return renderResult(Global.TRUE, text("删除文章表成功！"));
	}

}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.FileTemplateService;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文章表Controller
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FileTemplateService fileTemplateService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Article get(String id, boolean isNewRecord) {
		return articleService.get(id, isNewRecord);
	}

	/**
	 * 查询列表
	 * @throws IOException
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "list", "" })
	public String list(Article article, Boolean isAll, Model model) throws IOException {
		if (StringUtils.isNotBlank(article.getCategory().getCategoryCode())
				&& !Category.ROOT_CODE.equals(article.getCategory().getCategoryCode())) {
			article.setCategory(CmsUtils.getCategory(article.getCategory().getCategoryCode()));
		}
		// 栏目展现模式，当为（3：简介类栏目，栏目第一条内容）时，自动维护第一条内容
		if (Category.SHOW_MODES_FIRST_CONTENT.equals(article.getCategory().getShowModes())) {
			// 获取文章内容
			Page<Article> page = new Page<>(1, 1, -1);
			article.setPage(page);
			page = articleService.findPage(article);
			if (page.getList().size() > 0) {
				article = page.getList().get(0);
				article.setArticleData(articleService.get(new ArticleData(article.getId())));
			}
			return form(article, model);
		}
		model.addAttribute("isAll", isAll);
		return "modules/cms/articleList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Article> listData(Article article, Boolean isAll, HttpServletRequest request, HttpServletResponse response) {
		article.setPage(new Page<>(request, response));
		if (StringUtils.isBlank(article.getCategory().getSite().getSiteCode())) {
			article.getCategory().setSite(new Site(Site.getCurrentSiteCode()));
		}
		// 查询指定栏目以及下级栏目的文章（如果不需要，可以注释掉）
		if (StringUtils.isNotBlank(article.getCategory().getCategoryCode())) {
			article.getCategory().setIsQueryChildren(true);
		}
		// 是否查询全部，不过滤权限
		if (!(isAll != null && isAll) || Global.isStrictMode()){
			articleService.addDataScopeFilter(article);
		}
		if (!article.currentUser().isAdmin()) {
			// 如果没有审核权限，或者 草稿状态的文章 则只查看自己创建的文章。
			if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
				article.setCreateBy(article.currentUser().getUserCode());
			} else if (Article.STATUS_DRAFT.equals(article.getStatus())) {
				article.setCreateBy(article.currentUser().getUserCode());
			}
		}
		Page<Article> page = articleService.findPage(article);
		return page;
	}

	/**
	 * 查看编辑表单
	 * @throws IOException
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(Article article, Model model) throws IOException {
		if (StringUtils.isNotBlank(article.getCategory().getCategoryCode())) {
			Category categoryParam = new Category();
			categoryParam.setSite(new Site(Site.getCurrentSiteCode()));
			categoryParam.setParentCode(article.getCategory().getCategoryCode());
			List<Category> list = categoryService.findList(categoryParam);
			if (!list.isEmpty()) {
				article.setCategory(null); // 不允许在父节点上添加文章
			} else {
				article.setCategory(CmsUtils.getCategory(article.getCategory().getCategoryCode()));
			}
		}
		if (StringUtils.isBlank(article.getId())) {
			article.setStatus(Article.STATUS_DRAFT);
		}
		model.addAttribute("contentViewList", fileTemplateService.getTemplateContentDict(Article.DEFAULT_TEMPLATE));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
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
	public String save(@Validated Article article) {
		articleService.save(article);
		return renderResult(Global.TRUE, text("保存文章表成功！"));
	}

	/**
	 * 停用文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Article article) {
		article.setStatus(Article.STATUS_DISABLE);
		articleService.updateStatus(article);
		return renderResult(Global.TRUE, text("停用文章表成功"));
	}

	/**
	 * 启用文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Article article) {
		article.setStatus(Article.STATUS_NORMAL);
		articleService.updateStatus(article);
		return renderResult(Global.TRUE, text("启用文章表成功"));
	}

	/**
	 * 删除文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Article article) {
		articleService.delete(article);
		return renderResult(Global.TRUE, text("删除文章表成功！"));
	}

}
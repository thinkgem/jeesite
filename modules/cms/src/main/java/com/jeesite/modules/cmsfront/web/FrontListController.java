/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cmsfront.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.utils.CmsUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 网站栏目列表 Controller
 * @author ThinkGem、三片叶子、长春八哥、一往无前
 * @version 2026-01-10
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontListController extends BaseController {

	private final CategoryService categoryService;
	private final ArticleService articleService;

	public FrontListController(CategoryService categoryService, ArticleService articleService) {
		this.categoryService = categoryService;
		this.articleService = articleService;
	}

	/**
	 * 内容列表
	 */
	@RequestMapping(value = { "list-{categoryCode}", "list-{categoryCode}.html" })
	public String list(@PathVariable String categoryCode,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "30") Integer pageSize, Model model,
			HttpServletRequest request) {

		// 获取栏目信息
		Category category = CmsUtils.getCategory(categoryCode);
		if (category == null || !Category.STATUS_NORMAL.equals(category.getStatus())) {
			Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			model.addAttribute("site", site);
			return "error/404";
		}

		// 如果设置了外部链接，则跳转到指定链接
		if (StringUtils.isNotBlank(category.getHref())) {
			String ctxPath = Global.getCtxPath();
			if (category.getHref().startsWith(ctxPath)) {
				category.setHref(category.getHref().replaceFirst(ctxPath, StringUtils.EMPTY));
			}
			return REDIRECT + category.getHref();
		}

		// 获取站点信息
		Site site = CmsUtils.getSite(category.getSite().getId());
		model.addAttribute("site", site);

		// 当前栏目展现方式为：3：简介类栏目，栏目第一条内容
		if (Category.SHOW_MODES_FIRST_CONTENT.equals(category.getShowModes())) {
			return FORWARD + frontPath + "/view-" + categoryCode + "-.html";
		}

		// 当前展现方式为：1：默认，或者，2：栏目列表
		else {

			// 当前栏目的子栏目列表
			List<Category> categoryList;

			// 如果有子节点，则查询子栏目列表
			if (!category.getIsTreeLeaf()) {
				Category categoryParam = new Category();
				categoryParam.setSite(new Site(site.getSiteCode()));
				categoryParam.setParentCode(category.getCategoryCode());
				categoryList = categoryService.findList(categoryParam);
				model.addAttribute("categoryList", categoryList);
			}else{
				categoryList = ListUtils.newArrayList();
			}

			// 当前栏目展现方式为：2 、无子栏目或公共模型，显示栏目内容列表；1：无子栏目或一个子栏目，显示栏目内容列表
			if (Category.SHOW_MODES_CONTENT_LIST.equals(category.getShowModes()) || categoryList.size() <= 1) {

				// 有子栏目并展现方式为2，则获取第一个子栏目；无子栏目，则获取同级分类列表。
				if (!categoryList.isEmpty()) {
					category = categoryList.get(0);
				}

				// 如果第一个子栏目为简介类栏目，则获取该栏目第一篇文章并展现
				if (Category.SHOW_MODES_FIRST_CONTENT.equals(category.getShowModes())) {
					return FORWARD + frontPath + "/view-" + category.getCategoryCode() + "-.html";
				}

				// 否则，获取内容列表信息
				else {
					// 文章模型
					if ("article".equals(category.getModuleType())) {
						Page<Article> page = new Page<>(pageNo, pageSize);
						Article searchArticle= new Article(category);
						searchArticle.setPage(page);
						page = articleService.findPage(searchArticle);
						model.addAttribute("page", page);
					}
				}

				// 将数据信息传递到视图
				model.addAttribute("category", category);
				CmsUtils.addViewConfigAttribute(model, category);
				String view = Category.DEFAULT_TEMPLATE;
				if (StringUtils.isNotBlank(category.getCustomListView())) {
					view = category.getCustomListView();
				}
				return "modules/cmsfront/themes/" + site.getTheme() + "/" + view;
			}

			// 当前栏目展现方式为：0：默认时，有子栏目：显示子栏目列表
			else {
				model.addAttribute("category", category);
				CmsUtils.addViewConfigAttribute(model, category);
				String view = Category.DEFAULT_TEMPLATE + "Category";
				if (StringUtils.isNotBlank(category.getCustomListView())) {
					view = category.getCustomListView();
				}
				return "modules/cmsfront/themes/" + site.getTheme() + "/" + view;
			}
		}
	}

	/**
	 * 内容列表（通过url自定义视图）
	 */
	@RequestMapping(value = { "list-{categoryCode}-{customView}", "listc-{categoryCode}-{customView}.html" })
	public String listCustom(@PathVariable String categoryCode, @PathVariable String customView,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "30") Integer pageSize, Model model,
			HttpServletRequest request) {

		// 获取栏目信息
		Category category = CmsUtils.getCategory(categoryCode);
		if (category == null || !Category.STATUS_NORMAL.equals(category.getStatus())) {
			Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			model.addAttribute("site", site);
			return "error/404";
		}

		// 获取站点信息
		Site site = CmsUtils.getSite(category.getSite().getId());
		model.addAttribute("site", site);

		// 将数据信息传递到视图
		model.addAttribute("category", category);
		CmsUtils.addViewConfigAttribute(model, category);
		return "modules/cmsfront/themes/" + site.getTheme() + "/" + Category.DEFAULT_TEMPLATE + customView;
	}

}

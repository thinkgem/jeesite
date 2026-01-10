/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cmsfront.web;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 网站首页 Controller
 * @author ThinkGem、长春八哥、一往无前
 * @version 2026-01-10
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontIndexController extends BaseController {

	/**
	 * 主站首页
	 */
	@RequestMapping(value = { "", "index", "index.html" })
	public String index(Model model) {
		return REDIRECT + frontPath + "/index-" + Site.MAIN_SITE_CODE + ".html";
	}

	/**
	 * 站点首页
	 */
	@RequestMapping(value = { "index-{siteCode}", "index-{siteCode}.html" })
	public String index(@PathVariable String siteCode, Model model) {
		// 如果是主站，获取主站信息并进入主页
		if (Site.isMainSite(siteCode)) {
			Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			model.addAttribute("site", site);
			model.addAttribute("isIndex", true);
			return "modules/cmsfront/themes/" + site.getTheme() + "/index";
		}

		// 不是主站，则获取子站点信息
		Site site = CmsUtils.getSite(siteCode);
		model.addAttribute("site", site);

		// 子站有独立页面，则显示独立页面
		if (StringUtils.isNotBlank(site.getCustomIndexView())) {
			model.addAttribute("isIndex", true);
			return "modules/cmsfront/themes/" + site.getTheme() + "/" + site.getCustomIndexView();
		}

		// 否则显示子站第一个栏目
		List<Category> mainNavList = CmsUtils.getMainNavList(siteCode);
		if (!mainNavList.isEmpty()) {
			String firstCategoryCode = CmsUtils.getMainNavList(siteCode).get(0).getId();
			return REDIRECT + frontPath + "/list-" + firstCategoryCode + ".html";
		}

		// 站点中无栏目，则显示栏目分类空白页
		else {
			return "modules/cmsfront/themes/" + site.getTheme() + "/" + Category.DEFAULT_TEMPLATE + "Category";
		}
	}

	/**
	 * 站点地图
	 */
	@RequestMapping(value = { "map-{siteCode}", "map-{siteCode}.html" })
	public String map(@PathVariable String siteCode, Model model) {
		Site site = CmsUtils.getSite(siteCode);
		model.addAttribute("site", site);
		return "modules/cmsfront/themes/" + site.getTheme() + "/map";
	}

}

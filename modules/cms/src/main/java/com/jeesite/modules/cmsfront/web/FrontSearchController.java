/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cmsfront.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.utils.CmsUtils;

/**
 * 网站搜索Controller
 * @author ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${frontPath}/search")
public class FrontSearchController extends BaseController{
	
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 全站搜索
	 * @param t 搜索类型(article、guestbook)
	 * @param q 搜索关键字
	 * @param qand 包含以下全部的关键词
	 * @param qnot 不包含以下关键词
	 * @param bd 最后更新日期范围开始
	 * @param ed 最后更新日期范围结束
	 */
	@RequestMapping(value = "")
	public String search(String t, String q, String qand, String qnot, String bd, String ed, 
			String siteCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		long start = System.currentTimeMillis();
		Site site = CmsUtils.getSite(siteCode);
		model.addAttribute("site", site);

		// 执行检索（搜索词长度必须大于或等于两个字符）
		if (q != null && q.length() >= 2){
			
			// 文章检索
			if (StringUtils.isBlank(t) || "article".equals(t)){
				Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
				Map<String, String> parmas = MapUtils.newHashMap();
				if (StringUtils.isNotBlank(siteCode)){
					parmas.put("siteCode", siteCode);
				}
				page = articleService.searchPage(page, q, qand, qnot, bd, ed, parmas);
				page.setPageInfo("匹配结果，共耗时 " + TimeUtils.formatDateAgo(System.currentTimeMillis() - start) + "。");
				model.addAttribute("page", page);
			}
			
		}
		
		model.addAttribute("t", t);			// 搜索类型
		model.addAttribute("q", q);			// 搜索关键字
		model.addAttribute("qand", qand);	// 包含以下全部的关键词
		model.addAttribute("qnot", qnot);	// 不包含以下关键词
		return "modules/cmsfront/themes/"+site.getTheme()+"/search";
	}
	
}

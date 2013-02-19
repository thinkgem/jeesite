/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 文章Controller
 * @author ThinkGem
 * @version 2013-01-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/cms/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;
	
	@ModelAttribute
	public Article get(@RequestParam(required=false) Long id) {
		if (id != null){
			return articleService.get(id);
		}else{
			return new Article();
		}
	}
	
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = {"list", ""})
	public String list(Article article, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin() && !SecurityUtils.getSubject().isPermitted("cms:article:audit")){
			article.setUser(user);
		}
        Page<Article> page = articleService.find(new Page<Article>(request, response), article); 
        model.addAttribute("page", page);
		return "modules/cms/articleList";
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(Article article, Model model) {
		model.addAttribute("article", article);
		return "modules/cms/articleForm";
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "save")
	public String save(Article article, RedirectAttributes redirectAttributes) {
		if (beanValidators(redirectAttributes, article)){
			if (article.getArticleData().getContent()!=null){
				article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
						article.getArticleData().getContent()));
			}
			// 如果没有审核权限，则将当前内容改为待审核状态
			if (!SecurityUtils.getSubject().isPermitted("cms:article:audit")){
				article.setStatus(Article.STATUS_AUDIT);
			}
			articleService.save(article);
			addFlashMessage(redirectAttributes, "保存文章'" + StringUtils.abbreviate(article.getTitle(),20) + "'成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/article/?repage&category.id="+article.getCategory().getId();
	}
	
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, Long categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		articleService.delete(id, isRe);
		addFlashMessage(redirectAttributes, (isRe!=null&&isRe?"恢复":"")+"删除文章成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/article/?repage&category.id="+categoryId;
	}

	/**
	 * 文章选择列表
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "selectList")
	public String selectList(Article article, Model model) {
        list(article, model);
		return "modules/cms/articleSelectList";
	}
	
	/**
	 * 通过编号获取文章标题
	 */
	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids, Model model) {
		List<Object[]> list = articleService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}
}

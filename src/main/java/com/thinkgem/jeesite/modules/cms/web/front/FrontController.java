/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web.front;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Comment;
import com.thinkgem.jeesite.modules.cms.entity.Link;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.CommentService;
import com.thinkgem.jeesite.modules.cms.service.LinkService;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;

/**
 * 网站Controller
 * @author ThinkGem
 * @version 2013-01-15
 */
@Controller
@RequestMapping(value = BaseController.FRONT_PATH)
public class FrontController extends BaseController{
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(value = "")
	public String index(Model model) {
		return index(1L, model);
	}
	
	/**
	 * 首页
	 */
	@RequestMapping(value = "index-{siteId}" + URL_SUFFIX)
	public String index(@PathVariable Long siteId, Model model) {
		Site site = CmsUtils.getSite(siteId!=null?siteId:1L);
		model.addAttribute("site", site);
		return "modules/cms/front/themes/"+site.getTheme()+"/frontIndex";
	}
	
	/**
	 * 内容列表
	 */
	@RequestMapping(value = "list-{categoryId}" + URL_SUFFIX)
	public String list(@PathVariable Long categoryId, @RequestParam(required=false, defaultValue="1") Integer pageNo, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null){
			Site site = CmsUtils.getSite(1L);
			model.addAttribute("site", site);
			return "error/404";
		}
		model.addAttribute("site", category.getSite());
		// 2：栏目第一条内容
		if("2".equals(category.getShowModes()) && "article".equals(category.getModule())){
			Page<Article> page = new Page<Article>(1, 1, -1);
			Article article = new Article(category);
			page = articleService.find(page, article);
			if (page.getList().size()>0){
				article = page.getList().get(0);
				articleService.updateHitsAddOne(article.getId());
			}
			List<Category> categoryList = categoryService.findByParentId(
					article.getCategory().getParent().getId(), category.getSite().getId());
			model.addAttribute("category", article.getCategory());
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("article", article);
			return "modules/cms/front/themes/"+category.getSite().getTheme()+"/frontViewArticle";
		}else{
			List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
			// 展现方式为1 、无子栏目或公共模型，显示栏目内容列表
			if("1".equals(category.getShowModes())||categoryList.size()==0){
				// 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
				if(categoryList.size()>0){
					category = categoryList.get(0);
				}else{
					categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
				}
				// 获取内容列表
				if ("article".equals(category.getModule())){
					Page<Article> page = new Page<Article>(pageNo, 30);
					page = articleService.find(page, new Article(category));
					model.addAttribute("page", page);
				}else if ("link".equals(category.getModule())){
					Page<Link> page = new Page<Link>(1, -1);
					page = linkService.find(page, new Link(category));
					model.addAttribute("page", page);
				}
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				return "modules/cms/front/themes/"+category.getSite().getTheme()+"/frontList";
			}
			// 有子栏目：显示子栏目列表
			else{
				@SuppressWarnings("rawtypes")
				Map<Category, List> categoryMap = Maps.newLinkedHashMap();
				for (Category c : categoryList){
					if (Category.SHOW.equals(c.getInList())){
						if ("article".equals(c.getModule())){
							categoryMap.put(c, articleService.find(new Page<Article>(1, 5, -1),
									new Article(c)).getList());
						}else if ("link".equals(c.getModule())){
							categoryMap.put(c, linkService.find(new Page<Link>(1, 5, -1),
									new Link(c)).getList());
						}
					}
				}
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				model.addAttribute("categoryMap", categoryMap);
				return "modules/cms/front/themes/"+category.getSite().getTheme()+"/frontListCategory";
			}
		}
	}

	/**
	 * 显示内容
	 */
	@RequestMapping(value = "view-{categoryId}-{contentId}" + URL_SUFFIX)
	public String view(@PathVariable Long categoryId, @PathVariable Long contentId, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null){
			Site site = CmsUtils.getSite(1L);
			model.addAttribute("site", site);
			return "error/404";
		}
		model.addAttribute("site", category.getSite());
		if ("article".equals(category.getModule())){
			Article article = articleService.get(contentId);
			if (article==null || !Article.STATUS_RELEASE.equals(article.getStatus())){
				return "error/404";
			}
			model.addAttribute("article", article);
			model.addAttribute("category", article.getCategory());
			// 文章阅读次数+1
			articleService.updateHitsAddOne(contentId);
			// 分类列表
			List<Category> categoryList = categoryService.findByParentId(
					article.getCategory().getParent().getId(), category.getSite().getId());
			model.addAttribute("categoryList", categoryList);
			// 获取推荐文章列表
			List<Object[]> relationList = articleService.findByIds(article.getArticleData().getRelation());
			model.addAttribute("relationList", relationList); 
			return "modules/cms/front/themes/"+category.getSite().getTheme()+"/frontViewArticle";
		}
		return null;
	}
	
	/**
	 * 内容评论
	 */
	@RequestMapping(value = "comment", method=RequestMethod.GET)
	public String comment(String theme, Comment comment, Model model) {
		Page<Comment> page = new Page<Comment>(request, response);
		Comment c = new Comment();
		c.setModule(comment.getModule());
		c.setContentId(comment.getContentId());
		c.setStatus(Comment.STATUS_RELEASE);
		page = commentService.find(page, c);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "modules/cms/front/themes/"+theme+"/frontComment";
	}
	
	/**
	 * 内容评论保存
	 */
	@ResponseBody
	@RequestMapping(value = "comment", method=RequestMethod.POST)
	public String commentSave(Comment comment, String validateCode,@RequestParam(required=false) Long replyId,
			Model model, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotBlank(validateCode)){
			if (ValidateCodeServlet.validate(request, validateCode)){
				if (replyId!=null && replyId!=0){
					Comment replyComment = commentService.get(replyId);
					if (replyComment != null){
						comment.setContent("<div class=\"reply\">"+replyComment.getName()+":<br/>"
								+replyComment.getContent()+"</div>"+comment.getContent());
					}
				}
				comment.setIp(request.getRemoteAddr());
				comment.setCreateDate(new Date());
				comment.setStatus(Comment.STATUS_AUDIT);
				commentService.save(comment);
				return "{result:1, message:'提交成功，请等待管理员审核。'}";
			}else{
				return "{result:2, message:'验证码不正确。'}";
			}
		}else{
			return "{result:2, message:'验证码不能为空。'}";
		}
	}
	
	/**
	 * 站点地图
	 */
	@RequestMapping(value = "map-{siteId}" + URL_SUFFIX)
	public String map(@PathVariable Long siteId, Model model) {
		Site site = CmsUtils.getSite(siteId!=null?siteId:1L);
		model.addAttribute("site", site);
		return "modules/cms/front/themes/"+site.getTheme()+"/frontMap";
	}
	
	/**
	 * 全站搜索
	 */
	@RequestMapping(value = "search")
	public String search(String t, String q, Model model) {
		Site site = CmsUtils.getSite(1L);
		model.addAttribute("site", site);
		if (StringUtils.isBlank(t) || "article".equals(t)){
			if ("cmd:reindex".equals(q)){
				articleService.createIndex();
				model.addAttribute("message", "重建索引成功");
			}
			Page<Article> page = articleService.search(new Page<Article>(request, response, 10), q);
			model.addAttribute("page", page);
		}
		model.addAttribute("t", t);// 搜索类型
		model.addAttribute("q", q);// 搜索关键字
		return "modules/cms/front/themes/"+site.getTheme()+"/frontSearch";
	}
	
}

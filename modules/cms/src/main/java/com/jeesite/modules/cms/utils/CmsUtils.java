/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.ui.Model;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.SiteService;

/**
 * CmsUtils
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
public class CmsUtils {

	private static final class Static {
		private static SiteService siteService = SpringUtils.getBean(SiteService.class);
		private static CategoryService categoryService = SpringUtils.getBean(CategoryService.class);
		private static ArticleService articleService = SpringUtils.getBean(ArticleService.class);
		private static ServletContext context = SpringUtils.getBean(ServletContext.class);
	}

	private static final String CMS_CACHE = "cmsCache";

	/**
	 * 获得当前站点信息
	 * @param siteCode 站点编号
	 */
	public static Site getCurrentSite() {
		return getSite(Site.getCurrentSiteCode());
	}
	
	/**
	 * 获得站点信息
	 * @param siteCode 站点编号
	 */
	public static Site getSite(String siteCode) {
		String code = Site.MAIN_SITE_CODE;
		if (StringUtils.isNotBlank(siteCode)) {
			code = siteCode;
		}
		// 根据编码获取站点
		for (Site site : getSiteList()) {
			if (site.getSiteCode().equals(code)) {
				return site;
			}
		}
		return new Site(code);
	}

	/**
	 * 获得站点列表
	 */
	public static List<Site> getSiteList() {
		@SuppressWarnings("unchecked")
		List<Site> siteList = (List<Site>) getCache("siteList");
		if (siteList == null) {
			siteList = Static.siteService.findList(new Site());
			putCache("siteList", siteList);
		}
		return siteList;
	}

	/**
	 * 获得主导航列表
	 * @param siteCode 站点编号
	 */
	public static List<Category> getMainNavList(String siteCode) {
		@SuppressWarnings("unchecked")
		List<Category> mainNavList = (List<Category>) getCache("mainNavList_" + siteCode);
		if (mainNavList == null) {
			Category category = new Category();
			category.setSite(new Site(siteCode));
			category.setParent(new Category(Category.ROOT_CODE));
			category.setInMenu(Global.SHOW);
			mainNavList = Static.categoryService.findList(category);
			putCache("mainNavList_" + siteCode, mainNavList);
		}
		return mainNavList;
	}

	/**
	 * 获取栏目
	 * @param categoryCode 栏目编号
	 * @return
	 */
	public static Category getCategory(String categoryCode) {
		return Static.categoryService.get(categoryCode);
	}

	/**
	 * 获得栏目列表
	 * @param siteCode 站点编号
	 * @param parentCode 分类父编号
	 * @param number 获取数目
	 * @param params 预留参数，例： key1:'value1', key2:'value2' ...<br>
	 *        sortGrades : 获取的层次级别（从0开始，例如：sortGrades: \"0,1,2\" 则只获取0级1级2级的栏目）<br>
	 *        isChildList : 是否后续进行childList转换（1：进行获取childList列表）<br>
	 *        调用举例（Beetl）：<br>
	 *        1、获取前10条一级栏目：${categoryList(site.siteCode, '0', 10)}<br>
	 *        2、获取前200条一级和二级栏目，并对栏目进行子节点（childList）获取（注意：获取多级栏目的时候，这个200条数是全部的栏目条数包括所有子级个数）：
	 *          ${categoryList(site.siteCode, '0', 200, 'sortGrades:\"0,1\", isChildList:1')}<br>
	 *        3、获取二级栏目下的两级栏目： ${categoryList(site.siteCode, 'xcl', 200, 'sortGrades:\"1,2\"')}
	 */
	public static List<Category> getCategoryList(String siteCode, String parentCode, int number, String params) {
		if (StringUtils.isBlank(siteCode) || StringUtils.isBlank(parentCode)) {
			return ListUtils.newArrayList();
		}
		Page<Category> page = new Page<Category>(1, number, -1);
		Category category = new Category();
		category.setSite(new Site(siteCode));
		category.setParentCode(parentCode);
		Boolean isChildList = false; // 是否进行childList转换
		if (StringUtils.isNotBlank(params)) {
			@SuppressWarnings({ "rawtypes" })
			Map map = JsonMapper.fromJson("{" + params.trim() + "}", Map.class);

			// 获取的层级级别
			String sortGrades = ObjectUtils.toString(map.get("sortGrades"));
			if (StringUtils.isNotBlank(sortGrades)) {

				// 如果设置了级别，则清理ParentCode，并使用ParentCodes进行查询
				category.setParentCode(null);

				// 如果是跟节点则不加入条件，代表查询全部，不是跟节点的时候获取指定节点的所有下级
				if (!Category.ROOT_CODE.equals(parentCode)) {
					category.setParentCodes("%," + parentCode + ",%");
				}

				// 增加获取层次级别条件
				List<Integer> sortGradeList = ListUtils.newArrayList();
				for (String s : StringUtils.split(sortGrades, ",")) {
					sortGradeList.add(ObjectUtils.toInteger(s));
				}
				category.setSortGradeList(sortGradeList);
			}
			// 是否进行childList转换
			isChildList = ObjectUtils.toBoolean(map.get("isChildList"));
		}
		category.setPage(page);
		page = Static.categoryService.findPage(category);
		// 进行childList转换
		if (isChildList) {
			List<Category> sourceList = page.getList();
			List<Category> targetList = Static.categoryService.convertTreeList(sourceList, parentCode);
			page.setList(targetList);
		}
		return page.getList();
	}
	
	/**
	 * 获取文章
	 * @param articleId 文章编号
	 */
	public static Article getArticle(String articleId) {
		return Static.articleService.get(articleId);
	}

	/**
	 * 获取文章获取文章并点击数加一
	 */
	public static Article getArticleAndHitsAddOne(String articleId) {
		Article article = Static.articleService.get(articleId);
		Static.articleService.updateHitsAddOne(articleId);
		return article;
	}

	/**
	 * 获取文章列表
	 * @param siteCode 站点编号
	 * @param categoryCode 分类编号
	 * @param number 获取数目
	 * @param param 预留参数，例： key1:'value1', key2:'value2' ...<br>
	 *        posid : 推荐位（1：首页焦点图；2：栏目页文章推荐；）<br>
	 *        image : 文章图片（1：有图片的文章）<br>
	 *        isQueryArticleData : 是否查询文章详情信息，查询会影响效率<br>
	 *        orderBy : 排序字符串（字符串类型的需要加引号如：orderBy: \"hits desc\" ）<br>
	 *        调用举例（Beetl）：<br>
	 *        1、查询当前栏目下的前10篇文章： ${articleList(category.site.siteCode, category.categoryCode, 10, '')}<br>
	 *        2、查询当前栏目下的前10篇文章，并且是栏目页推荐文章，并按照访问次数排序：
	 *          ${articleList(category.site.siteCode, category.categoryCode, 10, 'posid:2, orderBy: \"hits desc\"')}<br>
	 *        3、查询当前栏目下的前10篇文章，并且是有图片的文章： ${articleList(category.site.siteCode, category.categoryCode, 10, 'image:1')}
	 */
	public static List<Article> getArticleList(String siteCode, String categoryCode, int number, String params) {
		if (StringUtils.isBlank(siteCode) || StringUtils.isBlank(categoryCode)) {
			return ListUtils.newArrayList();
		}
		Category category = new Category();
		category.setSite(new Site(siteCode));
		Page<Article> page = new Page<Article>(1, number, -1);
		if (!Category.ROOT_CODE.equals(categoryCode)) {
			category.setCategoryCode(categoryCode);
			category.setParentCodes(categoryCode);
		}
		Article article = new Article(category);
		if (StringUtils.isNotBlank(params)) {
			@SuppressWarnings({ "rawtypes" })
			Map map = JsonMapper.fromJson("{" + params.trim() + "}", Map.class);
			// 推荐位
			//String postid = ObjectUtils.toString(map.get("posid"));
			// 文章图片
			if (ObjectUtils.toBoolean(map.get("image"))) {
				article.setImage(Global.YES);
			}
			// 是否查询文章详情信息，查询会影响效率
			if (ObjectUtils.toBoolean(map.get("isQueryArticleData"))) {
				article.setIsQueryArticleData(true);
			}
			// 排序字符串
			String orderBy = ObjectUtils.toString(map.get("orderBy"));
			if (StringUtils.isNotBlank(orderBy)) {
				page.setOrderBy(orderBy);
			}
		}
		article.setStatus(Article.STATUS_NORMAL);
		article.setPage(page);
		page = Static.articleService.findPage(article);
		return page.getList();
	}

	public static <V> V getCache(String key) {
		return CacheUtils.get(CMS_CACHE, key);
	}

	public static <V> V getCache(String key, V defaultValue) {
		V value = CacheUtils.get(CMS_CACHE, key);
		return value != null ? value : defaultValue;
	}

	public static void putCache(String key, Object value) {
		CacheUtils.put(CMS_CACHE, key, value);
	}

	public static void removeCache(String key) {
		CacheUtils.remove(CMS_CACHE, key);
	}

	/**
	 * 获得文章动态URL地址
	 * @param article
	 * @return url
	 */
	public static String getUrlDynamic(Article article) {
		StringBuilder str = new StringBuilder();
		str.append(Static.context.getContextPath());
		if (StringUtils.isNotBlank(article.getHref())) {
			if (article.getHref().contains("://")) {
				return article.getHref();
			} else {
				str.append(article.getHref());
				return str.toString();
			}
		}
		str.append(Global.getFrontPath());
		str.append("/view-").append(article.getCategory().getCategoryCode());
		str.append("-").append(article.getId()).append(".html");
		return str.toString();
	}

	/**
	 * 获得栏目动态URL地址
	 * @param category
	 * @return url
	 */
	public static String getUrlDynamic(Category category) {
		StringBuilder str = new StringBuilder();
		str.append(Static.context.getContextPath()).append(Global.getFrontPath());
		if (StringUtils.isNotBlank(category.getHref())) {
			if (category.getHref().contains("://")) {
				return category.getHref();
			} else {
				str.append(category.getHref());
				return str.toString();
			}
		}
		str.append("/list-").append(category.getCategoryCode()).append(".html");
		return str.toString();
	}

	/**
	 * 获得栏目动态URL地址
	 * @param category
	 * @return url
	 */
	public static String getAdminUrlDynamic(Category category) {
		StringBuilder str = new StringBuilder();
		str.append(Static.context.getContextPath()).append(Global.getAdminPath());
		String adminUrlParam = null; // 管理地址的参数
		// 如果试图配置里配置了管理路径，则使用视图中的管理路径
		if (StringUtils.isNotBlank(category.getViewConfig())) {
			@SuppressWarnings("rawtypes")
			Map map = JsonMapper.fromJson("{" + category.getViewConfig().trim() + "}", Map.class);
			if (map != null) {
				// 管理地址
				String adminUrl = ObjectUtils.toString(map.get("adminUrl"));
				// 如果给管理地址设置了参数，则加入参数
				adminUrlParam = ObjectUtils.toString(map.get("adminUrlParam"));
				// 如果管理路径为false，则表示没有管理URL，则直接返回null
				if (Global.FALSE.equals(adminUrl)) {
					return "none";
				}
				// 如果设置了管理地址，则返回管理路径地址
				if (StringUtils.isNotBlank(adminUrl)) {
					// 外部地址，则直接返回
					if (adminUrl.contains("://")) {
						return adminUrl;
					}
					// 内部地址：contextPath + adminPath + adminUrlParam
					else {
						str.append(adminUrl);
						// 加入管理地址参数
						if (StringUtils.isNotBlank(adminUrlParam)) {
							if (str.toString().contains("?")) {
								str.append("&");
							} else {
								str.append("?");
							}
							str.append(adminUrlParam);
						}
						return str.toString();
					}
				}
			}
		}
		// 如果设置了外部链接，则返回外部链接地址
		if (StringUtils.isNotBlank(category.getHref())) {
			// 外部地址，则直接返回
			if (category.getHref().contains("://")) {
				return category.getHref();
			}
			// 内部地址：contextPath + adminPath + /cms + href
			else {
				str.append("/cms").append(category.getHref());
				return str.toString();
			}
		}
		// 如果为公共栏目，则返回空，无管理地址
		if (StringUtils.isBlank(category.getModuleType())) {
			return StringUtils.EMPTY;
		}
		// 返回默认的栏目管理地址：contextPath + adminPath + /cms/ + module + /list
		str.append("/cms/").append(category.getModuleType());
		str.append("/list?category.categoryCode=").append(category.getCategoryCode());
		// 加入管理地址参数
		if (StringUtils.isNotBlank(adminUrlParam)) {
			str.append("&").append(adminUrlParam);
		}
		// 默认使用大纲视图的站点（栏目的站点不为空，必须包含在配置中，已配置的地址中未配置outlineView时生效）
		String outlineViewOfSiteCodes = Global.getConfig("cms.article.outlineViewOfSiteCodes");
		if (StringUtils.isNotBlank(outlineViewOfSiteCodes) && !"0".equals(outlineViewOfSiteCodes)) {
			if (category.getSite() != null && StringUtils.isNotBlank(category.getSite().getSiteCode())) {
				if (StringUtils.inString(category.getSite().getSiteCode(), StringUtils.split(outlineViewOfSiteCodes, ","))) {
					if (!StringUtils.contains(str.toString(), "&outlineView=")) {
						str.append("&outlineView=true");
					}
				}
			}
		}
		return str.toString();
	}

	/**
	 * 从图片地址中去除ContextPath地址
	 * @param src
	 * @return
	 */
	public static String formatImageSrcToDb(String src) {
		if (StringUtils.isBlank(src)) {
			return src;
		}
		if (src.startsWith(Static.context.getContextPath() + "/userfiles")) {
			return src.substring(Static.context.getContextPath().length());
		} else {
			return src;
		}
	}

	/**
	 * 从图片地址中加入ContextPath地址
	 * @param src
	 * @return
	 */
	public static String formatImageSrcToWeb(String src) {
		if (StringUtils.isBlank(src)) {
			return src;
		}
		if (src.startsWith(Static.context.getContextPath() + "/userfiles")) {
			return src;
		} else {
			return Static.context.getContextPath() + src;
		}
	}

	/**
	 * 获取文章视图
	 * @param article
	 * @return
	 */
	public static String getArticleView(Article article) {
		if (StringUtils.isBlank(article.getCustomContentView())) {
			String view = null;
			Category c = article.getCategory();
			while (c != null) {
				if (StringUtils.isNotBlank(c.getCustomContentView())) {
					view = c.getCustomContentView();
					break;
				} else if (!c.getIsRoot()) {
					c = getCategory(c.getParentCode());
					if (c == null) {
						break;
					}
				} else {
					break;
				}
			}
			return StringUtils.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
		} else {
			return article.getCustomContentView();
		}
	}

	/**
	 * 视图配置属性设置
	 * @param model
	 * @param params
	 */
	public static void addViewConfigAttribute(Model model, String params) {
		if (StringUtils.isNotBlank(params)) {
			@SuppressWarnings("rawtypes")
			Map map = JsonMapper.fromJson("{" + params.trim() + "}", Map.class);
			if (map != null) {
				for (Object o : map.keySet()) {
					model.addAttribute("viewConfig_" + o.toString(), map.get(o));
				}
			}
		}
	}

	/**
	 * 视图配置属性设置
	 * @param model
	 * @param category
	 */
	public static void addViewConfigAttribute(Model model, Category category) {
		List<Category> categoryList = ListUtils.newArrayList();
		Category c = category;
		while (c != null) {
			categoryList.add(c);
			if (!c.getIsRoot()) {
				c = getCategory(c.getParentCode());
				if (c == null) {
					break;
				}
			} else {
				break;
			}
		}
		Collections.reverse(categoryList); // 顺序逆反，子集高优先级。
		for (Category ca : categoryList) {
			addViewConfigAttribute(model, ca.getViewConfig());
		}
	}

}
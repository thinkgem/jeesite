/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.FileTempleteService;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.utils.DictUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 栏目表Controller
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/category")
public class CategoryController extends BaseController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FileTempleteService fileTempleteService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Category get(String categoryCode, boolean isNewRecord) {
		return categoryService.get(categoryCode, isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = { "list", "" })
	public String list(Category category, Model model) {
		if (StringUtils.isBlank(category.getSite().getSiteCode())) {
			category.setSite(new Site(Site.getCurrentSiteCode()));
		}
		category.setSite(CmsUtils.getSite(category.getSite().getSiteCode()));
		model.addAttribute("category", category);
		return "modules/cms/categoryList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Category> listData(Category category) {
		if (StringUtils.isBlank(category.getParentCode())) {
			category.setParentCode(Office.ROOT_CODE);
		}
		if (StringUtils.isBlank(category.getSite().getSiteCode())) {
			category.setSite(new Site(Site.getCurrentSiteCode()));
		}
		List<Category> list = null;
		if (StringUtils.isNotBlank(category.getCategoryCode())) {
			list = ListUtils.newArrayList(get(category.getCategoryCode(), false));
		} else {
			list = categoryService.findList(category);
		}
		return list;
	}

	/**
	 * 查看编辑表单
	 * @throws IOException
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "form")
	public String form(Category category, Model model) throws IOException {
		// 创建并初始化下一个节点信息
		category = createNextNode(category);
		if (category.getParent() != null && StringUtils.isNotBlank(category.getParent().getId())) {
			category.setParent(categoryService.get(category.getParent().getCategoryCode()));
			if (category.getIsNewRecord()) {
				Category categoryChild = new Category();
				categoryChild.setParent(new Category(category.getParentCode()));
				List<Category> list = categoryService.findList(category);
				if (list.size() > 0) {
					category.setTreeSort(list.get(list.size() - 1).getTreeSort());
					if (category.getTreeSort() != null) {
						category.setTreeSort(category.getTreeSort() + 30);
					}
				}
			}
			if (category.getParent() != null) {
				// 继承上级的模块类型
				if (StringUtils.isBlank(category.getModuleType())) {
					category.setModuleType(category.getParent().getModuleType());
				}
				// 继承上级的视图配置参数
				if (StringUtils.isBlank(category.getViewConfig())) {
					category.setViewConfig(category.getParent().getViewConfig());
				}
			}
		}
		if (category.getModuleType() == null) {
			category.setModuleType("article");
		}
		if (category.getInMenu() == null) {
			category.setInMenu(Global.HIDE);
		}
		if (category.getInList() == null) {
			category.setInList(Global.SHOW);
		}
		if (category.getShowModes() == null) {
			category.setShowModes(Category.SHOW_MODES_AUTO);
		}
		if (category.getIsCanComment() == null) {
			category.setIsCanComment(Global.NO);
		}
		if (category.getIsNeedAudit() == null) {
			category.setIsNeedAudit(Global.NO);
		}
		model.addAttribute("listViewList", fileTempleteService.getTempleteContentDict(Category.DEFAULT_TEMPLATE));
		model.addAttribute("category_DEFAULT_TEMPLATE", Category.DEFAULT_TEMPLATE);
		model.addAttribute("contentViewList", fileTempleteService.getTempleteContentDict(Article.DEFAULT_TEMPLATE));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("category", category);
		return "modules/cms/categoryForm";
	}

	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public Category createNextNode(Category category) {
		if (StringUtils.isNotBlank(category.getParentCode())) {
			category.setParent(categoryService.get(category.getParentCode()));
		}
		if (category.getIsNewRecord()) {
			Category where = new Category();
			where.setParentCode(category.getParentCode());
			Category last = categoryService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null) {
				category.setTreeSort(last.getTreeSort() + 30);
				category.setCategoryCode(IdGen.nextCode(last.getCategoryCode()));
			} else if (category.getParent() != null) {
				category.setCategoryCode(category.getParent().getCategoryCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (category.getTreeSort() == null) {
			category.setTreeSort(Category.DEFAULT_TREE_SORT);
		}
		return category;
	}

	/**
	 * 保存栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Category category) {
		// 如果保存的时候，没有上一级，或者是根级别，给一个默认的站点
		if (Category.ROOT_CODE.equals(category.getParentCode())) {
			category.setSite(new Site(Site.getCurrentSiteCode()));
		}
		// 如果存在上级则保存时，归属站点与上一级同步
		else if (StringUtils.isNotBlank(category.getParent().getId())) {
			category.setSite(categoryService.get(category.getParent()).getSite());
		}
		// 同步到ID
		if (category.getParent() != null) {
			category.getParent().setId(category.getParent().getCategoryCode());
		}
		categoryService.save(category);
		return renderResult(Global.TRUE, text("保存栏目表成功！"));
	}

	/**
	 * 停用栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Category category) {
		Category where = new Category();
		where.setStatus(Category.STATUS_NORMAL);
		where.setParentCodes("," + category.getId() + ",");
		long count = categoryService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该栏目表包含未停用的子栏目表！"));
		}
		category.setStatus(Category.STATUS_DISABLE);
		categoryService.updateStatus(category);
		return renderResult(Global.TRUE, text("停用栏目表成功"));
	}

	/**
	 * 启用栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Category category) {
		category.setStatus(Category.STATUS_NORMAL);
		categoryService.updateStatus(category);
		return renderResult(Global.TRUE, text("启用栏目表成功"));
	}

	/**
	 * 删除栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Category category) {
		categoryService.delete(category);
		return renderResult(Global.TRUE, text("删除栏目表成功！"));
	}

	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String siteCode, String module, String excludeCode, Boolean isAll, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<Category> list = null;
		Category category = new Category();
		// 站点条件
		if (StringUtils.isNotBlank(siteCode)) {
			category.setSite(new Site(siteCode));
		} else {
			category.setSite(new Site(Site.getCurrentSiteCode()));
		}
		// 栏目模型条件
		if (StringUtils.isNotBlank(module)) {
			category.setModuleType(module);
		}
		// 是否查询全部，不过滤权限
		if (!(isAll != null && isAll)) {
			categoryService.addDataScopeFilter(category);
		}
		list = categoryService.findList(category);
		for (int i = 0; i < list.size(); i++) {
			Category e = list.get(i);
			// 过滤非正常的数据
			if (!Category.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCategoryCode(), e.getCategoryName()));
			map.put("title", e.getCategoryName() + " [" + DictUtils.getDictLabel(e.getModuleType(), "cms_module", "公共模型") + "]");
			map.put("module", e.getModuleType());
			map.put("showModes", e.getShowModes());
			// 是否仅获取可管理的栏目，指定 true 或 false
			String adminUrl = e.getAdminUrl();
			if (!"none".equals(adminUrl)) {
				map.put("adminUrl", "".equals(adminUrl) ? "none" : adminUrl);
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(Category category) {
		if (!UserUtils.getUser().isAdmin()) {
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		categoryService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}

}
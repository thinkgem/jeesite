/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.jeesite.modules.cms.entity.CmsCategory;
import com.jeesite.modules.cms.entity.CmsSite;
import com.jeesite.modules.cms.service.CmsCategoryService;
import com.jeesite.modules.cms.service.CmsTemplateService;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.sys.utils.DictUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.Logical;
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
import java.util.Map;

/**
 * 栏目表 Controller
 * @author ThinkGem、长春叭哥、一往无前
 * @version 2023-4-10
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/category")
public class CmsCategoryController extends BaseController {

	private final CmsCategoryService categoryService;
	private final CmsTemplateService templateService;

	public CmsCategoryController(CmsCategoryService categoryService, CmsTemplateService templateService) {
		this.categoryService = categoryService;
		this.templateService = templateService;
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute("category")
	public CmsCategory get(String categoryCode, boolean isNewRecord, HttpServletRequest request) {
		if (StringUtils.endsWith(request.getRequestURI(), "listData")) {
			return new CmsCategory();
		}
		CmsCategory category = new CmsCategory();
		category.setCategoryCode(categoryCode);
		category.setIsNewRecord(isNewRecord);
		if (StringUtils.isNotBlank(categoryCode)){
			categoryService.addDataScopeFilter(category);
		}
		return categoryService.getAndValid(category);
	}

	/**
	 * 管理主页
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "index")
	public String index(@ModelAttribute("category") CmsCategory category, Model model) {
		model.addAttribute("currentSite", CmsUtils.getCurrentSite());
		model.addAttribute("siteList", CmsUtils.getSiteList());
		model.addAttribute("category", category);
		return "modules/cms/categoryIndex";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = { "list", "" })
	public String list(@ModelAttribute("category") CmsCategory category, Model model) {
		if (StringUtils.isBlank(category.getSite().getSiteCode())) {
			category.setSite(new CmsSite(CmsSite.getCurrentSiteCode()));
		}
		category.setSite(CmsUtils.getSite(category.getSite().getSiteCode()));
		model.addAttribute("currentSite", CmsUtils.getCurrentSite());
		model.addAttribute("siteList", CmsUtils.getSiteList());
		model.addAttribute("category", category);
		return "modules/cms/categoryList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<CmsCategory> listData(@ModelAttribute("category") CmsCategory category) {
		if (StringUtils.isBlank(category.getParentCode())) {
			category.setParentCode(CmsCategory.ROOT_CODE);
		}
		if (StringUtils.isBlank(category.getSite().getSiteCode())) {
			category.setSite(new CmsSite(CmsSite.getCurrentSiteCode()));
		}
		if (StringUtils.isNotBlank(category.getCategoryCode())
				|| StringUtils.isNotBlank(category.getCategoryCode_like())
				|| StringUtils.isNotBlank(category.getCategoryName())
				|| StringUtils.isNotBlank(category.getRemarks())) {
			category.setParentCode(null);
		}
		List<CmsCategory> list = categoryService.findList(category);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("category") CmsCategory category, Model model) throws IOException {
		// 创建并初始化下一个节点信息
		category = createNextNode(category);
		if (category.getParent() != null && StringUtils.isNotBlank(category.getParent().getId())) {
			category.setParent(CmsUtils.getCategory(category.getParent().getCategoryCode()));
			if (category.getIsNewRecord()) {
				CmsCategory categoryChild = new CmsCategory();
				categoryChild.setParent(new CmsCategory(category.getParentCode()));
				List<CmsCategory> list = categoryService.findList(category);
				if (!list.isEmpty()) {
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
			category.setShowModes(CmsCategory.SHOW_MODES_AUTO);
		}
		if (category.getIsCanComment() == null) {
			category.setIsCanComment(Global.NO);
		}
		if (category.getIsNeedAudit() == null) {
			category.setIsNeedAudit(Global.NO);
		}
		model.addAttribute("category_DEFAULT_TEMPLATE", CmsCategory.DEFAULT_TEMPLATE);
		model.addAttribute("article_DEFAULT_TEMPLATE", CmsArticle.DEFAULT_TEMPLATE);
		model.addAttribute("listViewList", templateService.getTemplateContentDict(CmsCategory.DEFAULT_TEMPLATE));
		model.addAttribute("contentViewList", templateService.getTemplateContentDict(CmsArticle.DEFAULT_TEMPLATE));
		model.addAttribute("currentSite", CmsUtils.getCurrentSite());
		model.addAttribute("siteList", CmsUtils.getSiteList());
		model.addAttribute("category", category);
		return "modules/cms/categoryForm";
	}

	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public CmsCategory createNextNode(@ModelAttribute("category") CmsCategory category) {
		if (StringUtils.isNotBlank(category.getParentCode())) {
			category.setParent(categoryService.get(category.getParentCode()));
		}
		if (category.getIsNewRecord()) {
			CmsCategory where = new CmsCategory();
			where.setParentCode(category.getParentCode());
			CmsCategory last = categoryService.getLastByParentCode(where);
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
			category.setTreeSort(CmsCategory.DEFAULT_TREE_SORT);
		}
		return category;
	}

	/**
	 * 保存栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated @ModelAttribute("category") CmsCategory category) {
		// 归属站点，和上级栏目必须一致（忽略前端传参）只允许根节点更改归属站点
		String parentCode = category.getParentCode();
		if (StringUtils.isNotBlank(parentCode)) {
			CmsCategory parent = categoryService.get(parentCode);
			if (parent != null) {
				category.setSite(parent.getSite());
			}
		}
		// 如果仍没有站点，则设置默认站点
		if (category.getSite() == null) {
			category.setSite(new CmsSite(CmsSite.getCurrentSiteCode()));
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
	public String disable(@ModelAttribute("category") CmsCategory category) {
		CmsCategory where = new CmsCategory();
		where.setStatus(CmsCategory.STATUS_NORMAL);
		where.setParentCodes_rightLike(category.getParentCodes() + category.getId() + ",");
		long count = categoryService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该栏目表包含未停用的子栏目表！"));
		}
		category.setStatus(CmsCategory.STATUS_DISABLE);
		categoryService.updateStatus(category);
		return renderResult(Global.TRUE, text("停用栏目表成功"));
	}

	/**
	 * 启用栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(@ModelAttribute("category") CmsCategory category) {
		category.setStatus(CmsCategory.STATUS_NORMAL);
		categoryService.updateStatus(category);
		return renderResult(Global.TRUE, text("启用栏目表成功"));
	}

	/**
	 * 删除栏目表
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(@ModelAttribute("category") CmsCategory category) {
		categoryService.delete(category);
		return renderResult(Global.TRUE, text("删除栏目表成功！"));
	}

	/**
	 * 重建索引
	 * @author ThinkGem
	 */
	@RequiresPermissions("cms:category:rebuildIndex")
	@ResponseBody
	@RequestMapping(value = "rebuildIndex")
	public String rebuildIndex(@ModelAttribute("category") CmsCategory category)  {
		return renderResult(Global.TRUE, categoryService.rebuildIndex(category));
	}

	/**
	 * 重建向量数据库
	 * @author ThinkGem
	 */
	@RequiresPermissions("cms:category:rebuildVectorStore")
	@ResponseBody
	@RequestMapping(value = "rebuildVectorStore")
	public String rebuildVectorStore(@ModelAttribute("category") CmsCategory category)  {
		return renderResult(Global.TRUE, categoryService.rebuildVectorStore(category));
	}

	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 */
	@RequiresPermissions(value = {"cms:category:view", "cms:article:view"}, logical = Logical.OR)
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String siteCode, String module, String excludeCode, boolean isAll, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		CmsCategory category = new CmsCategory();
		// 站点条件
		if (StringUtils.isNotBlank(siteCode)) {
			category.setSite(new CmsSite(siteCode));
		} else {
			category.setSite(new CmsSite(CmsSite.getCurrentSiteCode()));
		}
		// 栏目模型条件
		if (StringUtils.isNotBlank(module)) {
			category.setModuleType(module);
		}
		// 是否查询全部，不过滤权限
		if (!isAll || Global.isStrictMode()){
			categoryService.addDataScopeFilter(category);
		}
		List<CmsCategory> list = categoryService.findList(category);
		for (CmsCategory e : list) {
			// 过滤非正常的数据
			if (!CmsCategory.STATUS_NORMAL.equals(e.getStatus())) {
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)) {
				if (e.getId().equals(excludeCode)) {
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")) {
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
			// 是否需要审核
			map.put("isNeedAudit", e.getIsNeedAudit());
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("cms:category:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(@ModelAttribute("category") CmsCategory category) {
		if (!category.currentUser().isAdmin()) {
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		categoryService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}

}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.biz.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizCategory;
import com.jeesite.modules.biz.service.BizCategoryService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 业务分类Controller
 * @author ThinkGem
 * @version 2019-08-12
 */
@Controller
@Api(tags = "BizCategory - 业务分类")
@RequestMapping(value = "${adminPath}/biz/bizCategory")
public class BizCategoryController extends BaseController {

	@Autowired
	private BizCategoryService bizCategoryService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizCategory get(String categoryCode, boolean isNewRecord) {
		return bizCategoryService.get(categoryCode, isNewRecord);
	}

	/**
	 * 管理首页
	 */
	@RequiresPermissions("biz:bizCategory:view")
	@RequestMapping(value = "index")
	public String index(BizCategory bizCategory, Model model) {
		model.addAttribute("bizCategory", bizCategory);
		return "modules/biz/bizCategoryIndex";
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizCategory:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizCategory bizCategory, Model model) {
		model.addAttribute("bizCategory", bizCategory);
		return "modules/biz/bizCategoryList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizCategory:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<BizCategory> listData(BizCategory bizCategory) {
		if (StringUtils.isBlank(bizCategory.getParentCode())) {
			bizCategory.setParentCode(BizCategory.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(bizCategory.getViewCode())) {
			bizCategory.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bizCategory.getCategoryName())){
			bizCategory.setParentCode(null);
		}
		if (StringUtils.isNotBlank(bizCategory.getRemarks())){
			bizCategory.setParentCode(null);
		}
		List<BizCategory> list = bizCategoryService.findList(bizCategory);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizCategory:view")
	@RequestMapping(value = "form")
	public String form(BizCategory bizCategory, Model model) {
		// 创建并初始化下一个节点信息
		bizCategory = createNextNode(bizCategory);
		model.addAttribute("bizCategory", bizCategory);
		return "modules/biz/bizCategoryForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("biz:bizCategory:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public BizCategory createNextNode(BizCategory bizCategory) {
		if (StringUtils.isNotBlank(bizCategory.getParentCode())){
			bizCategory.setParent(bizCategoryService.get(bizCategory.getParentCode()));
		}
		if (bizCategory.getIsNewRecord()) {
			BizCategory where = new BizCategory();
			where.setParentCode(bizCategory.getParentCode());
			BizCategory last = bizCategoryService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				bizCategory.setTreeSort(last.getTreeSort() + 30);
				bizCategory.setViewCode(IdGen.nextCode(last.getViewCode()));
			}else if (bizCategory.getParent() != null){
				bizCategory.setViewCode(bizCategory.getParent().getViewCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (bizCategory.getTreeSort() == null){
			bizCategory.setTreeSort(BizCategory.DEFAULT_TREE_SORT);
		}
		return bizCategory;
	}

	/**
	 * 保存业务分类
	 */
	@RequiresPermissions("biz:bizCategory:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizCategory bizCategory) {
		bizCategoryService.save(bizCategory);
		return renderResult(Global.TRUE, text("保存业务分类成功"));
	}
	
	/**
	 * 停用业务分类
	 */
	@RequiresPermissions("biz:bizCategory:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizCategory bizCategory) {
		BizCategory where = new BizCategory();
		where.setStatus(BizCategory.STATUS_NORMAL);
		where.setParentCodes("," + bizCategory.getId() + ",");
		long count = bizCategoryService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该业务分类包含未停用的子业务分类！"));
		}
		bizCategory.setStatus(BizCategory.STATUS_DISABLE);
		bizCategoryService.updateStatus(bizCategory);
		return renderResult(Global.TRUE, text("停用业务分类成功"));
	}
	
	/**
	 * 启用业务分类
	 */
	@RequiresPermissions("biz:bizCategory:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizCategory bizCategory) {
		bizCategory.setStatus(BizCategory.STATUS_NORMAL);
		bizCategoryService.updateStatus(bizCategory);
		return renderResult(Global.TRUE, text("启用业务分类成功"));
	}
	
	/**
	 * 删除业务分类
	 */
	@RequiresPermissions("biz:bizCategory:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizCategory bizCategory) {
		bizCategoryService.delete(bizCategory);
		return renderResult(Global.TRUE, text("删除业务分类成功"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param parentCode 设置父级编码返回一级
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("biz:bizCategory:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		BizCategory where = new BizCategory();
		where.setStatus(BizCategory.STATUS_NORMAL);
		if (StringUtils.isNotBlank(parentCode)){
			where.setParentCode(parentCode);
		}
		List<BizCategory> list = bizCategoryService.findList(where);
		for (int i=0; i<list.size(); i++){
			BizCategory e = list.get(i);
			// 过滤非正常的数据
			if (!BizCategory.STATUS_NORMAL.equals(e.getStatus())){
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
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCategoryCode(), e.getCategoryName()));
			map.put("value", e.getId());
			map.put("isParent", !e.getIsTreeLeaf());
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("biz:bizCategory:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(BizCategory bizCategory){
		if (!bizCategory.currentUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		bizCategoryService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}
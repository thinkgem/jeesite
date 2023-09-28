/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictType;
import com.jeesite.modules.sys.service.DictTypeService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典分类管理Controller
 * @author ThinkGem
 * @version 2019-3-24
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dictType")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
@Hidden
public class DictTypeController extends BaseController {

	@Autowired
	private DictTypeService dictTypeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DictType get(String id, boolean isNewRecord) {
		return dictTypeService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("sys:dictType:view")
	@RequestMapping(value = "list")
	public String list(DictType dictType, Model model) {
		if (!dictType.currentUser().isSuperAdmin()){
			dictType.setIsSys(Global.NO);
		}
		model.addAttribute("dictType", dictType);
		return "modules/sys/dictTypeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("sys:dictType:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DictType> listData(DictType dictType, HttpServletRequest request, HttpServletResponse response) {
		dictType.setPage(new Page<>(request, response));
		Page<DictType> page = dictTypeService.findPage(dictType); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("sys:dictType:view")
	@RequestMapping(value = "form")
	public String form(DictType dictType, Model model) {
		if (StringUtils.isBlank(dictType.getIsSys())){
			dictType.setIsSys(Global.YES);
		}
		model.addAttribute("dictType", dictType);
		return "modules/sys/dictTypeForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("sys:dictType:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DictType dictType, HttpServletRequest request) {
		// 获取老字典类型的isSys状态，如果是系统字典，则必须超级管理员编辑
		DictType old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !dictType.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		dictTypeService.save(dictType, old);
		return renderResult(Global.TRUE, text("保存字典类型成功"));
	}
	
	/**
	 * 验证字段类型是否有效
	 * @return
	 */
	@RequiresPermissions("sys:dictType:edit")
	@RequestMapping(value = "checkDictType")
	@ResponseBody
	public String checkDictType(String oldDictType, String dictType) {
		DictType where = new DictType();
		where.setDictType(dictType);
		if (dictType != null && dictType.equals(oldDictType)) {
			return Global.TRUE;
		} else if (dictType != null && dictTypeService.findCount(where) == 0) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}
	
	/**
	 * 停用字典类型
	 */
	@RequiresPermissions("sys:dictType:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(DictType dictType) {
		dictType.setStatus(DictType.STATUS_DISABLE);
		dictTypeService.updateStatus(dictType);
		return renderResult(Global.TRUE, text("停用字典类型成功"));
	}
	
	/**
	 * 启用字典类型
	 */
	@RequiresPermissions("sys:dictType:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(DictType dictType) {
		dictType.setStatus(DictType.STATUS_NORMAL);
		dictTypeService.updateStatus(dictType);
		return renderResult(Global.TRUE, text("启用字典类型成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("sys:dictType:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DictType dictType, HttpServletRequest request) {
		// 获取老字典类型的isSys状态，如果是系统字典，则必须超级管理员编辑
		DictType old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !dictType.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		dictTypeService.delete(dictType);
		return renderResult(Global.TRUE, text("删除字典类型成功"));
	}

	/**
	 * 获取树结构数据。
	 * @param dictType 字典类型
	 * @param excludeCode 排除的ID
	 * @param isShowCode 是否显示值（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 */
	@RequiresPermissions("sys:dictType:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String dictType, String excludeCode,
			@RequestParam(defaultValue="1") String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<DictType> list = dictTypeService.findList(new DictType());
		for (int i=0; i<list.size(); i++){
			DictType e = list.get(i);
			// 过滤非正常的数据
			if (!DictType.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getDictType(), e.getDictName()));
			map.put("value", e.getDictType());
			mapList.add(map);
		}
		return mapList;
	}
	
}
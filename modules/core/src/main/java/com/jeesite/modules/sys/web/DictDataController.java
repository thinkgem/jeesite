/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.DictType;
import com.jeesite.modules.sys.service.DictDataService;
import com.jeesite.modules.sys.service.DictTypeService;
import com.jeesite.modules.sys.utils.DictUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
 * 字典管理Controller
 * @author ThinkGem
 * @version 2019-07-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dictData")
@ConditionalOnProperty(name={"config.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class DictDataController extends BaseController {

	@Autowired
	private DictDataService dictDataService;
	@Autowired
	private DictTypeService dictTypeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DictData get(String dictCode, boolean isNewRecord, HttpServletRequest request) {
		if (StringUtils.endsWith(request.getRequestURI(), "listData")) {
			return new DictData();
		}
		return dictDataService.get(dictCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("sys:dictData:view")
	@RequestMapping(value = "list")
	public String list(DictData dictData, Model model) {
		model.addAttribute("dictData", dictData);
		return "modules/sys/dictDataList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("sys:dictData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<DictData> listData(DictData dictData) {
		if (StringUtils.isBlank(dictData.getParentCode())) {
			dictData.setParentCode(DictData.ROOT_CODE);
		}
		List<DictData> list = dictDataService.findList(dictData);
		return list;
	}
	
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("sys:dictData:view")
	@RequestMapping(value = "form")
	public String form(DictData dictData, Model model) {
		// 创建并初始化下一个节点信息
		dictData = createNextNode(dictData);
		model.addAttribute("dictData", dictData);
		return "modules/sys/dictDataForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("sys:dictData:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public DictData createNextNode(DictData dictData) {
		if (StringUtils.isNotBlank(dictData.getParentCode())) {
			dictData.setParent(dictDataService.get(dictData.getParentCode()));
		}
		if (dictData.getIsNewRecord()) {
			DictData where = new DictData();
			where.setDictType(dictData.getDictType());
			where.setParentCode(dictData.getParentCode());
			DictData last = dictDataService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				dictData.setTreeSort(last.getTreeSort() + 30);
				dictData.setDictValue(IdGen.nextCode(last.getDictValue()));
				// 默认设置是否系统
				if (dictData.getIsSys() == null){
					dictData.setIsSys(last.getIsSys());
				}
			}else if(dictData.getParent() != null){
				dictData.setDictValue(dictData.getParent().getDictValue() + "001");
				// 默认设置是否系统
				if (dictData.getIsSys() == null){
					// 验证字典类型是否设置正确，如果没有找到这个字典类型则不可保存
					DictType dictType = new DictType();
					dictType.setDictType(dictData.getDictType());
					dictType = dictTypeService.get(dictType);
					if (dictType != null){
						dictData.setIsSys(dictType.getIsSys());
					}
				}
			}
		}
		// 以下设置表单默认数据
		if (dictData.getTreeSort() == null){
			dictData.setTreeSort(DictData.DEFAULT_TREE_SORT);
		}
		return dictData;
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("sys:dictData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DictData dictData, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		DictData old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !dictData.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		// 只有系统管理员才能保存为系统字典！
		if (!dictData.currentUser().isSuperAdmin() && Global.YES.equals(dictData.getIsSys())){
			return renderResult(Global.FALSE, text("保存失败，只有系统管理员才能保存为系统字典！"));
		}
		// 验证字典类型是否设置正确，如果没有找到这个字典类型则不可保存
		DictType dictType = new DictType();
		dictType.setDictType(dictData.getDictType());
		dictType = dictTypeService.get(dictType);
		if (dictType == null){
			return renderResult(Global.FALSE, text("保存失败，没有找到''{0}''字典类型！", dictData.getDictType()));
		}
		// 如果字段类型是系统字典类型，则它的字段数据也是系统的
		if (Global.YES.equals(dictType.getIsSys()) && !Global.YES.equals(dictData.getIsSys())){
			return renderResult(Global.FALSE, text("保存失败，字典类型是系统的，字典数据也必须是系统字典！"));
		}
		// 如果字典类型不是系统字典，则默认情况下字典数据的isSys使用字典类型的
		if (StringUtils.isBlank(dictData.getIsSys())){
			dictData.setIsSys(dictType.getIsSys());
		}
		dictDataService.save(dictData);
		return renderResult(Global.TRUE, text("保存字典成功"));
	}
	
	/**
	 * 停用字典
	 * @param dictData
	 */
	@RequiresPermissions("sys:dictData:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(DictData dictData, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		DictData old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !dictData.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
//		DictData where = new DictData();
//		where.setStatus(DictData.STATUS_NORMAL);
//		where.setParentCodes_rightLike(dictData.getParentCodes() + dictData.getId() + ",");
//		long count = dictDataService.findCount(where);
//		if (count > 0) {
//			return renderResult(Global.FALSE, text("该字典包含未停用的子字典！"));
//		}
		dictData.setStatus(DictData.STATUS_DISABLE);
		dictDataService.updateStatus(dictData);
		return renderResult(Global.TRUE, text("停用字典成功"));
	}

	/**
	 * 启用字典
	 * @param dictData
	 */
	@RequiresPermissions("sys:dictData:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(DictData dictData, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		DictData old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !dictData.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		dictData.setStatus(DictData.STATUS_NORMAL);
		dictDataService.updateStatus(dictData);
		return renderResult(Global.TRUE, text("启用字典成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("sys:dictData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DictData dictData, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		DictData old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !dictData.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		dictDataService.delete(dictData);
		return renderResult(Global.TRUE, text("删除字典成功"));
	}
	
	/**
	 * 获取树结构数据。
	 * @param dictType 字典类型，加 __all（双下划线+all） 后缀，则返回停用的字典 v4.2.0
	 * @param excludeCode 排除的ID
	 * @param isShowCode 是否显示值（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @param isShowRawName 是否显示原文（默认false）
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String dictType, String excludeCode,
			String isShowCode, boolean isShowRawName) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<DictData> list = DictUtils.getDictList(dictType);
		for (int i=0; i<list.size(); i++){
			DictData e = list.get(i);
			// 过滤非正常的数据（状态为空的除外）
			if (!DictData.STATUS_NORMAL.equals(e.getStatus()) && e.getStatus() != null){
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
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getDictValue(),
					isShowRawName ? e.getDictLabelRaw() : e.getDictLabel()));
			map.put("value", e.getDictValue());
			if (StringUtils.isNotBlank(e.getDictIcon())) {
				map.put("icon", e.getDictIcon());
			}
			if (StringUtils.isNotBlank(e.getCssClass())) {
				map.put("cssClass", e.getCssClass());
			}
			if (StringUtils.isNotBlank(e.getCssStyle())) {
				map.put("cssStyle", e.getCssStyle());
			}
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 树结构数据修复
	 * @return
	 */
	@RequiresPermissions("sys:dictData:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(DictData dictData){
		if (!dictData.currentUser().isAdmin()){
			return renderResult(Global.FALSE, text("操作失败，只有管理员才能进行修复！"));
		}
		dictDataService.fixTreeData();
		return renderResult(Global.TRUE, text("数据修复成功"));
	}
	
}
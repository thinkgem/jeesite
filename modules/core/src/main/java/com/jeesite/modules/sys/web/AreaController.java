/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;

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

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.sys.utils.AreaUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	/**
	 * 获取区域
	 */
	@ModelAttribute
	public Area get(String areaCode, boolean isNewRecord) {
		return areaService.get(areaCode, isNewRecord);
	}
	
	/**
	 * 区域列表
	 * @param area
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "list")
	public String list(Area area, Model model) {
		return "modules/sys/areaList";
	}
	
	/**
	 * 查询区域数据
	 * @param area
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Area> listData(Area area) {
		if (StringUtils.isBlank(area.getParentCode())) {
			area.setParentCode(Area.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(area.getAreaCode())
				|| StringUtils.isNotBlank(area.getAreaName())){
			area.setParentCode(null);
		}
		List<Area> list = areaService.findList(area);
		return list;
	}
	
	/**
	 * 查看编辑区域
	 * @param area
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		// 创建并初始化下一个节点信息
		area = createNextNode(area);
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public Area createNextNode(Area area) {
		if (StringUtils.isNotBlank(area.getParentCode())) {
			area.setParent(areaService.get(area.getParentCode()));
		}
		if (area.getIsNewRecord()) {
			Area where = new Area();
			where.setParentCode(area.getParentCode());
			Area last = areaService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				area.setTreeSort(last.getTreeSort() + 30);
				area.setAreaCode(IdGen.nextCode(last.getAreaCode()));
			}else if(area.getParent() != null){
				area.setAreaCode(area.getParent() + "001");
			}
		}
		// 以下设置表单默认数据
		if (area.getTreeSort() == null){
			area.setTreeSort(Area.DEFAULT_TREE_SORT);
		}
		return area;
	}
	
	/**
	 * 保存区域
	 * @param area
	 */
	@RequiresPermissions("sys:area:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Area area, Model model) {
		areaService.save(area);
		return renderResult(Global.TRUE, "保存区域'" + area.getAreaName() + "'成功");
	}

	/**
	 * 停用区域
	 * @param area
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Area area) {
		Area where = new Area();
		where.setStatus(Area.STATUS_NORMAL);
		where.setParentCodes("," + area.getId() + ",");
		long count = areaService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, "该区域包含未停用的子区域！");
		}
		area.setStatus(Area.STATUS_DISABLE);
		areaService.updateStatus(area);
		return renderResult(Global.TRUE, "停用区域" + area.getAreaName() + "成功");
	}

	/**
	 * 启用区域
	 * @param area
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Area area) {
		area.setStatus(Area.STATUS_NORMAL);
		areaService.updateStatus(area);
		return renderResult(Global.TRUE, "启用区域" + area.getAreaName() + "成功");
	}

	/**
	 * 删除区域
	 * @param area
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Area area) {
		areaService.delete(area);
		return renderResult(Global.TRUE, "删除区域成功");
	}

	/**
	 * 获取区域树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<Area> list = AreaUtils.getAreaAllList();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			// 过滤非正常的数据
			if (!Area.STATUS_NORMAL.equals(e.getStatus())){
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
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getId(), e.getAreaName()));
			mapList.add(map);
		}
		return mapList;
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		areaService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
}

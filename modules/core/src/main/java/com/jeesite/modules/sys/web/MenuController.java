/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.Module;
import com.jeesite.modules.sys.service.MenuService;
import com.jeesite.modules.sys.service.ModuleService;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
 * 菜单管理Controller
 * @author ThinkGem
 * @version 2019-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private ModuleService moduleService;

	@ModelAttribute
	public Menu get(String menuCode, boolean isNewRecord, HttpServletRequest request) {
		if (StringUtils.endsWith(request.getRequestURI(), "listData")) {
			return new Menu();
		}
		return menuService.get(menuCode, isNewRecord);
	}
	
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "index")
	public String index(Menu menu, Model model) {
		if (StringUtils.isBlank(menu.getSysCode())){
			menu.setSysCode(Menu.SYS_CODE_DEFAULT);
		}
		model.addAttribute("menu", menu);
		return "modules/sys/menuIndex";
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "list")
	public String list(Menu menu, Model model) {
		if (StringUtils.isBlank(menu.getSysCode())){
			menu.setSysCode(Menu.SYS_CODE_DEFAULT);
		}
		model.addAttribute("menu", menu);
		return "modules/sys/menuList";
	}
	
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Menu> listData(Menu menu) {
		if (StringUtils.isBlank(menu.getParentCode())) {
			menu.setParentCode(Menu.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(menu.getMenuCode())
				|| StringUtils.isNotBlank(menu.getMenuNameRaw())
				|| StringUtils.isNotBlank(menu.getMenuHref())
				|| StringUtils.isNotBlank(menu.getPermission())){
			menu.setParentCode(null);
		}
		List<Menu> list = menuService.findList(menu);
		return list;
	}
	
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		// 创建并初始化下一个节点信息
		menu = createNextNode(menu);
		model.addAttribute("menu", menu);
		// 获取所有模块列表
		Module module = new Module();
		List<Module> moduleList = moduleService.findList(module);
		model.addAttribute("moduleList", moduleList);
		return "modules/sys/menuForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public Menu createNextNode(Menu menu) {
		if (StringUtils.isNotBlank(menu.getParentCode())) {
			menu.setParent(menuService.get(menu.getParentCode()));
		}
		if (menu.getIsNewRecord()) {
			Menu where = new Menu();
			where.setParentCode(menu.getParentCode());
			Menu last = menuService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				menu.setTreeSort(last.getTreeSort() + 30);
				menu.setMenuType(last.getMenuType());
				if (last.getIsRoot()) {
					menu.setModuleCodes(Module.MODULE_CORE);
				}else{
					menu.setModuleCodes(last.getModuleCodes());
				}
			}else if(menu.getParent() != null){
				menu.setMenuType(menu.getParent().getMenuType());
				menu.setModuleCodes(menu.getParent().getModuleCodes());
			}
		}
		// 以下设置表单默认数据
		if (menu.getTreeSort() == null){
			menu.setTreeSort(Menu.DEFAULT_TREE_SORT);
		}
		if (menu.getWeight() == null) {
			menu.setWeight(Menu.WEIGHT_SEC_ADMIN);
		}
		if (StringUtils.isBlank(menu.getSysCode())){
			menu.setSysCode(Menu.SYS_CODE_DEFAULT);
		}
		if (StringUtils.isBlank(menu.getMenuType())){
			menu.setMenuType(Menu.TYPE_MENU);
		}
		if (StringUtils.isBlank(menu.getIsShow())){
			menu.setIsShow(Global.YES);
		}
		return menu;
	}

	@RequiresPermissions("sys:menu:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Menu menu) {
		if (!menu.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		menuService.save(menu);
		return renderResult(Global.TRUE, text("保存菜单''{0}''成功", menu.getMenuNameRaw()), menu);
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Menu menu, HttpServletRequest request){
		if (!menu.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		menu.setStatus(Menu.STATUS_DISABLE);
		menuService.updateStatus(menu);
		return renderResult(Global.TRUE, text("停用菜单''{0}''成功", menu.getMenuName()));
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Menu menu, HttpServletRequest request){
		if (!menu.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		menu.setStatus(Menu.STATUS_NORMAL);
		menuService.updateStatus(menu);
		return renderResult(Global.TRUE, text("启用菜单''{0}''成功", menu.getMenuName()));
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Menu menu) {
		if (!menu.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		menuService.delete(menu);
		return renderResult(Global.TRUE, text("删除菜单''{0}''成功", menu.getMenuNameRaw()));
	}

	/**
	 * 返回树结构数据
	 * @param excludeCode 排除的编码
	 * @param isShowRawName 是否显示原文（默认false）
	 * @return
	 */
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, String isShowHide,
			String sysCode, boolean isShowRawName, HttpServletResponse response) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		Menu where = new Menu();
		where.setStatus(Menu.STATUS_NORMAL);
		if (StringUtils.isNotBlank(parentCode)){
			where.setParentCode(parentCode);
		}
		List<Menu> list = menuService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			Menu e = list.get(i);
			// 过滤非正常的数据
			if (!Menu.STATUS_NORMAL.equals(e.getStatus())){
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
			// 是否隐藏（0：隐藏的不查询；1：查询隐藏的）
			if (StringUtils.isNotBlank(isShowHide) && isShowHide.equals(Global.HIDE)
					&& e.getIsShow().equals(Global.HIDE)) {
				continue;
			}
			// 只查询该归属系统下的菜单
			if (StringUtils.isNotBlank(sysCode) && !sysCode.equals(e.getSysCode())){
				continue;
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", isShowRawName ? e.getMenuNameRaw() : e.getMenuName());
			map.put("isParent", !e.getIsTreeLeaf());
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateTreeSort")
	@ResponseBody
	public String updateTreeSort(String[] ids, Integer[] sorts) {
		if (!UserUtils.getUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		for (int i = 0; i < ids.length; i++) {
			Menu menu = new Menu(ids[i]);
			menu.setTreeSort(sorts[i]);
			menuService.updateTreeSort(menu);
		}
		return renderResult(Global.TRUE, text("保存菜单排序成功"));
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(Menu menu){
		if (!menu.currentUser().isAdmin()){
			return renderResult(Global.FALSE, text("操作失败，只有管理员才能进行修复！"));
		}
		menuService.fixTreeData();
		return renderResult(Global.TRUE, text("数据修复成功"));
	}
	
}

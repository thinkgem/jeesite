/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.role;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.service.MenuService;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.utils.DictUtils;
import io.swagger.v3.oas.annotations.Hidden;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 角色 Controller
 * @author ThinkGem
 * @version 2026-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class RoleMenuController extends BaseController {

	private final RoleService roleService;
	private final MenuService menuService;

	public RoleMenuController(RoleService roleService, MenuService menuService) {
		this.roleService = roleService;
		this.menuService = menuService;
	}

	/**
	 * 查询菜单的树结构数据
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "menuTreeData")
	@ResponseBody
	public Map<String, Object> menuTreeData(Role role) {
		Map<String, Object> model = MapUtils.newHashMap();
		List<String> sysCodes = ListUtils.newArrayList();
		for (DictData sysCode : DictUtils.getDictList("sys_menu_sys_code")) {
			sysCodes.add(sysCode.getDictValue());
		}
		List<Menu> menuList = roleService.findManageMenuList(role);
		Map<String, List<Map<String, String>>> map = MapUtils.newLinkedHashMap();
		for (Menu menu : menuList){
			// 过滤已经禁用的子系统
			if (!sysCodes.contains(menu.getSysCode())) {
				continue;
			}
			List<Map<String, String>> list = map.computeIfAbsent(menu.getSysCode(), k -> ListUtils.newArrayList());
			Map<String, String> m = MapUtils.newHashMap();
			m.put("id", menu.getMenuCode());
			m.put("pId", menu.getParentCode());
			m.put("name", menu.getMenuName() + "<font color=#888> &nbsp; &nbsp; "
					+ StringUtils.abbr(ObjectUtils.toString(menu.getPermission()) + " &nbsp; "
					+ ObjectUtils.toString(menu.getMenuHref()), 50) + "</font>");
			m.put("title", menu.getMenuName() + "&nbsp;"
					+ ObjectUtils.toString(menu.getPermission()) + "\n"
					+ ObjectUtils.toString(menu.getMenuHref()));
			list.add(m);
		}
		model.put("menuMap", map);
		if (StringUtils.isNotBlank(role.getRoleCode())) {
			Menu menuWhere = new Menu();
			menuWhere.setRoleCode(role.getRoleCode());
			List<Menu> roleMenuList = menuService.findByRoleCode(menuWhere);
			model.put("roleMenuList", roleMenuList);
		}
		return model;
	}

	/**
	 * 查询角色的菜单树结构数据
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "menuTreeDataByRoleCode")
	@ResponseBody
	public List<Map<String, Object>> menuTreeDataByRoleCode(String roleCode, String sysCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		if (StringUtils.isBlank(roleCode)) {
			return mapList;
		}
		Menu menuWhere = new Menu();
		menuWhere.setRoleCode(roleCode);
		menuWhere.setSysCode(sysCode);
		menuWhere.setHasFieldScope(true);
		menuWhere.setHasDataScope(true);
		List<Menu> list = menuService.findByRoleCode(menuWhere);
		for (Menu menu : list){
			Map<String, Object> m = MapUtils.newHashMap();
			m.put("id", menu.getId());
			m.put("pId", menu.getParentCode());
			m.put("name", menu.getMenuName() + "<font color=#888> &nbsp; &nbsp; "
					+ ObjectUtils.toString(menu.getPermission()) + "</font>");
			m.put("title", menu.getMenuName() + "&nbsp;"
					+ ObjectUtils.toString(menu.getPermission()));
			m.put("rawName", menu.getMenuName());
			m.put("isParent", !menu.getIsTreeLeaf());
			m.put("permission", menu.getPermission());
			m.put("hasFieldScope", menu.getHasFieldScope());
			m.put("hasDataScope", menu.getHasDataScope());
			mapList.add(m);
		}
		return mapList;
	}

}
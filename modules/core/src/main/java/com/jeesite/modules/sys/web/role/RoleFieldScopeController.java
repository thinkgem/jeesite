/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.role;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.MapperRefresh;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.MapperHelper;
import com.jeesite.common.reflect.ReflectUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.RoleFieldScope;
import com.jeesite.modules.sys.service.FieldScopeService;
import com.jeesite.modules.sys.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@ApiIgnore
public class RoleFieldScopeController extends BaseController {

	private final RoleService roleService;
	private final FieldScopeService fieldScopeService;

	public RoleFieldScopeController(RoleService roleService, FieldScopeService fieldScopeService) {
		this.roleService = roleService;
		this.fieldScopeService = fieldScopeService;
	}

	/**
	 * 获取实体列表
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "entityListData")
	@ResponseBody
	public List<Map<String, Object>> entityListData() {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		for(Class<?> clazz : MapperRefresh.getTypeSet()){
			Table table = MapperHelper.getTableCache(clazz);
			if (table == null || StringUtils.isBlank(table.name())) {
				continue;
			}
			String className = clazz.getName();
			String simpleName = clazz.getSimpleName();
			Map<String, Object> m = MapUtils.newHashMap();
			m.put("value", simpleName);
			String label = table.label();
			if (StringUtils.isBlank(label)) {
				label = simpleName;
			}
			m.put("label", label + " (" + simpleName + ")");
			m.put("entityClass", className);
			m.put("entityLabel", table.label());
			mapList.add(m);
		}
		return mapList;
	}

	/**
	 * 获取实体属性列表
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "entityAttrListData")
	@ResponseBody
	public List<Map<String, Object>> entityAttrListData(String className) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		if (StringUtils.isBlank(className)) {
			return mapList;
		}
		Class<?> clazz = ReflectUtils.loadClass(className);
		Table table = MapperHelper.getTable(clazz);
		for(Column c : MapperHelper.getColumns(clazz, table)){
			String attrName = MapperHelper.getAttrName(c);
			entityAttrBuildMap(mapList, c, attrName);
		}
		for (JoinTable joinTable : table.joinTable()) {
			for(Column c : MapperHelper.getColumns(clazz, joinTable)){
				String attrName = joinTable.attrName() + "." + MapperHelper.getAttrName(c);
				entityAttrBuildMap(mapList, c, attrName);
			}
		}
		return mapList;
	}

	/**
	 * 构建实体属性数据
	 */
	private void entityAttrBuildMap(List<Map<String, Object>> mapList, Column c, String attrName) {
		if (StringUtils.containsAny(attrName, "corpCode", "corpName")) {
			return;
		}
		Map<String, Object> m = MapUtils.newHashMap();
		m.put("value", attrName);
		String label = c.label();
		if (StringUtils.isBlank(label)) {
			label = attrName;
		}
		m.put("label", label + " (" + attrName + ")");
		mapList.add(m);
	}

	/**
	 * 角色授权字段权限
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "roleFieldScopeListData")
	@ResponseBody
	public Page<RoleFieldScope> roleFieldScopeListData(RoleFieldScope roleFieldScope, HttpServletRequest request, HttpServletResponse response) {
		roleFieldScope.setPage(new Page<RoleFieldScope>(request, response));
		List<RoleFieldScope> roleFieldScopeList = fieldScopeService.findFieldScopeList(roleFieldScope);
		roleFieldScope.getPage().setList(roleFieldScopeList);
		return roleFieldScope.getPage();
	}

	/**
	 * 角色授权字段权限
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "formAuthFieldScope")
	public String formAuthFieldScope(RoleFieldScope roleFieldScope, Model model) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role role = roleService.get(roleFieldScope.getRoleCode());
		if (role == null){
			role = new Role();
		}
		RoleFieldScope entity = fieldScopeService.getFieldScopeList(roleFieldScope);
		if (entity == null) {
			entity = new RoleFieldScope();
			entity.setRoleCode(roleFieldScope.getRoleCode());
			entity.setMenuCode(roleFieldScope.getMenuCode());
		}
		model.addAttribute("role", role);
		model.addAttribute("roleFieldScope", entity);
		return "modules/sys/roleFormAuthFieldScope";
	}

	/**
	 * 保存角色授权字段权限
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveAuthFieldScope")
	@ResponseBody
	public String saveAuthFieldScope(RoleFieldScope roleFieldScope) {
		if (StringUtils.isBlank(roleFieldScope.getRoleCode())){
			return renderResult(Global.FALSE, text("角色编码不能为空"));
		}
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = roleService.get(roleFieldScope.getRoleCode());
		if (old != null && Global.YES.equals(old.getIsSys()) && !roleFieldScope.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		roleFieldScope.setRole(old);
		fieldScopeService.saveAuthFieldScope(roleFieldScope);
		return renderResult(Global.TRUE, text("角色授权数据权限成功"));
	}

	/**
	 * 删除角色授权字段权限
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "deleteAuthFieldScope")
	@ResponseBody
	public String deleteAuthFieldScope(RoleFieldScope roleFieldScope) {
		roleFieldScope = fieldScopeService.getFieldScopeList(roleFieldScope);
		if (roleFieldScope == null) {
			return renderResult(Global.FALSE, text("没有找到该权限数据"));
		}
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = roleService.get(roleFieldScope.getRoleCode());
		if (old != null && Global.YES.equals(old.getIsSys()) && !roleFieldScope.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		roleFieldScope.setRole(old);
		fieldScopeService.deleteAuthFieldScope(roleFieldScope);
		return renderResult(Global.TRUE, text("角色授权数据权限成功"));
	}

}
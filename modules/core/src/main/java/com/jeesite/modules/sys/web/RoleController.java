/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.modules.sys.service.MenuService;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.utils.DictUtils;
import com.jeesite.modules.sys.utils.ModuleUtils;
import com.jeesite.modules.sys.utils.RoleUtils;
import io.swagger.v3.oas.annotations.Hidden;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 * @author ThinkGem
 * @version 2020-3-20
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;

	@ModelAttribute
	public Role get(String roleCode, boolean isNewRecord) {
		return roleService.get(roleCode, isNewRecord);
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "list")
	public String list(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Role> listData(Role role, String ctrlPermi, HttpServletRequest request, HttpServletResponse response) {
		// 不是超级管理员，则添加数据权限过滤
		if (!role.currentUser().isSuperAdmin()){
			roleService.addDataScopeFilter(role, ctrlPermi);
		}
		role.setPage(new Page<>(request, response));
		Page<Role> page = roleService.findPage(role);
		return page;
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, String op, Model model) {
		if(role.getIsNewRecord()){
			role.setRoleSort((int)roleService.findCount(role) * 10);
			role.setUserType(User.USER_TYPE_EMPLOYEE);
			role.setIsSys(Global.NO);
			role.setIsShow(Global.SHOW);
		}
		// 操作类型：add: 全部； edit: 编辑； auth: 授权；
		model.addAttribute("op", op);
		model.addAttribute("role", role);
		return "modules/sys/roleForm";
	}

	@RequiresPermissions("sys:role:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Role role, String op, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		// 只有系统管理员才能保存为系统字典！
		if (!role.currentUser().isSuperAdmin() && Global.YES.equals(role.getIsSys())){
			return renderResult(Global.FALSE, text("保存失败，只有系统管理员才能保存为系统角色！"));
		}
		if (!Global.TRUE.equals(checkRoleName(old != null ? old.getRoleName() : "", role.getRoleName()))) {
			return renderResult(Global.FALSE, text("保存角色''{0}''失败，角色名称已存在", role.getRoleName()));
		}
		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_EDIT)){
			roleService.save(role);
		}
		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_AUTH)){
			roleService.saveAuth(role);
		}
		return renderResult(Global.TRUE, text("保存角色''{0}''成功", role.getRoleName()));
	}
	
	/**
	 * 验证角色名是否有效
	 * @param oldRoleName
	 * @param roleName
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "checkRoleName")
	@ResponseBody
	public String checkRoleName(String oldRoleName, String roleName) {
		Role role = new Role();
		role.setRoleName(roleName);
		if (roleName != null && roleName.equals(oldRoleName)) {
			return Global.TRUE;
		} else if (roleName != null && roleService.getByRoleName(role) == null) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}

	/**
	 * 停用角色
	 * @param role
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Role role, HttpServletRequest request){
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		role.setStatus(Role.STATUS_DISABLE);
		roleService.updateStatus(role);
		return renderResult(Global.TRUE, text("停用角色''{0}''成功", role.getRoleName()));
	}
	
	/**
	 * 启用角色
	 * @param role
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Role role, HttpServletRequest request){
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		role.setStatus(Role.STATUS_NORMAL);
		roleService.updateStatus(role);
		return renderResult(Global.TRUE, text("启用角色''{0}''成功", role.getRoleName()));
	}
	
	/**
	 * 删除角色
	 * @param role
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Role role, HttpServletRequest request) {
		if (Role.CORP_ADMIN_ROLE_CODE.equals(role.getRoleCode())){
			return renderResult(Global.FALSE, text("非法操作，此角色为内置角色，不允许删除！"));
		}
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		//if(roleService.hasUserRoleByRoleCode(role)){
		//	return renderResult(Global.FALSE, text("删除角色''{0}''失败，角色关联了用户", role.getRoleName()));
		//}
		roleService.delete(role);
		return renderResult(Global.TRUE, text("删除角色''{0}''成功", role.getRoleName()));
	}

	/**
	 * 判断某用户是包含某角色
	 * @param userCode
	 * @param roleCode
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "hasUserRole")
	@ResponseBody
	public Boolean hasUserRole(String userCode, String roleCode){
		if (StringUtils.isNotBlank(userCode)){
			return RoleUtils.hasUserRole(userCode, roleCode);
		}else{
			return RoleUtils.hasCurrentUserRole(roleCode);
		}
	}
	
	/**
	 * 查询菜单的树结构数据
	 * @param role
	 * @param request
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "menuTreeData")
	@ResponseBody
	public Map<String, Object> menuTreeData(Role role, HttpServletRequest request) {
		Map<String, Object> model = MapUtils.newHashMap();
		List<Menu> menuList = null;
		// 获取菜单列表，根据归属系统转换为zTree能够接受的数据
		Menu menuWhere = new Menu();
		// 根据权限设置可查看的菜单权重级别
		User user = role.currentUser();
		if (user.isSuperAdmin()){
			menuWhere.setWeight_lt(Menu.WEIGHT_SUPER_ADMIN);
			menuList = menuService.findList(menuWhere);
		}else if (User.MGR_TYPE_CORP_ADMIN.equals(user.getMgrType())
				&& Global.getPropertyToBoolean("role.corpAdminAllMenu", "false")){
			menuWhere.setWeight_lt(Menu.WEIGHT_CORP_ADMIN);
			menuList = menuService.findList(menuWhere);
		}else{
			// 二级管理员、普通 用户，只可分配自己的拥有的菜单
			menuWhere.setUserCode(user.getUserCode());
			menuList = menuService.findByUserCode(menuWhere);
		}
		List<String> sysCodes = ListUtils.newArrayList();
		for (DictData sysCode : DictUtils.getDictList("sys_menu_sys_code")) {
			sysCodes.add(sysCode.getDictValue());
		}
		Map<String, List<Map<String, String>>> map = MapUtils.newLinkedHashMap();
		for (Menu menu : menuList){
			// 过滤已经禁用的子系统
			if (!sysCodes.contains(menu.getSysCode())) {
				continue;
			}
			List<Map<String, String>> list = map.get(menu.getSysCode());
			if (list == null){
				list = ListUtils.newArrayList();
				map.put(menu.getSysCode(), list);
			}
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
			menuWhere = new Menu();
			menuWhere.setRoleCode(role.getRoleCode());
			List<Menu> roleMenuList = menuService.findByRoleCode(menuWhere);
			model.put("roleMenuList", roleMenuList);
		}
		return model;
	}

	/** 
	 * 角色授权数据权限
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "formAuthDataScope")
	public String formAuthDataScope(Role role, String checkbox, Model model, HttpServletRequest request) {
		RoleDataScope roleDataScope = new RoleDataScope();
		roleDataScope.setRoleCode(role.getRoleCode());
		List<RoleDataScope> roleDataScopeList = roleService.findDataScopeList(roleDataScope);
		model.addAttribute("roleDataScopeList", roleDataScopeList);
		model.addAttribute("role", role);
		model.addAttribute("moduleCodes", ModuleUtils.getEnableModuleCodes());
		model.addAttribute("dataScopes", JsonMapper.fromJson(Global.getConfig("user.dataScopes", "[]"), List.class));
		return "modules/sys/roleFormAuthDataScope";
	}
	
	/** 
	 * 保存角色授权数据权限
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveAuthDataScope")
	@ResponseBody
	public String saveAuthDataScope(Role role, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Role old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		roleService.saveAuthDataScope(role);
		return renderResult(Global.TRUE, text("角色授权数据权限成功"));
	}

	/**
	 * 获取角色树结构数据
	 * @param isAll			是否显示所有机构（true：不进行权限过滤）
	 * @param isShowCode	是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String userType, Boolean isAll, String isShowCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		Role where = new Role();
		where.setStatus(Role.STATUS_NORMAL);
		if (!(isAll != null && isAll) || Global.isStrictMode()){
			if (!"__all".equals(userType)) {
				where.setUserType(StringUtils.defaultIfBlank(userType, User.USER_TYPE_EMPLOYEE));
			}
			roleService.addDataScopeFilter(where, ctrlPermi);
		}
		List<Role> list = roleService.findList(where);
		list.forEach(e -> {
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("code", e.getViewCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getViewCode(), e.getRoleName()) + (!"__all".equals(userType)
					? "" : "(" + DictUtils.getDictLabel("sys_user_type", e.getUserType(), text("未知")) + ")"));
			mapList.add(map);
		});
		return mapList;
	}
	
	/** 
	 * 角色授权给用户
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "formAuthUser")
	public String formAuthUser(Role role, Model model, HttpServletRequest request) {
		model.addAttribute("role", role);
		return "modules/sys/roleFormAuthUser";
	}
	
	/** 
	 * 保存角色授权给用户
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveAuthUser")
	@ResponseBody
	public String saveAuthUser(Role role, HttpServletRequest request) {
//		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
//		Role old = super.getWebDataBinderSource(request);
//		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
//			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
//		}
		roleService.saveAuthUser(role);
		return renderResult(Global.TRUE, text("角色授权给用户成功"));
	}
	
	/** 
	 * 删除角色授权给用户
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "deleteAuthUser")
	@ResponseBody
	public String deleteAuthUser(Role role, HttpServletRequest request) {
//		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
//		Role old = super.getWebDataBinderSource(request);
//		if (old != null && Global.YES.equals(old.getIsSys()) && !role.currentUser().isSuperAdmin()){
//			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
//		}
		roleService.deleteAuthUser(role);
		return renderResult(Global.TRUE, text("取消用户角色授权成功"));
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.user;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 租户和系统管理员Controller
 * @author ThinkGem
 * @version 2017-03-26
 */
@Controller
@Tag(name = "CorpAdmin - 租户管理员管理")
@RequestMapping(value = "${adminPath}/sys/corpAdmin")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class CorpAdminController extends BaseController {

	private final UserService userService;
	private final RoleService roleService;

	public CorpAdminController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@ModelAttribute
	public User get(String userCode, boolean isNewRecord) {
		return userService.get(userCode, isNewRecord);
	}
	
	@RequiresPermissions("sys:corpAdmin:view")
	@RequestMapping(value = "list")
	public String list(User user, Model model) {
		user.setCorpCode(null);
		user.setCorpName(null);
		model.addAttribute("user", user);
		model.addAttribute("currentCorpCode", user.currentUser().getCorpCode_());
		model.addAttribute("currentCorpName", user.currentUser().getCorpName_());
		model.addAttribute("useCorpModel", Global.getConfigToBoolean("user.useCorpModel", "false"));
		return "modules/sys/user/corpAdminList";
	}

	@RequiresPermissions("sys:corpAdmin:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response) {
		user.setMgrType(User.MGR_TYPE_CORP_ADMIN);	// 租户管理员
		// 禁用自动添加租户代码条件，添加自定义租户查询条件
		user.sqlMap().getWhere().disableAutoAddCorpCodeWhere()
			.and("corp_code", QueryType.EQ, user.getCorpCode_())
			.and("corp_name", QueryType.LIKE, user.getCorpName_());
		user.setPage(new Page<>(request, response));
		Page<User> page = userService.findPage(user);
		return page;
	}

	@RequiresPermissions("sys:corpAdmin:view")
	@RequestMapping(value = "form")
	public String form(User user, String op, Model model) {
		if (user.getIsNewRecord()){
			// 新增租户管理员，如果已存在，则不能保存
			if ("addCorp".equals(op)){
				user.setCorpCode_(StringUtils.EMPTY);  // 租户代码
				user.setCorpName_(StringUtils.EMPTY);  // 租户名称
			}
		}
		// 获取当前用户所拥有的角色
		Role role = new Role();
		role.setUserCode(user.getUserCode());
		List<Role> roleList = roleService.findListByUserCode(role);
		roleList.add(new Role(Role.CORP_ADMIN_ROLE_CODE));
		model.addAttribute("roleList", roleList);
		model.addAttribute("corpAdminRoleCode", Role.CORP_ADMIN_ROLE_CODE);
		// 操作类型：addCorp: 添加租户； addAdmin: 添加管理员； edit: 编辑
		model.addAttribute("op", op);
		model.addAttribute("user", user);
		model.addAttribute("useCorpModel", Global.getConfigToBoolean("user.useCorpModel", "false"));
		return "modules/sys/user/corpAdminForm";
	}

	@RequiresPermissions("sys:corpAdmin:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated User user, String op, HttpServletRequest request) {
		if (!user.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		if (StringUtils.isBlank(user.getCorpCode_())){
			return renderResult(Global.FALSE, text("租户代码不能为空！"));
		}
		User old = super.getWebDataBinderSource(request);
		if (!Global.TRUE.equals(userService.checkLoginCode(old != null ? old.getLoginCode() : "", user.getLoginCode()))) {
			return renderResult(Global.FALSE, text("保存管理员失败，登录账号''{0}''已存在", user.getLoginCode()));
		}
		if (user.getIsNewRecord()){
			user.setUserType(User.USER_TYPE_NONE); // 仅登录用户
		}
		user.setMgrType(User.MGR_TYPE_CORP_ADMIN); // 租户管理员
		// 如果新增，则验证租户代码合法性
		if (user.getIsNewRecord()){
			User where = new User();
			where.setCorpCode_(user.getCorpCode_());
			List<User> list = userService.findCorpList(where);
			if (!list.isEmpty()){
				// 新增租户，如果已存在，则不能保存
				if ("addCorp".equals(op)){
					return renderResult(Global.FALSE, text("保存租户失败，租户代码已存在"));
				}
				// 新增管理员，则使用已有的租户代码和名称
				else if ("addAdmin".equals(op)){
					user.setCorpCode_(list.get(0).getCorpCode_());
					user.setCorpName_(list.get(0).getCorpName_());
				}
				// 非法操作
				else{
					return renderResult(Global.FALSE, text("非法操作，参数错误。"));
				}
			}
		}
		userService.save(user);
		userService.saveAuth(user);
		// 如果修改的是当前用户，则清除当前用户缓存
		if (user.getUserCode().equals(user.currentUser().getUserCode())) {
			UserUtils.clearCache();
		}
		return renderResult(Global.TRUE, text("保存管理员''{0}''成功", user.getLoginCode()));
	}

	/**
	 * 停用用户
	 */
	@RequiresPermissions("sys:corpAdmin:edit")
	@ResponseBody
	@RequestMapping(value = "disable")
	public String disable(User user) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		if (user.currentUser().getUserCode().equals(user.getUserCode())) {
			return renderResult(Global.FALSE, text("停用用户失败，不允许停用当前用户"));
		}
		user.setStatus(User.STATUS_DISABLE);
		userService.updateStatus(user);
		return renderResult(Global.TRUE, text("停用管理员''{0}''成功", user.getLoginCode()));
	}
	
	/**
	 * 启用用户
	 */
	@RequiresPermissions("sys:corpAdmin:edit")
	@ResponseBody
	@RequestMapping(value = "enable")
	public String enable(User user) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		user.setStatus(User.STATUS_NORMAL);
		userService.updateStatus(user);
		return renderResult(Global.TRUE, text("启用管理员''{0}''成功", user.getLoginCode()));
	}
	
	/**
	 * 密码重置
	 */
	@RequiresPermissions("sys:corpAdmin:edit")
	@RequestMapping(value = "resetpwd")
	@ResponseBody
	public String resetpwd(User user) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		userService.updatePassword(user.getUserCode(), null);
		return renderResult(Global.TRUE, text("重置管理员''{0}''密码成功", user.getLoginCode()));
	}

	/**
	 * 删除用户
	 */
	@RequiresPermissions("sys:corpAdmin:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(User user) {
		if (User.isSuperAdmin(user.getUserCode())) {
			return renderResult(Global.FALSE, text("非法操作，不能够操作此用户！"));
		}
		if (user.currentUser().getUserCode().equals(user.getUserCode())) {
			return renderResult(Global.FALSE, text("删除用户失败，不允许删除当前用户"));
		}
		if (User.USER_TYPE_NONE.equals(user.getUserType())){
			// 删除系统管理员
			userService.delete(user);
			return renderResult(Global.TRUE, text("删除管理员''{0}''成功", user.getLoginCode()));
		}else{
			// 取消系统管理员身份
			user.setMgrType(User.MGR_TYPE_NOT_ADMIN);
			userService.updateMgrType(user);
			return renderResult(Global.TRUE, text("取消管理员''{0}''身份成功", user.getLoginCode()));
		}
	}
	
	/**
	 * 查询租户数据树格式
	 * @param pId 父级编码，默认 -1
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 */
	//@RequiresPermissions("user") // 注释掉，允许配置URI控制权限
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String pId, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		User where = new User();
		List<User> list = userService.findCorpList(where);
		for (int i = 0; i < list.size(); i++) {
			User e = list.get(i);
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getCorpCode_());
			map.put("pId", StringUtils.defaultIfBlank(pId, "-1"));
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCorpCode_(), e.getCorpName_()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 切换租户
	 */
	@RequiresPermissions("sys:corpAdmin:edit")
	@RequestMapping(value = "switch/{corpCode}")
	@ResponseBody
	public String switchCorp(@PathVariable String corpCode) {
		if (UserUtils.getUser().isSuperAdmin()){
			User where = new User();
			where.setCorpCode_(corpCode);
			where.setPage(new Page<>(1, 1, -1));
			List<User> list = userService.findCorpList(where);
			if (!list.isEmpty()){
				User user = list.get(0);
				Session session = UserUtils.getSession();
				session.setAttribute("corpCode", user.getCorpCode_());
				session.setAttribute("corpName", user.getCorpName_());
				return renderResult(Global.TRUE, text("租户切换成功！"));
			}else{
				return renderResult(Global.TRUE, text("租户切换失败，没有这个租户！"));
			}
		}
		return renderResult(Global.FALSE, text("租户切换失败，只有超级管理员才可以操作！"));
	}

}

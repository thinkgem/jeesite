/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.sys.entity.PostRole;
import com.jeesite.modules.sys.service.PostService;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 状态切换Controller
 * @author ThinkGem
 * @version 2025-01-27
 */
@Controller
@Api(tags = "Switch - 状态切换")
@RequestMapping(value = "${adminPath}")
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public class SwitchController extends BaseController{

	@Autowired
	private PostService postService;

	/**
	 * 切换系统菜单（菜单归属子系统）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "switch/{sysCode}")
	public String switchSys(@PathVariable String sysCode, HttpServletRequest request, HttpServletResponse response) {
		Session session = UserUtils.getSession();
		if (StringUtils.isNotBlank(sysCode)){
			session.setAttribute("sysCode", sysCode); // 5.4.0+ 支持多个，逗号隔开
		}else{
			session.removeAttribute("sysCode");
		}
		// 切换系统时，清除当前岗位和角色状态
		session.removeAttribute("postCode");
		session.removeAttribute("roleCode");
		UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		if (ServletUtils.isAjaxRequest(request)) {
			return renderResult(response, Global.TRUE, text("子系统切换成功"));
		}
		return REDIRECT + adminPath + "/index";
	}

	/**
	 * 切换角色菜单（用户->角色）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"switchRole","switchRole/{roleCode}"})
	public String switchRole(@PathVariable(required=false) String roleCode, HttpServletRequest request, HttpServletResponse response) {
		Session session = UserUtils.getSession();
		if (StringUtils.isNotBlank(roleCode)){
			// 只能设置当前用户的角色，查询权限的时候系统也会二次验证当前用户角色
			if (UserUtils.getUser().getRoleList().stream().noneMatch((r) ->
					StringUtils.equals(roleCode, r.getRoleCode()))){
				return renderResult(response, Global.FALSE, text("没有权限切换到该岗位"));
			}
			session.setAttribute("roleCode", roleCode); // 5.4.0+ 支持多个，逗号隔开
		}else{
			session.removeAttribute("roleCode");
		}
		UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		if (ServletUtils.isAjaxRequest(request)) {
			return renderResult(response, Global.TRUE, text("角色切换成功"));
		}
		return REDIRECT + adminPath + "/index";
	}

	/**
	 * 切换岗位菜单（用户->岗位->角色）v4.9.2
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"switchPost","switchPost/{postCode}"})
	public String switchPost(@PathVariable(required=false) String postCode, HttpServletRequest request, HttpServletResponse response) {
		Session session = UserUtils.getSession();
		if (StringUtils.isNotBlank(postCode)){
			// 只能设置当前用户的岗位，查询权限的时候系统也会二次验证当前用户岗位
			if (EmpUtils.getEmployeePostList().stream().noneMatch((ep) ->
					StringUtils.equals(postCode, ep.getPostCode()))){
				return renderResult(response, Global.FALSE, text("没有权限切换到该岗位"));
			}
			// 开启 user.postRolePermi 参数后，才可以使用岗位关联角色过滤菜单权限
			if (!Global.getConfigToBoolean("user.postRolePermi", "false")) {
				return renderResult(response, Global.FALSE, text("请开启 user.postRolePermi 参数。"));
			}
			// 查询岗位关联的角色
			PostRole where = new PostRole();
			where.setPostCode(postCode);
			where.sqlMap().loadJoinTableAlias("r");
			Set<String> roleCodes = SetUtils.newHashSet();
			postService.findPostRoleList(where).forEach(e -> {
				if (e.getRole() != null && PostRole.STATUS_NORMAL.equals(e.getRole().getStatus())) {
					roleCodes.add(e.getRoleCode());
				}
			});
			if (roleCodes.isEmpty()){
				roleCodes.add("__none__");
			}
			session.setAttribute("postCode", postCode);
			session.setAttribute("roleCode", StringUtils.joinComma(roleCodes)); // 5.4.0+ 支持多个，逗号隔开
		}else{
			session.removeAttribute("postCode");
			session.removeAttribute("roleCode");
		}
		UserUtils.removeCache(UserUtils.CACHE_AUTH_INFO+"_"+session.getId());
		if (ServletUtils.isAjaxRequest(request)) {
			return renderResult(response, Global.TRUE, text("岗位切换成功"));
		}
		return REDIRECT + adminPath + "/index";
	}

	/**
	 * 切换主题风格
	 */
	//@RequiresPermissions("user")
	@RequestMapping(value = "switchSkin/{skinName}")
	public String switchSkin(@PathVariable String skinName, @RequestParam(defaultValue="${adminPath}/index") String url,
							 HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(skinName) && !"select".equals(skinName)){
			CookieUtils.setCookie(response, "skinName", EncodeUtils.encodeUrl(EncodeUtils.xssFilter(skinName, request)));
			if (ServletUtils.isAjaxRequest(request)) {
				return renderResult(response, Global.TRUE, text("主题切换成功"));
			}
			return REDIRECT + EncodeUtils.decodeUrl2(url);
		}
		return "modules/sys/switchSkin";
	}

}

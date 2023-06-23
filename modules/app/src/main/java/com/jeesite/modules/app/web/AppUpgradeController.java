/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppUpgrade;
import com.jeesite.modules.app.service.AppUpgradeService;

/**
 * APP版本管理Controller
 * @author ThinkGem
 * @version 2021-04-09
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUpgrade")
public class AppUpgradeController extends BaseController {

	@Autowired
	private AppUpgradeService appUpgradeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppUpgrade get(String id, boolean isNewRecord) {
		return appUpgradeService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appUpgrade:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppUpgrade appUpgrade, Model model) {
		model.addAttribute("appUpgrade", appUpgrade);
		return "modules/app/appUpgradeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appUpgrade:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppUpgrade> listData(AppUpgrade appUpgrade, HttpServletRequest request, HttpServletResponse response) {
		appUpgrade.setPage(new Page<>(request, response));
		Page<AppUpgrade> page = appUpgradeService.findPage(appUpgrade);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appUpgrade:view")
	@RequestMapping(value = "form")
	public String form(AppUpgrade appUpgrade, Model model) {
		model.addAttribute("appUpgrade", appUpgrade);
		return "modules/app/appUpgradeForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("app:appUpgrade:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppUpgrade appUpgrade) {
		appUpgradeService.save(appUpgrade);
		return renderResult(Global.TRUE, text("保存版本成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("app:appUpgrade:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(AppUpgrade appUpgrade) {
		appUpgrade.setStatus(AppUpgrade.STATUS_DISABLE);
		appUpgradeService.updateStatus(appUpgrade);
		return renderResult(Global.TRUE, text("停用版本成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("app:appUpgrade:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(AppUpgrade appUpgrade) {
		appUpgrade.setStatus(AppUpgrade.STATUS_NORMAL);
		appUpgradeService.updateStatus(appUpgrade);
		return renderResult(Global.TRUE, text("启用版本成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("app:appUpgrade:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppUpgrade appUpgrade) {
		appUpgradeService.delete(appUpgrade);
		return renderResult(Global.TRUE, text("删除版本成功！"));
	}
	
}
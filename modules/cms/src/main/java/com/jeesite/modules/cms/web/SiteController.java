/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.CookieUtils;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.FileTempleteService;
import com.jeesite.modules.cms.service.SiteService;
import com.jeesite.modules.sys.utils.CorpUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 站点表Controller
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/site")
public class SiteController extends BaseController {

	@Autowired
	private SiteService siteService;

	@Autowired
	private FileTempleteService fileTempleteService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Site get(String siteCode, boolean isNewRecord) {
		return siteService.get(siteCode, isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = { "list", "" })
	public String list(Site site, Model model) {

		model.addAttribute("site", site);
		return "modules/cms/siteList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Site> listData(Site site, HttpServletRequest request, HttpServletResponse response) {
		site.setPage(new Page<>(request, response));
		Page<Site> page = siteService.findPage(site);
		return page;
	}

	/**
	 * 查看编辑表单
	 * @throws IOException
	 */
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = "form")
	public String form(Site site, Model model) throws IOException {
		model.addAttribute("indexViewList", fileTempleteService.getTempleteContentDict(Site.DEFAULT_TEMPLATE));
		model.addAttribute("site_DEFAULT_TEMPLATE", Site.DEFAULT_TEMPLATE);
		model.addAttribute("site", site);
		model.addAttribute("demos", site);
		return "modules/cms/siteForm";
	}

	/**
	 * 保存站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Site site) {
		siteService.save(site);
		return renderResult(Global.TRUE, text("保存站点表成功！"));
	}

	/**
	 * 停用站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Site site) {
		site.setStatus(Site.STATUS_DISABLE);
		siteService.updateStatus(site);
		return renderResult(Global.TRUE, text("停用站点表成功"));
	}

	/**
	 * 启用站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Site site) {
		site.setStatus(Site.STATUS_NORMAL);
		siteService.updateStatus(site);
		return renderResult(Global.TRUE, text("启用站点表成功"));
	}

	/**
	 * 删除站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Site site) {
		siteService.delete(site);
		return renderResult(Global.TRUE, text("删除站点表成功！"));
	}
	
	/**
	 * 选择站点
	 */
	@RequestMapping(value = "select")
	public String select(String siteCode, String redirect, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isBlank(siteCode)){
			return REDIRECT + adminPath + "/cms/index";
		}
		UserUtils.putCache("currentSiteCode", siteCode);
		// 保存到Cookie中，下次登录后自动切换到该站点
		String cookieName = "siteCode";
		if (Global.isUseCorpModel()){
			cookieName = CorpUtils.getCurrentCorpCode() + "_" + cookieName;
		}
		CookieUtils.setCookie(response, cookieName, siteCode);
		if (StringUtils.isNotBlank(redirect)){
			return REDIRECT + redirect;
		}
		return renderResult(response, Global.TRUE, "切换站点成功！");
	}
}
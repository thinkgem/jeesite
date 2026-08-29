/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.CmsSite;
import com.jeesite.modules.cms.service.CmsSiteService;
import com.jeesite.modules.cms.service.CmsTemplateService;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 站点表 Controller
 * @author ThinkGem、长春叭哥、一往无前
 * @version 2023-4-10
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/site")
public class CmsSiteController extends BaseController {

	private final CmsSiteService siteService;
	private final CmsTemplateService templateService;

	public CmsSiteController(CmsSiteService siteService, CmsTemplateService templateService) {
		this.siteService = siteService;
		this.templateService = templateService;
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute("site")
	public CmsSite get(String siteCode, boolean isNewRecord) {
		return siteService.get(siteCode, isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = { "list", "" })
	public String list(@ModelAttribute("site") CmsSite site, Model model) {
		model.addAttribute("site", site);
		return "modules/cms/siteList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CmsSite> listData(@ModelAttribute("site") CmsSite site, HttpServletRequest request, HttpServletResponse response) {
		site.setPage(new Page<>(request, response));
		Page<CmsSite> page = siteService.findPage(site);
		return page;
	}

	/**
	 * 查看编辑表单
	 * @throws IOException
	 */
	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("site") CmsSite site, Model model) throws IOException {
		if (site.getSiteSort() == null) {
			site.setSiteSort(30);
		}
		model.addAttribute("indexViewList", templateService.getTemplateContentDict(CmsSite.DEFAULT_TEMPLATE));
		model.addAttribute("site_DEFAULT_TEMPLATE", CmsSite.DEFAULT_TEMPLATE);
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
	public String save(@Validated @ModelAttribute("site") CmsSite site) {
		siteService.save(site);
		return renderResult(Global.TRUE, text("保存站点表成功！"));
	}

	/**
	 * 停用站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(@ModelAttribute("site") CmsSite site) {
		site.setStatus(CmsSite.STATUS_DISABLE);
		siteService.updateStatus(site);
		return renderResult(Global.TRUE, text("停用站点表成功"));
	}

	/**
	 * 启用站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(@ModelAttribute("site") CmsSite site) {
		site.setStatus(CmsSite.STATUS_NORMAL);
		siteService.updateStatus(site);
		return renderResult(Global.TRUE, text("启用站点表成功"));
	}

	/**
	 * 删除站点表
	 */
	@RequiresPermissions("cms:site:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(@ModelAttribute("site") CmsSite site) {
		siteService.delete(site);
		return renderResult(Global.TRUE, text("删除站点表成功！"));
	}

	/**
	 * 重建索引
	 * @author ThinkGem
	 */
	@RequiresPermissions("cms:site:rebuildIndex")
	@ResponseBody
	@RequestMapping(value = "rebuildIndex")
	public String rebuildIndex(@ModelAttribute("site") CmsSite site)  {
		return renderResult(Global.TRUE, siteService.rebuildIndex(site));
	}

	/**
	 * 重建向量数据库
	 * @author ThinkGem
	 */
	@RequiresPermissions("cms:site:rebuildVectorStore")
	@ResponseBody
	@RequestMapping(value = "rebuildVectorStore")
	public String rebuildVectorStore(@ModelAttribute("site") CmsSite site)  {
		return renderResult(Global.TRUE, siteService.rebuildVectorStore(site));
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
		if (StringUtils.isNotBlank(redirect)){
			return REDIRECT + redirect;
		}
		return renderResult(response, Global.TRUE, "切换站点成功！");
	}
}

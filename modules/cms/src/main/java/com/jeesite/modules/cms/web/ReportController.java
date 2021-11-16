/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

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
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Report;
import com.jeesite.modules.cms.service.ReportService;

/**
 * 内容举报表Controller
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/report")
public class ReportController extends BaseController {

	@Autowired
	private ReportService reportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Report get(String id, boolean isNewRecord) {
		return reportService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:report:view")
	@RequestMapping(value = {"list", ""})
	public String list(Report report, Model model) {
		model.addAttribute("report", report);
		return "modules/cms/reportList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:report:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Report> listData(Report report, HttpServletRequest request, HttpServletResponse response) {
		report.setPage(new Page<>(request, response));
		Page<Report> page = reportService.findPage(report); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:report:view")
	@RequestMapping(value = "form")
	public String form(Report report, Model model) {
		model.addAttribute("report", report);
		return "modules/cms/reportForm";
	}

	/**
	 * 保存内容举报表
	 */
	@RequiresPermissions("cms:report:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Report report) {
		reportService.save(report);
		return renderResult(Global.TRUE, text("保存内容举报表成功！"));
	}
	
	/**
	 * 停用内容举报表
	 */
	@RequiresPermissions("cms:report:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Report report) {
		report.setStatus(Report.STATUS_DISABLE);
		reportService.updateStatus(report);
		return renderResult(Global.TRUE, text("停用内容举报表成功"));
	}
	
	/**
	 * 启用内容举报表
	 */
	@RequiresPermissions("cms:report:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Report report) {
		report.setStatus(Report.STATUS_NORMAL);
		reportService.updateStatus(report);
		return renderResult(Global.TRUE, text("启用内容举报表成功"));
	}
	
	/**
	 * 删除内容举报表
	 */
	@RequiresPermissions("cms:report:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Report report) {
		reportService.delete(report);
		return renderResult(Global.TRUE, text("删除内容举报表成功！"));
	}
	
}
/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.FileTemplate;
import com.jeesite.modules.cms.service.FileTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模板管理
 * @author 长春叭哥、ThinkGem
 * @version 2025-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/template")
public class FileTemplateController extends BaseController {
	
	private final FileTemplateService fileTemplateService;

	public FileTemplateController(FileTemplateService fileTemplateService) {
		this.fileTemplateService = fileTemplateService;
	}

	/**
	 * 模版管理页
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = { "list", "" })
	public String index() {
		return "modules/cms/tplIndex";
	}

	/**
	 * 模版查看页
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "form")
	public String form(String name, Model model) throws IOException {
		model.addAttribute("template", fileTemplateService.getFileTemplate(name));
		return "modules/cms/tplForm";
	}

	/**
	 * 保存模版文件
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "saveFileTemplate")
	@ResponseBody
	public String saveFileTemplate(String fileName, String fileContent) throws IOException {
		if (!Global.getPropertyToBoolean("cms.template.edit", "false")) {
			return renderResult(Global.FALSE, "模版编辑功能已禁用，请在配置文件中开启！");
		}
		fileContent = EncodeUtils.decodeBase64String(fileContent);
		try {
			fileTemplateService.saveFileTemplate(fileName, fileContent);
			return renderResult(Global.TRUE, "保存模版成功！");
		} catch (ServiceException e) {
			return renderResult(Global.FALSE, text(e.getMessage()));
		}
	}

	/**
	 * 删除模版文件
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "deleteFileTemplate")
	@ResponseBody
	public String deleteFileTemplate(String fileName) throws IOException {
		if (!Global.getPropertyToBoolean("cms.template.edit", "false")) {
			return renderResult(Global.FALSE, "模版编辑功能已禁用，请在配置文件中开启！");
		}
		try {
			fileTemplateService.deleteFileTemplate(fileName);
			return renderResult(Global.TRUE, "删除模版成功！");
		} catch (ServiceException e) {
			return renderResult(Global.FALSE, text(e.getMessage()));
		}
	}

	/**
	 * 模版帮助页
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "help")
	public String help() {
		return "modules/cms/tplHelp";
	}

	/**
	 * 获取树结构数据
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData() throws IOException {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		for (FileTemplate e : fileTemplateService.getTemplateListForEdit(StringUtils.EMPTY)) {
			Map<String, Object> map = MapUtils.newHashMap();
			String path = e.getFilePath();
			String separator = (StringUtils.isNotBlank(path) ? "/" : "");
			map.put("id", e.getFilePath() + separator + e.getFileName());
			map.put("pId", e.getFilePath());
			map.put("name", e.getFileName());
			map.put("isDirectory", e.isDirectory());
			mapList.add(map);
		}
		return mapList;
	}
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.FileTemplate;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.FileTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/template")
public class FileTemplateController extends BaseController {
	
	@Autowired
	private FileTemplateService fileTemplateService;

	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = { "list", "" })
	public String index() {
		return "modules/cms/tplIndex";
	}

	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "form")
	public String form(String name, Model model) throws IOException {
		model.addAttribute("template", fileTemplateService.getFileTemplate(name));
		return "modules/cms/tplForm";
	}

	/*@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "saveFileTemplate")
	@ResponseBody
	public String saveFileTemplate(String fileName, String fileContent) throws IOException {
		FileTemplate template = fileTemplateService.getFileTemplate(fileName);
		String newFileName = FileUtils.path(FileUtils.getWebappPath() + "/WEB-INF/classes/" + fileName);
		File templateFile = template.resource().getFile();
		if (templateFile.getAbsoluteFile().exists()) {
			String bakFileName = StringUtils.replace(templateFile.getAbsolutePath(), templateFile.getName(),
					templateFile.getName() + "." + DateUtils.getDate("yyyyMMddHHmmssSSS"));
			FileUtils.copyFile(templateFile.getAbsolutePath(), bakFileName);
		}
		FileUtils.writeToFile(newFileName, EncodeUtils.decodeBase64String(fileContent), false);
		return renderResult(Global.TRUE, "模版保存成功！");
	}

	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "deleteFileTemplate")
	@ResponseBody
	public String deleteFileTemplate(String fileName) throws IOException {
		FileTemplate template = fileTemplateService.getFileTemplate(fileName);
		File templateFile = template.resource().getFile();
		if (templateFile.getAbsoluteFile().exists()) {
			FileUtils.deleteFile(templateFile.getAbsolutePath());
			return renderResult(Global.TRUE, "模版删除成功！");
		}
		return renderResult(Global.FALSE, "模版文件不存在！");
	}*/

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
		List<FileTemplate> listFileTemplate = fileTemplateService.getFileTemplateListForEdit(Site.TEMPLATE_BASE_DIRECTION);
		for (int i = 0; i < listFileTemplate.size(); i++) {
			FileTemplate e = listFileTemplate.get(i);
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getFilePath() + "/" + e.getFileName());
			map.put("isDirectory", e.isDirectory());
			map.put("pId", e.getFilePath());
			map.put("title", e.getFileName());
			map.put("name", e.getFileName());
			mapList.add(map);
		}
		return mapList;
	}
}

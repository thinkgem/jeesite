/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.gen.entity.GenTable;
import com.jeesite.modules.gen.utils.GenTableUtils;
import com.jeesite.modules.gen.utils.GenUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.FileTemplete;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.FileTempleteService;

/**
 * 模板管理
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/template")
public class FileTemplateController extends BaseController {
	
	@Autowired
	private FileTempleteService fileTempleteService;

	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = { "list", "" })
	public String index() {
		return "modules/cms/tplIndex";
	}

//	@RequiresPermissions("cms:template:edit")
//	@RequestMapping(value = "tree")
//	public String tree(Model model) throws IOException {
//		//根据系统默认的主题获取模板地址
//		model.addAttribute("templateList", fileTempleteService
//				.getFileTempleteListForEdit(Site.TEMPLETE_BASE_DIRECTION + "/" + "default"));
//		return "modules/cms/tplTree";
//	}

	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "form")
	public String form(String name, Model model) throws IOException {
		model.addAttribute("template", fileTempleteService.getFileTemplete(name));
		return "modules/cms/tplForm";
	}

	/*@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "saveFileTemplate")
	@ResponseBody
	public String saveFileTemplate(String fileName, String fileContent) throws IOException {
		FileTemplete template = fileTempleteService.getFileTemplete(fileName);
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
		FileTemplete template = fileTempleteService.getFileTemplete(fileName);
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
		List<FileTemplete> listFileTemplete = fileTempleteService.getFileTempleteListForEdit(Site.TEMPLETE_BASE_DIRECTION);
		for (int i = 0; i < listFileTemplete.size(); i++) {
			FileTemplete e = listFileTemplete.get(i);
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

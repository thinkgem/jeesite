/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

	//构想，给一个模板列表，模板下存放一個screenshots1~10.png,做为模版快照。
	//README.md模版使用与说明文件
	//zip上传模版文件
	//模版目录格式：{design:{index.png,list-x.png,detail-x.png},readme.md,shots}
	//支持在线 安装与卸载 模板   if（）是否存在这个主题文件夹，  存在则判断init内配置文件 开启|关闭的配置
	
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

//	@RequiresPermissions("cms:template:edit")
//	@RequestMapping(value = "saveFileTemplate")
//	@ResponseBody
//	public String saveFileTemplate(String filePath, String fileName, String fileSource, Model model) throws IOException {
//
//		FileTemplete currentFile = FilesTempleteUtils.getFileTempleteByResource(filePath);
//		//在修改文件之前，开启模版的备份模式
//		FileUtils.copyDirectory(currentFile.getFile().getAbsolutePath(),
//				currentFile.getFile().getAbsolutePath().replaceAll(currentFile.getFile().getName(), "") + DateUtils.getDate()
//						+ currentFile.getFile());
//		FileUtils.writeToFile(currentFile.getFile().getAbsolutePath(), EncodeUtils.decodeHtml(fileSource), false);
//
//		if (!currentFile.getFile().getName().equals(fileName)) {
//			String path = currentFile.getFile().getAbsolutePath().replaceAll(currentFile.getFile().getName(), "") + fileName;
//			currentFile.getFile().renameTo(new File(path));
//			return renderResult(Global.SHOW, "模版保存成功,请左侧菜单刷新后再操作模版！");
//		}
//
//		return renderResult(Global.TRUE, "模版保存成功！");
//	}

	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "help")
	public String help() {
		return "modules/cms/tplHelp";
	}

	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("cms:template:edit")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String siteCode, String module, String extCode, Boolean isAll, String isShowCode)
			throws IOException {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		//根据系统默认的主题获取模板地址
		List<FileTemplete> listFileTemplete = fileTempleteService
				.getFileTempleteListForEdit(Site.TEMPLETE_BASE_DIRECTION);
		// 处理转换数据，并返回ztree支持的格式
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

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.modules.cms.entity.FileTemplate;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.cms.utils.FileTemplateUtils;
import com.jeesite.modules.sys.entity.DictData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 模版文件Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-7
 */
@Service
public class FileTemplateService {

	/**
	 * 获取对应视图模板列表，兼容 Select 字典
	 * @param prefix
	 */
	public List<DictData> getTemplateContentDict(String prefix) throws IOException {
		List<DictData> listSite = ListUtils.newArrayList();
		String solutionPath = CmsUtils.getSite(Site.getCurrentSiteCode()).getSolutionPath();
		List<FileTemplate> tplList = FileTemplateUtils.getFileTemplateListByPath(solutionPath);
		for (FileTemplate tpl : tplList) {
			if (tpl.getFileName().startsWith(prefix)) {
				listSite.add(new DictData(tpl.getFileName()
						.substring(0, tpl.getFileName().indexOf("."))));
			}
		}
		return listSite;
	}

	/**
	 * 获取编辑文件列表
	 * @param path 模板路径
	 */
	public List<FileTemplate> getFileTemplateListForEdit(String path) throws IOException {
		return FileTemplateUtils.getFileTemplateListForEdit(path);
	}

	/**
	 * 获取模版文件内容
	 * @param fileName 模板文件
	 */
	public FileTemplate getFileTemplate(String fileName) throws IOException {
		return FileTemplateUtils.getFileTemplateByResource(fileName);
	}
	
}

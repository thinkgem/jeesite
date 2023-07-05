/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.modules.cms.entity.FileTemplete;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.cms.utils.FileTempleteUtils;
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
public class FileTempleteService {

//	public List<String> getTempleteContent(String prefix) throws IOException {
//		List<String> tplList = getFileTempleteNameListByPrefix(CmsUtils.getSite(Site.getCurrentSiteCode()).getSolutionPath());
//		tplList = FilesTempleteUtils.templeteTrim(tplList, prefix, "");
//		return tplList;
//	}

	/**
	 * 获取对应视图模板列表，兼容 Select 字典
	 * @param prefix
	 */
	public List<DictData> getTempleteContentDict(String prefix) throws IOException {
		List<DictData> listSite = ListUtils.newArrayList();
		String solutionPath = CmsUtils.getSite(Site.getCurrentSiteCode()).getSolutionPath();
		List<FileTemplete> tplList = FileTempleteUtils.getFileTempleteListByPath(solutionPath);
		for (FileTemplete tpl : tplList) {
			if (tpl.getFileName().startsWith(prefix)) {
				listSite.add(new DictData(tpl.getFileName()
						.substring(0, tpl.getFileName().indexOf("."))));
			}
		}
		return listSite;
	}

//	/**
//	 * 通过前缀获取文件名集合
//	 * @param filePath
//	 * @return List<String> 文件名集合
//	 * @author 长春叭哥
//	 * @throws IOException
//	 */
//	public List<String> getFileTempleteNameListByPrefix(String filePath) throws IOException {
//		return FilesTempleteUtils.getFileTempleteNameListByPrefix(filePath);
//	}

//	/**
//	 * 获取模版文件列表
//	 * @param path 路径
//	 * @param directory
//	 * @return List<FileTemplete> 模版文件集合
//	 * @author 长春叭哥
//	 * @throws IOException
//	 */
//	public List<FileTemplete> getFileTempleteListByPath(String path, boolean directory) throws IOException {
//		return FilesTempleteUtils.getFileTempleteListByPath(path, directory);
//	}

	/**
	 * 获取编辑文件列表
	 * @param path 模板路径
	 */
	public List<FileTemplete> getFileTempleteListForEdit(String path) throws IOException {
		return FileTempleteUtils.getFileTempleteListForEdit(path);
	}

	/**
	 * 获取模版文件内容
	 * @param fileName 模板文件
	 */
	public FileTemplete getFileTemplete(String fileName) throws IOException {
		return FileTempleteUtils.getFileTempleteByResource(fileName);
	}
	
}

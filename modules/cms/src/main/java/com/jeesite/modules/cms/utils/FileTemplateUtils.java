/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.utils;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.cms.entity.FileTemplate;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 模板文件公共类库
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-7
 */
public class FileTemplateUtils {

	/**
	 * 获取模版文件
	 * @param fileName
	 */
	public static FileTemplate getFileTemplateByResource(String fileName) {
		if (!StringUtils.startsWith(fileName, "views/modules/cmsfront")) {
			fileName = "views/modules/cmsfront/themes/default/index.html";
		}
		Resource resource = ResourceUtils.getResource(fileName);
		return new FileTemplate(resource, fileName);
	}

	/**
	 * 获取模板文件集合
	 * @param path 前缀路径
	 */
	public static List<FileTemplate> getFileTemplateListByPath(String path) throws IOException {
		List<FileTemplate> list = ListUtils.newArrayList();
		Resource[] resources = ResourceUtils.getResources("classpath*:" + path + "/**/*.html");
		for (Resource resource : resources) {
			if (resource.exists()) {
				list.add(new FileTemplate(resource, path));
			}
		}
		return list;
	}

	/**
	 * 获取模板文件相关属性（含目录）
	 * @param path 前缀路径
	 */
	public static List<FileTemplate> getFileTemplateListForEdit(String path) throws IOException {

		List<FileTemplate> list = getFileTemplateListByPath(path);
		Set<FileTemplate> set = SetUtils.newLinkedHashSet();

		// 获取目录
		list.forEach(e -> {
			set.add(new FileTemplate(e));
		});
		set.addAll(list);
		
		return ListUtils.newArrayList(set);
	}

}

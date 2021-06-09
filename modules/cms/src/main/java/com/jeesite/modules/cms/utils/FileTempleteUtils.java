/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.modules.cms.entity.FileTemplete;

/**
 * 模板文件公共类库
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-7
 */
public class FileTempleteUtils {

	/**
	 * 获取模版文件
	 * @param fileName
	 */
	public static FileTemplete getFileTempleteByResource(String fileName) throws IOException {
		Resource resource = ResourceUtils.getResource(fileName);
		return new FileTemplete(resource, fileName);
	}

	/**
	 * 获取模板文件集合
	 * @param path 前缀路径
	 */
	public static List<FileTemplete> getFileTempleteListByPath(String path) throws IOException {
		List<FileTemplete> list = ListUtils.newArrayList();
		Resource[] resources = ResourceUtils.getResources("classpath*:" + path + "/**/*.html");
		for (Resource resource : resources) {
			if (resource.exists()) {
				list.add(new FileTemplete(resource, path));
			}
		}
		return list;
	}

	/**
	 * 获取模板文件相关属性（含目录）
	 * @param path 前缀路径
	 */
	public static List<FileTemplete> getFileTempleteListForEdit(String path) throws IOException {

		List<FileTemplete> list = getFileTempleteListByPath(path);
		Set<FileTemplete> set = SetUtils.newLinkedHashSet();

		// 获取目录
		list.forEach(e -> {
			set.add(new FileTemplete(e));
		});
		set.addAll(list);
		
		return ListUtils.newArrayList(set);
	}

}

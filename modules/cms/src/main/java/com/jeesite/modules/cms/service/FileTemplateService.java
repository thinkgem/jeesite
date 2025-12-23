/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.beetl.BeetlUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.cms.entity.FileTemplate;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 模版文件 Service
 * @author 长春叭哥、ThinkGem
 * @version 2025-12-22
 */
@Service
public class FileTemplateService extends BaseService {

	/**
	 * 获取模板文件集合
	 * @param path 前缀路径
	 */
	public List<FileTemplate> getTemplateListByPath(String path) {
		if (StringUtils.contains(path, "..")) {
			throw new ServiceException(Global.getText("非法请求！"));
		}
		List<FileTemplate> list = ListUtils.newArrayList();
		Resource[] resources = ResourceUtils.getResources("classpath*:"
				+ Site.TEMPLATE_BASE_DIRECTION + path + "/**/*.html");
		for (Resource resource : resources) {
			if (resource.exists()) {
				list.add(new FileTemplate(resource));
			}
		}
		return list;
	}

	/**
	 * 获取对应视图模板列表
	 * @param prefix 路径前缀
	 */
	public List<DictData> getTemplateContentDict(String prefix) {
		List<DictData> listSite = ListUtils.newArrayList();
		Site site = CmsUtils.getSite(Site.getCurrentSiteCode());
		List<FileTemplate> tplList = getTemplateListByPath(site.getTheme());
		for (FileTemplate tpl : tplList) {
			if (tpl.getFileName().startsWith(prefix)) {
				listSite.add(new DictData(tpl.getFileName()
						.substring(0, tpl.getFileName().indexOf("."))));
			}
		}
		return listSite;
	}

	/**
	 * 获取模板文件相关属性（含目录）
	 * @param prefix 前缀路径
	 */
	public List<FileTemplate> getTemplateListForEdit(String prefix) {
		List<FileTemplate> fileList = getTemplateListByPath(prefix);

		// 提取所有父目录
		Set<String> directoryPaths = SetUtils.newLinkedHashSet();
		for (FileTemplate file : fileList) {
			String current = StringUtils.EMPTY;
			String path = file.getFilePath() + "/" + file.getFileName();
			String[] parts = path.split("/");
			for (int i = 0; i < parts.length - 1; i++) {
				if (parts[i].isEmpty()) continue;
				current = current.isEmpty() ? parts[i] : current + "/" + parts[i];
				directoryPaths.add(current);
			}
		}

		// 添加目录
		Set<FileTemplate> result = SetUtils.newLinkedHashSet();
		for (String dirPath : directoryPaths) {
			result.add(new FileTemplate(dirPath));
		}

		// 添加文件
		result.addAll(fileList);

		return ListUtils.newArrayList(result);
	}

	/**
	 * 获取模版文件
	 * @param fileName 模板文件
	 */
	public FileTemplate getFileTemplate(String fileName) {
		if (StringUtils.contains(fileName, "..")
				|| !StringUtils.endsWith(fileName, ".html")) {
			return new FileTemplate(StringUtils.EMPTY);
		}
		fileName = Site.TEMPLATE_BASE_DIRECTION + fileName;
		Resource resource = ResourceUtils.getResource(fileName);
		return new FileTemplate(resource);
	}

	/**
	 * 获取模版文件
	 * @param fileName 模板文件
	 * @author ThinkGem
	 */
	private FileTemplate getTemplateWithException(String fileName) {
		Global.assertDemoMode();
		if (StringUtils.contains(fileName, "..")
				|| !StringUtils.endsWith(fileName, ".html")) {
			throw new ServiceException(Global.getText("非法请求！"));
		}
		FileTemplate template = this.getFileTemplate(fileName);
		if (template.resource() == null || !template.resource().exists()) {
			logger.debug("Resource: {} not exists.", fileName);
			throw new ServiceException(Global.getText("模版文件不存在！")); // 不允许新增文件
		}
		return template;
	}

	/**
	 * 备份文件模版
	 * @author ThinkGem
	 */
	private void backFileTemplate(String saveFileName) {
		String backFileName = saveFileName + "-" + UserUtils.getUser().getUserCode() + "-" + IdGen.nextId() + ".bak";
		if (FileUtils.copyFile(saveFileName, backFileName)) {
			logger.debug("Backup: {}", backFileName);
		}
	}

	/**
	 * 获取 src 目录中的文件模版文件，取不到时返回 null
	 * @author ThinkGem
	 */
	private File getSrcFileTemplate(String saveFileName) {
		String searchSeq = StringUtils.replace("/target/classes/", "/", File.separator);
		if (StringUtils.contains(saveFileName, searchSeq)) {
			String replacement = StringUtils.replace("/src/main/resources/", "/", File.separator);
			File templateFile = new File(StringUtils.replace(saveFileName, searchSeq, replacement));
			if (templateFile.exists()) {
				return templateFile;
			}
		}
		return null;
	}

	/**
	 * 保存文件模版
	 * @author ThinkGem
	 */
	public void saveFileTemplate(String fileName, String fileContent) throws IOException {
		FileTemplate template = getTemplateWithException(fileName);
		// 资源为文件时
		if (template.resource().isFile()) {
			File templateFile = template.resource().getFile();
			String saveFileName = templateFile.getAbsolutePath();
			// 备份 classes 下的文件
			backFileTemplate(saveFileName);
			// 更新 classes 下的文件
			FileUtils.writeToFile(saveFileName, fileContent, false);
			logger.debug("Update: {}", saveFileName);
			// 更新 src 目录中的文件模版文件，源码一般有git就不进行备份了
			templateFile = getSrcFileTemplate(saveFileName);
			if (templateFile != null) {
				saveFileName = templateFile.getAbsolutePath();
				FileUtils.writeToFile(saveFileName, fileContent, false);
				logger.debug("Update: {}", saveFileName);
			}
		}
		// 资源为 jar 时，将文件保存到 web 项目的 classes 中
		else {
			String saveFileName = FileUtils.getClassesPath();
			// web 项目是整包部署时不能操作
			if (saveFileName == null) {
				throw new ServiceException(Global.getText("模版文件不能保存！"));
			}
			saveFileName = new File(saveFileName, fileName).getAbsolutePath();
			// 备份 classes 下的文件
			backFileTemplate(saveFileName);
			// 更新 classes 下的文件
			FileUtils.writeToFile(saveFileName, fileContent, false);
			logger.debug("Update: {}", saveFileName);
			// 清理 beetl 缓存
			BeetlUtils.clearResourceTemplateCache();
		}
	}

	/**
	 * 删除文件模版
	 * @author ThinkGem
	 */
	public void deleteFileTemplate(String fileName) throws IOException {
		FileTemplate template = getTemplateWithException(fileName);
		// 资源为文件时
		if (template.resource().isFile()) {
			File templateFile = template.resource().getFile();
			String saveFileName = templateFile.getAbsolutePath();
			// 备份 classes 下的文件
			backFileTemplate(saveFileName);
			// 删除 classes 下的文件
			FileUtils.deleteFile(saveFileName);
			logger.debug("Delete: {}", saveFileName);
			// 删除 src 目录中的文件模版文件，源码一般有git就不进行备份了
			templateFile = getSrcFileTemplate(saveFileName);
			if (templateFile != null) {
				saveFileName = templateFile.getAbsolutePath();
				FileUtils.deleteFile(saveFileName);
				logger.debug("Delete: {}", saveFileName);
			}
		}
		// 资源为 jar 时，在 web 项目的 classes 中查找，如果存在，则删除
		else {
			String saveFileName = FileUtils.getClassesPath();
			// web 项目是整包部署时不能操作
			if (saveFileName == null) {
				throw new ServiceException(Global.getText("模版文件不能删除！"));
			}
			saveFileName = new File(saveFileName, fileName).getAbsolutePath();
			// 备份 classes 下的文件
			backFileTemplate(saveFileName);
			// 删除 classes 下的文件
			FileUtils.deleteFile(saveFileName);
			// 清理 beetl 缓存
			BeetlUtils.clearResourceTemplateCache();
		}
	}
	
}

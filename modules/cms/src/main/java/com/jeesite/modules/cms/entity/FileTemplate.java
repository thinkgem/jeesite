/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.IOUtils;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * CMS模块-模版文件实体
 * @author 长春叭哥、ThinkGem
 * @version 2025-12-22
 */
public class FileTemplate implements Comparable<FileTemplate>, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(FileTemplate.class);

	private static final long serialVersionUID = 1L;
	private Resource resource;
	private String fileName;
	private String filePath;
	private String fileExtension;
	private boolean isDirectory;

	public FileTemplate(String path) {
		int index = path.lastIndexOf('/');
		if (index == -1) {
			this.fileName = path;
			this.filePath = StringUtils.EMPTY;
		} else {
			this.fileName = path.substring(index + 1);
			this.filePath = path.substring(0, index);
		}
		this.fileExtension = FileUtils.getFileExtension(this.fileName);
		this.isDirectory = !StringUtils.contains(fileName, StringUtils.DOT);
	}

	public FileTemplate(Resource resource) {
		this.resource = resource;
		this.fileName = resource.getFilename();
		try {
			String filePath = resource.getURI().getPath();
			this.filePath = StringUtils.defaultString(StringUtils.substringBetween(
					filePath, Site.TEMPLATE_BASE_DIRECTION, "/" + this.fileName));
		} catch (IOException e) {
			logger.trace(e.getMessage());
			this.resource = null;
		}
		this.fileExtension = FileUtils.getFileExtension(this.fileName);
		this.isDirectory = !StringUtils.contains(fileName, StringUtils.DOT);
	}

	public Resource resource() {
		return resource;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	
	/**
	 * 文件的内容
	 */
	public String getFileContent() {
		if (resource == null || !resource.exists() || isDirectory) {
			return StringUtils.EMPTY;
		}
		try(InputStream is = resource.getInputStream()){
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		}catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	@Override
	public int compareTo(FileTemplate o) {
		return this.getFileName().compareTo(o.getFileName());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FileTemplate e = (FileTemplate) o;
		return Objects.equals(this.fileName, e.fileName)
				&& Objects.equals(this.filePath, e.filePath)
				&& Objects.equals(this.fileExtension, e.fileExtension)
				&& Objects.equals(this.isDirectory, e.isDirectory);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.fileName, this.filePath,
				this.fileExtension, this.isDirectory);
	}

}

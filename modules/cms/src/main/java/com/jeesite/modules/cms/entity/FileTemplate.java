/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.core.io.Resource;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.IOUtils;
import com.jeesite.common.lang.ExceptionUtils;

/**
 * CMS模块-模版文件实体
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-7
 */
public class FileTemplate implements Comparable<FileTemplate>, Serializable {

	private static final long serialVersionUID = 1L;
	private final Resource resource;
	private String fileName;
	private String fileExtension;
	private String filePath;
	private boolean isDirectory;
	
	public FileTemplate(Resource resource, String path) {
		this.resource = resource;
		this.fileName = resource.getFilename();
		this.fileExtension = FileUtils.getFileExtension(this.fileName);
		try {
			String filePath = resource.getURI().toString();
			int beginIndex = 0;
			if (path != null) {
				beginIndex = filePath.indexOf(path);
			}
			int endIndex = filePath.length();
			if (this.fileName != null && this.fileName.contains(".")) {
				endIndex = filePath.lastIndexOf('/');
			}
			this.filePath = filePath.substring(beginIndex, endIndex);
//			System.out.println(this.filePath);
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
		if (this.fileName != null) {
			this.isDirectory = !this.fileName.contains(".");
		}
//		System.out.println(filePath);
//		System.out.println(fileName);
	}
	
	public FileTemplate(FileTemplate source) {
		int index = source.filePath.lastIndexOf('/');
		this.fileName = source.filePath.substring(index+1);
		this.filePath = source.filePath.substring(0, index);
		this.fileExtension = null;
		this.isDirectory = true;
		this.resource = null;
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

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
		if (resource == null) {
			return null;
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
				&& Objects.equals(this.fileExtension, e.fileExtension)
				&& Objects.equals(this.filePath, e.filePath)
				&& Objects.equals(this.isDirectory, e.isDirectory);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.fileName,
				this.fileExtension,
				this.filePath,
				this.isDirectory);
	}

}

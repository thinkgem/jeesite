/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.file.web;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.entity.FileUploadParams;
import com.jeesite.modules.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 文件管理Controller
 * @author ThinkGem
 * @version 2019-12-23
 */
@Controller
@RequestMapping(value = "${adminPath}/file")
@ConditionalOnProperty(name={"file.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class FileUploadController extends BaseController {

	@Autowired
	private FileUploadService fileUploadService;
	
	/**
	 * 上传文件参数
	 */
	@RequestMapping(value = "params")
	@ResponseBody
	public Map<String, Object> params() {
		Map<String, Object> model = MapUtils.newHashMap();
		model.put("imageAllowSuffixes", Global.getConfig("file.imageAllowSuffixes", FileUploadParams.DEFAULT_IMAGE_ALLOW_SUFFIXES));
		model.put("mediaAllowSuffixes", Global.getConfig("file.mediaAllowSuffixes", FileUploadParams.DEFAULT_MEDIA_ALLOW_SUFFIXES));
		model.put("fileAllowSuffixes", Global.getConfig("file.fileAllowSuffixes", FileUploadParams.DEFAULT_FILE_ALLOW_SUFFIXES));
		model.put("chunked", Global.getConfig("file.chunked", "true"));
		model.put("chunkSize", Global.getConfigToInteger("file.chunkSize", "10*1024*1024"));
		model.put("threads", Global.getConfigToInteger("file.threads", "3"));
		model.put("imageMaxWidth", Global.getConfigToInteger("file.imageMaxWidth", "1024"));
		model.put("imageMaxHeight", Global.getConfigToInteger("file.imageMaxHeight", "768"));
		return model;
	}
	
	/**
	 * 上传文件
	 */
	@RequestMapping(value = "upload")
	@ResponseBody
	public Map<String, Object> uploadFile(FileUploadParams params) {
		return fileUploadService.uploadFile(new FileUpload(), params);
	}
	
	/**
	 * 下载文件
	 */
	@RequestMapping(value = "/download/{fileUploadId}")
	public String downloadFile(@PathVariable("fileUploadId") String fileUploadId, String preview, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileUpload fileUpload = fileUploadService.getFile(new FileUpload(fileUploadId));
		return fileUploadService.downloadFile(fileUpload, preview, "file", request, response);
	}

	/**
	 * 获取文件列表
	 * @param fileUpload bizKey 和 bizType 为必填参数
	 * @param bizKeyIsLike 是否对 bizKey 使用 RightLike 右模糊查询
	 */
	@RequestMapping(value = "fileList")
	@ResponseBody
	public String getFileList(FileUpload fileUpload, Boolean bizKeyIsLike) {
		return fileUploadService.getFileList(fileUpload, bizKeyIsLike);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.file.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
		model.put("imageAllowSuffixes", Global.getConfig("file.imageAllowSuffixes", ".gif,.bmp,.jpeg,.jpg,.ico,.png,.tif,.tiff,"));
		model.put("mediaAllowSuffixes", Global.getConfig("file.mediaAllowSuffixes", ".flv,.swf,.mkv,webm,.mid,.mov,.mp3,.mp4,.m4v,.mpc,.mpeg,.mpg,.swf,.wav,.wma,.wmv,.avi,.rm,.rmi,.rmvb,.aiff,.asf,.ogg,.ogv,"));
		model.put("fileAllowSuffixes", Global.getConfig("file.fileAllowSuffixes", ".doc,.docx,.rtf,.xls,.xlsx,.csv,.ppt,.pptx,.pdf,.vsd,.txt,.md,.xml,.rar,.zip,.7z,.tar,.tgz,.jar,.gz,.gzip,.bz2,.cab,.iso,"));
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
	public Map<String, Object> upload(FileUploadParams params) {
		return fileUploadService.uploadFile(params);
	}
	
	/**
	 * 下载文件
	 */
	@RequestMapping(value = "/download/{fileUploadId}")
	public String download(@PathVariable("fileUploadId") String fileUploadId, String preview, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileUpload fileUpload = fileUploadService.get(fileUploadId);
		if (fileUpload != null && fileUpload.getFileEntity() != null && fileUpload.getFileEntity().getFileMd5() != null) {
			// 如果是文件预览，则跳转到文件下载页面，并传输文件预览参数
			if (StringUtils.isNotBlank(preview)){
				fileUpload.setPreviewFileUrl(true);
				String url = fileUpload.getFileUrl();
				if (StringUtils.contains(url, "://")) {
					url = "/userfiles/?url=" + EncodeUtils.encodeUrl(url);
					url += "&uid=" + fileUploadId;
				}
				url += StringUtils.contains(url, "?") ? "&" : "?";
				url += "fileName=" + EncodeUtils.encodeUrl(fileUpload.getFileName());
				url += "&preview=" + EncodeUtils.encodeUrl(preview);
				return REDIRECT + url;
			}
			// 下载文件流或获取下载文件URL并跳转
			String url = fileUploadService.getFileUploadServiceExtend().downFile(fileUpload, request, response);
			if (!"404".equals(url)){
				if (StringUtils.isNotBlank(url)){
					return REDIRECT + url;
				}
				return null;
			}
		}
		request.setAttribute("responseStatus", 200);
		request.setAttribute("message", text("sys.file.downloadFileNotExist"));
		request.getRequestDispatcher("/error/404").forward(request, response);
		return null;
	}
	
	/**
	 * 获取业务附件列表数据
	 * @param fileUpload bizKey 和 bizType 为必填参数
	 */
	@RequestMapping(value = "fileList")
	@ResponseBody
	public String fileList(FileUpload fileUpload, Boolean bizKeyIsLike) {
		if (StringUtils.isNotBlank(fileUpload.getBizKey())
				&& StringUtils.isNotBlank(fileUpload.getBizType())){
			if (bizKeyIsLike != null && bizKeyIsLike){
				fileUpload.setBizKey_rightLike(fileUpload.getBizKey());
				fileUpload.setBizKey(null);
			}
			List<FileUpload> list = fileUploadService.findList(fileUpload);
			if(list != null && !list.isEmpty()){
				return JsonMapper.toJson(list);
			}
		}
		return renderResult(Global.FALSE, "No files.");
	}
	
}
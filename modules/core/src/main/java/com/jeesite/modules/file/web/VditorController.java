/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.file.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.entity.FileUploadParams;
import com.jeesite.modules.file.utils.FileUploadUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Vditor Controller
 * @author ThinkGem
 * @version 2025-04-09
 */
@Controller
@RequestMapping(value = "${adminPath}/file/vditor")
@ConditionalOnProperty(name={"file.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class VditorController extends BaseController {

	@RequestMapping(value = "upload")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile[] files, String md5s, String bizKey, String bizType, String updateType) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		if (files == null || files.length == 0 || StringUtils.isAnyBlank(md5s, bizKey, bizType)) {
			result.put("msg", text("上传的文件不能为空"));
			return result;
		}
		String[] fileMd5s = StringUtils.splitComma(md5s);
		if (fileMd5s.length != files.length) {
			result.put("msg", text("文件校验码与文件的长度不等"));
			return result;
		}
		Map<String, Object> data = new HashMap<>();
		Map<String, String> succMap = new HashMap<>();
		Map<String, String> errFiles = new HashMap<>();
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			FileUploadParams params = new FileUploadParams();
			params.setFileMd5(fileMd5s[i]);
			params.setFileName(file.getOriginalFilename());
			params.setBizKey(bizKey);
			params.setBizType(bizType);
			params.setUploadType(updateType);
			params.setFile(file);
			Map<String, Object> res = FileUploadUtils.saveFileUpload(params);
			if (Global.FALSE.equals(res.get("result"))) {
				errFiles.put(params.getFileName(), (String)res.get("message"));
			} else {
				FileUpload fileUpload = (FileUpload)res.get("fileUpload");
				succMap.put(params.getFileName(), Global.getCtxPath() + fileUpload.getFileUrl());
			}
		}
		if (!errFiles.isEmpty()) {
			result.put("code", 1);
			if (errFiles.size() == files.length) {
				result.put("msg", StringUtils.join(errFiles.values(), "; "));
			} else {
				result.put("msg", text("部分上传失败：") + StringUtils.join(errFiles.values(), "; "));
			}
			data.put("errFiles", errFiles.keySet());
		} else {
			result.put("msg", text("上传的文件成功"));
			data.put("succMap", succMap);
		}
		result.put("data", data);
		return result;
	}

}
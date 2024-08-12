/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.file.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 用户文件下载
 * @author ThinkGem
 * @version 2022-09-27
 */
@Controller
@ConditionalOnProperty(name="file.isFileStreamDown", havingValue="true", matchIfMissing=true)
@ApiIgnore
public class UserfilesController extends BaseController {

	@RequestMapping(value="/userfiles/**")
	public String fileStreamDown(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 获取相对文件地址
		String fileUri = request.getRequestURI();
		String filePath = StringUtils.substringAfter(fileUri, Global.USERFILES_BASE_URL);
		String fileName = request.getParameter("fileName");
		
		// 如果开启了文件预览，则跳转到具体的文件预览组件地址。
		String preview = request.getParameter("preview");
		if (StringUtils.isNotBlank(preview)){
			String fileUrl = request.getRequestURL() + "?source=preview";
			String url = request.getParameter("url");
			String uid = request.getParameter("uid");
			if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(uid)){
				fileUrl = url; //EncodeUtils.decodeUrl(url); 不用解码，否则腾讯云存储的时候预览不能显示
				fileUri = Global.getCtxPath() + Global.getAdminPath() + "/file/download/" + uid;
				filePath = fileName;
			} else if (StringUtils.isNotBlank(fileName)){
				fileUri += "?fileName=" + EncodeUtils.encodeUrl(fileName);
			}
			String previewUrl = "/file/" + preview + "/preview";
			request.setAttribute("fileUrl", fileUrl);	// 文件访问地址
			request.setAttribute("fileUri", fileUri);	// 文件下载地址（fileDown）
			request.setAttribute("filePath", filePath);	// 文件相对路径或文件名
			request.setAttribute("fileUrls", request.getParameter("urls"));	// 前后照片列表
			request.setAttribute(RequestDispatcher.FORWARD_REQUEST_URI, previewUrl);
			request.getRequestDispatcher(previewUrl).forward(request, response);
			return null;
		}

		// 获取文件实际路径
		filePath = Global.getUserfilesBaseDir(filePath);
		
		// 根据实际路径获取文件对象
		File file = new File(EncodeUtils.decodeUrl(filePath));
		
		// 如果文件不存在，尝试下gbk编码
		if (!file.exists()){
			File gbkFile = new File(EncodeUtils.decodeUrl(filePath, "GBK"));
			if (gbkFile.exists()){
				file = gbkFile;
			}
		}
		
		// 下载文件，发送到客户浏览器
		String range = request.getHeader("Range");
		if (StringUtils.isNotBlank(range)){
			logger.debug("File: {}  Range: {}", file, range);
		}else{
			logger.debug("File: {}", file);
		}
		if (StringUtils.isBlank(fileName)){
			fileName = file.getName();
		}
		// 替换到百度编辑器上传的日期时间后缀
		String filenameTimeSuffix = StringUtils.substringAfterLast(FileUtils.getFileNameWithoutExtension(fileName), "_$");
		if (NumberUtils.isCreatable(filenameTimeSuffix)){
			fileName = StringUtils.replace(fileName, "_$" + filenameTimeSuffix, "");
		}
		if (file.exists()){
			FileUtils.downFile(file, request, response, fileName);
			return null;
		}

		// 找不到下载文件，提示文件丢失或不存在
		request.setAttribute("responseStatus", 200);
		request.setAttribute("message", text("sys.file.downloadFileNotExist"));
		request.getRequestDispatcher("/error/404").forward(request, response);
		return null;
	}
	
}

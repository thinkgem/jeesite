///**
// * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.sys.service.impl;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Service;
//
//import com.jeesite.modules.file.entity.FileEntity;
//import com.jeesite.modules.file.entity.FileUpload;
//import com.jeesite.modules.file.service.FileUploadServiceExtendSupport;
//
///**
// * 文件上传扩展实现类
// * @author ThinkGem
// * @version 2018年10月13日
// */
//@Service
//public class FileUploadServiceExtendImpl extends FileUploadServiceExtendSupport {
//
//	@Override
//	public boolean fileExists(FileEntity fileEntity) {
//		System.out.println("============= fileupload fileExists ");
//		return super.fileExists(fileEntity);
//	}
//	
//	@Override
//	public void uploadFile(FileEntity fileEntity) {
//		System.out.println("============= fileupload uploadFile ");
//		super.uploadFile(fileEntity);
//	}
//	
//	@Override
//	public void saveUploadFile(FileUpload fileUpload) {
//		System.out.println("============= fileupload saveUploadFile ");
//		super.saveUploadFile(fileUpload);
//	}
//	
//	@Override
//	public String getFileUrl(FileUpload fileUpload) {
//		System.out.println("============= fileupload getFileUrl ");
//		return super.getFileUrl(fileUpload);
//	}
//	
//	@Override
//	public String downFile(FileUpload fileUpload, HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("============= fileupload downFile ");
//		return super.downFile(fileUpload, request, response);
//	}
//	
//}

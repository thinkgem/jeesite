/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.entity.Report;
import com.jeesite.modules.cms.dao.ReportDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 内容举报表Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Service
@Transactional(readOnly=true)
public class ReportService extends CrudService<ReportDao, Report> {
	
	/**
	 * 获取单条数据
	 * @param report
	 * @return
	 */
	@Override
	public Report get(Report report) {
		return super.get(report);
	}
	
	/**
	 * 查询分页数据
	 * @param report 查询条件
	 * @param report.page 分页对象
	 * @return
	 */
	@Override
	public Page<Report> findPage(Report report) {
		return super.findPage(report);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param report
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Report report) {
		super.save(report);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(report, report.getId(), "report_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(report, report.getId(), "report_file");
	}
	
	/**
	 * 更新状态
	 * @param report
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Report report) {
		super.updateStatus(report);
	}
	
	/**
	 * 删除数据
	 * @param report
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Report report) {
		super.delete(report);
	}
	
}
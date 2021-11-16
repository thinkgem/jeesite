/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.VisitLogDao;
import com.jeesite.modules.cms.entity.VisitLog;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 访问日志表Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Service
@Transactional(readOnly=true)
public class VisitLogService extends CrudService<VisitLogDao, VisitLog> {
	
	/**
	 * 获取单条数据
	 * @param visitLog
	 * @return
	 */
	@Override
	public VisitLog get(VisitLog visitLog) {
		return super.get(visitLog);
	}
	
	/**
	 * 查询分页数据
	 * @param visitLog 查询条件
	 * @param visitLog.page 分页对象
	 * @return
	 */
	@Override
	public Page<VisitLog> findPage(VisitLog visitLog) {
		return super.findPage(visitLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param visitLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(VisitLog visitLog) {
		super.save(visitLog);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(visitLog, visitLog.getId(), "visitLog_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(visitLog, visitLog.getId(), "visitLog_file");
	}
	
	/**
	 * 更新状态
	 * @param visitLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(VisitLog visitLog) {
		super.updateStatus(visitLog);
	}
	
	/**
	 * 删除数据
	 * @param visitLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(VisitLog visitLog) {
		super.delete(visitLog);
	}
	
}
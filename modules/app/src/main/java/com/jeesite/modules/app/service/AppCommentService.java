/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppComment;
import com.jeesite.modules.app.dao.AppCommentDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * APP意见反馈Service
 * @author ThinkGem
 * @version 2021-04-09
 */
@Service
public class AppCommentService extends CrudService<AppCommentDao, AppComment> {
	
	/**
	 * 获取单条数据
	 * @param appComment
	 * @return
	 */
	@Override
	public AppComment get(AppComment appComment) {
		return super.get(appComment);
	}
	
	/**
	 * 查询分页数据
	 * @param appComment 查询条件
	 * @param appComment page 分页对象
	 * @return
	 */
	@Override
	public Page<AppComment> findPage(AppComment appComment) {
		return super.findPage(appComment);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appComment
	 */
	@Override
	@Transactional
	public void save(AppComment appComment) {
		super.save(appComment);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(appComment, appComment.getId(), "appComment_image");
	}
	
	/**
	 * 更新状态
	 * @param appComment
	 */
	@Override
	@Transactional
	public void updateStatus(AppComment appComment) {
		super.updateStatus(appComment);
	}
	
	/**
	 * 删除数据
	 * @param appComment
	 */
	@Override
	@Transactional
	public void delete(AppComment appComment) {
		super.delete(appComment);
	}
	
}
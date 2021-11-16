/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.CommentDao;
import com.jeesite.modules.cms.entity.Comment;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 文章评论表Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Service
@Transactional(readOnly=true)
public class CommentService extends CrudService<CommentDao, Comment> {
	
	/**
	 * 获取单条数据
	 * @param comment
	 * @return
	 */
	@Override
	public Comment get(Comment comment) {
		return super.get(comment);
	}
	
	/**
	 * 查询分页数据
	 * @param comment 查询条件
	 * @param comment.page 分页对象
	 * @return
	 */
	@Override
	public Page<Comment> findPage(Comment comment) {
		return super.findPage(comment);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param comment
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Comment comment) {
		super.save(comment);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(comment, comment.getId(), "comment_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(comment, comment.getId(), "comment_file");
	}
	
	/**
	 * 更新状态
	 * @param comment
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Comment comment) {
		super.updateStatus(comment);
	}
	
	/**
	 * 删除数据
	 * @param comment
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Comment comment) {
		super.delete(comment);
	}
	
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Comment;
import com.jeesite.modules.cms.service.CommentService;

/**
 * 文章评论表Controller
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Comment get(String id, boolean isNewRecord) {
		return commentService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:comment:view")
	@RequestMapping(value = {"list", ""})
	public String list(Comment comment, Model model) {
		model.addAttribute("comment", comment);
		return "modules/cms/commentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:comment:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Comment> listData(Comment comment, HttpServletRequest request, HttpServletResponse response) {
		comment.setPage(new Page<>(request, response));
		Page<Comment> page = commentService.findPage(comment); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:comment:view")
	@RequestMapping(value = "form")
	public String form(Comment comment, Model model) {
		model.addAttribute("comment", comment);
		return "modules/cms/commentForm";
	}

	/**
	 * 保存文章评论表
	 */
	@RequiresPermissions("cms:comment:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Comment comment) {
		commentService.save(comment);
		return renderResult(Global.TRUE, text("保存文章评论表成功！"));
	}
	
	/**
	 * 停用文章评论表
	 */
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Comment comment) {
		comment.setStatus(Comment.STATUS_DISABLE);
		commentService.updateStatus(comment);
		return renderResult(Global.TRUE, text("停用文章评论表成功"));
	}
	
	/**
	 * 启用文章评论表
	 */
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Comment comment) {
		comment.setStatus(Comment.STATUS_NORMAL);
		commentService.updateStatus(comment);
		return renderResult(Global.TRUE, text("启用文章评论表成功"));
	}
	
	/**
	 * 删除文章评论表
	 */
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Comment comment) {
		commentService.delete(comment);
		return renderResult(Global.TRUE, text("删除文章评论表成功！"));
	}
	
}
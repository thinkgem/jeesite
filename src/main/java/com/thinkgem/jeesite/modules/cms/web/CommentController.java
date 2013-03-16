/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.web;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Comment;
import com.thinkgem.jeesite.modules.cms.service.CommentService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 评论Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/cms/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	
	@ModelAttribute
	public Comment get(@RequestParam(required=false) Long id) {
		if (id != null){
			return commentService.get(id);
		}else{
			return new Comment();
		}
	}
	
	@RequiresPermissions("cms:comment:view")
	@RequestMapping(value = {"list", ""})
	public String list(Comment comment) {
        Page<Comment> page = commentService.find(new Page<Comment>(request, response), comment); 
        addModelAttribute("page", page);
		return "modules/cms/commentList";
	}

	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "save")
	public String save(Comment comment) {
		if (beanValidator(comment)){
			if (comment.getAuditUser() == null){
				comment.setAuditUser(UserUtils.getUser());
				comment.setAuditDate(new Date());
			}
			comment.setStatus(Comment.STATUS_RELEASE);
			commentService.save(comment);
			addFlashMessage(DictUtils.getDictLabel(comment.getStatus(), "cms_status", "保存")
					+"评论'" + StringUtils.abbreviate(StringUtils.replaceHtml(comment.getContent()),20) + "'成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/comment/?repage&status=2";
	}
	
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, @RequestParam(required=false) Boolean isRe) {
		commentService.delete(id, isRe);
		addFlashMessage((isRe!=null&&isRe?"恢复审核":"删除")+"评论成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/cms/comment/?repage&status=2";
	}

}

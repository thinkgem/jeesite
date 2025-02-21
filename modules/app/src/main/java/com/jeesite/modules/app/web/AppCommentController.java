/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppComment;
import com.jeesite.modules.app.service.AppCommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * APP意见反馈Controller
 * @author ThinkGem
 * @version 2021-04-09
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appComment")
public class AppCommentController extends BaseController {

	@Autowired
	private AppCommentService appCommentService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppComment get(String id, boolean isNewRecord) {
		return appCommentService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appComment:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppComment appComment, Model model) {
		model.addAttribute("appComment", appComment);
		return "modules/app/appCommentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appComment:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppComment> listData(AppComment appComment, HttpServletRequest request, HttpServletResponse response) {
		appComment.setPage(new Page<>(request, response));
		Page<AppComment> page = appCommentService.findPage(appComment);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appComment:view")
	@RequestMapping(value = "form")
	public String form(AppComment appComment, Model model) {
		model.addAttribute("appComment", appComment);
		return "modules/app/appCommentForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("app:appComment:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppComment appComment) {
		if (StringUtils.isNotBlank(appComment.getReplyContent())) {
			if (StringUtils.isBlank(appComment.getReplyUserCode())) {
				appComment.setReplyUserCode(appComment.currentUser().getUserCode());
			}
			if (StringUtils.isBlank(appComment.getReplyUserName())) {
				appComment.setReplyUserName(appComment.currentUser().getUserName());
			}
			if (appComment.getReplyDate() == null) {
				appComment.setReplyDate(new Date());
			}
		}
		appCommentService.save(appComment);
		return renderResult(Global.TRUE, text("保存意见成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("app:appComment:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(AppComment appComment) {
		appComment.setStatus(AppComment.STATUS_DISABLE);
		appCommentService.updateStatus(appComment);
		return renderResult(Global.TRUE, text("停用意见成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("app:appComment:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(AppComment appComment) {
		appComment.setStatus(AppComment.STATUS_NORMAL);
		appCommentService.updateStatus(appComment);
		return renderResult(Global.TRUE, text("启用意见成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("app:appComment:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppComment appComment) {
		appCommentService.delete(appComment);
		return renderResult(Global.TRUE, text("删除意见成功！"));
	}
	
}
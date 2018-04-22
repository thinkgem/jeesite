/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

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
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.service.PostService;

/**
 * 岗位管理Controller
 * @author ThinkGem
 * @version 2017-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/post")
public class PostController extends BaseController {

	@Autowired
	private PostService postService;
	
	@ModelAttribute
	public Post get(String postCode, boolean isNewRecord) {
		return postService.get(postCode, isNewRecord);
	}
	
	@RequiresPermissions("sys:post:view")
	@RequestMapping(value = "list")
	public String list(Post post, Model model) {
		return "modules/sys/postList";
	}
	
	@RequiresPermissions("sys:post:view")
	@RequestMapping(value = {"listData"})
	@ResponseBody
	public Page<Post> listData(Post post, HttpServletRequest request, HttpServletResponse response) {
		Page<Post> page = postService.findPage(new Page<Post>(request, response), post); 
		return page;
	}

	@RequiresPermissions("sys:post:view")
	@RequestMapping(value = "form")
	public String form(Post post, Model model) {
		if(post.getIsNewRecord()){
			post.setPostSort((int)postService.findCount(post) * 10);
		}
		model.addAttribute("post", post);
		return "modules/sys/postForm";
	}

	@RequiresPermissions("sys:post:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Post post, String oldRoleName, Model model) {
		if (!"true".equals(checkPostName(oldRoleName, post.getPostName()))) {
			return renderResult(Global.FALSE, text("保存岗位失败，岗位名称''{0}''已存在", post.getPostName()));
		}
		postService.save(post);
		return renderResult(Global.TRUE, text("保存岗位''{0}''成功", post.getPostName()));
	}
	
	@RequiresPermissions("sys:post:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Post post, HttpServletRequest request, HttpServletResponse response, Model model) {
		post.setStatus(Post.STATUS_DISABLE);
		postService.updateStatus(post);
		return renderResult(Global.TRUE, text("停用岗位''{0}''成功", post.getPostName()));
	}
	
	@RequiresPermissions("sys:post:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Post post, HttpServletRequest request, HttpServletResponse response, Model model) {
		post.setStatus(Post.STATUS_NORMAL);
		postService.updateStatus(post);
		return renderResult(Global.TRUE, text("启用岗位''{0}''成功", post.getPostName()));
	}
	
	@RequiresPermissions("sys:post:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Post post) {
		postService.delete(post);
		return renderResult(Global.TRUE, text("删除岗位''{0}''成功", post.getPostName()));
	}
	
	/**
	 * 验证岗位名是否有效
	 * @param oldPostName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "checkPostName")
	@ResponseBody
	public String checkPostName(String oldPostName, String postName) {
		Post post = new Post();
		post.setPostName(postName);
		if (postName != null && postName.equals(oldPostName)) {
			return Global.TRUE;
		} else if (postName != null && postService.getByPostName(post) == null) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}
	
}
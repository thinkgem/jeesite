/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.entity.PostRole;
import com.jeesite.modules.sys.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 岗位管理Controller
 * @author ThinkGem
 * @version 2017-03-25
 */
@Controller
@Tag(name = "Post - 岗位管理")
@RequestMapping(value = "${adminPath}/sys/post")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
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
		model.addAttribute("post", post);
		return "modules/sys/postList";
	}

	@RequiresPermissions("sys:post:view")
	@RequestMapping(value = {"listData"})
	@ResponseBody
	public Page<Post> listData(Post post, HttpServletRequest request, HttpServletResponse response) {
		post.setPage(new Page<>(request, response));
		Page<Post> page = postService.findPage(post);
		return page;
	}

	@RequiresPermissions("sys:post:view")
	@RequestMapping(value = "form")
	public String form(Post post, Model model) {
		if(post.getIsNewRecord()){
			post.setPostSort(((int)postService.findCount(post) + 1) * 10);
		}
		// 查询岗位所关联的角色信息
		if (StringUtils.isNotBlank(post.getPostCode())){
			PostRole where = new PostRole();
			where.setPostCode(post.getPostCode());
			where.sqlMap().loadJoinTableAlias("r");
			List<String> roleCodes = ListUtils.newArrayList();
			List<String> roleNames = ListUtils.newArrayList();
			postService.findPostRoleList(where).forEach(e -> {
				if (e.getRole() != null && PostRole.STATUS_NORMAL.equals(e.getRole().getStatus())) {
					roleCodes.add(e.getRoleCode());
					roleNames.add(e.getRole().getRoleName());
				}
			});
			model.addAttribute("roleCodes", StringUtils.joinComma(roleCodes));
			model.addAttribute("roleNames", StringUtils.joinComma(roleNames));
		}
		model.addAttribute("post", post);
		return "modules/sys/postForm";
	}

	@RequiresPermissions("sys:post:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Post post, HttpServletRequest request) {
		Post old = super.getWebDataBinderSource(request);
		if (!"true".equals(checkPostName(old != null ? old.getPostName() : "", post.getPostName()))) {
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
	 * @param postName
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

	/**
	 * 获取岗位树结构数据
	 * @param isShowCode    是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String userType, String isShowCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		Post where = new Post();
		where.setStatus(Post.STATUS_NORMAL);
		List<Post> list = postService.findList(where);
		list.forEach(e -> {
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("code", e.getViewCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getViewCode(), e.getPostName()));
			mapList.add(map);
		});
		return mapList;
	}

}
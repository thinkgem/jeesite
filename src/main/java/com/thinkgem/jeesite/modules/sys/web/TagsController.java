/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.web;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 标签Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/tags")
public class TagsController extends BaseController {
	
	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequiresUser
	@RequestMapping(value = "treeselect")
	public String treeselect() {
		addModelAttribute("url", request.getParameter("url")); 	// 树结构数据URL
		addModelAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		addModelAttribute("parentIds", request.getParameter("parentIds"));// 父编号层次，用于默认选中节点
		addModelAttribute("checked", request.getParameter("checked")); // 是否可复选
		addModelAttribute("checkedIds", request.getParameter("checkedIds")); // 如果是可复选，则指定默认选中的Id
		addModelAttribute("module", request.getParameter("module"));	// 过滤栏目模型（仅针对CMS的Category树）
		return "modules/sys/tagsTreeselect";
	}
	
}

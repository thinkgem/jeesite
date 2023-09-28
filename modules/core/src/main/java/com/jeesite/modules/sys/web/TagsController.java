/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.web.BaseController;
import com.jeesite.common.web.http.ServletUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公共标签Controller
 * @author ThinkGem
 * @version 2020-5-7
 */
@Controller
@RequestMapping(value = "tags")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
@Hidden
public class TagsController extends BaseController {

	/**
	 * 树结构选择标签使用
	 */
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAllAttributes(ServletUtils.getParameters(request));
		return "tagsview/form/treeselect";
	}
	
	/**
	 * 图标选择标签
	 */
	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAllAttributes(ServletUtils.getParameters(request));
		return "tagsview/form/iconselect";
	}
	
	/**
	 * 图片裁剪标签
	 */
	@RequestMapping(value = "imageclip")
	public String imageclip(HttpServletRequest request, Model model) {
		model.addAllAttributes(ServletUtils.getParameters(request));
		return "tagsview/form/imageclip";
	}
	
}

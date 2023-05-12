/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.file.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.ueditor.ActionEnter;
import com.jeesite.common.web.BaseController;

/**
 * UEditor Controller
 * @author ThinkGem
 * @version 2019-10-3
 */
@Controller
@RequestMapping(value = "${adminPath}/file/ueditor")
@ConditionalOnProperty(name={"file.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class UeditorController extends BaseController {

	@RequestMapping(value = "")
	@ResponseBody
	public String upload(HttpServletRequest request, HttpServletResponse response) {
		return upload(null, request, response);
	}

	@RequestMapping(value = "{action}")
	@ResponseBody
	public String upload(@PathVariable String action, HttpServletRequest request, HttpServletResponse response) {
		String rootPath = Global.getUserfilesBaseDir(null);
		return new ActionEnter(request, rootPath, action).exec();
	}

}
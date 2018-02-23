/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.state.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * EhCache管理
 * @author ThinkGem
 * @version 2017年12月31日
 */
@Controller
@RequestMapping(value="${adminPath}/state/ehcache")
public class EhCacheController {

	@RequiresPermissions("stste:ehcache")
	@RequestMapping(value="")
	public String index(Model model){
		model.addAttribute("message", "敬请期待！");
		return "modules/state/ehcacheIndex";
	}
	
}

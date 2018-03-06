/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.state.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务器状态
 * @author ThinkGem
 * @version 2017年12月31日
 */
@Controller
@RequestMapping(value="${adminPath}/state/server")
public class ServerStateController {

	@RequiresPermissions("sys:state:server")
	@RequestMapping(value="")
	public String index(){
		return "modules/state/serverIndex";
	}
	
}

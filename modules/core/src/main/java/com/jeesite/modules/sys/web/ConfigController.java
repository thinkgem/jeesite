/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import io.swagger.v3.oas.annotations.Hidden;
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

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.service.ConfigService;


/**
 * 参数设置Controller
 * @author ThinkGem
 * @version 2019-07-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/config")
@ConditionalOnProperty(name={"config.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class ConfigController extends BaseController {

	@Autowired
	private ConfigService configService;
	
	/**
	 * 获取数据
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public Config get(String id, boolean isNewRecord) {
		return configService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 * @param config
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:config:view")
	@RequestMapping(value = "list")
	public String list(Config config, Model model) {
		model.addAttribute("config", config);
		return "modules/sys/configList";
	}
	
	/**
	 * 查询列表
	 * @param config
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:config:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Config> listData(Config config, HttpServletRequest request, HttpServletResponse response) {
		config.setPage(new Page<>(request, response));
		Page<Config> page = configService.findPage(config); 
		return page;
	}

	/**
	 * 查看编辑表单
	 * @param config
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:config:view")
	@RequestMapping(value = "form")
	public String form(Config config, Model model) {
		model.addAttribute("config", config);
		return "modules/sys/configForm";
	}
	
	/**
	 * 保存数据
	 * @param config
	 * @return
	 */
	@RequiresPermissions("sys:config:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Config config, HttpServletRequest request) {
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Config old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !config.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		// 不是超级管理员，不能修改Name和Key
		if (!config.currentUser().isSuperAdmin()){
			config.setConfigName(old.getConfigName());
			config.setConfigKey(old.getConfigKey());
			config.setIsSys(Global.NO);
		}
		configService.save(config);
		return renderResult(Global.TRUE, text("保存参数成功"));
	}
	
	/**
	 * 验证Key是否有效
	 */
	@RequiresPermissions("sys:config:edit")
	@RequestMapping(value = "checkConfigKey")
	@ResponseBody
	public String checkConfigKey(String oldConfigKey, String configKey) {
		Config where = new Config();
		where.setConfigKey(configKey);
		if (configKey != null && configKey.equals(oldConfigKey)) {
			return Global.TRUE;
		} else if (configKey != null && configService.findCount(where) == 0) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}
	
	/**
	 * 删除数据
	 * @param config
	 * @return
	 */
	@RequiresPermissions("sys:config:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Config config, HttpServletRequest request) {
		if (StringUtils.isNotBlank(request.getParameter("isSys"))){
			return renderResult(Global.FALSE, text("越权操作，isSys非法参数"));
		}
		// 获取原数据的isSys状态，如果是系统数据，则必须超级管理员编辑
		Config old = super.getWebDataBinderSource(request);
		if (old != null && Global.YES.equals(old.getIsSys()) && !config.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改系统数据！"));
		}
		configService.delete(config);
		return renderResult(Global.TRUE, text("删除参数成功"));
	}

}
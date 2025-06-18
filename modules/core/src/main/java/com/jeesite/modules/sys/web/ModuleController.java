/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.gen.entity.config.GenConfig;
import com.jeesite.modules.gen.utils.GenModuleUtils;
import com.jeesite.modules.gen.utils.GenUtils;
import com.jeesite.modules.sys.entity.Module;
import com.jeesite.modules.sys.service.ModuleService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模块管理Controller
 * @author ThinkGem
 * @version 2020-3-21
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/module")
@ConditionalOnProperty(name={"config.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class ModuleController extends BaseController {

	@Autowired
	private ModuleService moduleService;
	
	/**
	 * 获取数据
	 * @param moduleCode
	 * @return
	 */
	@ModelAttribute
	public Module get(String moduleCode, boolean isNewRecord) {
		return moduleService.get(moduleCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 * @param module
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:module:view")
	@RequestMapping(value = "list")
	public String list(Module module, Model model) {
		module.setStatus(StringUtils.EMPTY);
		model.addAttribute("module", module);
		return "modules/sys/moduleList";
	}
	
	/**
	 * 查询列表
	 * @param module
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:module:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Module> listData(Module module, HttpServletRequest request, HttpServletResponse response) {
		module.setPage(new Page<>(request, response));
		Page<Module> page = moduleService.findPage(module); 
		return page;
	}

	/**
	 * 仅用来测试使用
	 */
	@RequiresPermissions("sys:module:view")
	@RequestMapping(value = "selectData")
	@ResponseBody
	public List<Map<String, Object>> selectData(Module module) {
		return moduleService.findList(module).stream().map(m -> {
			Map<String, Object> map = new HashMap<>();
			map.put("label", m.getModuleName());
			map.put("value", m.getModuleCode());
			return map;
		}).collect(Collectors.toList());
	}

	/**
	 * 查看编辑表单
	 * @param module
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:module:view")
	@RequestMapping(value = "form")
	public String form(Module module, Model model) {
		if (StringUtils.isBlank(module.getMainClassName())){
			module.setMainClassName("com.jeesite.modules.sys.web.LoginController");
		}
		if (StringUtils.isBlank(module.getCurrentVersion())) {
			module.setCurrentVersion(SpringUtils.getLastVersion());
		}
		GenConfig config = GenUtils.getConfig();
		model.addAttribute("config", config);
		List<String> genBaseDirList = GenModuleUtils.getGenBaseDirList();
		model.addAttribute("genBaseDirList", genBaseDirList);
		if (StringUtils.isNotBlank(module.getGenBaseDir())) {
			model.addAttribute("genBaseDir", module.getGenBaseDir());
		} else {
			model.addAttribute("genBaseDir", genBaseDirList.get(0));
		}
		model.addAttribute("module", module);
		return "modules/sys/moduleForm";
	}

	/**
	 * 保存数据
	 * @param module
	 * @return
	 */
	@RequiresPermissions("sys:module:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Module module) {
		if (!module.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		if (StringUtils.equals(module.getGenFlag(), "2") && StringUtils.isBlank(module.getTplCategory())) {
			return renderResult(Global.FALSE, text("请选择代码生成模板！"));
		}
		moduleService.save(module);
		// 如果设置生成标记，则编译或生成代码   1编译输出到控制台  2生成文件
		if (StringUtils.inString(module.getGenFlag(), "1", "2") && StringUtils.isNotBlank(module.getTplCategory())){
			String result = GenModuleUtils.generateCode(module);
			String flagMsg = ("1".equals(module.getGenFlag()) ? "编译" : "生成");
			String msg = "posfull:保存模块并" + flagMsg + "成功: <br/>" + result;
			return renderResult(Global.TRUE, msg);
		}else {
			return renderResult(Global.TRUE, text("保存模块成功"));
		}
	}
	
	/**
	 * 验证编码是否有效
	 * @return
	 */
	@RequiresPermissions("sys:module:edit")
	@RequestMapping(value = "checkModuleCode")
	@ResponseBody
	public String checkModuleCode(String oldCode, String moduleCode) {
		Module module = new Module();
		module.setModuleCode(moduleCode);
		if (moduleCode != null && moduleCode.equals(oldCode)) {
			return Global.TRUE;
		} else if (moduleCode != null && moduleService.get(module) == null) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}

	/**
	 * 停用数据
	 * @param module
	 * @return
	 */
	@RequiresPermissions("sys:module:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Module module) {
		if (!module.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		if (Module.MODULE_CORE.equals(module.getModuleCode())){
			return renderResult(Global.FALSE, text("核心模块，不允许停用"));
		}
		module.setStatus(Module.STATUS_DISABLE);
		moduleService.updateStatus(module);
		return renderResult(Global.TRUE, text("停用模块成功"));
	}

	/**
	 * 启用数据
	 * @param module
	 * @return
	 */
	@RequiresPermissions("sys:module:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Module module) {
		if (!module.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		module.setStatus(Module.STATUS_NORMAL);
		moduleService.updateStatus(module);
		return renderResult(Global.TRUE, text("启用模块成功"));
	}
	
	/**
	 * 删除数据
	 * @param module
	 * @return
	 */
	@RequiresPermissions("sys:module:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Module module) {
		if (!module.currentUser().isSuperAdmin()){
			return renderResult(Global.FALSE, text("越权操作，只有超级管理员才能修改此数据！"));
		}
		if (Module.MODULE_CORE.equals(module.getModuleCode())){
			return renderResult(Global.FALSE, text("核心模块，不允许删除"));
		}
		moduleService.delete(module);
		return renderResult(Global.TRUE, text("删除模块成功"));
	}

}
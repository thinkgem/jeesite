<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>controller</name>
	<filePath>src/main/java/${packageName}/${moduleName}/web/${subModuleName}</filePath>
	<fileName>${ClassName}Controller.java</fileName>
	<content><![CDATA[
/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package ${packageName}.${moduleName}.web<#if subModuleName != "">.${subModuleName}</#if>;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import ${packageName}.${moduleName}.entity<#if subModuleName != "">.${subModuleName}</#if>.${ClassName};
import ${packageName}.${moduleName}.service<#if subModuleName != "">.${subModuleName}</#if>.${ClassName}Service;

/**
 * ${functionName}Controller
 * @author ${functionAuthor}
 * @version ${functionVersion}
 */
@Controller
@RequestMapping(value = "${r"${adminPath}"}/${urlPrefix}")
public class ${ClassName}Controller extends BaseController {

	@Autowired
	private ${ClassName}Service ${className}Service;
	
	@ModelAttribute
	public ${ClassName} get(@RequestParam(required=false) String id) {
		${ClassName} entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ${className}Service.get(id);
		}
		if (entity == null){
			entity = new ${ClassName}();
		}
		return entity;
	}
	
	@RequiresPermissions("${permissionPrefix}:view")
	@RequestMapping(value = {"list", ""})
	public String list(${ClassName} ${className}, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<${ClassName}> page = ${className}Service.findPage(new Page<${ClassName}>(request, response), ${className}); 
		model.addAttribute("page", page);
		return "${lastPackageName}/${viewPrefix}List";
	}

	@RequiresPermissions("${permissionPrefix}:view")
	@RequestMapping(value = "form")
	public String form(${ClassName} ${className}, Model model) {
		model.addAttribute("${className}", ${className});
		return "${lastPackageName}/${viewPrefix}Form";
	}

	@RequiresPermissions("${permissionPrefix}:edit")
	@RequestMapping(value = "save")
	public String save(${ClassName} ${className}, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ${className})){
			return form(${className}, model);
		}
		${className}Service.save(${className});
		addMessage(redirectAttributes, "保存${functionNameSimple}成功");
		return "redirect:"+Global.getAdminPath()+"/${viewPrefix}/?repage";
	}
	
	@RequiresPermissions("${permissionPrefix}:edit")
	@RequestMapping(value = "delete")
	public String delete(${ClassName} ${className}, RedirectAttributes redirectAttributes) {
		${className}Service.delete(${className});
		addMessage(redirectAttributes, "删除${functionNameSimple}成功");
		return "redirect:"+Global.getAdminPath()+"/${viewPrefix}/?repage";
	}

}]]>
	</content>
</template>
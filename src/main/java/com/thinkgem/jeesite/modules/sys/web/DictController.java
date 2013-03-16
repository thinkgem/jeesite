/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;

/**
 * 字典Controller
 * @author ThinkGem
 * @version 2013-3-15
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@ModelAttribute
	public Dict get(@RequestParam(required=false) Long id) {
		if (id != null){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict) {
		List<String> typeList = dictService.findTypeList();
		addModelAttribute("typeList", typeList);
        Page<Dict> page = dictService.find(new Page<Dict>(request, response), dict); 
        addModelAttribute("page", page);
		return "modules/sys/dictList";
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict) {
		addModelAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")//@Valid 
	public String save(Dict dict) {
		if (!beanValidator(dict)){
			return form(dict);
		}
		dictService.save(dict);
		addFlashMessage("保存字典'" + dict.getLabel() + "'成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/dict/?repage";
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id) {
		dictService.delete(id);
		addFlashMessage("删除字典成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/sys/dict/?repage";
	}

}

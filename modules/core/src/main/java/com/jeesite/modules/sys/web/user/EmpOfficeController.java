/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.EmployeeOffice;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.service.EmployeeOfficeService;
import com.jeesite.modules.sys.service.PostService;

/**
 * 附属机构Controller
 * @author ThinkGem
 * @version 2019-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/empOffice")
public class EmpOfficeController extends BaseController {

	@Autowired
	private EmployeeOfficeService employeeOfficeService;
	@Autowired
	private PostService postService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public EmployeeOffice get(String empCode, String officeCode, boolean isNewRecord) {
		return employeeOfficeService.get(new Class<?>[]{String.class, String.class},
				new Object[]{empCode, officeCode}, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(EmployeeOffice employeeOffice, Model model) {
		model.addAttribute("employeeOffice", employeeOffice);
		return "modules/sys/user/empOfficeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<EmployeeOffice> listData(EmployeeOffice employeeOffice, HttpServletRequest request, HttpServletResponse response) {
		employeeOffice.setPage(new Page<>(request, response));
		Page<EmployeeOffice> page = employeeOfficeService.findPage(employeeOffice);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "form")
	public String form(EmployeeOffice employeeOffice, Model model) {
		// 获取岗位列表
		Post post = new Post();
		model.addAttribute("postList", postService.findList(post));
		model.addAttribute("employeeOffice", employeeOffice);
		return "modules/sys/user/empOfficeForm";
	}

	/**
	 * 保存附属机构
	 */
	@RequiresPermissions("sys:empUser:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated EmployeeOffice employeeOffice) {
		employeeOfficeService.save(employeeOffice);
		return renderResult(Global.TRUE, text("保存附属机构成功！"));
	}
	
	/**
	 * 删除附属机构
	 */
	@RequiresPermissions("sys:empUser:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(EmployeeOffice employeeOffice) {
		employeeOfficeService.delete(employeeOffice);
		return renderResult(Global.TRUE, text("删除附属机构成功！"));
	}
	
}
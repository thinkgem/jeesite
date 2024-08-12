/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.OfficeService;
import io.swagger.annotations.Api;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 公司管理Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@Api(tags = "Company - 公司管理")
@RequestMapping(value = "${adminPath}/sys/company")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class CompanyController extends BaseController {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OfficeService officeService;

	/**
	 * 获取公司
	 */
	@ModelAttribute
	public Company get(String companyCode, boolean isNewRecord) {
		return companyService.get(companyCode, isNewRecord);
	}
	
	/**
	 * 公司管理
	 */
	@RequiresPermissions("sys:company:view")
	@RequestMapping(value = "index")
	public String index(Company company, Model model) {
		model.addAttribute("company", company);
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/companyIndex";
	}

	/**
	 * 公司列表
	 * @param company
	 */
	@RequiresPermissions("sys:company:view")
	@RequestMapping(value = "list")
	public String list(Company company, Model model) {
		model.addAttribute("company", company);
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/companyList";
	}

	/**
	 * 查询公司数据
	 * @param company
	 */
	@RequiresPermissions("sys:company:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Company> listData(Company company, String ctrlPermi) {
		if (StringUtils.isBlank(company.getParentCode())) {
			company.setParentCode(Company.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(company.getViewCode())
				|| StringUtils.isNotBlank(company.getCompanyName())
				|| StringUtils.isNotBlank(company.getFullName())){
			company.setParentCode(null);
		}
		companyService.addDataScopeFilter(company, ctrlPermi);
		List<Company> list = companyService.findList(company);
		return list;
	}
	
	/**
	 * 查看编辑公司
	 * @param company
	 */
	@RequiresPermissions("sys:company:view")
	@RequestMapping(value = "form")
	public String form(Company company, Model model, HttpServletRequest request) {
		// 创建并初始化下一个节点信息
		company = createNextNode(company);
		// 查询公司所关联的机构信息
		if (StringUtils.isNotBlank(company.getCompanyCode())){
			Office where = new Office();
			where.setCompanyCode(company.getCompanyCode());
			List<String> officeCodes = ListUtils.newArrayList();
			List<String> officeNames = ListUtils.newArrayList();
			officeService.findList(where).forEach(e -> {
				officeCodes.add(e.getOfficeCode());
				officeNames.add(e.getOfficeCode());
			});
			model.addAttribute("officeCodes", StringUtils.joinComma(officeCodes));
			model.addAttribute("officeNames", StringUtils.joinComma(officeNames));
		}
		model.addAttribute("company", company);
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/companyForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("sys:company:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public Company createNextNode(Company company) {
		if (StringUtils.isNotBlank(company.getParentCode())) {
			company.setParent(companyService.get(company.getParentCode()));
		}
		if (company.getIsNewRecord()) {
			Company where = new Company();
			where.setParentCode(company.getParentCode());
			Company last = companyService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				company.setTreeSort(last.getTreeSort() + 30);
				company.setViewCode(IdGen.nextCode(last.getViewCode()));
			}else if(company.getParent() != null){
				company.setViewCode(company.getParent().getViewCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (company.getTreeSort() == null){
			company.setTreeSort(Company.DEFAULT_TREE_SORT);
		}
		return company;
	}
	
	/**
	 * 保存公司
	 * @param company
	 */
	@RequiresPermissions("sys:company:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Company company) {
		companyService.save(company);
		return renderResult(Global.TRUE, text("保存公司''{0}''成功", company.getCompanyName()));
	}
	
	/**
	 * 停用公司
	 * @param company
	 */
	@RequiresPermissions("sys:company:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Company company) {
		Company where = new Company();
		where.setStatus(Company.STATUS_NORMAL);
		where.setParentCodes("," + company.getId() + ",");
		long count = companyService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该公司包含未停用的子公司！"));
		}
		company.setStatus(Company.STATUS_DISABLE);
		companyService.updateStatus(company);
		return renderResult(Global.TRUE, text("停用公司''{0}''成功", company.getCompanyName()));
	}
	
	/**
	 * 启用公司
	 * @param company
	 */
	@RequiresPermissions("sys:company:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Company company) {
		company.setStatus(Company.STATUS_NORMAL);
		companyService.updateStatus(company);
		return renderResult(Global.TRUE, text("启用公司''{0}''成功", company.getCompanyName()));
	}

	/**
	 * 删除公司
	 * @param company
	 */
	@RequiresPermissions("sys:company:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Company company) {
		companyService.delete(company);
		return renderResult(Global.TRUE, text("删除公司''{0}''成功", company.getCompanyName()));
	}

	/**
	 * 获取公司树结构数据
	 * @param excludeCode 排除的ID
	 * @param parentCode 设置父级编码返回一级
	 * @param isAll 是否显示所有机构（true：不进行权限过滤）
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @param isShowFullName 是否显示全公司名称
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, Boolean isAll, String isShowCode,
			String isShowFullName, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		Company where = new Company();
		where.setStatus(Company.STATUS_NORMAL);
		if (StringUtils.isNotBlank(parentCode)){
			where.setParentCode(parentCode);
		}
		if (!(isAll != null && isAll) || Global.isStrictMode()){
			companyService.addDataScopeFilter(where, ctrlPermi);
		}
		List<Company> list = companyService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			Company e = list.get(i);
			// 过滤非正常的数据
			if (!Company.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			String name = e.getCompanyName();
			if ("true".equals(isShowFullName) || "1".equals(isShowFullName)){
				name = e.getFullName();
			}
			map.put("code", e.getViewCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getViewCode(), name));
			map.put("title", e.getFullName());
			map.put("isParent", !e.getIsTreeLeaf());
			mapList.add(map);
		}
		return mapList;
	}

	@RequiresPermissions("sys:company:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(Company company) {
		if (!company.currentUser().isAdmin()){
			return renderResult(Global.FALSE, text("操作失败，只有管理员才能进行修复！"));
		}
		companyService.fixTreeData();
		return renderResult(Global.TRUE, text("数据修复成功"));
	}

}
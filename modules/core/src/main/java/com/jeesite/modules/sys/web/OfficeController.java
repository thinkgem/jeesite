/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;

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

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.web.user.EmpUserController;

/**
 * 机构Controller
 * @author ThinkGem
 * @version 2014-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private EmpUserController empUserController;

	/**
	 * 获取机构
	 */
	@ModelAttribute
	public Office get(String officeCode, boolean isNewRecord) {
		return officeService.get(officeCode, isNewRecord);
	}

	/**
	 * 机构列表
	 * @param office
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "list")
	public String list(Office office, Model model) {
		model.addAttribute("office", office);
		return "modules/sys/officeList";
	}
	
	/**
	 * 查询机构数据
	 * @param office
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Office> listData(Office office, String ctrlPermi) {
		if (StringUtils.isBlank(office.getParentCode())){
			office.setParentCode(Office.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(office.getViewCode())
				|| StringUtils.isNotBlank(office.getOfficeName())
				|| StringUtils.isNotBlank(office.getFullName())){
			office.setParentCode(null);
		}
		officeService.addDataScopeFilter(office, ctrlPermi);
		List<Office> list = officeService.findList(office);
		return list;
	}

	/**
	 * 查看编辑机构
	 * @param office
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		// 创建并初始化下一个节点信息
		office = createNextNode(office);
		if (StringUtils.isNotBlank(office.getParentCode())) {
			office.setParent(officeService.get(office.getParentCode()));
		}
		if (office.getIsNewRecord()) {
			office.setTreeSort(30);
			Office where = new Office();
			where.setParentCode(office.getParentCode());
			Office last = officeService.getLastByParentCode(where);
			if (last != null){
				office.setTreeSort(last.getTreeSort() + 30);
				office.setViewCode(IdGen.nextCode(last.getViewCode()));
			}else if (office.getParent() != null){
				office.setViewCode(office.getParent().getViewCode() + "001");
			}
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public Office createNextNode(Office office) {
		if (StringUtils.isNotBlank(office.getParentCode())) {
			office.setParent(officeService.get(office.getParentCode()));
		}
		if (office.getIsNewRecord()) {
			Office where = new Office();
			where.setParentCode(office.getParentCode());
			Office last = officeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				office.setTreeSort(last.getTreeSort() + 30);
				office.setViewCode(IdGen.nextCode(last.getViewCode()));
			}else if(office.getParent() != null){
				office.setViewCode(office.getParent().getViewCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (office.getTreeSort() == null){
			office.setTreeSort(Office.DEFAULT_TREE_SORT);
		}
		return office;
	}

	/**
	 * 保存机构
	 * @param office
	 */
	@RequiresPermissions("sys:office:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Office office, String cmd, Model model) {
		officeService.save(office);
		return renderResult(Global.TRUE, text("保存机构''{0}''成功", office.getOfficeName()));
	}

	/**
	 * 停用机构
	 * @param office
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Office office) {
		Office where = new Office();
		where.setStatus(Office.STATUS_NORMAL);
		where.setParentCodes("," + office.getId() + ",");
		long count = officeService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该机构包含未停用的子机构！"));
		}
		office.setStatus(Office.STATUS_DISABLE);
		officeService.updateStatus(office);
		return renderResult(Global.TRUE, text("停用机构''{0}''成功", office.getOfficeName()));
	}

	/**
	 * 启用机构
	 * @param office
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Office office) {
		office.setStatus(Office.STATUS_NORMAL);
		officeService.updateStatus(office);
		return renderResult(Global.TRUE, text("启用机构''{0}''成功", office.getOfficeName()));
	}

	/**
	 * 删除机构
	 * @param office
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Office office) {
		officeService.delete(office);
		return renderResult(Global.TRUE, text("删除机构''{0}''成功", office.getOfficeName()));
	}

	/**
	 * 获取机构树结构数据
	 * @param excludeCode		排除的ID
	 * @param parentCode	上级Code
	 * @param isAll			是否显示所有机构（true：不进行权限过滤）
	 * @param officeTypes	机构类型（1：省级公司；2：市级公司；3：部门）
	 * @param companyCode	仅查询公司下的机构
	 * @param isShowCode	是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @param isShowFullName 是否显示全机构名称
	 * @param isLoadUser	是否加载机构下的用户
	 * @param postCode		机构下的用户过滤岗位
	 * @param roleCode		机构下的用户过滤角色
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, Boolean isAll,
			String officeTypes, String companyCode, String isShowCode, String isShowFullName,
			Boolean isLoadUser, String postCode, String roleCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		Office where = new Office();
		where.setStatus(Office.STATUS_NORMAL);
		where.setCompanyCode(companyCode);
		if (!(isAll != null && isAll)){
			officeService.addDataScopeFilter(where, ctrlPermi);
		}
		List<Office> list = officeService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			// 过滤非正常的数据
			if (!Office.STATUS_NORMAL.equals(e.getStatus())){
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
			// 根据父节点过滤数据
			if (StringUtils.isNotBlank(parentCode)){
				if (!e.getOfficeCode().equals(parentCode)){
					continue;
				}
				if (!e.getParentCodes().contains("," + parentCode + ",")){
					continue;
				}
			}
			// 根据部门类型过滤数据
			if (StringUtils.isNotBlank(officeTypes)){
				if (!StringUtils.inString(e.getOfficeType(), officeTypes.split(","))){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			String name = e.getOfficeName();
			if ("true".equals(isShowFullName) || "1".equals(isShowFullName)){
				name = e.getFullName();
			}
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getViewCode(), name));
			map.put("title", e.getFullName());
			// 一次性后台加载用户，提高性能(推荐方法)
			if (isLoadUser != null && isLoadUser) {
				map.put("isParent", true);
				List<Map<String, Object>> userList;
				userList = empUserController.treeData("u_", e.getOfficeCode(), e.getOfficeCode(), 
						companyCode, postCode, roleCode, isAll, isShowCode, ctrlPermi);
				mapList.addAll(userList);
			}
			mapList.add(map);
		}
		return mapList;
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData() {
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		officeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}

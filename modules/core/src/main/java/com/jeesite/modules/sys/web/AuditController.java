/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Audit;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.service.AuditService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 安全审计Controller
 * @author ThinkGem
 * @version 2020-3-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/audit")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
@Hidden
public class AuditController extends BaseController {

	@Autowired
	private AuditService auditService;

	/**
	 * 安全审计列表
	 */
	@RequiresPermissions("sys:audit:pwd")
	@RequestMapping(value = "list")
	public String auditList(Audit audit, Model model) {
		model.addAttribute("audit", audit);
		return "modules/sys/auditList";
	}

	/**
	 * 安全审计列表数据
	 */
	@RequiresPermissions("sys:audit:pwd")
	@ResponseBody
	@RequestMapping(value = "listData")
	public Page<Audit> auditListData(Audit audit, HttpServletRequest request, HttpServletResponse response) {
		audit.setPage(new Page<>(request, response));
		Page<Audit> page = auditService.findAuditPage(audit);
		return page;
	}

	/**
	 * 安全审计数据导出
	 */
	@RequiresPermissions("sys:audit:pwd")
	@RequestMapping(value = "exportData")
	public void auditExportData(Audit audit, HttpServletRequest request, HttpServletResponse response) {
		String fileName = "安全审计数据" + DateUtils.getDate("yyyyMMdd") + ".xlsx";
		audit.setPage(new Page<>(1, Page.PAGE_SIZE_NOT_PAGING, Page.COUNT_NOT_COUNT));
		List<Audit> list = auditService.findAuditPage(audit).getList();
		try (ExcelExport ee = new ExcelExport("安全审计数据", Audit.class)) {
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 根据权限查用户
	 */
	@RequiresPermissions("sys:audit:user")
	@RequestMapping(value = "userList")
	public String userList(Audit audit, Model model) {
		model.addAttribute("audit", audit);
		return "modules/sys/auditUserList";
	}

	/**
	 * 根据权限查用户数据
	 */
	@RequiresPermissions("sys:audit:user")
	@RequestMapping(value = "userListData")
	@ResponseBody
	public Page<Audit> userListData(Audit audit, HttpServletRequest request, HttpServletResponse response) {
		audit.setPage(new Page<>(request, response));
		Page<Audit> page = auditService.findUserPage(audit);
		return page;
	}
	
	/**
	 * 根据用户查权限
	 */
	@RequiresPermissions("sys:audit:menu")
	@RequestMapping(value = "menuList")
	public String menuList(Audit audit, Model model) {
		model.addAttribute("audit", audit);
		return "modules/sys/auditMenuList";
	}

	/**
	 * 根据用户查权限数据
	 */
	@RequiresPermissions("sys:audit:menu")
	@ResponseBody
	@RequestMapping(value = "menuTreeData")
	public Map<String, Object> menuListData(Audit audit) {
		Map<String, Object> model = MapUtils.newHashMap();
		List<Menu> menuList = auditService.findMenuList(audit);
		Map<String, List<Map<String, String>>> map = MapUtils.newLinkedHashMap();
		for (Menu menu : menuList){
			List<Map<String, String>> list = map.get(menu.getSysCode());
			if (list == null){
				list = ListUtils.newArrayList();
				map.put(menu.getSysCode(), list);
			}
			Map<String, String> m = MapUtils.newHashMap();
			m.put("id", menu.getMenuCode());
			m.put("pId", menu.getParentCode());
			m.put("name", menu.getMenuName() + "<font color=#888> &nbsp; &nbsp; "
					+ StringUtils.abbr(ObjectUtils.toString(menu.getPermission()) + " &nbsp; "
					+ ObjectUtils.toString(menu.getMenuHref()), 50) + "</font>");
			m.put("title", menu.getMenuName() + "&nbsp;" 
					+ ObjectUtils.toString(menu.getPermission()) + "\n"
					+ ObjectUtils.toString(menu.getMenuHref()));
			list.add(m);
		}
		model.put("menuMap", map);
		return model;
	}

}
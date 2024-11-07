/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web.user;

import com.alibaba.fastjson.JSONValidator;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.shiro.realm.AuthorizingRealm;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.modules.sys.service.*;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.ModuleUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 员工用户Controller
 * @author ThinkGem
 * @version 2017-03-26
 */
@Controller
@Tag(name = "EmpUser - 员工用户管理")
@RequestMapping(value = "${adminPath}/sys/empUser")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class EmpUserController extends BaseController {

	@Autowired
	private EmpUserService empUserService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@ModelAttribute
	public EmpUser get(String userCode, boolean isNewRecord) {
		return empUserService.get(userCode, isNewRecord);
	}

	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "index")
	public String index(EmpUser empUser, Model model) {
		model.addAttribute("empUser", empUser);
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/user/empUserIndex";
	}
	
	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "list")
	public String list(EmpUser empUser, Model model) {
		// 获取角色列表
		Role role = new Role();
		role.setUserType(User.USER_TYPE_EMPLOYEE);
		model.addAttribute("roleList", roleService.findList(role));
		// 获取岗位列表
		Post post = new Post();
		model.addAttribute("postList", postService.findList(post));
		model.addAttribute("empUser", empUser);
		// 获取控制权限类型
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/user/empUserList";
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<EmpUser> listData(EmpUser empUser,
								  @Parameter(description = "查询全部数据") @RequestParam(required = false) Boolean isAll,
								  @Parameter(description = "数据控制权限") @RequestParam(required = false) String ctrlPermi,
								  HttpServletRequest request, HttpServletResponse response) {
		empUser.getEmployee().getOffice().setIsQueryChildren(true);
		empUser.getEmployee().getCompany().setIsQueryChildren(true);
		if (!(isAll != null && isAll) || Global.isStrictMode()){
			empUserService.addDataScopeFilter(empUser, ctrlPermi);
		}
		empUser.setPage(new Page<>(request, response));
		Page<EmpUser> page = empUserService.findPage(empUser);
		return page;
	}

	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "form")
	public String form(EmpUser empUser, @Parameter(description = "操作类型") String op, Model model) {
		
		Employee employee = empUser.getEmployee();
		
		// 设置默认的部门
		if (StringUtils.isBlank(employee.getCompany().getCompanyCode())) {
			employee.setCompany(EmpUtils.getCompany());
		}
		
		// 设置默认的公司
		if (StringUtils.isBlank(employee.getOffice().getOfficeCode())) {
			employee.setOffice(EmpUtils.getOffice());
		}
		
		// 获取岗位列表
		Post post = new Post();
		model.addAttribute("postList", postService.findList(post));
		
		if (StringUtils.isNotBlank(employee.getEmpCode())){
			// 获取当前用户所拥有的岗位
			employee.setEmployeePostList(employeeService.findEmployeePostList(employee));
			// 获取当前员工关联的附属机构信息
			employee.setEmployeeOfficeList(employeeService.findEmployeeOfficeList(employee));
		}
		
		// 获取当前编辑用户的角色和权限
		if (StringUtils.inString(op, Global.OP_AUTH)) {
			
			// 获取当前用户所拥有的角色
			Role role = new Role();
			role.setUserCode(empUser.getUserCode());
			model.addAttribute("roleList", roleService.findListByUserCode(role));

		}
		
		// 操作类型：add: 全部； edit: 编辑； auth: 授权
		model.addAttribute("op", op);
		model.addAttribute("empUser", empUser);
		
		// 获取控制权限类型
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/user/empUserForm";
	}

	@RequiresPermissions(value={"sys:empUser:edit","sys:empUser:authRole"}, logical=Logical.OR)
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated EmpUser empUser, @Parameter(description = "操作类型") String op, HttpServletRequest request) {
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		EmpUser old = super.getWebDataBinderSource(request);
		if (!Global.TRUE.equals(userService.checkLoginCode(old != null ? old.getLoginCode() : "", empUser.getLoginCode()))) {
			return renderResult(Global.FALSE, text("保存用户失败，登录账号''{0}''已存在", empUser.getLoginCode()));
		}
		if (StringUtils.isBlank(empUser.getEmployee().getEmpNo())){
			empUser.getEmployee().setEmpNo(empUser.getLoginCode());
		}
		if (!Global.TRUE.equals(checkEmpNo(old != null ? old.getEmployee().getEmpNo() : "", empUser.getEmployee().getEmpNo()))) {
			return renderResult(Global.FALSE, text("保存用户失败，员工工号''{0}''已存在", empUser.getEmployee().getEmpNo()));
		}
		Subject subject = UserUtils.getSubject();
		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_EDIT) && subject.isPermitted("sys:empUser:edit")){
			empUserService.save(empUser);
		}
		if (StringUtils.inString(op, Global.OP_ADD, Global.OP_AUTH) && subject.isPermitted("sys:empUser:authRole")){
			userService.saveAuth(empUser);
		}
		return renderResult(Global.TRUE, text("保存用户''{0}''成功", empUser.getUserName()));
	}
	
	/**
	 * 验证工号是否有效
	 * @param oldEmpNo
	 * @param empNo
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "checkEmpNo")
	@ResponseBody
	public String checkEmpNo(@Parameter(description = "旧员工编号") @RequestParam String oldEmpNo,
							 @Parameter(description = "新员工编号") @RequestParam("employee.empNo") String empNo) {
		Employee employee = new Employee();
		employee.setEmpNo(empNo);
		if (empNo != null && empNo.equals(oldEmpNo)) {
			return Global.TRUE;
		} else if (empNo != null && employeeService.getByEmpNo(employee) == null) {
			return Global.TRUE;
		}
		return Global.FALSE;
	}

	/**
	 * 导出用户数据
	 */
	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "exportData")
	public void exportData(EmpUser empUser, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
		empUser.getEmployee().getOffice().setIsQueryChildren(true);
		empUser.getEmployee().getCompany().setIsQueryChildren(true);
		if (!(isAll != null && isAll) || Global.isStrictMode()){
			empUserService.addDataScopeFilter(empUser, ctrlPermi);
		}
		List<EmpUser> list = empUserService.findList(empUser);
		String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("用户数据", EmpUser.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 下载导入用户数据模板
	 */
	@RequiresPermissions("sys:empUser:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		EmpUser empUser = new EmpUser();
		User user = UserUtils.getUser();
		if (User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			empUser = empUserService.get(user.getUserCode());
		}else{
			BeanUtils.copyProperties(user, empUser);
		}
		List<EmpUser> list = ListUtils.newArrayList(empUser);
		String fileName = "用户数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("用户数据", EmpUser.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	/**
	 * 导入用户数据
	 */
	@ResponseBody
	@RequiresPermissions("sys:empUser:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = empUserService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	
	/**
	 * 停用用户
	 * @param empUser
	 * @return
	 */
	@RequiresPermissions("sys:empUser:updateStatus")
	@ResponseBody
	@RequestMapping(value = "disable")
	public String disable(EmpUser empUser, boolean freeze) {
		if (User.isSuperAdmin(empUser.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		String text = freeze ? "冻结" : "停用";
		if (empUser.currentUser().getUserCode().equals(empUser.getUserCode())) {
			return renderResult(Global.FALSE, text(text + "用户失败，不允许" + text + "当前用户"));
		}
		empUser.setStatus(freeze ? User.STATUS_FREEZE : User.STATUS_DISABLE);
		empUserService.updateStatus(empUser);
		return renderResult(Global.TRUE, text(text + "用户''{0}''成功", empUser.getUserName()));
	}
	
	/**
	 * 启用用户
	 * @param empUser
	 * @return
	 */
	@RequiresPermissions("sys:empUser:updateStatus")
	@ResponseBody
	@RequestMapping(value = "enable")
	public String enable(EmpUser empUser, boolean freeze) {
		if (User.isSuperAdmin(empUser.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		String text = freeze ? "解冻" : "启用";
		empUser.setStatus(User.STATUS_NORMAL);
		empUserService.updateStatus(empUser);
		AuthorizingRealm.isValidCodeLogin(empUser.getLoginCode(), empUser.getCorpCode_(), null, "success");
		return renderResult(Global.TRUE, text(text + "用户''{0}''成功", empUser.getUserName()));
	}
	
	/**
	 * 密码重置
	 * @param empUser
	 * @return
	 */
	@RequiresPermissions("sys:empUser:resetpwd")
	@RequestMapping(value = "resetpwd")
	@ResponseBody
	public String resetpwd(EmpUser empUser) {
		if (User.isSuperAdmin(empUser.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		userService.updatePassword(empUser.getUserCode(), null);
		AuthorizingRealm.isValidCodeLogin(empUser.getLoginCode(), empUser.getCorpCode_(), null, "success");
		return renderResult(Global.TRUE, text("重置用户''{0}''密码成功", empUser.getUserName()));
	}

	/**
	 * 删除用户
	 * @param empUser
	 * @return
	 */
	@RequiresPermissions("sys:empUser:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(EmpUser empUser) {
		if (User.isSuperAdmin(empUser.getUserCode())) {
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		if (empUser.currentUser().getUserCode().equals(empUser.getUserCode())) {
			return renderResult(Global.FALSE, text("删除用户失败，不允许删除当前用户"));
		}
		empUserService.delete(empUser);
		return renderResult(Global.TRUE, text("删除用户''{0}''成功", empUser.getUserName()));
	}
	
	/** 
	 * 用户授权数据权限
	 */
	@RequiresPermissions("sys:empUser:authDataScope")
	@RequestMapping(value = "formAuthDataScope")
	public String formAuthDataScope(EmpUser empUser, Model model, HttpServletRequest request) {
		UserDataScope userDataScope = new UserDataScope();
		userDataScope.setUserCode(empUser.getUserCode());
		userDataScope.setCtrlPermi(UserDataScope.CTRL_PERMI_HAVE);
		List<UserDataScope> userDataScopeList = userService.findDataScopeList(userDataScope);
		model.addAttribute("userDataScopeList", userDataScopeList);
		model.addAttribute("empUser", empUser);
		model.addAttribute("moduleCodes", ModuleUtils.getEnableModuleCodes());
		model.addAttribute("dataScopes", JsonMapper.fromJson(Global.getConfig("user.dataScopes", "[]"), List.class));
		model.addAttribute("ctrlPermi", Global.getConfig("user.adminCtrlPermi", "2"));
		return "modules/sys/user/empUserFormAuthDataScope";
	}
	
	/** 
	 * 保存用户授权数据权限
	 */
	@RequiresPermissions("sys:empUser:authDataScope")
	@RequestMapping(value = "saveAuthDataScope")
	@ResponseBody
	public String saveAuthDataScope(EmpUser empUser, HttpServletRequest request) {
		if (!EmpUser.USER_TYPE_EMPLOYEE.equals(empUser.getUserType())){
			return renderResult(Global.FALSE, "非法操作，不能够操作此用户！");
		}
		empUser.setMgrType(User.MGR_TYPE_NOT_ADMIN);
		userService.saveAuthDataScope(empUser);
		return renderResult(Global.TRUE, text("用户分配数据权限成功"));
	}

	/**
	 * 根据机构查询用户树格式
	 * @param idPrefix id前缀，默认 u_
	 * @param officeCode 机构Code
	 * @param companyCode 公司Code
	 * @param postCode 岗位Code
	 * @param roleCode 角色Code
	 * @param isAll 是否显示所有用户（true：不进行权限过滤）
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String idPrefix,
			String[] officeCode, String companyCode, String postCode, String roleCode, 
			Boolean isAll, String isShowCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		EmpUser empUser = new EmpUser();
		Employee employee = empUser.getEmployee();
		if (officeCode != null && officeCode.length == 1) {
			employee.getOffice().setOfficeCode(officeCode[0]);
		}else {
			employee.getOffice().setId_in(officeCode);
		}
		employee.getOffice().setIsQueryChildren(false);
		employee.getCompany().setCompanyCode(companyCode);
		employee.getCompany().setIsQueryChildren(false);
		employee.setPostCode(postCode);
		empUser.setRoleCode(roleCode);
		empUser.setStatus(User.STATUS_NORMAL);
		empUser.setUserType(User.USER_TYPE_EMPLOYEE);
		if (!(isAll != null && isAll) || Global.isStrictMode()) {
			empUserService.addDataScopeFilter(empUser, ctrlPermi);
		}
		List<EmpUser> list = empUserService.findList(empUser);
		for (int i = 0; i < list.size(); i++) {
			EmpUser e = list.get(i);
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", ObjectUtils.defaultIfNull(idPrefix, "u_") + e.getId());
			map.put("pId", StringUtils.defaultIfBlank(e.getEmployee().getOffice().getOfficeCode(), "0"));
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getLoginCode(), e.getUserName()));
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 选择员工对话框
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "empUserSelect")
	public String empUserSelect(EmpUser empUser, String selectData, Model model) {
		String selectDataJson = EncodeUtils.decodeUrl(selectData);
		if (selectDataJson != null && JSONValidator.from(selectDataJson).validate()){
			model.addAttribute("selectData", selectDataJson);
		}
		// 获取角色列表
//		Role role = new Role();
//		role.setUserType(User.USER_TYPE_MEMBER);
//		model.addAttribute("roleList", roleService.findList(role));
		model.addAttribute("empUser", empUser);
		return "modules/sys/user/empUserSelect";
	}

	/**
	 * 获取当前用户附属部门
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "officeListData")
	@ResponseBody
	public List<EmployeeOffice> officeListData () {
		Employee employee = EmpUtils.getEmployee();
		return employeeService.findEmployeeOfficeList(employee);
	}


	/**
	 * 切换当前用户到附属部门
	 * @param officeCode
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "switchOffice/{officeCode}")
	@ResponseBody
	public String switchOffice(@PathVariable String officeCode) {
		if (StringUtils.equals(officeCode, "default")) {
			Office office = EmpUtils.getOffice();
			EmpUtils.setCurrentOffice(office.getOfficeCode(), office.getOfficeName());
			return renderResult(Global.TRUE, text("部门切换成功！"));
		}
		List<EmployeeOffice> employeeOfficeList = EmpUtils.getEmployeeOfficeList().stream()
				.filter(e -> StringUtils.equals(e.getOfficeCode(), officeCode)).collect(Collectors.toList());
		if (!employeeOfficeList.isEmpty()) {
			EmployeeOffice office = employeeOfficeList.get(0);
			EmpUtils.setCurrentOffice(office.getOfficeCode(), office.getOfficeName());
			return renderResult(Global.TRUE, text("部门切换成功！"));
		} else {
			return renderResult(Global.FALSE, text("部门切换失败，所切换部门不是您的附属部门！"));
		}
	}
	
}

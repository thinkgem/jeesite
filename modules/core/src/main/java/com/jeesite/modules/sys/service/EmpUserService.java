/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.sys.dao.EmpUserDao;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 员工管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
@Service
@Transactional(readOnly=true)
public class EmpUserService extends CrudService<EmpUserDao, EmpUser> {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 获取单条数据
	 */
	@Override
	public EmpUser get(EmpUser empUser) {
		return super.get(empUser);
	}
	
	/**
	 * 添加数据权限过滤条件
	 * @param entity 控制对象
	 * @param ctrlPermi 控制权限类型（拥有的数据权限：DataScope.CTRL_PERMI_HAVE、可管理的数据权限：DataScope.CTRL_PERMI_HAVE）
	 */
	@Override
	public void addDataScopeFilter(EmpUser empUser, String ctrlPermi) {
		empUser.getSqlMap().getDataScope().addFilter("dsfOffice",
				"Office", "e.office_code", "a.create_by", ctrlPermi);
		if (StringUtils.isNotBlank(EmpUtils.getCompany().getCompanyCode())){
			empUser.getSqlMap().getDataScope().addFilter("dsfCompany",
					"Company", "e.company_code", "a.create_by", ctrlPermi);
		}
	}

	/**
	 * 分页查询数据
	 */
	@Override
	public Page<EmpUser> findPage(Page<EmpUser> page, EmpUser empUser) {
		return super.findPage(page, empUser);
	}

	/**
	 * 保存用户员工
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(EmpUser user) {
		// 1、初始化用户信息
		if (user.getIsNewRecord()){
			userService.genId(user, user.getLoginCode());
			user.setUserCode(user.getUserCode()+"_"+IdGen.randomBase62(4).toLowerCase());
			user.setUserType(EmpUser.USER_TYPE_EMPLOYEE);
			user.setMgrType(EmpUser.MGR_TYPE_NOT_ADMIN);
		}
		Employee employee = user.getEmployee();
		// 如果员工编码为空，则使用用户编码
		if (StringUtils.isBlank(employee.getEmpCode())){
			employee.setEmpCode(user.getUserCode());
		}
		// 如果员工姓名为空，则使用昵称名
		if (StringUtils.isBlank(employee.getEmpName())){
			employee.setEmpName(user.getUserName());
		}
		// 2、保存用户
		user.setRefCode(employee.getEmpCode());
		user.setRefName(employee.getEmpName());
		userService.save(user);
		// 3、保存员工
		employee.setIsNewRecord(user.getIsNewRecord());
		employeeService.save(employee);
	}

	/**
	 * 导入用户数据
	 * @param file 导入的用户数据文件
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 */
	@Transactional(readOnly=false)
	public String importData(MultipartFile file, Boolean isUpdateSupport) {
		if (file == null){
			throw new ServiceException("请选择导入的数据文件！");
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<EmpUser> list = ei.getDataList(EmpUser.class);
			for (EmpUser user : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(user);
					// 验证是否存在这个用户
					User u = UserUtils.getByLoginCode(user.getLoginCode());
					if (u == null){
						this.save(user);
						successNum++;
						successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginCode() + " 导入成功");
					} else if (isUpdateSupport){
						user.setUserCode(u.getUserCode());
						this.save(user);
						successNum++;
						successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginCode() + " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、账号 " + user.getLoginCode() + " 导入失败：";
					if (e instanceof ConstraintViolationException){
						List<String> messageList = ValidatorUtils.extractPropertyAndMessageAsList((ConstraintViolationException)e, ": ");
						for (String message : messageList) {
							msg += message + "; ";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

	/**
	 * 更新状态
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(EmpUser empUser) {
		userService.delete(empUser);
		employeeService.delete(empUser.getEmployee());
	}
	
	/**
	 * 删除用户
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(EmpUser empUser) {
		userService.delete(empUser);
		employeeService.delete(empUser.getEmployee());
	}
	
}
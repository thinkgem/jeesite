/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.utils;

import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.EmployeeOffice;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.EmployeeService;
import com.jeesite.modules.sys.service.OfficeService;

/**
 * 员工工具类
 * @author ThinkGem
 * @version 2016年11月2日
 */
public class EmpUtils {

	// 部门和公司缓存常量
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	public static final String CACHE_COMPANY_ALL_LIST = "companyAllList";
	public static final String CACHE_COMPANY_OFFICE_LIST = "employeeOfficeList";
	
	/**
	 * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
	 */
	private static final class Static {
		private static OfficeService officeService = SpringUtils.getBean(OfficeService.class);
		private static CompanyService companyService = SpringUtils.getBean(CompanyService.class);
		private static EmployeeService employeeService = SpringUtils.getBean(EmployeeService.class);
	}
	
	/**
	 * 根据员工编码获取员工
	 * @author ThinkGem
	 */
	public static Employee get(String empCode){
		return Static.employeeService.get(empCode);
	}
	
	/**
	 * 根据用户对象获取员工，不是员工返回null
	 * @author ThinkGem
	 */
	public static Employee get(User user){
		if (user != null && User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			return (Employee)user.getRefObj();
		}
		return null;
	}
	
	/**
	 * 根据用户编码获取员工，找不到或不是员工返回null
	 * @author ThinkGem
	 */
	public static Employee getByUserCode(String userCode){
		User user = UserUtils.get(userCode);
		Employee employee = get(user);
		return employee;
	}
	
	/**
	 * 根据登录账号获取员工，找不到或不是员工返回null
	 * @author ThinkGem
	 */
	public static Employee getByLoginCode(String loginCode){
		User user = UserUtils.getByLoginCode(loginCode);
		Employee employee = get(user);
		return employee;
	}
	
	/**
	 * 获取当前登录的员工
	 * @author ThinkGem
	 */
	public static Employee getEmployee(){
		User user = UserUtils.getUser();
		Employee employee = get(user);
		if (employee == null){
			employee = new Employee();
		}
		return employee;
	}

	/**
	 * 获取当前附属部门对象列表
	 */
	public static List<EmployeeOffice> getEmployeeOfficeList(){
		List<EmployeeOffice> list = UserUtils.getCache(CACHE_COMPANY_OFFICE_LIST);
		if (list == null){
			list = Static.employeeService.findEmployeeOfficeList(getEmployee());
			UserUtils.putCache(CACHE_COMPANY_OFFICE_LIST, list);
		}
		return list;
	}
	
	/**
	 * 获取所有部门编码，包括附属部门（数据权限用）
	 * @return
	 * @author ThinkGem
	 */
	public static String[] getOfficeCodes(){
		List<String> list = ListUtils.newArrayList();
		list.add(getOffice().getOfficeCode());
		getEmployeeOfficeList().forEach(e -> {
			list.add(e.getOfficeCode());
		});
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 获取所有部门编码，包括附属部门（数据权限用）
	 * @return
	 * @author ThinkGem
	 */
	public static String[] getOfficeParentCodess(){
		List<String> list = ListUtils.newArrayList();
		list.add(getOffice().getParentCodes());
		getEmployeeOfficeList().forEach(e -> {
			list.add(e.getParentCodes());
		});
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 获取部门对象
	 * @param officeCode
	 * @return
	 */
	public static Office getOffice(String officeCode){
		List<Office> officeList = getOfficeAllList();
		for (Office office : officeList){
			if (office.getOfficeCode().equals(officeCode)){
				return office;
			}
		}
		return null;
	}
	
	/**
	 * 获取当前员工附属部门
	 */
	public static Office getOffice(){
		return getEmployee().getOffice();
	}

	/**
	 * 获取所有的机构
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)CorpUtils.getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			Office where = new Office();
			where.setStatus(Office.STATUS_NORMAL);
			officeList = Static.officeService.findList(where);
			CorpUtils.putCache(CACHE_OFFICE_ALL_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前公司对象
	 */
	public static Company getCompany(){
		return getEmployee().getCompany();
	}
	
	/**
	 * 获取公司对象
	 * @param companyCode
	 * @return
	 */
	public static Company getCompany(String companyCode){
		List<Company> companyList = getCompanyAllList();
		for (Company company : companyList){
			if (company.getCompanyCode().equals(companyCode)){
				return company;
			}
		}
		return null;
	}
	
	/**
	 * 获取所有的公司
	 * @return
	 */
	public static List<Company> getCompanyAllList(){
		@SuppressWarnings("unchecked")
		List<Company> companyList = (List<Company>)CorpUtils.getCache(CACHE_COMPANY_ALL_LIST);
		if (companyList == null){
			Company where = new Company();
			where.setStatus(Office.STATUS_NORMAL);
			companyList = Static.companyService.findList(where);
			CorpUtils.putCache(CACHE_COMPANY_ALL_LIST, companyList);
		}
		return companyList;
	}
	
	/**
	 * 清除指定用户缓存，不包括改用的SESSION缓存
	 * @param user
	 */
	public static void removeCache(String key){
		if (StringUtils.inString(key, CACHE_OFFICE_ALL_LIST, CACHE_COMPANY_ALL_LIST)){
			CorpUtils.removeCache(key);
		}
	}
}

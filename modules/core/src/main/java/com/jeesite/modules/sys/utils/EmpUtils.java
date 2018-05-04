/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.utils;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.OfficeService;

/**
 * 员工工具类
 * @author ThinkGem
 * @version 2016年11月2日
 */
public class EmpUtils {

//	// 用户缓存常量
//	public static final String CACHE_OFFICE_LIST = "officeList";
//	public static final String CACHE_COMPANY_LIST = "companyList";
	
	// 部门和公司缓存常量
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	public static final String CACHE_COMPANY_ALL_LIST = "companyAllList";
	
	/**
	 * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
	 */
	private static final class Static {
		private static OfficeService officeService = SpringUtils.getBean(OfficeService.class);
		private static CompanyService companyService = SpringUtils.getBean(CompanyService.class);
	}
	
	/**
	 * 获取当前登录的员工
	 * @author ThinkGem
	 */
	public static Employee getEmployee(){
		User user = UserUtils.getUser();
		Employee employee = null;
		if (User.USER_TYPE_EMPLOYEE.equals(user.getUserType())){
			employee = (Employee)UserUtils.getUser().getRefObj();
		}
		if (employee == null){
			employee = new Employee();
		}
		return employee;
	}

	/**
	 * 获取当前部门对象
	 */
	public static Office getOffice(){
		return getEmployee().getOffice();
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
//	
//	/**
//	 * 获取当前用户有权限访问的机构
//	 * @return
//	 */
//	public static List<Office> getOfficeList(){
//		@SuppressWarnings("unchecked")
//		List<Office> officeList = (List<Office>)UserUtils.getCache(CACHE_OFFICE_LIST);
//		if (officeList == null){
//			User user = UserUtils.getUser();
//			if (user.isAdmin()){
//				officeList = officeService.findList(new Office());
//			}else{
//				Office office = new Office();
//				// 添加数据权限过滤条件
//				officeService.addDataScopeFilter(office);
//				officeList = officeService.findList(office);
//			}
//			UserUtils.putCache(CACHE_OFFICE_LIST, officeList);
//		}
//		return officeList;
//	}

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
//	
//	/**
//	 * 获取当前用户授权的公司
//	 * @return
//	 */
//	public static List<Company> getCompanyList(){
//		@SuppressWarnings("unchecked")
//		List<Company> companyList = (List<Company>)UserUtils.getCache(CACHE_COMPANY_LIST);
//		if (companyList == null){
//			User user = UserUtils.getUser();
//			if (user.isAdmin()){
//				companyList = companyService.findList(new Company());
//			}else{
//				Company company = new Company();
//				// 添加数据权限过滤条件
//				companyService.addDataScopeFilter(company);
//				companyList = companyService.findList(company);
//			}
//			UserUtils.putCache(CACHE_COMPANY_LIST, companyList);
//		}
//		
//		return companyList;
//	}
	
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
//		if (StringUtils.inString(key, CACHE_OFFICE_LIST, CACHE_COMPANY_LIST)){
//			UserUtils.removeCache(key);
//		}else 
		if (StringUtils.inString(key, CACHE_OFFICE_ALL_LIST, CACHE_COMPANY_ALL_LIST)){
			CorpUtils.removeCache(key);
		}
	}
}

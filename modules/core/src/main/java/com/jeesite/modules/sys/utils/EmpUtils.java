/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.utils;

import java.util.List;
import java.util.Set;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
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
 * 员工部门工具类
 * @author ThinkGem
 * @version 2020-5-20
 */
public class EmpUtils {

	// 机构和公司缓存常量
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
			return user.getRefObj();
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
	 * 获取当前附属机构对象列表
	 * @author ThinkGem
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
	 * 根据机构编码获取机构对象
	 * @param officeCode
	 * @author ThinkGem
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
	 * 获取当前员工机构
	 * @author ThinkGem
	 */
	public static Office getOffice(){
		return getEmployee().getOffice();
	}

	/**
	 * 获取当前员工所有的机构
	 * @author ThinkGem
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
	 * 获取当前员工所有机构编码，包括附属机构（数据权限用）
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
	 * 获取当前员工所有机构编码，包括附属机构以及子机构（数据权限用）V4.2.0
	 * @author ThinkGem
	 */
	public static String[] getOfficeCodesAndChildren(){
		Set<String> list = SetUtils.newLinkedHashSet();
		Set<String> parentCodess = SetUtils.newHashSet();
		Office currentOffice = getOffice();
		list.add(currentOffice.getOfficeCode());
		parentCodess.add(currentOffice.getParentCodes() + currentOffice.getOfficeCode() + ",");
		// 添加附属机构
		getEmployeeOfficeList().forEach(e -> {
			list.add(e.getOfficeCode());
			parentCodess.add(e.getParentCodes() + e.getOfficeCode() + ",");
		});
		// 查找并添加子机构
		getOfficeAllList().forEach(e -> {
			for (String parentCodes : parentCodess) {
				if (e.getParentCodes().startsWith(parentCodes)) {
					list.add(e.getOfficeCode());
					break;
				}
			}
		});
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 根据机构类型，获取当前员工所有机构编码，包括附属机构（数据权限用）
	 * @author ThinkGem
	 */
	public static String[] getOfficeCodesByType(String type){
		List<String> list = ListUtils.newArrayList();
		Office office = getOffice();
		if (type.equals(office.getOfficeType())){
			list.add(office.getOfficeCode());
		}else{
			Office parent = getOffice().getParentByType(type);
			if (parent != null){
				list.add(parent.getOfficeCode());
			}
		}
		getEmployeeOfficeList().forEach(e -> {
			Office office2 = getOffice(e.getOfficeCode());
			if (type.equals(office2.getOfficeType())){
				list.add(office2.getOfficeCode());
			}else{
				Office parent2 = office2.getParentByType(type);
				if (parent2 != null){
					list.add(parent2.getOfficeCode());
				}
			}
		});
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 获取当前员工所有上级机构编码，包括附属机构（数据权限用）
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
	 * 根据机构类型，获取当前员工所有机构编码，包括附属机构（数据权限用）
	 * @author ThinkGem
	 */
	public static String[] getOfficeParentCodessByType(String type){
		List<String> list = ListUtils.newArrayList();
		Office office = getOffice();
		if (type.equals(office.getOfficeType())){
			list.add(office.getParentCodes());
		}else{
			Office parent = getOffice().getParentByType(type);
			if (parent != null){
				list.add(parent.getParentCodes());
			}
		}
		getEmployeeOfficeList().forEach(e -> {
			Office office2 = getOffice(e.getOfficeCode());
			if (type.equals(office2.getOfficeType())){
				list.add(office2.getParentCodes());
			}else{
				Office parent2 = office2.getParentByType(type);
				if (parent2 != null){
					list.add(parent2.getParentCodes());
				}
			}
		});
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 根据公司编码获取公司对象
	 * @param companyCode
	 * @author ThinkGem
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
	 * 获取当前员工公司对象
	 * @author ThinkGem
	 */
	public static Company getCompany(){
		return getEmployee().getCompany();
	}

	/**
	 * 获取当前员工所有的公司
	 * @author ThinkGem
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
	 * 获取当前员工所有公司编码，包括子公司（数据权限用）V4.2.0
	 * @author ThinkGem
	 */
	public static String[] getCompanyCodesAndChildren(){
		Set<String> list = SetUtils.newLinkedHashSet();
		Set<String> parentCodess = SetUtils.newHashSet();
		Company currentCompany = getCompany();
		list.add(currentCompany.getCompanyCode());
		parentCodess.add(currentCompany.getParentCodes() + currentCompany.getCompanyCode() + ",");
		// 查找并添加子公司
		getCompanyAllList().forEach(e -> {
			for (String parentCodes : parentCodess) {
				if (e.getParentCodes().startsWith(parentCodes)) {
					list.add(e.getCompanyCode());
					break;
				}
			}
		});
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 清除指定用户缓存，不包括改用的SESSION缓存
	 * @author ThinkGem
	 */
	public static void removeCache(String key){
		if (StringUtils.inString(key, CACHE_OFFICE_ALL_LIST, CACHE_COMPANY_ALL_LIST)){
			CorpUtils.removeCache(key);
		}
	}
}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.autoconfigure.sys;

import com.jeesite.common.mybatis.MyBatisFactoryBean;
import com.jeesite.modules.sys.dao.CompanyOfficeDao;
import com.jeesite.modules.sys.dao.EmployeeOfficeDao;
import com.jeesite.modules.sys.dao.EmployeePostDao;
import com.jeesite.modules.sys.dao.PostRoleDao;
import com.jeesite.modules.sys.service.*;
import com.jeesite.modules.sys.service.support.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 系统核心实现类
 * @author ThinkGem
 * @version 2018-10-13
 */
@AutoConfiguration
@ConditionalOnBean(MyBatisFactoryBean.class)
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public class SysAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public AreaService areaService(){
		return new AreaServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CompanyService companyService(CompanyOfficeDao companyOfficeDao, DataScopeService dataScopeService, EmpUserService empUserService){
		return new CompanyServiceSupport(companyOfficeDao, dataScopeService, empUserService);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public EmployeeService employeeService(EmployeePostDao employeePostDao, EmployeeOfficeDao employeeOfficeDao){
		return new EmployeeServiceSupport(employeePostDao, employeeOfficeDao);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public EmpUserService empUserService(UserService userService, EmployeeService employeeService, EmployeeOfficeDao employeeOfficeDao){
		return new EmpUserServiceSupport(userService, employeeService, employeeOfficeDao);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public LogService logService(){
		return new LogServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public OfficeService officeService(DataScopeService dataScopeService, EmpUserService empUserService){
		return new OfficeServiceSupport(dataScopeService, empUserService);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PostService postService(PostRoleDao postRoleDao, EmpUserService empUserService){
		return new PostServiceSupport(postRoleDao, empUserService);
	}

}

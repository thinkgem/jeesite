/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.autoconfigure.sys;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.EmployeeService;
import com.jeesite.modules.sys.service.LogService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.service.PostService;
import com.jeesite.modules.sys.service.support.AreaServiceSupport;
import com.jeesite.modules.sys.service.support.CompanyServiceSupport;
import com.jeesite.modules.sys.service.support.EmpUserServiceSupport;
import com.jeesite.modules.sys.service.support.EmployeeServiceSupport;
import com.jeesite.modules.sys.service.support.LogServiceSupport;
import com.jeesite.modules.sys.service.support.OfficeServiceSupport;
import com.jeesite.modules.sys.service.support.PostServiceSupport;

/**
 * 系统核心实现类
 * @author ThinkGem
 * @version 2018-10-13
 */
@Configuration
public class SysAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public AreaService areaService(){
		return new AreaServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CompanyService companyService(){
		return new CompanyServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public EmployeeService employeeService(){
		return new EmployeeServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public EmpUserService empUserService(){
		return new EmpUserServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public LogService logService(){
		return new LogServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public OfficeService officeService(){
		return new OfficeServiceSupport();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PostService postService(){
		return new PostServiceSupport();
	}
	
}

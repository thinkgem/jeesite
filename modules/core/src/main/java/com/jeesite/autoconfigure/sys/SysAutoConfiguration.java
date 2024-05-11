/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.autoconfigure.sys;

import com.jeesite.common.mybatis.MyBatisFactoryBean;
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

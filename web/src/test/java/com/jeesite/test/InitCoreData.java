/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.modules.config.Application;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2017-10-22
 */
@ActiveProfiles("test")
@SpringBootTest(classes=Application.class)
@Rollback(false)
public class InitCoreData extends com.jeesite.modules.sys.db.InitCoreData {
	
	@Test
	public void initCoreData() throws Exception{
		createTable();
		initLog();
		initArea();
		initConfig();
		initModule();
		initDict();
		initRole();
		initMenu();
		initUser();
		initOffice();
		initCompany();
		initPost();
		initEmpUser();
	}
	
}

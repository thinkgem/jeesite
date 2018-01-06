/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.test.annotation.Commit;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2017-10-22
 */
@Commit
public class InitCoreData extends com.jeesite.modules.db.InitCoreData {
	
	@Test
	public void initCoreData() throws Exception{
		initLog();
		initConfig();
		initModule();
		initDict();
		initRole();
		initMenu();
		initUser();
//		initArea();
		initOffice();
		initCompany();
		initPost();
		initEmpUser();
	}
	
}

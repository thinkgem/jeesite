/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.modules.Application;

/**
 * 初始化文件管理表数据
 * @author ThinkGem
 * @version 2019-4-7
 */
@ActiveProfiles("test")
@SpringBootTest(classes=Application.class)
@Rollback(false)
public class InitFilemanagerData extends com.jeesite.modules.filemanager.db.InitFilemanagerData {
	
	@Test
	public void initFilemanagerData() throws Exception{
		createTable();
		initFilemanagerFolder();
	}
	
}

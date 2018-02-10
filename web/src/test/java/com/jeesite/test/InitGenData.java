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
 * 初始化代码生成表测试数据
 * @author ThinkGem
 * @version 2017-10-22
 */
@ActiveProfiles("test")
@SpringBootTest(classes=Application.class)
@Rollback(false)
public class InitGenData extends com.jeesite.modules.gen.db.InitGenData {

	@Test
	public void initGenData() throws Exception{
		createGenTable();
		initGenTestData();
		initGenTreeData();
	}

}

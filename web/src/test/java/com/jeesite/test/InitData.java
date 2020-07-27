/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.Application;

/**
 * 初始化数据表
 * @author ThinkGem
 * @version 2020-5-26
 */
@ActiveProfiles("test")
@SpringBootTest(classes=Application.class)
public class InitData extends BaseInitDataTests {
	
	@Test
	public void initData01() throws Exception{
		logger.info("数据库初始化完成。");
	}

	@Override
	public void initProperty() {
		System.setProperty("jeesite.initdata", "true");
	}
	
}

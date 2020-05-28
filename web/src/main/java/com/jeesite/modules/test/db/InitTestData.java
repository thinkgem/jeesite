/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.gen.utils.GenUtils;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2020-5-26
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitTestData extends BaseInitDataTests {

	@Override
	public boolean initData() throws Exception {
		if (GenUtils.isTableExists("test_data")) {
			return true; // 如果表已存在，则无需初始化
		}
		runCreateScript("test.sql");
		return true;
	}
	
}

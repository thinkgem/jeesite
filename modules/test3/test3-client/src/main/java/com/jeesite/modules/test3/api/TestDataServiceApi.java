/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test3.api;

import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.service.rest.CrudServiceRest;
import com.jeesite.modules.test3.entity.TestData;

/**
 * 测试数据API
 * @author ThinkGem
 * @version 2021-06-22
 */
@RequestMapping(value = "/inner/api/test3/testData")
public interface TestDataServiceApi extends CrudServiceRest<TestData> {
	
}
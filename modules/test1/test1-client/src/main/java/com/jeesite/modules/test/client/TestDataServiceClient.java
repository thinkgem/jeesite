/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.modules.test.api.TestDataServiceApi;

/**
 * TestDataClient
 * @author ThinkGem
 * @version 2018-10-18
 */
@FeignClient(name="jeesite-cloud-module-test1")
@RequestMapping(value = "/js/api/test1/testData")
public interface TestDataServiceClient extends TestDataServiceApi {
	
}

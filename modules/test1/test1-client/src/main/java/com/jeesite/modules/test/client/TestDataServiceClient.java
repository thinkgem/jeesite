/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.test.api.TestDataServiceApi;

/**
 * TestDataClient
 * @author ThinkGem
 * @version 2018-10-18
 */
@FeignClient(name="${service.test1.name}", path="${service.test1.path}")
@ConditionalOnNotCurrentApplication(name="${service.test1.name}")
public interface TestDataServiceClient extends TestDataServiceApi {
	
}

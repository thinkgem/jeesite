/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.test.api.TestTreeServiceApi;

/**
 * TestDataClient
 * @author ThinkGem
 * @version 2018-10-18
 */
@FeignClient(name="${service.test2.name}", path="${service.test2.path}")
@ConditionalOnNotCurrentApplication(name="${service.test2.name}")
public interface TestTreeServiceClient extends TestTreeServiceApi {
	
}

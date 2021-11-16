/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test3.client;

import org.springframework.cloud.openfeign.FeignClient;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.jeesite.modules.test3.api.TestDataServiceApi;

/**
 * 测试数据API
 * @author ThinkGem
 * @version 2021-06-22
 */
@FeignClient(name="${service.test3.name}", path="${service.test3.path}")
@ConditionalOnNotCurrentApplication(name="${service.test3.name}")
public interface TestDataServiceClient extends TestDataServiceApi {
	
}
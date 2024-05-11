/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.autoconfigure.sys;

import com.jeesite.common.mybatis.MyBatisFactoryBean;
import com.jeesite.modules.msg.service.MsgInnerService;
import com.jeesite.modules.msg.service.support.MsgInnerServiceSupport;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 系统核心实现类
 * @author ThinkGem
 * @version 2018-10-13
 */
@AutoConfiguration
@ConditionalOnBean(MyBatisFactoryBean.class)
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public class MsgAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MsgInnerService msgInnerService(){
		return new MsgInnerServiceSupport();
	}

}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.task;

import org.springframework.context.annotation.Configuration;

/**
 * 消息服务，如果需要支持定时任务，则要在作业管理里添加该任务：msgLocalSendTask.execute(); 
 * @author ThinkGem
 * @version 2018年1月10日
 */
@Configuration
public class MsgTaskConfig {

//	@Bean
//	public MsgLocalSendTask msgLocalSendTask(){
//		MsgLocalSendTask bean = new MsgLocalSendTask();
//		bean.setSmsDemoSendService(new SmsDemoSendService());
//		bean.setEmailSendService(new EmailSendService());
//		bean.setWeixinSendService(new WeixinSendService());
//		return bean;
//	}
    
}

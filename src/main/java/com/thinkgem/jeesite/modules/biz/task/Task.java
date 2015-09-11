package com.thinkgem.jeesite.modules.biz.task;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component 
@Lazy(false)
public class Task {

	@Scheduled(cron = "0/5 * *  * * ? ")
	// 每5秒执行一次
	public void excute() {
		// TODO Auto-generated method stub
		System.err.println("spring  task");
	}
}

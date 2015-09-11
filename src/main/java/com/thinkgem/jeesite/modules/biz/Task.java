package com.thinkgem.jeesite.modules.biz;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {
	@Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次 
	public void aaa(){
		System.err.println("spring  task");
	}
}

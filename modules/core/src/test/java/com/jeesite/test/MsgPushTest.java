/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.msg.entity.content.AppMsgContent;
import com.jeesite.modules.msg.entity.content.EmailMsgContent;
import com.jeesite.modules.msg.entity.content.PcMsgContent;
import com.jeesite.modules.msg.entity.content.SmsMsgContent;
import com.jeesite.modules.msg.service.MsgPushService;
import com.jeesite.modules.msg.task.MsgLocalPushTask;
import com.jeesite.modules.msg.utils.MsgPushUtils;

/**
 * 消息推送测试类
 * @author ThinkGem
 * @version 2018-5-11
 */
@ActiveProfiles("test")
@SpringBootTest(classes=ApplicationTest.class)
@Rollback(false)
public class MsgPushTest extends BaseSpringContextTests {

	@Test
	public void testSend(){
//		for (int i=0; i<1; i++){
//			testPC();
//			testApp();
//			testSMS();
//			testMail();
//		}
		testTask();
	}
	
	@Autowired
	private MsgPushService msgPushService;
	
	public void testTask(){
		MsgLocalPushTask task = new MsgLocalPushTask();
		task.setMsgPushService(msgPushService);
		task.execute();
	}
	
	public void testPC(){
		PcMsgContent msgContent = new PcMsgContent();
		msgContent.setTitle("提示信息");
		msgContent.setContent("您有1条新的任务");
		msgContent.addButton("办理", "/a/task/execute?id=123");
		// 即时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system");
		// 定时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"), Global.YES);
		// 延迟推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", new Date(), Global.YES);
	}
	
	public void testApp(){
		AppMsgContent msgContent = new AppMsgContent();
		msgContent.setTitle("提示信息");
		msgContent.setContent("您有1条新的任务");
		// 即时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system");
		// 定时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"), Global.YES);
		// 延迟推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", new Date(), Global.YES);
	}
	
	public void testSMS(){
		SmsMsgContent msgContent = new SmsMsgContent();
		msgContent.setTitle("提示信息");
		msgContent.setContent("您好，您的验证码是：123456（请勿透露给其他人）感谢您的使用。");
		// 即时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system");
		// 定时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"), Global.YES);
		// 延迟推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", new Date(), Global.YES);
	}
	
	public void testMail(){
		EmailMsgContent msgContent = new EmailMsgContent();
		msgContent.setTitle("提示信息");
		msgContent.setContent("这是一条测试邮件内容");
		// 即时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system");
		// 定时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"), Global.YES);
		// 延迟推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", new Date(), Global.YES);
	}
	
}

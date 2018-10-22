/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.msg.entity.MsgTemplate;
import com.jeesite.modules.msg.entity.content.AppMsgContent;
import com.jeesite.modules.msg.entity.content.EmailMsgContent;
import com.jeesite.modules.msg.entity.content.PcMsgContent;
import com.jeesite.modules.msg.entity.content.SmsMsgContent;
import com.jeesite.modules.msg.service.MsgTemplateService;
import com.jeesite.modules.msg.task.impl.MsgLocalMergePushTask;
import com.jeesite.modules.msg.task.impl.MsgLocalPushTask;
import com.jeesite.modules.msg.utils.MsgPushUtils;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 消息推送测试类
 * @author ThinkGem
 * @version 2018-5-11
 */
@ActiveProfiles("test")
@SpringBootTest(classes=ApplicationTest.class)
@Rollback(false)
public class MsgPushTest extends BaseSpringContextTests {

	@Autowired
	private UserService userService;
	
	@Test
	public void testSend(){
		User user = UserUtils.get("system");
		if (StringUtils.isAnyBlank(user.getMobile(), user.getEmail())){
			user.setMobile("18555555555");
			user.setEmail("test@163.com");
			userService.updateUserInfo(user);
		}
		for (int i=0; i<1; i++){
			testPC();
			testApp();
			testSMS();
			testMail();
			testMailTpl();
		}
		for (int j=0; j<3; j++){
			testTaskMergePush();
			testTaskPush();
		}
	}
	
	public void testPC(){
		PcMsgContent msgContent = new PcMsgContent();
		msgContent.setTitle("提示信息");
		msgContent.setContent("您有1条新的任务");
		msgContent.addButton("办理", "/a/task/execute?id=123");
		// 即时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system");
		// 定时推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"));
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
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"));
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
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"));
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
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", DateUtils.parseDate("2018-05-05 08:30"));
		// 延迟推送消息
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system", new Date(), Global.YES);
	}

	@Autowired
	private MsgTemplateService msgTemplateService;
	
	public void testMailTpl(){
		MsgTemplate msgTemplate = new MsgTemplate();
		msgTemplate.setTplKey("mail_send_test");
		List<MsgTemplate> tplList = msgTemplateService.findList(msgTemplate);
		if (tplList.size() == 0){
			msgTemplate.setTplName("邮件提示信息");
			msgTemplate.setTplContent("你好，${keyword1}，请于 ${keyword2}，准时参加${keyword3}");
			msgTemplate.setTplType("email");
			msgTemplateService.save(msgTemplate);
		}
		EmailMsgContent msgContent = new EmailMsgContent();
		msgContent.setTitle("邮件提示信息");
		msgContent.setTplKey("mail_send_test");
		msgContent.addTplData("keyword1", "小王");
		msgContent.addTplData("keyword2", "2018-8-28 20:00");
		msgContent.addTplData("keyword3", "ERP项目方案讨论视频会议");
		// 即时推送模板消息，模板内容：你好，${keyword1}，请于 ${keyword2}，准时参加${keyword3}
		MsgPushUtils.push(msgContent, "BizKey", "BizType", "system");
	}
	
	@Autowired
	private MsgLocalMergePushTask msgLocalMergePushTask;
	public void testTaskMergePush(){
		msgLocalMergePushTask.execute();
	}
	
	@Autowired
	private MsgLocalPushTask msgLocalPushTask;
	public void testTaskPush(){
		msgLocalPushTask.execute();
	}
	
}

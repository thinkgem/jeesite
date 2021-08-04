/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.msg.send;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.modules.msg.entity.MsgPush;
import com.jeesite.modules.msg.entity.content.EmailMsgContent;

/**
 * 电子邮件发送服务实现
 * @author ThinkGem
 * @version 2018年5月13日
 */
@Service
public class EmailSendService extends BaseService implements MsgSendService{

	@Override
	public void sendMessage(MsgPush msgPush) {
		try{
			String fromAddress = Global.getProperty("msg.email.fromAddress");
			String fromPassword = Global.getProperty("msg.email.fromPassword");
			String fromHostName = Global.getProperty("msg.email.fromHostName");
			Integer smtpPort = Global.getPropertyToInteger("msg.email.smtpPort", "25");
			String sslOnConnect = Global.getProperty("msg.email.sslOnConnect", "false");
			String sslSmtpPort = Global.getProperty("msg.email.sslSmtpPort", "465");
			
			HtmlEmail htmlEmail = new HtmlEmail();
			htmlEmail.setCharset(EncodeUtils.UTF_8);
			htmlEmail.setFrom(fromAddress);
			htmlEmail.setAuthentication(fromAddress, fromPassword);
			htmlEmail.setHostName(fromHostName);
			htmlEmail.setSmtpPort(smtpPort);
			if ("true".equals(sslOnConnect)) {
				htmlEmail.setSSLOnConnect(true);
				htmlEmail.setSslSmtpPort(sslSmtpPort);
			}
			htmlEmail.addTo(msgPush.getReceiveCode(), msgPush.getReceiveUserName());
			
			// 内容
			EmailMsgContent content = msgPush.parseMsgContent(EmailMsgContent.class);
			htmlEmail.setSubject(content.getTitle());
			htmlEmail.setMsg(content.getContent());
			
			// 抄送
			if (StringUtils.isNotBlank(content.getCc())) {
				for (String email : content.getCc().split(";")) {
					htmlEmail.addCc(email);
				}
			}
			// 密送
			if (StringUtils.isNotBlank(content.getBcc())) {
				for (String email : content.getBcc().split(";")) {
					htmlEmail.addBcc(email);
				}
			}
			
			// 发送邮件
			String result = htmlEmail.send();
			
			// 发送成功
			msgPush.setPushStatus(MsgPush.PUSH_STATUS_SUCCESS);
			msgPush.addPushReturnContent(result);
			
		} catch (Exception ex) {
			logger.error("发送邮件失败！ ", ex);
			msgPush.setPushStatus(MsgPush.PUSH_STATUS_FAIL);
			msgPush.addPushReturnContent(ExceptionUtils.getStackTraceAsString(ex));
		}
	}
}

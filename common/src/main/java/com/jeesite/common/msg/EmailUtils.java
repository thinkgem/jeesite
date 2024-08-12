/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.msg;

import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.io.PropertiesUtils;

/**
 * 发送电子邮件
 */
public class EmailUtils {

	private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

	/**
	 * 发送邮件
	 * @return
	 */
	public static boolean send(String toAddress, String subject, String content) {
		PropertiesUtils props = PropertiesUtils.getInstance();
		String fromAddress = props.getProperty("msg.email.fromAddress");
		String fromPassword = props.getProperty("msg.email.fromPassword");
		String fromHostName = props.getProperty("msg.email.fromHostName");
		Integer smtpPort = props.getPropertyToInteger("msg.email.smtpPort", "25");
		String sslOnConnect = props.getProperty("msg.email.sslOnConnect", "false");
		String sslSmtpPort = props.getProperty("msg.email.sslSmtpPort");
		return send(fromAddress, fromPassword, fromHostName, smtpPort, sslOnConnect, sslSmtpPort, toAddress, subject, content);
	}

//	/**
//	 * 发送邮件
//	 * @param toAddress 接收地址
//	 * @param subject 标题
//	 * @param content 内容
//	 * @return
//	 */
//	@Deprecated
//	public static boolean send(String fromAddress, String fromPassword, String fromHostName,
//							   String sslOnConnect, String sslSmtpPort, String toAddress, String subject, String content) {
//		return send(fromAddress, fromPassword, fromHostName, 25, sslOnConnect, sslSmtpPort, toAddress, subject, content);
//	}

	/**
	 * 发送邮件
	 * @param toAddress 接收地址
	 * @param subject 标题
	 * @param content 内容
	 * @return
	 */
	public static boolean send(String fromAddress, String fromPassword, String fromHostName, Integer smtpPort,
							   String sslOnConnect, String sslSmtpPort, String toAddress, String subject, String content) {
		try {
			HtmlEmail htmlEmail = new HtmlEmail();
			// 发送地址
			htmlEmail.setFrom(fromAddress);
			// 密码校验
			htmlEmail.setAuthentication(fromAddress, fromPassword);
			// 发送服务器主机名
			htmlEmail.setHostName(fromHostName);
			// 发送服务器端口
			htmlEmail.setSmtpPort(smtpPort);

			// SSL
			if ("true".equals(sslOnConnect)) {
				htmlEmail.setSSLOnConnect(true);
				htmlEmail.setSslSmtpPort(sslSmtpPort);
			}

			// 接收地址
			htmlEmail.addTo(toAddress);

			// 标题
			htmlEmail.setSubject(subject);
			// 内容
			htmlEmail.setMsg(content);

			// 其他信息
			htmlEmail.setCharset(EncodeUtils.UTF_8);

			// 发送
			htmlEmail.send();
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return false;
	}

//	public static void main(String[] args) {
//		EmailUtils.send("jeesite_demo@163.com", "jeesitedemo1234", "smtp.163.com", 
//				"false", "465", "jeesite_demo@163.com", "测试邮件", "测试<b>邮件</b>的内容");
//	}

}
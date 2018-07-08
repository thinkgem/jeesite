/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.mail;

/**
 * 发送电子邮件
 */
@Deprecated
public class EmailUtils {

	/**
	 * 发送邮件
	 * @param toAddress 接收地址
	 * @param subject 标题
	 * @param content 内容
	 * @return
	 */
	@Deprecated
	public static boolean sendEmail(String toAddress, String subject, String content) {
		return com.jeesite.common.msg.EmailUtils.send(toAddress, subject, content);
	}
	
	/**
	 * 发送邮件
	 * @param toAddress 接收地址
	 * @param subject 标题
	 * @param content 内容
	 * @return
	 */
	@Deprecated
	public static boolean sendEmail(String fromAddress, String fromPassword, String fromHostName,
			String sslOnConnect, String sslSmtpPort, String toAddress, String subject, String content) {
		return com.jeesite.common.msg.EmailUtils.send(fromAddress, fromPassword, fromHostName, sslOnConnect, sslSmtpPort, toAddress, subject, content);
	}

}
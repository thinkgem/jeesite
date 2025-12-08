/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.msg;

import com.jeesite.common.msg.EmailUtils;

/**
 * 发送电子邮件工具测试类
 * @author ThinkGem
 * @version 2025-12-07
 */
public class EmailUtilsTest {

	public static void main(String[] args) {
		EmailUtils.send(
				"jeesite_demo@163.com",
				"jeesitedemo1234",
				"smtp.163.com",
				25,
				"false",
				"465",
				"jeesite_demo@163.com",
				"测试邮件",
				"测试<b>邮件</b>的内容");
	}

}

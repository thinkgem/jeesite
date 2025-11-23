/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.impl;

import com.jeesite.common.lang.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
public class TestAiTools {

	private final Logger logger = LoggerFactory.getLogger(TestAiTools.class);

	/**
	 * 获取服务器时间
	 */
	public String getCurrentDateTime() {
		String dateTime = "当前日期时间：" + DateUtils.getDateTime();
		logger.info("当前日期时间 ============== {}", dateTime);
		return dateTime;
	}

	/**
	 * 开关房间的灯
	 */
	public String roomLightSwitch(String roomName, boolean on) {
		String message = roomName + " 房间里的灯被 " + (on ? "打开" : "关闭");
		logger.info("房间灯光开关 ============== {}", message);
		return String.format("""
			{
			  "message": "%s",
			  "roomName": "%s",
			  "on": %s
			}
			""", message, roomName, on);
	}

}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;

/**
 * AI 工具调用、Tool calling（需选择支持 Tools 的模型）
 * @author ThinkGem
 */
public class CmsAiTools {

	private final Logger logger = LoggerFactory.getLogger(CmsAiTools.class);

	/**
	 * 未联网搜索的时候，可获取到服务器时间
	 */
	@Tool(description = "当前时间，当前日期，几点了")
	public String getCurrentDateTime() {
		String dateTime = "当前日期时间：" + LocalDateTime.now();
		logger.info(dateTime + " ============== ");
		return dateTime;
	}

	/**
	 * 你可以询问：打开客厅的灯，关闭卧室的灯（需创建新对话）
	 */
	@Tool(description = "房间里的灯打开或关闭")
	public String turnLight(@ToolParam(description = "房间") String roomName, @ToolParam(description = "开关") boolean on) {
		String message = roomName + " 房间里的灯被 " + (on ? "打开" : "关闭");
		logger.info(message + " ============== ");
		return String.format("""
					message: %s
					roomName: %s
					on: %s
					""",
				roomName, on, message);
	}
}
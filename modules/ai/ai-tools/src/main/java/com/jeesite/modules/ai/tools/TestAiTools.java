/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools;

import com.jeesite.common.lang.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
@Component
public class TestAiTools {

	private final Logger logger = LoggerFactory.getLogger(TestAiTools.class);

	/**
	 * 获取服务器时间
	 */
	@Tool(name="当前服务器时间", description = "当前服务器日期时间，格式为 yyyy-MM-dd HH:mm:ss。")
	public String getCurrentDateTime() {
		String dateTime = "当前日期时间：" + DateUtils.getDateTime();
		logger.info("当前日期时间 ============== {}", dateTime);
		return dateTime;
	}

	/**
	 * 开关房间的灯
	 */
	@Tool(
		name = "房间灯光开关",
		description = "控制指定房间的灯光开关。需要提供房间名称（如 '客厅'、'卧室'）和目标状态（true 表示开灯，false 表示关灯）。"
	)
	public String roomLightSwitch(
		@ToolParam(description = "要控制的房间名称，例如：'客厅'、'卧室'、'餐厅'、'厨房'") String roomName,
		@ToolParam(description = "灯光目标状态：true 表示打开灯，false 表示关闭灯") boolean on) {
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
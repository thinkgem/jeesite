/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.local;

import com.jeesite.modules.ai.tools.annotation.AiTools;
import com.jeesite.modules.ai.tools.impl.TestAiTools;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
@AiTools
@ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "false", matchIfMissing = true)
public class LocalTestAiTools extends TestAiTools {

	/**
	 * 获取服务器时间
	 */
	@Tool(name="server-time", description = "当前服务器日期时间，格式为 yyyy-MM-dd HH:mm:ss。")
	public String getCurrentDateTime() {
		return super.getCurrentDateTime();
	}

	/**
	 * 开关房间的灯
	 */
	@Tool(
		name = "root-light-switch",
		description = "控制指定房间的灯光开关。需要提供房间名称（如 '客厅'、'卧室'）和目标状态（true 表示开灯，false 表示关灯）。"
	)
	public String roomLightSwitch(
			@ToolParam(description = "要控制的房间名称，例如：'客厅'、'卧室'、'餐厅'、'厨房'") String roomName,
			@ToolParam(description = "灯光目标状态：true 表示打开灯，false 表示关闭灯") boolean on) {
		return super.roomLightSwitch(roomName, on);
	}

}
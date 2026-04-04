/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.mcp;

import com.jeesite.modules.ai.tools.impl.TestAiTools;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
@Component
@ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "true", matchIfMissing = false)
public class McpTestAiTools extends TestAiTools {

	/**
	 * 获取服务器时间
	 */
	@McpTool(name="server-time", description = "当前服务器日期时间，格式为 yyyy-MM-dd HH:mm:ss。")
	public String getCurrentDateTime() {
		return super.getCurrentDateTime();
	}

	/**
	 * 开关房间的灯
	 */
	@McpTool(
		name = "root-light-switch",
		description = "控制指定房间的灯光开关。需要提供房间名称（如 '客厅'、'卧室'）和目标状态（true 表示开灯，false 表示关灯）。"
	)
	public String roomLightSwitch(
			@McpToolParam(description = "要控制的房间名称，例如：'客厅'、'卧室'、'餐厅'、'厨房'") String roomName,
			@McpToolParam(description = "灯光目标状态：true 表示打开灯，false 表示关闭灯") boolean on) {
		return super.roomLightSwitch(roomName, on);
	}

}
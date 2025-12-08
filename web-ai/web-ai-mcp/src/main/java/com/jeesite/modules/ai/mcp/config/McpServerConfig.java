/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.mcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * AI MCP 服务配置
 * @author ThinkGem
 */
@Lazy(false)
@Configuration
public class McpServerConfig {

//	/**
//	 * Spring AI 1.1.0 中自动扫描 McpTool 无需手动配置
//	 */
//	@Bean
//	public ToolCallbackProvider mcpServerTools(TestMcpAiTools testMcpAiTools, UserMcpAiTools userMcpAiTools) {
//		return MethodToolCallbackProvider.builder().toolObjects(testMcpAiTools, userMcpAiTools).build();
//	}

}

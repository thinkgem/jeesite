/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.mcp.config;

import com.jeesite.modules.ai.tools.TestAiTools;
import com.jeesite.modules.ai.tools.UserAITools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * AI MCP 服务配置
 * @author ThinkGem
 */
@Lazy(false)
@Configuration
public class McpServerConfig {

	@Bean
	public ToolCallbackProvider mcpServerTools(TestAiTools testAiTools, UserAITools userAITools) {
		return MethodToolCallbackProvider.builder().toolObjects(testAiTools, userAITools).build();
	}

}

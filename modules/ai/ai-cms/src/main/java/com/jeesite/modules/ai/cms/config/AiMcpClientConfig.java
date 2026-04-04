/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.config;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ai.cms.properties.AiCmsProperties;
import com.jeesite.modules.ai.tools.context.AiToolContextProvider;
import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI MCP 配置类
 * @author ThinkGem
 */
@Configuration
@EnableConfigurationProperties(AiCmsProperties.class)
public class AiMcpClientConfig {

	/**
	 * 聊天对话客户端（使用 MCP Tools）
	 * @author ThinkGem
	 */
	@Bean("chatClient")
	@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "true", matchIfMissing = false)
	public ChatClient mcpChatClient(ChatClient.Builder builder, AiCmsProperties properties,
									SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {
		if (StringUtils.isNotBlank(properties.getDefaultSystem())) {
			builder.defaultSystem(properties.getDefaultSystem());
		}
		builder.defaultToolCallbacks(syncMcpToolCallbackProvider.getToolCallbacks());
		return builder.build();
	}

	@Bean
	@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "true", matchIfMissing = false)
	public McpSyncClientCustomizer mcpSyncClientCustomizer() {
		return (name, syncSpec) -> syncSpec
				.transportContextProvider(AiToolContextProvider.INSTANCE);
	}

	@Bean
	@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "true", matchIfMissing = false)
	public McpSyncHttpClientRequestCustomizer mcpSyncHttpClientRequestCustomizer() {
		return (builder, method, endpoint, body, context) -> {
			String sessionId = (String) context.get(AiToolContextProvider.SESSION_ID_KEY);
			if (StringUtils.isNotBlank(sessionId)) {
				builder.header(Global.getProperty("session.sessionIdHeaderName", "x-token"), sessionId);
			}
		};
	}

}

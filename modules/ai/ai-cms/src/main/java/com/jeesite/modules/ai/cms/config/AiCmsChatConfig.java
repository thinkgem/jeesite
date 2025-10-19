/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.config;

import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ai.cms.properties.AiCmsProperties;
import com.jeesite.modules.ai.cms.service.CacheChatMemoryRepository;
import com.jeesite.modules.ai.tools.TestAiTools;
import com.jeesite.modules.ai.tools.UserAITools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * AI 聊天配置类
 * @author ThinkGem
 */
@Configuration
@EnableConfigurationProperties(AiCmsProperties.class)
public class AiCmsChatConfig {

	/**
	 * 聊天对话客户端（使用本地 Tools）
	 * @author ThinkGem
	 */
	@Bean("chatClient")
	@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "false", matchIfMissing = true)
	public ChatClient chatClient(ChatClient.Builder builder, AiCmsProperties properties,
								 TestAiTools testAiTools, UserAITools userAITools) {
		if (StringUtils.isNotBlank(properties.getDefaultSystem())) {
			builder.defaultSystem(properties.getDefaultSystem());
		}
		if (properties.getTools().getEnabled()) {
			builder.defaultTools(testAiTools, userAITools);
		}
		return builder.build();
	}

	/**
	 * 聊天对话客户端（使用 MCP Tools）
	 * @author ThinkGem
	 */
	@Bean("chatClient")
	@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "true", matchIfMissing = false)
	public ChatClient chatClientMcp(ChatClient.Builder builder, AiCmsProperties properties,
								 SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {
		if (StringUtils.isNotBlank(properties.getDefaultSystem())) {
			builder.defaultSystem(properties.getDefaultSystem());
		}
		builder.defaultToolCallbacks(syncMcpToolCallbackProvider.getToolCallbacks());
		return builder.build();
	}

	/**
	 * 聊天对话数据存储
	 * @author ThinkGem
	 */
	@Bean
	public ChatMemory chatMemory(CacheChatMemoryRepository cacheChatMemoryRepository) {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(cacheChatMemoryRepository)
				.maxMessages(1024)
				.build();
	}

//	@Bean
//	public BatchingStrategy batchingStrategy() {
//		return new TokenCountBatchingStrategy(EncodingType.CL100K_BASE, Integer.MAX_VALUE, 0.1);
//	}

	/**
	 * PG向量库数据源
	 * @author ThinkGem
	 */
	@Bean
	@Primary
	@ConditionalOnProperty(name = "jdbc.ds_pgvector.type")
	public JdbcTemplate pgVectorStoreJdbcTemplate() {
		return DataSourceHolder.getRoutingDataSource()
				.getJdbcTemplate("ds_pgvector");
	}

}

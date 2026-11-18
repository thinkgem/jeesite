/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.autoconfigure.ai;

import com.jeesite.modules.ai.service.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * AI 智能体与对话 Service 自动配置
 * <p>
 * 模型、工具、MCP、向量库等通用 Service 由 framework-ai 的
 * {@code Ai0AutoConfiguration} 注册；本类注册 ai-core 保留的智能体、对话、聊天消息 Service。
 * @author ThinkGem
 * @version 2026-11-17
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "ai", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AiCoreAutoConfiguration {

	/**
	 * 智能体与工具关联 Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public AiAgentToolService aiAgentToolService() {
		return new AiAgentToolService();
	}

	/**
	 * 智能体与MCP服务关联 Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public AiAgentMcpService aiAgentMcpService() {
		return new AiAgentMcpService();
	}

	/**
	 * 智能体与向量库绑定关联 Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public AiAgentVectorService aiAgentVectorService() {
		return new AiAgentVectorService();
	}

	/**
	 * AI智能体 Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public AiAgentService aiAgentService(AiAgentToolService aiAgentToolService, AiAgentMcpService aiAgentMcpService,
			AiAgentVectorService aiAgentVectorService, AiVectorBizService aiVectorBizService) {
		return new AiAgentService(aiAgentToolService, aiAgentMcpService,
				aiAgentVectorService, aiVectorBizService);
	}

	/**
	 * AI聊天消息 Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public AiChatMessageService aiChatMessageService() {
		return new AiChatMessageService();
	}

	/**
	 * AI聊天对话 Service
	 */
	@Bean
	@ConditionalOnMissingBean
	public AiChatService aiChatService(AiAgentService aiAgentService,
			AiChatMessageService aiChatMessageService) {
		return new AiChatService(aiAgentService, aiChatMessageService);
	}

}

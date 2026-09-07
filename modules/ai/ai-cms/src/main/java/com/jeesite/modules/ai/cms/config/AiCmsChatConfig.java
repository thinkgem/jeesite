/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.config;

import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.ai.cms.properties.AiCmsProperties;
import com.jeesite.modules.ai.cms.service.CacheChatMemoryRepository;
import com.jeesite.modules.ai.tools.annotation.AiTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.http.okhttp.SpringAiOpenAiHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

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
	public ChatClient chatClient(ChatClient.Builder builder, AiCmsProperties properties) {
		if (StringUtils.isNotBlank(properties.getDefaultSystem())) {
			builder.defaultSystem(properties.getDefaultSystem());
		}
		if (properties.getTools().getEnabled()) {
			Map<String, Object> tools = SpringUtils.getApplicationContext().getBeansWithAnnotation(AiTools.class);
			if (!tools.isEmpty()) {
				builder.defaultTools(tools.values().toArray());
			}
		}
		return builder.build();
	}

	/**
	 * 生图模型：手动构建 OpenAI 客户端，显式设置请求超时
	 * （Spring AI 自动装配的客户端默认 60 秒超时且图像客户端还会自动重试，
	 * 本地生图往往需要数分钟），配置见 spring.ai.openai.image.*
	 * 注意：返回类型必须为具体类型 OpenAiImageModel，
	 * 自动装配按该类型做 @ConditionalOnMissingBean 检查，避免 Bean 定义冲突。
	 * @author ThinkGem
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.ai.model.image", havingValue = "openai")
	public OpenAiImageModel openAiImageModel(Environment env) {
		// 生图超时：优先 spring.ai.openai.image.timeout，其次 spring.ai.openai.timeout，默认 15 分钟
		String timeout = env.getProperty("spring.ai.openai.image.timeout",
				env.getProperty("spring.ai.openai.timeout", "15m"));
		com.openai.core.ClientOptions clientOptions = com.openai.core.ClientOptions.builder()
				.httpClient(SpringAiOpenAiHttpClient.builder().timeout(DurationStyle.detectAndParse(timeout)).build())
				.baseUrl(env.getProperty("spring.ai.openai.image.base-url", "https://api.openai.com/v1"))
				.credential(com.openai.credential.BearerTokenCredential.create(
						env.getProperty("spring.ai.openai.image.api-key", "sk-xxx")))
				.timeout(DurationStyle.detectAndParse(timeout))
				.build();
		OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
				.model(env.getProperty("spring.ai.openai.image.model"))
				.size(env.getProperty("spring.ai.openai.image.size"))
				.build();
		return OpenAiImageModel.builder()
				.openAiClient(new com.openai.client.OpenAIClientImpl(clientOptions))
				.options(imageOptions)
				.build();
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

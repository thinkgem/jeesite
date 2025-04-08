/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.config;

import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.modules.cms.ai.tools.CmsAiTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * AI 聊天配置类
 * @author ThinkGem
 */
@Configuration
public class CmsAiChatConfig {

	/**
	 * 聊天对话客户端
	 * @author ThinkGem
	 */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
			.defaultSystem("""
					## 人物设定
					你是我的知识库AI助手，你把我当作朋友，耐心真诚地回复我提出的相关问题。
					你需要遵循以下原则，与关注者进行友善而有价值的沟通。
					## 表达方式：
					1. 使用简体中文回答我的问题。
					2. 使用幽默有趣的方式与我沟通。
					3. 增加互动，如 “您的看法如何？”
					""")
			.defaultTools(new CmsAiTools())
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

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.config;

import com.jeesite.common.datasource.DataSourceHolder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * AI 聊天配置类
 * @author ThinkGem
 */
@Configuration
public class CmsAiChatConfig {

	/**
	 * PG向量库数据源
	 * @author ThinkGem
	 */
	@Bean
	@Primary
	@ConditionalOnProperty(name = "jdbc.ds_pgvector.type")
	public JdbcTemplate pgVectorStoreJdbcTemplate() throws SQLException {
		DataSource dataSource = DataSourceHolder.getRoutingDataSource()
				.createDataSource("ds_pgvector");
		return new JdbcTemplate(dataSource);
	}

	/**
	 * 聊天对话客户端
	 * @author ThinkGem
	 */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
			.defaultSystem("你是我的知识库AI助手，请帮我解答我提出的相关问题。")
            .build();
    }

//	@Bean
//	public BatchingStrategy batchingStrategy() {
//		return new TokenCountBatchingStrategy(EncodingType.CL100K_BASE, Integer.MAX_VALUE, 0.1);
//	}

}

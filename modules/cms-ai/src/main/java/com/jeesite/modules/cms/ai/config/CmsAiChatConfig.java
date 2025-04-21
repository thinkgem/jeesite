/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.config;

import com.jeesite.common.datasource.DataSourceHolder;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.cms.ai.properties.CmsAiProperties;
import com.jeesite.modules.cms.ai.tools.CmsAiTools;
import org.springframework.ai.chat.client.ChatClient;
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
@EnableConfigurationProperties(CmsAiProperties.class)
public class CmsAiChatConfig {

	/**
	 * 聊天对话客户端
	 * @author ThinkGem
	 */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, CmsAiProperties properties) {
		if (StringUtils.isNotBlank(properties.getDefaultSystem())) {
			builder.defaultSystem(properties.getDefaultSystem());
		}
		if (properties.getToolCalls()) {
			builder.defaultTools(new CmsAiTools());
		}
        return builder.build();
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

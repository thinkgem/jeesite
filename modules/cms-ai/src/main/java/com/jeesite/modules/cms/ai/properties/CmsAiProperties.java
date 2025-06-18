package com.jeesite.modules.cms.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("spring.ai")
public class CmsAiProperties {

	/**
	 * 是否启用 Tool calling 工具调用
	 */
	private Boolean toolCalls = false;

	/**
	 * 默认系统提示词
	 */
	private String defaultSystem = "";

	/**
	 * 默认问题模板格式
	 */
	private String defaultPromptTemplate = "";

	/**
	 * 向量数据库设置
	 */
	@NestedConfigurationProperty
	private final Vectorstore vectorstore = new Vectorstore();

	public Boolean getToolCalls() {
		return toolCalls;
	}

	public void setToolCalls(Boolean toolCalls) {
		this.toolCalls = toolCalls;
	}

	public String getDefaultSystem() {
		return defaultSystem;
	}

	public void setDefaultSystem(String defaultSystem) {
		this.defaultSystem = defaultSystem;
	}

	public String getDefaultPromptTemplate() {
		return defaultPromptTemplate;
	}

	public void setDefaultPromptTemplate(String defaultPromptTemplate) {
		this.defaultPromptTemplate = defaultPromptTemplate;
	}

	public Vectorstore getVectorstore() {
		return vectorstore;
	}

	public static class Vectorstore {

		/**
		 * 向量库类型选择：chroma、pgvector、elasticsearch、milvus
		 */
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}

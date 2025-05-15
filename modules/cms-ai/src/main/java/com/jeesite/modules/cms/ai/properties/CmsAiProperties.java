package com.jeesite.modules.cms.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
}

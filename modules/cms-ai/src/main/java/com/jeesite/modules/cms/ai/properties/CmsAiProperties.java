package com.jeesite.modules.cms.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.ai")
public class CmsAiProperties {

	private Boolean toolCalls = false;

	private String defaultSystem = "";

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
}

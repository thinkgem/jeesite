package com.jeesite.modules.cms.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.ai")
public class CmsAiProperties {

	private Boolean toolCalls = false;

	public Boolean getToolCalls() {
		return toolCalls;
	}

	public void setToolCalls(Boolean toolCalls) {
		this.toolCalls = toolCalls;
	}
}

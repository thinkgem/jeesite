package com.jeesite.modules.ai.cms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("spring.ai")
public class AiCmsProperties {

	/**
	 * 向量数据库设置
	 */
	@NestedConfigurationProperty
	private final Vectorstore vectorstore = new Vectorstore();

	/**
	 * 是否启用 Tool calling 工具调用【例子详见 TestAiTools.java、UserAiTools.java 】
	 */
	@NestedConfigurationProperty
	private final Tools tools = new Tools();

	/**
	 * 默认系统提示词
	 */
	private String defaultSystem = "";

	/**
	 * 默认问题模板格式
	 */
	private String defaultPromptTemplate = "";

	/**
	 * 对话每轮携带的最大图片数量（取最近的图片，0 表示不限制）
	 */
	private Integer mediaLimit = 3;

	public Vectorstore getVectorstore() {
		return vectorstore;
	}

	public Tools getTools() {
		return tools;
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

	public Integer getMediaLimit() {
		return mediaLimit;
	}

	public void setMediaLimit(Integer mediaLimit) {
		this.mediaLimit = mediaLimit;
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

	public static class Tools {

		/**
		 * 是否启用 Tool calling 工具调用【例子详见 TestAiTools.java、UserAiTools.java 】
		 */
		private Boolean enabled = false;

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
	}
}

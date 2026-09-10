package com.jeesite.modules.ai.cms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

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

	/**
	 * AI 模型调用（对话、生图等）重试配置，仅由 AiRetryUtils 统一使用。
	 * <p>
	 * 命名为 autoRetry 而非 retry，是为了避免与 Spring AI 自动装配在 ChatClient/ImageModel 上的
	 * 内置重试配置 spring.ai.retry 冲突（二者同前缀会被同一份属性绑定，形成“双重重试”）。
	 * @author ThinkGem
	 */
	@NestedConfigurationProperty
	private final AutoRetry autoRetry = new AutoRetry();

	public Vectorstore getVectorstore() {
		return vectorstore;
	}

	public Tools getTools() {
		return tools;
	}

	public Integer getMediaLimit() {
		return mediaLimit;
	}

	public void setMediaLimit(Integer mediaLimit) {
		this.mediaLimit = mediaLimit;
	}

	public AutoRetry getAutoRetry() {
		return autoRetry;
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

	public static class AutoRetry {

		/**
		 * 最大尝试次数（含首次请求），1 表示不重试
		 */
		private int maxAttempts = 3;

		/**
		 * 指数退避参数
		 */
		private final Backoff backoff = new Backoff();

		public int getMaxAttempts() {
			return maxAttempts;
		}

		public void setMaxAttempts(int maxAttempts) {
			this.maxAttempts = maxAttempts;
		}

		public Backoff getBackoff() {
			return backoff;
		}

		public static class Backoff {

			/**
			 * 首次重试的等待时间
			 */
			private Duration initialInterval = Duration.ofSeconds(2);

			/**
			 * 等待时间的递增倍数（指数退避）
			 */
			private double multiplier = 2.0;

			/**
			 * 等待时间的上限
			 */
			private Duration maxInterval = Duration.ofSeconds(30);

			public Duration getInitialInterval() {
				return initialInterval;
			}

			public void setInitialInterval(Duration initialInterval) {
				this.initialInterval = initialInterval;
			}

			public double getMultiplier() {
				return multiplier;
			}

			public void setMultiplier(double multiplier) {
				this.multiplier = multiplier;
			}

			public Duration getMaxInterval() {
				return maxInterval;
			}

			public void setMaxInterval(Duration maxInterval) {
				this.maxInterval = maxInterval;
			}
		}
	}
}

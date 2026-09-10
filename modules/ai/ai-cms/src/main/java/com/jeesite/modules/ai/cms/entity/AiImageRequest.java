/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeesite.common.lang.StringUtils;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.openai.OpenAiImageOptions;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 生图请求对象（OpenAI v1 {@code /v1/images/generations} 请求体）。
 * <p>
 * 请求示例：
 * <pre>{@code
 * {
 *   "prompt": "一只可爱的橘猫，坐在窗台上晒太阳",
 *   "model": "Kwai-Kolors/Kolors",
 *   "n": 1,
 *   "size": "1024x1024",
 *   "quality": "standard",
 *   "style": "vivid",
 *   "response_format": "url",
 *   "user": "user-001",
 *   "bizKey": "会话ID",
 *   "bizType": "cms-chat"
 * }
 * }</pre>
 * 约定：
 * <ul>
 *   <li>model、n、size、quality、style、user 等参数非空时，覆盖 spring.ai.openai.image 默认配置，
 *       未设置的项交由模型默认配置兜底；</li>
 *   <li>bizKey、bizType 为 JeeSite 扩展参数，用于把生成的图片归属到指定业务（如聊天会话）。</li>
 * </ul>
 *
 * @author ThinkGem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiImageRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 图片描述提示词（必填） */
	private String prompt;

	/** 生图模型，为空时使用默认配置 spring.ai.openai.image.model */
	private String model;

	/** 生成图片数量，默认 1 */
	private Integer n;

	/** 图片尺寸，如 1024x1024、1024x1536、auto */
	private String size;

	/** 图片质量，如 standard、hd、low、medium、high、auto */
	private String quality;

	/** 图片风格，如 vivid、natural */
	private String style;

	/** 返回格式：url（默认）、b64_json */
	@JsonProperty("response_format")
	private String responseFormat;

	/** 终端用户标识，便于厂商监控滥用 */
	private String user;

	/** 业务关联键（JeeSite 扩展，如聊天会话 ID，为空时归属到当前用户） */
	private String bizKey;

	/** 业务类型（JeeSite 扩展，为空时默认 ai-image） */
	private String bizType;

	/**
	 * 转换为 Spring AI 生图参数
	 * @param defaultModel 默认生图模型（spring.ai.openai.image.model），请求未指定 model 时使用，
	 *        不可为 null 兜底：OpenAiImageOptions 在 model 为空时会自动填充官方默认模型
	 *        （gpt-image-1-mini），反而覆盖 spring.ai.openai.image.model 的配置值
	 * @author ThinkGem
	 */
	public ImageOptions toImageOptions(String defaultModel) {
		OpenAiImageOptions.Builder builder = OpenAiImageOptions.builder();
		builder.model(StringUtils.isNotBlank(model) ? model : defaultModel);
		if (n != null && n > 0) {
			builder.n(n);
		}
		if (StringUtils.isNotBlank(size)) {
			builder.size(size);
		}
		if (StringUtils.isNotBlank(quality)) {
			builder.quality(quality);
		}
		if (StringUtils.isNotBlank(style)) {
			builder.style(style);
		}
		if (StringUtils.isNotBlank(responseFormat)) {
			builder.responseFormat(responseFormat);
		}
		if (StringUtils.isNotBlank(user)) {
			builder.user(user);
		}
		return builder.build();
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getN() {
		return n;
	}

	public void setN(Integer n) {
		this.n = n;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getResponseFormat() {
		return responseFormat;
	}

	public void setResponseFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBizKey() {
		return bizKey;
	}

	public void setBizKey(String bizKey) {
		this.bizKey = bizKey;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

}

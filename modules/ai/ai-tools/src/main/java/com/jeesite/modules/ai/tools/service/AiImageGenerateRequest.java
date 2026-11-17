/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.service;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * AI 生图请求参数
 * <p>
 * 生图模型按以下顺序解析：
 * <ol>
 *   <li>{@link #modelCode}：调用方直接指定的模型编码（优先级最高）</li>
 *   <li>{@link #toolCode}：工具管理（ai_tool）中配置的模型，见 {@code options.image.modelCode}</li>
 *   <li>未指定时使用「AI模型」中类型为图像生成、权重最高的启用模型</li>
 * </ol>
 * @author ThinkGem
 * @version 2026-11-17
 */
public class AiImageGenerateRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 业务关联键（如聊天会话 ID，为空时归属到当前用户） */
	protected String bizKey;

	/** 业务类型（如 cms-chat，为空时默认 ai-image） */
	protected String bizType;

	/** 图片描述提示词 */
	protected String prompt;

	/** 工具编码（工具管理中的 ai_tool.tool_code，用于读取该工具配置的模型） */
	protected String toolCode;

	/** 模型编码（ai_model.model_code，为空时按 toolCode 或默认模型解析） */
	protected String modelCode;

	/** 请求级生图参数（如 size、quality、n、style），非空时覆盖模型配置中的同名项 */
	protected Map<String, Object> options;

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

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

}

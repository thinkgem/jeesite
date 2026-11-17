/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.service;

import java.util.Map;

/**
 * AI 生图服务接口（生图工具的 SPI）。
 * 由具体业务模块实现并注册为 Spring Bean（例如 AI CMS 模块的 AiCmsImageService），
 * 生图工具（LocalImageAiTools、McpImageAiTools）通过该接口生图，
 * 这样 ai-tools 模块无需依赖任何业务模块。
 * <p>
 * 若运行环境装有 ai-core 模块（模型注册、工具管理），生图工具会优先调用
 * ai-core 的生图服务（按类名反射调用，不产生模块依赖），使用工具管理里配置的生图模型；
 * 未安装时回退到本接口的实现。
 * @author ThinkGem
 */
public interface ImageGenerateService {

	/**
	 * 是否启用生图模型
	 */
	boolean isEnabled();

	/**
	 * 文生图，生成图片并保存到文件服务器，返回图片访问地址等信息
	 * @param bizKey 业务关联键（如聊天会话 ID，为空时归属到当前用户）
	 * @param bizType 业务类型（如 cms-chat，为空时默认 ai-image）
	 * @param prompt 图片描述提示词
	 */
	Map<String, Object> generateImage(String bizKey, String bizType, String prompt);

	/**
	 * 文生图（指定模型编码，用于「工具管理」里给本地工具选择生图模型）
	 * <p>
	 * 默认忽略模型编码、走三参实现；需要按模型生图的实现请覆盖本方法
	 * （如 web-ai-pro 模块的 AiImageGenerateService，按「AI模型」注册的模型生图）。
	 * @param bizKey 业务关联键（如聊天会话 ID，为空时归属到当前用户）
	 * @param bizType 业务类型（如 cms-chat，为空时默认 ai-image）
	 * @param prompt 图片描述提示词
	 * @param modelCode 生图模型编码（可为空，为空时由实现选择默认模型）
	 */
	default Map<String, Object> generateImage(String bizKey, String bizType, String prompt, String modelCode) {
		return generateImage(bizKey, bizType, prompt);
	}

}

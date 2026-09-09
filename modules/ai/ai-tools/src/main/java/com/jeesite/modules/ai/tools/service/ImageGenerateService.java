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

}

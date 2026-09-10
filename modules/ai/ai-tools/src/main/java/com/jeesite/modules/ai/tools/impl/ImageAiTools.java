/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.impl;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.ai.tools.service.ImageGenerateService;

import java.util.Map;

/**
 * AI 生图工具：聊天模型识别到“画图、生成图片”等意图后自动调用，
 * 生图后保存到文件服务器并返回图片访问地址。
 * <p>生图能力由 ImageGenerateService 提供（如 AI CMS 模块的 AiCmsImageService），
 * 工具注解分别在：本地工具 LocalImageAiTools（@Tool）、MCP 工具 McpImageAiTools（@McpTool）。
 * <p>生效条件：spring.ai.tools.enabled=true（本地工具），且聊天模型支持 function calling。
 * @author ThinkGem
 */
public class ImageAiTools {

	/**
	 * 根据文字描述生成图片，返回 Markdown 图片语法字符串（![描述](图片地址)），
	 * 便于聊天模型直接输出、前端 markdown-it 直接渲染展示图片。
	 * @param prompt 图片内容的详细描述
	 */
	public String generateImage(String prompt) {
		ImageGenerateService imageService = getImageService();
		if (imageService == null || !imageService.isEnabled()) {
			return "未启用图片生成模型，请配置 spring.ai.model.image: openai";
		}
		try {
			Map<String, Object> data = imageService.generateImage(null, "ai-image", prompt);
			return buildMarkdownImage(String.valueOf(data.get("prompt")),
					String.valueOf(data.get("fileUrl")));
		} catch (Exception e) {
			return "生成图片失败：" + e.getMessage();
		}
	}

	/**
	 * 构建 Markdown 图片语法：![描述](图片地址)，并对 alt 中的特殊字符转义，避免破坏语法
	 */
	private static String buildMarkdownImage(String alt, String url) {
		String safeAlt = StringUtils.replaceEach(StringUtils.defaultString(alt),
				new String[]{"\\", "]", "("}, new String[]{"\\\\", "\\]", "\\("});
		return "![" + safeAlt + "](" + url + ")";
	}

	/**
	 * 获取生图服务：延迟获取，因为 ChatClient 构建时会扫描 Tools Bean，
	 * 若直接注入生图服务（它依赖 ChatClient）会造成循环依赖
	 */
	protected ImageGenerateService getImageService() {
		try {
			return SpringUtils.getBean(ImageGenerateService.class);
		} catch (Exception e) {
			// 未引入生图实现模块（如未安装 AI CMS 模块）
			return null;
		}
	}

}

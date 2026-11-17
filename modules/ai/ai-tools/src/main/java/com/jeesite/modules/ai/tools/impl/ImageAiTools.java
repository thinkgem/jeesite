/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.impl;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.ai.tools.service.ImageGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;

import java.util.Map;

/**
 * AI 生图工具：聊天模型识别到“画图、生成图片”等意图后自动调用，
 * 生图后保存到文件服务器并返回图片访问地址。
 * <p>
 * 生图能力由 {@link ImageGenerateService} 实现并注册为 Spring Bean（默认实现为 ai-tools 的 AiImageGenerateService），
 * 生图模型取「工具管理」中该工具配置的模型（{@code options.image.modelCode}），
 * 未配置时由实现选择默认模型。
 * <p>
 * 工具注解分别在：本地工具 LocalImageAiTools（@Tool）、MCP 工具 McpImageAiTools（@McpTool）。
 * @author ThinkGem
 */
public class ImageAiTools {

	/** 业务类型：AI 生图 */
	public static final String BIZ_TYPE = "ai-image";

	/**
	 * 工具管理配置的生图模型编码在工具上下文中的键
	 * <p>
	 * 与 ai-core 的 {@code AiToolUtils.AI_TOOL_MODEL_CODE_KEY} 保持一致，
	 * 键值类型为字符串，ai-tools 无需依赖 ai-core 的类型。
	 */
	public static final String MODEL_CODE_KEY = "aiToolImageModelCode";

	protected static final Logger logger = LoggerFactory.getLogger(ImageAiTools.class);

	/**
	 * 根据文字描述生成图片（不带工具上下文，使用默认生图模型）
	 * @param prompt 图片内容的详细描述
	 */
	public String generateImage(String prompt) {
		return generateImage(prompt, null);
	}

	/**
	 * 根据文字描述生成图片，返回 Markdown 图片语法字符串（![描述](图片地址)），
	 * 便于聊天模型直接输出、前端 markdown-it 直接渲染展示图片。
	 * @param prompt 图片内容的详细描述
	 * @param toolContext 工具上下文（工具管理配置的模型等，由 Spring AI 注入，不暴露给模型）
	 */
	public String generateImage(String prompt, ToolContext toolContext) {
		ImageGenerateService imageService = getImageService();
		if (imageService == null || !imageService.isEnabled()) {
			return "未启用图片生成模型，请先在「AI模型」中注册模型类型为图像生成的模型";
		}
		try {
			Map<String, Object> data = imageService.generateImage(null, BIZ_TYPE, prompt,
					getModelCode(toolContext));
			if (data == null) {
				return "生成图片失败：未获取到图片数据";
			}
			return buildMarkdownImage(String.valueOf(data.get("prompt")),
					String.valueOf(data.get("fileUrl")));
		} catch (Exception e) {
			logger.error("生成图片失败：{}", e.getMessage(), e);
			return "生成图片失败：" + e.getMessage();
		}
	}

	/**
	 * 获取工具管理中配置的生图模型编码
	 * @param toolContext 工具上下文（可为空）
	 */
	protected String getModelCode(ToolContext toolContext) {
		if (toolContext == null || toolContext.getContext() == null) {
			return null;
		}
		Object value = toolContext.getContext().get(MODEL_CODE_KEY);
		return value == null ? null : StringUtils.trimToNull(value.toString());
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
			// 未引入生图实现模块
			logger.debug("未找到生图服务实现：{}", e.getMessage());
			return null;
		}
	}

}

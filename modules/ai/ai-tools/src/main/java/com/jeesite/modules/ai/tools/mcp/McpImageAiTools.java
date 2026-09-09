/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.mcp;

import com.jeesite.modules.ai.tools.impl.ImageAiTools;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AI MCP 工具调用（生图）
 * @author ThinkGem
 */
@Component
@ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "true", matchIfMissing = false)
public class McpImageAiTools extends ImageAiTools {

	/**
	 * 根据文字描述生成图片，返回图片访问地址
	 */
	@McpTool(name = "generate-image", description = "根据文字描述生成图片并保存到文件服务器。"
			+ "当用户要求画图、生成图片、画一张、P 一张、生成海报、插画、头像时使用。"
			+ "返回图片的访问地址 fileUrl，请把该地址用 Markdown 图片语法展示给用户。")
	@Override
	public Map<String, Object> generateImage(
			@McpToolParam(description = "图片内容的详细描述，尽量包含主体、风格、构图、光线、色彩等要素，中文即可")
			String prompt) {
		return super.generateImage(prompt);
	}

}

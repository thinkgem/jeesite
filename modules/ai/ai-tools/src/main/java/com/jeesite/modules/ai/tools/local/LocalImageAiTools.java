/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.local;

import com.jeesite.modules.ai.tools.annotation.AiTools;
import com.jeesite.modules.ai.tools.impl.ImageAiTools;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * AI 本地工具调用（生图）
 * @author ThinkGem
 */
@AiTools
@ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "false", matchIfMissing = true)
public class LocalImageAiTools extends ImageAiTools {

	/**
	 * 根据文字描述生成图片，返回 Markdown 图片语法字符串
	 */
	@Tool(name = "generate-image", description = "根据文字描述生成图片并保存到文件服务器。"
			+ "当用户要求画图、生成图片、画一张、P 一张、生成海报、插画、头像时使用。"
			+ "返回 Markdown 图片语法字符串：![描述](图片地址)，"
			+ "请直接把该字符串原样输出给用户，让用户能看到生成的图片，不要额外解释或改写。")
	@Override
	public String generateImage(
			@ToolParam(description = "图片内容的详细描述，尽量包含主体、风格、构图、光线、色彩等要素，中文即可")
			String prompt) {
		return super.generateImage(prompt);
	}

}

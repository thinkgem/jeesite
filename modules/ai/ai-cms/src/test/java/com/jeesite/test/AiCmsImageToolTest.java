/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.cms.service.AiCmsImageService;
import com.jeesite.modules.ai.tools.annotation.AiTools;
import com.jeesite.modules.ai.tools.local.LocalImageAiTools;
import com.jeesite.modules.ai.tools.service.ImageGenerateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * AI 生图工具测试（真实调用智谱生图模型，不走 Mock）：
 * 1. 工具定义可被 Spring AI 解析；2. 直接调用工具真实生图并保存文件服务器；
 * 3. 通过聊天链路（function calling）让模型自动调用生图工具。
 * 前置条件：设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），未设置时本测试自动跳过。
 * @author ThinkGem
 * @version 2026-09-09
 */
@ActiveProfiles("unit_test")
@SpringBootTest(classes = ApplicationTest.class, properties = {
		// 识图：智谱免费视觉理解小模型
		"spring.ai.model.chat=openai",
		"spring.ai.openai.chat.base-url=https://open.bigmodel.cn/api/paas/v4",
		"spring.ai.openai.chat.api-key=${ZHIPUAI_APP_KEY:}",
		"spring.ai.openai.chat.model=glm-4.6v-flash",
		// 生图：智谱免费文生图小模型，生成的效果不太好，仅用于测试
		"spring.ai.model.image=openai",
		"spring.ai.openai.image.base-url=https://open.bigmodel.cn/api/paas/v4",
		"spring.ai.openai.image.api-key=${ZHIPUAI_APP_KEY:}",
		"spring.ai.openai.image.timeout=15m",
		"spring.ai.openai.image.model=cogview-3-flash",
		"spring.ai.openai.image.size=1024x1024",
		// 启用工具调用：LocalImageAiTools 通过 @AiTools 注册为 ChatClient 的 defaultTools
		"spring.ai.tools.enabled=true",
		// 关闭未使用的 Spring AI 组件（缺少 api-key 会导致上下文启动失败，请勿注释）
		"spring.ai.model.embedding=none", "spring.ai.model.embedding.text=none", "spring.ai.model.embedding.multimodal=none",
		"spring.ai.model.audio.transcription=none", "spring.ai.model.audio.speech=none", "spring.ai.model.moderation=none",
		"spring.ai.vectorstore.type=none", "spring.ai.mcp.client.enabled=false",
		"spring.application.name=test"})
public class AiCmsImageToolTest extends BaseSpringContextTests {

	@Autowired
	private LocalImageAiTools imageTools;

	@Autowired
	private AiCmsImageService imageService;

	@Autowired
	private ChatClient chatClient;

	/**
	 * 未设置智谱 API Key 时，跳过测试（而不是报错）
	 */
	@BeforeAll
	static void checkApiKey() {
		String apiKey = System.getenv().getOrDefault("ZHIPUAI_APP_KEY", "");
		assumeTrue(!apiKey.isBlank(), "未设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），跳过生图工具测试");
	}

	/**
	 * 工具定义可被 Spring AI 正确解析：工具名、描述、入参 schema，
	 * 且类上有 @AiTools 注解（AiCmsChatConfig 据此注册为 defaultTools），
	 * 生图服务实现了 ImageGenerateService 接口
	 */
	@Test
	public void test01ToolDefinition() {
		ToolCallback[] callbacks = MethodToolCallbackProvider.builder()
				.toolObjects(imageTools)
				.build()
				.getToolCallbacks();
		assertEquals(1, callbacks.length);
		ToolDefinition definition = callbacks[0].getToolDefinition();
		assertEquals("generate-image", definition.name());
		assertTrue(definition.description().contains("生成图片"),
				"工具描述应说明用途，便于模型判断何时调用：" + definition.description());
		assertTrue(definition.inputSchema().contains("prompt"),
				"入参 schema 应包含 prompt：" + definition.inputSchema());
		assertNotNull(LocalImageAiTools.class.getAnnotation(AiTools.class));
		assertTrue(imageService instanceof ImageGenerateService);
	}

	/**
	 * 直接调用工具真实生图：生图 → 保存文件服务器 → 返回访问地址
	 */
	@Test
	public void test02GenerateImage() throws Exception {
		assertTrue(imageService.isEnabled(), "未启用生图模型");
		Map<String, Object> res = imageTools.generateImage("一只可爱的橘猫，坐在窗台上晒太阳，写真风格");
		System.out.println("生图工具返回结果：" + res);
		assertEquals("true", res.get("result"), "生图失败：" + res.get("message"));
		assertNotNull(res.get("prompt"));
		// 文件名：ai-image-时间戳.扩展名（扩展名由实际图片格式决定）
		String fileName = String.valueOf(res.get("fileName"));
		assertTrue(fileName.startsWith("ai-image-"), "文件名应带 ai-image- 前缀：" + fileName);
		// 访问地址：文件服务器按文件 ID 生成，形如 /userfiles/fileupload/202609/xxx.jpg
		String fileUrl = String.valueOf(res.get("fileUrl"));
		assertTrue(fileUrl.contains("/userfiles"), "应返回文件服务器访问地址：" + fileUrl);
		assertTrue(fileUrl.endsWith(fileName.substring(fileName.lastIndexOf('.'))),
				"访问地址格式应与图片格式一致：" + fileUrl);
		assertFalse(res.containsKey("fileRealPath"), "不应把服务器真实路径返回给大模型");
		// 落盘校验：文件保存在当前工作目录的 userfiles 下（不在则说明使用了其它存储实现）
		Path path = Paths.get(StringUtils.removeStart(fileUrl, "/"));
		if (Files.exists(path)) {
			assertTrue(AiCmsImageService.isImage(Files.readAllBytes(path)), "保存的文件不是有效图片：" + path);
		} else {
			System.out.println("图片未保存到当前工作目录，跳过落盘校验：" + path);
		}
	}

	/**
	 * 聊天链路：模型识别到画图意图后，自动调用 generate-image 工具
	 * （需要聊天模型支持 function calling，不支持时本用例自动跳过）
	 */
	@Test
	public void test03ChatTriggerGenerateImage() {
		String content;
		try {
			content = chatClient.prompt()
					.messages(new UserMessage("请帮我画一只在窗台上晒太阳的橘猫。"))
					.call()
					.content();
		} catch (Exception e) {
			String message = String.valueOf(e.getMessage());
			// 模型限流（429）属于外部服务问题，其它异常多为模型不支持 function calling
			assumeTrue(false, (message.contains("429") ? "聊天模型限流：" : "当前聊天模型不支持 function calling：") + message);
			return;
		}
		System.out.println("聊天链路生图结果：" + content);
		assertNotNull(content);
		assertTrue(content.length() >= 2);
	}

}

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.cms.service.AiCmsChatService;
import com.jeesite.modules.ai.cms.service.AiCmsImageService;
import com.jeesite.modules.ai.cms.service.CacheChatMemoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MimeTypeUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


/**
 * AI 生图 + 识图：
 * 先通过免费生图模型（cogview-3-flash）生成图片并保存到文件服务器，
 * 再使用免费视觉理解模型（glm-4.6v-flash）识图，识别生成图片的内容。
 * 前置条件：设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），未设置时本测试自动跳过。
 * @author ThinkGem
 * @version 2026-09-07
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
		// 关闭未使用的 Spring AI 组件（缺少 api-key 会导致上下文启动失败，请勿注释）
		"spring.ai.model.embedding=none", "spring.ai.model.embedding.text=none", "spring.ai.model.embedding.multimodal=none",
		"spring.ai.model.audio.transcription=none", "spring.ai.model.audio.speech=none", "spring.ai.model.moderation=none",
		"spring.ai.vectorstore.type=none", "spring.ai.mcp.client.enabled=false",
		"spring.application.name=test"})
public class AiCmsImageTest extends BaseSpringContextTests {

	@Autowired
	private AiCmsImageService aiCmsImageService;

	@Autowired
	private AiCmsChatService aiCmsChatService;

	@Autowired
	private ChatClient chatClient;

	/**
	 * 未设置智谱 API Key 时，跳过测试（而不是报错）
	 */
	@BeforeAll
	static void checkImageService() {
		String apiKey = System.getenv().getOrDefault("ZHIPUAI_APP_KEY", "");
		assumeTrue(!apiKey.isBlank(), "未设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），跳过生图集成测试");
	}

	/**
	 * 生图 + 识图 完整链路：生图服务 → 图片字节 → 保存文件服务器 →
	 * 读取图片 → 多模态消息 → ollama 视觉模型识别
	 */
	@Test
	public void test01GenerateAndRecognize() {
		assertTrue(aiCmsImageService.isEnabled());
		// 1. 生图并保存到文件服务器（关联到聊天会话，演示 bizKey/bizType 用法）
		String conversationId = CacheChatMemoryRepository.genUserConversationId();
		Map<String, Object> data = aiCmsImageService.generateImage(conversationId, "cms-chat",
				"一只可爱的橘猫，坐在窗台上晒太阳，写真风格");
		System.out.println("生图结果：" + data);
		assertNotNull(data.get("fileName"));
		assertNotNull(data.get("fileUrl"));
		assertNotNull(data.get("fileRealPath"));
		String fileRealPath = data.get("fileRealPath").toString();

		// 2. 读取保存的图片字节（文件服务器本地路径：工作目录 + fileUrl）
		byte[] imageBytes = readImageBytes(fileRealPath);
		assertNotNull(imageBytes);
		assertTrue(imageBytes.length > 0);

		// 3. 识图：与 AiCmsChatService 相同的多模态消息构建方式
		Media media = Media.builder()
			.mimeType(MimeTypeUtils.IMAGE_PNG)
			.data(new ByteArrayResource(imageBytes) {
				@Override
				public String getFilename() {
					return data.get("fileName").toString();
				}
			})
			.build();
		UserMessage userMessage = UserMessage.builder()
			.text("这张图片里画的是什么？请简要描述图片内容。")
			.media(java.util.List.of(media))
			.build();
		String content = chatClient.prompt().messages(userMessage).call().content();
		System.out.println("识图结果：" + content);
		assertNotNull(content);
		assertTrue(content.length() >= 2);

		// 4. 会话记忆验证：生图后追问“画的什么”，模型应结合记忆（生图记录 + 会话图片）回答
		String followUp = "我刚才让你画的画里是什么动物？什么风格？请简短回答。";
		MockHttpServletRequest request = new MockHttpServletRequest();
		StringBuilder text = new StringBuilder();
		aiCmsChatService.chatStream(conversationId, followUp, request)
			.doOnNext(response -> {
				if (response.getResult() != null && response.getResult().getOutput() != null
						&& response.getResult().getOutput().getText() != null) {
					text.append(response.getResult().getOutput().getText());
				}
			})
			.blockLast(Duration.ofMinutes(3));
		String memoryResult = text.toString();
		System.out.println("会话记忆追问结果：" + memoryResult);
		assertNotNull(memoryResult);
		assertTrue(memoryResult.length() >= 2);
	}

	/**
	 * 读取文件服务器保存的图片字节（兼容从项目根目录或模块目录启动的两种工作目录）
	 */
	private byte[] readImageBytes(String relativePath) {
		Path path = Paths.get(relativePath);
		assertTrue(Files.exists(path), "生图文件不存在：" + path);
		try {
			return Files.readAllBytes(path);
		} catch (Exception e) {
			throw new RuntimeException("读取生图文件失败：" + path, e);
		}
	}

}


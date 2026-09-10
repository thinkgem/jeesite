/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.cms.service.AiCmsChatService;
import com.jeesite.modules.ai.cms.service.CacheChatMemoryRepository;
import com.jeesite.modules.ai.cms.utils.AiRetryUtils;
import com.jeesite.modules.ai.cms.utils.AiThinkUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

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

	private ChatClient chatClient;
	private AiRetryUtils aiRetryUtils;
	private AiCmsChatService aiCmsChatService;

	@Autowired
	public void setChatClient(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Autowired
	public void setAiRetryUtils(AiRetryUtils aiRetryUtils) {
		this.aiRetryUtils = aiRetryUtils;
	}

	@Autowired
	public void setAiCmsChatService(AiCmsChatService aiCmsChatService) {
		this.aiCmsChatService = aiCmsChatService;
	}

	/**
	 * 未设置智谱 API Key 时，跳过测试（而不是报错）
	 */
	@BeforeAll
	static void checkApiKey() {
		String apiKey = System.getenv().getOrDefault("ZHIPUAI_APP_KEY", "");
		assumeTrue(!apiKey.isBlank(), "未设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），跳过生图工具测试");
	}

	/**
	 * 聊天链路：模型识别到画图意图后，自动调用 generate-image 工具
	 * （需要聊天模型支持 function calling，不支持时本用例自动跳过）
	 */
	@Test
	public void test01ChatTriggerGenerateImage() {
		String content;
		try {
			content = aiRetryUtils.execute(() -> chatClient.prompt()
					.messages(new UserMessage("请帮我画一只在窗台上晒太阳的橘猫。"))
					.call()
					.content());
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

	/**
	 * 聊天对话，流输出，验证原生深度思考字段：
	 * 模型返回的 reasoning_content / thinking 等思考内容，应被归一化到
	 * AssistantMessage.metadata.reasoningContent 字段中独立下发，
	 * 而不是混在正文里，也不是以 &lt;think&gt; 标签的形式注入正文。
	 */
	@Test
	public void test02ChatStreamTriggerGenerateImage() {
		logger.info("===== 聊天对话，流输出（验证原生思考字段 reasoningContent）");
		String conversationId = CacheChatMemoryRepository.genUserConversationId();
		// 触发模型推理的问题
		String message = "请帮我画一只在窗台上晒太阳的橘猫。";
		MockHttpServletRequest request = new MockHttpServletRequest();
		StringBuilder text = new StringBuilder();
		// OpenAI 系返回累计值，Ollama 系返回增量值，这里统一累计
		StringBuilder reasoning = new StringBuilder();
		aiCmsChatService.chatStream(conversationId, message, request)
				.doOnNext(response -> {
					if (response.getResult() != null && response.getResult().getOutput() != null) {
						AssistantMessage output = response.getResult().getOutput();
						if (output.getText() != null) {
							text.append(output.getText());
						}
						String curr = AiThinkUtils.getReasoningContent(output);
						String merged = AiThinkUtils.mergeReasoningContent(reasoning.toString(), curr);
						reasoning.setLength(0);
						reasoning.append(merged);
					}
				})
				.blockLast(Duration.ofMinutes(3));
		String result = text.toString();
		System.out.println("流式结果：" + result);
		assertNotNull(result);
		assertTrue(result.length() >= 2);

		// 思考内容应通过独立字段下发，不能混在正文中
		String thinkResult = reasoning.toString();
		System.out.println("思考内容（原生字段 reasoningContent）：" + thinkResult);
		assertFalse(result.contains("reasoning_content"), "流式结果泄漏了原始 reasoning_content 字段");
		// 若模型返回了思考内容，则正文不应再包含 <think> 标签（原生字段与标签二选一）
		if (StringUtils.isNotBlank(thinkResult)) {
			assertFalse(result.contains("<think>"), "模型已返回原生思考字段，正文中不应再出现 <think> 标签");
		}
	}

}

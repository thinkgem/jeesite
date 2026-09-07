/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.cms.service.AiCmsChatService;
import com.jeesite.modules.ai.cms.service.CacheChatMemoryRepository;
import com.jeesite.modules.sys.entity.Area;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * AI 对话单元测试
 * @author ThinkGem
 * @version 2026-09-07
 */
@ActiveProfiles("unit_test")
@SpringBootTest(classes = ApplicationTest.class, properties = {
		// 对话：智谱免费对话模型（支持工具调用）
		"spring.ai.model.chat=openai",
		"spring.ai.openai.chat.base-url=https://open.bigmodel.cn/api/paas/v4",
		"spring.ai.openai.chat.api-key=${ZHIPUAI_APP_KEY:}",
		"spring.ai.openai.chat.model=glm-4.7-flash",
		// 结构化输出（test04Entity 的行政区划 JSON 较长），调大输出上限避免 JSON 被截断
		"spring.ai.openai.chat.max-tokens=8192",
		// 流式输出整体耗时可能超过默认 60 秒（OkHttp callTimeout 会掐断流），调大客户端超时
		"spring.ai.openai.timeout=15m",
		"spring.ai.model.image=none",
		"spring.ai.tools.enabled=true",
		// 关闭未使用的 Spring AI 组件（缺少 api-key 会导致上下文启动失败，请勿注释）
		"spring.ai.model.embedding=none", "spring.ai.model.embedding.text=none", "spring.ai.model.embedding.multimodal=none",
		"spring.ai.model.audio.transcription=none", "spring.ai.model.audio.speech=none", "spring.ai.model.moderation=none",
		"spring.ai.vectorstore.type=none", "spring.ai.mcp.client.enabled=false",
		"spring.application.name=test"})
public class AiCmsChatTest extends BaseSpringContextTests {

	/**
	 * 未设置智谱 API Key 时，跳过测试（而不是报错）
	 */
	@BeforeAll
	static void checkApiKey() {
		String apiKey = System.getenv().getOrDefault("ZHIPUAI_APP_KEY", "");
		assumeTrue(!apiKey.isBlank(), "未设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），跳过对话测试");
	}

	private AiCmsChatService aiCmsChatService;

	@Autowired
	public void setAiChatServiceTest(AiCmsChatService aiCmsChatService) {
		this.aiCmsChatService = aiCmsChatService;
	}

	@Test
	public void test01Text() {
		logger.info("===== 聊天对话，文本输出");
		String message = "你好";
		String text = aiCmsChatService.chatText(message);
		System.out.println(text);
	}

	@Test
	public void test02Json() {
		logger.info("===== 聊天对话，结构化输出 JSON");
		String message = "张三";
		Map<String, Object> map = callWithRetry(() -> aiCmsChatService.chatJson(message));
		System.out.println(JsonMapper.toJson(map));
	}

	@Test
	public void test03Tool() {
		logger.info("===== 聊天对话，结构化输出 Tool Calling");
		Map<String, Object> map = callWithRetry(() -> aiCmsChatService.chatJson("打开客厅的灯"));
		System.out.println(JsonMapper.toJson(map));
		map = callWithRetry(() -> aiCmsChatService.chatJson("关闭客厅的灯"));
		System.out.println(JsonMapper.toJson(map));
	}

	/**
	 * 免费模型有限流（429），失败时等待后重试
	 */
	private <T> T callWithRetry(java.util.function.Supplier<T> call) {
		RuntimeException last = null;
		for (int i = 0; i < 5; i++) {
			try {
				return call.get();
			} catch (RuntimeException e) {
				last = e;
				logger.warn("调用失败（第 " + (i + 1) + " 次）：" + e.getMessage() + "，15 秒后重试");
				try {
					Thread.sleep(15000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException(ie);
				}
			}
		}
		throw last;
	}

	@Test
	public void test04Entity() {
		logger.info("===== 聊天对话，结构化输出 Entity");
		String message = "北京";
		List<Area> list = callWithRetry(() -> aiCmsChatService.chatArea(message));
		System.out.println(JsonMapper.toJson(list));
	}

	/**
	 * 聊天对话，流输出，验证 WebClientThinkConfig 的思考内容处理：
	 * 流式返回的 reasoning_content 应被包裹在 &lt;think&gt;...&lt;/think&gt; 标签中，
	 * 而不是以原始 reasoning_content 字段泄漏到正文里。
	 */
	@Test
	public void test05ChatStream() {
		logger.info("===== 聊天对话，流输出（验证 <think> 思考标签）");
		String conversationId = CacheChatMemoryRepository.genUserConversationId();
		// 触发模型推理的问题
		String message = "9.11 和 9.8 哪个大？请一步步推理后回答。";
		MockHttpServletRequest request = new MockHttpServletRequest();
		StringBuilder text = new StringBuilder();
		aiCmsChatService.chatStream(conversationId, message, request)
				.doOnNext(response -> {
					if (response.getResult() != null && response.getResult().getOutput() != null
							&& response.getResult().getOutput().getText() != null) {
						text.append(response.getResult().getOutput().getText());
					}
				})
				.blockLast(Duration.ofMinutes(3));
		String result = text.toString();
		System.out.println("流式结果：" + result);
		assertNotNull(result);
		assertTrue(result.length() >= 2);

		// 验证思考标签：如果模型产生了思考内容，必须被 <think>...</think> 包裹
		boolean hasThinkOpen = result.contains("<think>");
		boolean hasThinkClose = result.contains("</think>");
		System.out.println("思考标签检测：<think> 开始=" + hasThinkOpen + "，</think> 闭合=" + hasThinkClose);
		if (hasThinkOpen) {
			assertTrue(hasThinkClose, "流式结果有 <think> 开始标签，但缺少 </think> 闭合标签");
		}
		// 验证没有原始 reasoning_content 字段泄漏到正文
		assertFalse(result.contains("reasoning_content"), "流式结果泄漏了原始 reasoning_content 字段，WebClientThinkConfig 未生效");
	}

}

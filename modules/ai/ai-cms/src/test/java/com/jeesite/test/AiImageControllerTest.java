/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.cms.service.AiCmsImageService;
import com.jeesite.modules.ai.cms.web.CmsAiImageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AI 生图控制器测试（OpenAI v1 images/generations），真实调用生图模型。
 * <p>
 * 启动完整 Spring 容器拿到真实的 {@link AiCmsImageService}，再用 standalone MockMvc 驱动
 * {@link CmsAiImageController}（${adminPath} 占位符通过 addPlaceholderValue 解析），
 * 因此生图链路是真实的：模型生成 → 下载图片 → 校验图片格式 → 保存到文件服务器 → 返回访问地址。
 * <p>
 * 前置条件：设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），
 * 未设置时真实生图的用例自动跳过，参数校验类用例仍会执行。
 *
 * @author ThinkGem
 * @version 2026-09-10
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
public class AiImageControllerTest extends BaseSpringContextTests {

	/** ${adminPath} 占位符取值 + 控制器映射 images/generations */
	private static final String URL = "/js/a/cms/images/generations";

	private AiCmsImageService aiCmsImageService;
	private MockMvc mockMvc;

	@Autowired
	public void setAiCmsImageService(AiCmsImageService aiCmsImageService) {
		this.aiCmsImageService = aiCmsImageService;
	}

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new CmsAiImageController(aiCmsImageService))
				.addPlaceholderValue("adminPath", "/js/a")
				.build();
		checkApiKey();
	}

	/**
	 * 未设置智谱 API Key 时，跳过测试（而不是报错）
	 */
	static void checkApiKey() {
		String apiKey = System.getenv().getOrDefault("ZHIPUAI_APP_KEY", "");
		assumeTrue(!apiKey.isBlank(), "未设置环境变量 ZHIPUAI_APP_KEY（智谱开放平台 API Key），跳过生图工具测试");
	}
	
	/**
	 * 真实生图：仅传必填 prompt，模型与尺寸取 spring.ai.openai.image 默认配置
	 */
	@Test
	public void test01Generations() throws Exception {
		logger.info("===== 生图接口：仅必填 prompt（使用默认模型 cogview-3-flash）");
		assertTrue(aiCmsImageService.isEnabled());

		String content = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("""
				{"prompt": "一只可爱的橘猫，坐在窗台上晒太阳"}"""))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.created").exists())
				.andExpect(jsonPath("$.data.length()").value(1))
				.andExpect(jsonPath("$.data[0].url").exists())
				.andReturn().getResponse().getContentAsString();
		System.out.println("生图结果：" + content);
		assertFalse(content.contains("\"revised_prompt\""), "智谱模型不返回改写提示词时不应输出该字段");
	}

	/**
	 * 真实生图：完整参数（model、n、size、response_format、user）与 JeeSite 扩展的 bizKey、bizType，
	 * response_format 为 b64_json 时应同时返回可访问的地址与 Base64 数据
	 */
	@Test
	public void test02GenerationsWithParams() throws Exception {
		logger.info("===== 生图接口：完整参数 + response_format=b64_json");

		String content = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "prompt": "一只可爱的橘猫，坐在窗台上晒太阳",
				  "model": "cogview-3-flash",
				  "n": 1,
				  "size": "1024x1024",
				  "response_format": "b64_json",
				  "user": "unit-test",
				  "bizKey": "conv-unit-test",
				  "bizType": "cms-chat"
				}"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.created").exists())
				.andExpect(jsonPath("$.data.length()").value(1))
				.andExpect(jsonPath("$.data[0].url").exists())
				.andExpect(jsonPath("$.data[0].b64_json").exists())
				.andReturn().getResponse().getContentAsString();
		System.out.println("生图结果（Base64 已省略）：" + content.substring(0, Math.min(content.length(), 200)));
	}

	/**
	 * 真实生图：n=2 请求多张图片。
	 * <p>实测：请求已正确带上 n=2（打开 org.springframework.ai.openai 的 TRACE 日志可见
	 * {@code ImageGenerateParams{... n=2 ...}}），但智谱 cogview-3-flash 免费模型会忽略 n 大于 1，
	 * 只返回 1 张，属于模型侧能力限制，不是接口实现问题。
	 * 因此这里只校验至少返回一张且地址可访问；如需验证多张，请换用支持 n 的模型
	 * （如 OpenAI dall-e-2、gpt-image-1）。
	 */
	@Test
	public void test03GenerationsMultipleImages() throws Exception {
		logger.info("===== 生图接口：n=2 请求多张图片");

		String content = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("""
				{"prompt": "一只可爱的橘猫，坐在窗台上晒太阳", "n": 2}"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$.data[0].url").exists())
				.andReturn().getResponse().getContentAsString();
		logger.info("n=2 的实际返回（智谱免费模型只返回 1 张）：" + content);
	}

}

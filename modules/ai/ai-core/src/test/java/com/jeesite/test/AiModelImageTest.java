/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.utils.AiModelUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * AI 生图 + 识图单元测试（使用「AI模型」ai_model 中的动态配置，通过 {@link AiModelUtils} 调用）
 * <p>
 * 流程：按「AI模型」配置生图 → 读取图片字节（兼容 b64_json 与图片地址）→
 * 构建多模态消息 → 视觉模型识图。
 * <p>
 * 前置条件：
 * <ul>
 *   <li>数据库中已注册：图像生成模型 kwai-kolors-kolors（{@link #MODEL_CODE_IMAGE}）</li>
 *   <li>识图需配置多模态视觉模型（{@link #MODEL_CODE_VISION}，如 qwen3-vl）</li>
 * </ul>
 * @author ThinkGem
 * @version 2026-11-19
 */
@ActiveProfiles("unit_test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(classes = ApplicationTest.class)
public class AiModelImageTest extends BaseSpringContextTests {

	/** 图像生成模型编码（ai_model.model_code） */
	private static final String MODEL_CODE_IMAGE = "kwai-kolors-kolors";

	/** 视觉理解模型编码（识图，需为多模态模型，如 qwen3-vl-4b） */
	private static final String MODEL_CODE_VISION = "qwen3-vl-4b";

	/** 共享的 HTTP 客户端（下载生图返回的图片地址） */
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(30))
			.build();

	/**
	 * 获取模型，不存在、停用或未配置接口地址时跳过当前测试
	 * @param modelCode 模型编码
	 * @param modelType 模型用途（用于跳过提示）
	 */
	protected AiModel getModelOrSkip(String modelCode, String modelType) {
		AiModel aiModel;
		try {
			aiModel = AiModelUtils.getModel(modelCode);
		} catch (Exception e) {
			assumeTrue(false, "读取「AI模型」配置失败：" + e.getMessage());
			return null;
		}
		assumeTrue(aiModel != null, "未在「AI模型」中配置" + modelType + "模型：" + modelCode);
		assumeTrue(AiModel.STATUS_NORMAL.equals(aiModel.getStatus()), "模型已停用：" + modelCode);
		assumeTrue(StringUtils.isNotBlank(aiModel.getBaseUrl()), "模型未配置接口地址：" + modelCode);
		return aiModel;
	}

	/**
	 * 按「AI模型」配置生图，再用视觉模型识图
	 */
	@Test
	public void test01ImageAndVision() {
		logger.info("===== AI 生图 + 识图，生图模型：{}，识图模型：{}", MODEL_CODE_IMAGE, MODEL_CODE_VISION);
		AiModel imageModel = getModelOrSkip(MODEL_CODE_IMAGE, "图像生成");
		AiModel visionModel = getModelOrSkip(MODEL_CODE_VISION, "视觉理解");

		// 1. 生图（模型配置中的 options.image 生效）
		byte[] imageBytes = generateImage(imageModel,
				"一只可爱的橘猫坐在窗台上晒太阳，写真风格，柔和的光线");
		assertNotNull(imageBytes, "生图失败，未获取到图片数据");
		assertTrue(imageBytes.length > 0);
		System.out.println("生图成功，图片大小：" + imageBytes.length + " 字节");

		// 2. 识图：多模态消息（与 AiChatService 的构建方式一致）
		Media media = Media.builder()
				.mimeType(getImageMimeType(imageBytes))
				.data(new ByteArrayResource(imageBytes) {
					@Override
					public String getFilename() {
						return "ai-image" + getImageExtension(imageBytes);
					}
				})
				.build();
		UserMessage userMessage = UserMessage.builder()
				.text("这张图片画的是什么？请简要描述图片内容。")
				.media(List.of(media))
				.build();
		String content;
		try {
			content = AiModelUtils.getChatClient(visionModel)
					.prompt()
					.messages(userMessage)
					.call()
					.content();
		} catch (Exception e) {
			// 识图模型不支持多模态时跳过，不判定为测试失败
			assumeTrue(false, "识图失败（请配置多模态视觉模型）：" + e.getMessage());
			return;
		}
		System.out.println("识图结果：" + content);
		assertNotNull(content);
		assertFalse(content.isBlank());
	}

	/**
	 * 按「AI模型」配置生图，返回图片字节（兼容 b64_json 与图片地址两种返回）
	 * @param aiModel 生图模型
	 * @param prompt 图片描述
	 */
	protected byte[] generateImage(AiModel aiModel, String prompt) {
		ImageModel imageModel = AiModelUtils.getImageModel(aiModel);
		ImageResponse response = imageModel.call(new ImagePrompt(prompt));
		if (response == null || response.getResult() == null) {
			return null;
		}
		Image image = response.getResult().getOutput();
		if (image == null) {
			return null;
		}
		// 优先 Base64 数据，否则下载图片地址
		if (StringUtils.isNotBlank(image.getB64Json())) {
			String b64Json = image.getB64Json();
			if (b64Json.startsWith("data:") && b64Json.contains(",")) {
				b64Json = StringUtils.substringAfter(b64Json, ",");
			}
			return Base64.getMimeDecoder().decode(b64Json);
		}
		if (StringUtils.isNotBlank(image.getUrl())) {
			try {
				HttpRequest request = HttpRequest.newBuilder(URI.create(image.getUrl()))
						.timeout(Duration.ofMinutes(5)).GET().build();
				HttpResponse<byte[]> res = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
				if (res.statusCode() == 200) {
					return res.body();
				}
				logger.error("下载生图文件失败，状态码：{}，地址：{}", res.statusCode(), image.getUrl());
			} catch (Exception e) {
				logger.error("下载生图文件失败：{}，地址：{}", e.getMessage(), image.getUrl());
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt();
				}
			}
		}
		return null;
	}

	/**
	 * 按图片魔数获取 MIME 类型
	 */
	protected MimeType getImageMimeType(byte[] imageBytes) {
		return ".jpg".equals(getImageExtension(imageBytes)) ? MimeTypeUtils.IMAGE_JPEG : MimeTypeUtils.IMAGE_PNG;
	}

	/**
	 * 按图片魔数获取扩展名（默认 png）
	 */
	protected static String getImageExtension(byte[] imageBytes) {
		if (imageBytes != null && imageBytes.length > 2
				&& (imageBytes[0] & 0xFF) == 0xFF && (imageBytes[1] & 0xFF) == 0xD8) {
			return ".jpg";
		}
		return ".png";
	}

}

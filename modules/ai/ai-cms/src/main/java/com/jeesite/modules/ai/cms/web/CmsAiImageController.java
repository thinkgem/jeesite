/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.web;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ai.cms.entity.AiImageRequest;
import com.jeesite.modules.ai.cms.entity.AiImageResponse;
import com.jeesite.modules.ai.cms.service.AiCmsImageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * AI 生图控制器类
 * @author ThinkGem
 */
@RestController
@RequestMapping("${adminPath}/cms/images")
public class CmsAiImageController extends BaseController {

	private static final Log LOG = LogFactory.getLog(CmsAiImageController.class);

	private final AiCmsImageService aiCmsImageService;

	public CmsAiImageController(AiCmsImageService aiCmsImageService) {
		this.aiCmsImageService = aiCmsImageService;
	}

	/**
	 * AI 生图（OpenAI v1 兼容，对应官方 POST /v1/images/generations）
	 * <p>
	 * 请求示例：
	 * <pre>{@code
	 * POST http://127.0.0.1:8980/js/a/cms/images/generations
	 * {
	 *   "prompt": "一只可爱的橘猫，坐在窗台上晒太阳",
	 *   "model": "Kwai-Kolors/Kolors",
	 *   "n": 1,
	 *   "size": "1024x1024",
	 *   "quality": "standard",
	 *   "style": "vivid",
	 *   "response_format": "url",
	 *   "user": "user-001",
	 *   "bizKey": "会话ID",
	 *   "bizType": "cms-chat"
	 * }
	 * }</pre>
	 * 响应示例（字段与 OpenAI 一致，snake_case）：
	 * <pre>{@code
	 * {
	 *   "created": 1757000000,
	 *   "data": [ { "url": "/js/userfiles/.../ai-image-xxx.png", "revised_prompt": "..." } ]
	 * }
	 * }</pre>
	 * 说明：
	 * <ul>
	 *   <li>生图参数见 {@link AiImageRequest}，响应结构见 {@link AiImageResponse}；</li>
	 *   <li>失败时返回 OpenAI 错误结构：{"error":{"message":"...","type":"invalid_request_error","param":"prompt"}}。</li>
	 * </ul>
	 * @author ThinkGem
	 */
	@RequestMapping(value = "generations", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> generations(@RequestBody(required = false) AiImageRequest request) {
		try {
			AiImageRequest req = request != null ? request : new AiImageRequest();
			if (StringUtils.isBlank(req.getPrompt())) {
				return renderError("prompt 为必填参数", "invalid_request_error", "prompt", HttpStatus.BAD_REQUEST);
			}
			if (!aiCmsImageService.isEnabled()) {
				return renderError("未启用图片生成模型，请配置 spring.ai.model.image: openai",
						"invalid_request_error", "model", HttpStatus.SERVICE_UNAVAILABLE);
			}
			List<Map<String, Object>> images = aiCmsImageService.generateImages(req.getBizKey(), req.getBizType(),
					req.getPrompt(), req.toImageOptions(null));
			return ResponseEntity.ok(AiImageResponse.of(images));
		} catch (ServiceException e) {
			return renderError(e.getMessage(), "invalid_request_error", null, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.error("Image generations error: " + e.getMessage(), e);
			return renderError(e.getMessage(), "server_error", null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * OpenAI 标准错误响应结构：{"error":{"message":"...","type":"...","param":"..."}}
	 * @author ThinkGem
	 */
	private static ResponseEntity<?> renderError(String message, String type, String param, HttpStatus status) {
		return ResponseEntity.status(status)
				.body(Map.of("error", new AiImageResponse.AiImageError(message, type, param)));
	}

}

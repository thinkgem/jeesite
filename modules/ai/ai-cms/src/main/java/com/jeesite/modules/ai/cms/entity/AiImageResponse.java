/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AI 生图响应对象（OpenAI v1 images 响应体）。
 * <p>输出与 OpenAI v1 {@code /v1/images/generations} 的响应结构一致：
 * <pre>{@code
 * {
 *   "created": 1757000000,
 *   "data": [
 *     { "url": "/js/userfiles/.../ai-image-xxx.png", "b64_json": "...", "revised_prompt": "..." }
 *   ]
 * }
 * }</pre>
 * 约定：
 * <ul>
 *   <li>{@code url} 为图片保存到文件服务器后的访问地址；</li>
 *   <li>{@code b64_json} 仅在请求 response_format 为 b64_json 时输出；</li>
 *   <li>{@code revised_prompt} 仅在模型返回改写后的提示词时输出（如 dall-e-3、gpt-image-1）；</li>
 *   <li>失败时输出 {@link AiImageError} 错误结构。</li>
 * </ul>
 *
 * @author ThinkGem
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiImageResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** Unix 时间戳（秒） */
	private Long created;

	/** 生成的图片列表 */
	private List<AiImageData> data = new ArrayList<>();

	/**
	 * 生图服务结果转换为 OpenAI v1 响应
	 * @param images 每张图片的信息：created、fileUrl、b64Json、revisedPrompt
	 * @author ThinkGem
	 */
	public static AiImageResponse of(List<Map<String, Object>> images) {
		AiImageResponse res = new AiImageResponse();
		if (images != null) {
			for (Map<String, Object> image : images) {
				if (res.created == null && image.get("created") != null) {
					res.created = ((Number) image.get("created")).longValue();
				}
				res.data.add(new AiImageData((String) image.get("fileUrl"), (String) image.get("b64Json"),
						(String) image.get("revisedPrompt")));
			}
		}
		if (res.created == null) {
			res.created = System.currentTimeMillis() / 1000;
		}
		return res;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public List<AiImageData> getData() {
		return data;
	}

	public void setData(List<AiImageData> data) {
		this.data = data;
	}

	/**
	 * 生成的图片数据（OpenAI data 元素）
	 * @author ThinkGem
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class AiImageData implements Serializable {

		@Serial
		private static final long serialVersionUID = 1L;

		/** 图片访问地址（response_format 为 url 时返回） */
		private String url;

		/** Base64 图片数据（response_format 为 b64_json 时返回） */
		@JsonProperty("b64_json")
		private String b64Json;

		/** 模型改写后的提示词（如 dall-e-3、gpt-image-1） */
		@JsonProperty("revised_prompt")
		private String revisedPrompt;

		public AiImageData() {
		}

		public AiImageData(String url, String b64Json, String revisedPrompt) {
			this.url = url;
			this.b64Json = b64Json;
			this.revisedPrompt = revisedPrompt;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getB64Json() {
			return b64Json;
		}

		public void setB64Json(String b64Json) {
			this.b64Json = b64Json;
		}

		public String getRevisedPrompt() {
			return revisedPrompt;
		}

		public void setRevisedPrompt(String revisedPrompt) {
			this.revisedPrompt = revisedPrompt;
		}
	}

	/**
	 * 错误响应对象（OpenAI error 结构）
	 * <pre>{@code
	 * { "error": { "message": "...", "type": "invalid_request_error", "param": "prompt" } }
	 * }</pre>
	 * @author ThinkGem
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class AiImageError implements Serializable {

		@Serial
		private static final long serialVersionUID = 1L;

		/** 错误描述 */
		private String message;

		/** 错误类型：invalid_request_error、server_error 等 */
		private String type;

		/** 出错的请求参数名 */
		private String param;

		public AiImageError() {
		}

		public AiImageError(String message, String type, String param) {
			this.message = message;
			this.type = type;
			this.param = param;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
	}

}

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.service;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.Md5Utils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.factory.AiModelFactory;
import com.jeesite.modules.ai.utils.AiModelUtils;
import com.jeesite.modules.ai.utils.AiToolUtils;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.entity.FileUploadParams;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.*;
import org.springframework.ai.openai.metadata.OpenAiImageGenerationMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 生图服务实现，按「AI模型」中注册的模型生图
 * <p>
 * 实现本模块的 {@link ImageGenerateService} SPI，供生图工具（LocalImageAiTools、McpImageAiTools）调用：
 * 模型来源优先级为「工具管理」中该工具配置的模型（{@code options.image.modelCode}）
 * &gt; 类型为图像生成、权重最高的启用模型。
 * <p>
 * 生图参数（尺寸、质量、数量、风格、超时等）取模型配置中的 {@code options.image}，
 * 请求级参数非空时覆盖同名项；图片生成后保存到文件服务器，返回图片访问地址。
 * @author ThinkGem
 * @version 2026-11-17
 */
@Service
public class AiImageGenerateService implements ImageGenerateService {

	/** 模型类型：图像生成（与字典 ai_model_type 保持一致） */
	public static final String MODEL_TYPE_IMAGE = "image";

	/** 默认业务类型 */
	public static final String DEFAULT_BIZ_TYPE = "ai-image";

	/** 下载图片地址的超时时间 */
	protected static final Duration DOWNLOAD_TIMEOUT = Duration.ofMinutes(5);

	/** 共享的 HTTP 客户端（用于下载图片地址，默认使用系统代理设置） */
	protected static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(30))
			.build();

	protected static final Logger logger = LoggerFactory.getLogger(AiImageGenerateService.class);

	/**
	 * 是否启用生图能力（存在可用的图像生成模型）
	 */
	@Override
	public boolean isEnabled() {
		try {
			return findDefaultModel() != null;
		} catch (Exception e) {
			logger.debug("获取默认生图模型失败：{}", e.getMessage());
			return false;
		}
	}

	/**
	 * 文生图（使用工具配置或默认生图模型）
	 */
	@Override
	public Map<String, Object> generateImage(String bizKey, String bizType, String prompt) {
		return generateImage(bizKey, bizType, prompt, null);
	}

	/**
	 * 文生图（指定模型编码）
	 * @param bizKey 业务关联键（如聊天会话 ID，为空时归属到当前用户）
	 * @param bizType 业务类型（如 cms-chat，为空时默认 ai-image）
	 * @param prompt 图片描述提示词
	 * @param modelCode 模型编码（可为空，为空时按默认模型解析）
	 */
	@Override
	public Map<String, Object> generateImage(String bizKey, String bizType, String prompt, String modelCode) {
		AiImageGenerateRequest request = new AiImageGenerateRequest();
		request.setBizKey(bizKey);
		request.setBizType(bizType);
		request.setPrompt(prompt);
		request.setModelCode(modelCode);
		return generateImage(request);
	}

	/**
	 * 文生图，生成图片并保存到文件服务器，返回图片访问地址等信息
	 * @param request 生图请求（提示词、业务关联、模型、生图参数）
	 * @return 图片信息：created、prompt、modelCode、fileName、fileUrl、fileRealPath，模型改写提示词时附加 revisedPrompt
	 */
	public Map<String, Object> generateImage(AiImageGenerateRequest request) {
		if (request == null || StringUtils.isBlank(request.getPrompt())) {
			throw new ServiceException("图片描述不能为空");
		}
		AiModel aiModel = resolveModel(request);
		ImageModel imageModel = AiModelUtils.getImageModel(aiModel);
		ImageOptions imageOptions = getImageOptions(aiModel, request);
		ImageResponse response = imageOptions != null
				? imageModel.call(new ImagePrompt(request.getPrompt(), imageOptions))
				: imageModel.call(new ImagePrompt(request.getPrompt()));
		List<ImageGeneration> generations = response.getResults();
		if (generations == null || generations.isEmpty()) {
			throw new ServiceException("生图失败，模型未返回图片数据");
		}
		// 业务关联键：为空时归属到当前用户（未登录时归属到 system 用户）
		String bizKey = StringUtils.defaultIfBlank(request.getBizKey(), getCurrentUserBizKey());
		// 业务类型：为空时默认 ai-image
		String bizType = StringUtils.defaultIfBlank(request.getBizType(), DEFAULT_BIZ_TYPE);
		// 生成时间（秒），OpenAI images 响应的 created 为 Unix 时间戳
		Long created = response.getMetadata() != null && response.getMetadata().getCreated() != null
				? response.getMetadata().getCreated() / 1000 : System.currentTimeMillis() / 1000;
		for (ImageGeneration generation : generations) {
			Image image = generation.getOutput();
			if (image == null) {
				continue;
			}
			byte[] imageBytes = getImageBytes(image);
			if (imageBytes == null) {
				continue;
			}
			return saveImage(imageBytes, generation, bizKey, bizType, request.getPrompt(), aiModel, created);
		}
		throw new ServiceException("生图失败，未获取到有效的图片数据（详见后台日志）");
	}

	// ==================== 模型解析 ====================

	/**
	 * 解析生图模型：请求模型编码 &gt; 工具配置的模型 &gt; 默认模型
	 * @param request 生图请求
	 */
	protected AiModel resolveModel(AiImageGenerateRequest request) {
		String modelCode = StringUtils.trimToNull(request.getModelCode());
		if (modelCode == null) {
			modelCode = getToolModelCode(request.getToolCode());
		}
		if (modelCode != null) {
			return AiModelUtils.getModelOrThrow(modelCode);
		}
		AiModel aiModel = findDefaultModel();
		if (aiModel == null) {
			throw new ServiceException("未配置图像生成模型，请先在「AI模型」中注册模型类型为图像生成的模型");
		}
		return aiModel;
	}

	/**
	 * 获取默认生图模型：类型为图像生成、状态正常的启用模型中权重最高的一个
	 */
	public AiModel findDefaultModel() {
		List<AiModel> list = AiModelUtils.findModelList(MODEL_TYPE_IMAGE);
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (AiModel aiModel : list) {
			if (StringUtils.isNotBlank(aiModel.getBaseUrl())) {
				return aiModel;
			}
		}
		return null;
	}

	/**
	 * 获取工具配置的生图模型编码（工具管理 ai_tool 的 options.image.modelCode）
	 * @param toolCode 工具编码
	 */
	public static String getToolModelCode(String toolCode) {
		if (StringUtils.isBlank(toolCode)) {
			return null;
		}
		try {
			return AiToolUtils.getToolModelCode(AiToolUtils.getTool(toolCode));
		} catch (Exception e) {
			// 工具不存在或数据库未就绪时不阻塞生图，回退默认模型
			logger.debug("读取工具模型配置失败：{}，{}", toolCode, e.getMessage());
			return null;
		}
	}

	/**
	 * 构建生图参数：模型配置的 options.image 为基础，请求级参数非空时覆盖同名项
	 * @param aiModel 模型
	 * @param request 生图请求
	 */
	protected ImageOptions getImageOptions(AiModel aiModel, AiImageGenerateRequest request) {
		AiModelFactory factory = AiModelUtils.getAiModelFactory(aiModel);
		return factory.getImageOptions(aiModel, request.getOptions());
	}

	// ==================== 图片保存 ====================

	/**
	 * 保存图片到文件服务器并返回图片信息
	 * @param imageBytes 图片字节数据
	 * @param generation 模型返回的图片结果（含改写后的提示词等元数据）
	 * @param bizKey 业务关联键
	 * @param bizType 业务类型
	 * @param prompt 用户提示词
	 * @param aiModel 生图模型
	 * @param created 生成时间（秒）
	 */
	protected Map<String, Object> saveImage(byte[] imageBytes, ImageGeneration generation,
			String bizKey, String bizType, String prompt, AiModel aiModel, Long created) {
		// 按实际图片格式确定文件名和类型
		String ext = getImageExtension(imageBytes);
		FileUploadParams params = new FileUploadParams();
		params.setFileMd5(EncodeUtils.encodeHex(Md5Utils.md5(imageBytes)));
		params.setFileName("ai-image-" + System.currentTimeMillis() + ext);
		params.setBizKey(bizKey);
		params.setBizType(bizType);
		params.setFile(new ByteArrayMultipartFile(params.getFileName(),
				"image/" + ext.replace(".", ""), imageBytes));
		Map<String, Object> res = FileUploadUtils.saveFileUpload(params);
		if (Global.FALSE.equals(res.get("result"))) {
			throw new ServiceException((String) res.get("message"));
		}
		FileUpload fileUpload = (FileUpload) res.get("fileUpload");
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("created", created);
		result.put("prompt", prompt);
		result.put("modelCode", aiModel.getModelCode());
		result.put("fileName", params.getFileName());
		result.put("fileUrl", Global.getCtxPath() + fileUpload.getFileUrl());
		result.put("fileRealPath", fileUpload.getFileEntity().getFileRealPath());
		// 模型改写后的提示词（如 OpenAI dall-e-3、gpt-image-1）
		if (generation.getMetadata() instanceof OpenAiImageGenerationMetadata meta
				&& StringUtils.isNotBlank(meta.getRevisedPrompt())) {
			result.put("revisedPrompt", meta.getRevisedPrompt());
		}
		logger.info("Generate image: model={}, fileName={}, fileUrl={}",
				aiModel.getModelCode(), params.getFileName(), result.get("fileUrl"));
		return result;
	}

	/**
	 * 获取当前用户作为业务关联键（未登录时归属到 system 用户）
	 */
	protected static String getCurrentUserBizKey() {
		try {
			String userId = UserUtils.getUser().getId();
			if (StringUtils.isNotBlank(userId)) {
				return userId;
			}
		} catch (Exception e) {
			// 忽略，未登录场景
		}
		return "system";
	}

	/**
	 * 获取图片字节数据（优先 Base64 数据，否则下载图片地址），并校验图片格式有效性
	 */
	protected byte[] getImageBytes(Image image) {
		byte[] imageBytes = null;
		if (StringUtils.isNotBlank(image.getB64Json())) {
			// 兼容 data URI 前缀格式：data:image/png;base64,xxxx
			String b64Json = image.getB64Json();
			if (b64Json.startsWith("data:") && b64Json.contains(",")) {
				b64Json = StringUtils.substringAfter(b64Json, ",");
			}
			imageBytes = Base64.getMimeDecoder().decode(b64Json);
		} else if (StringUtils.isNotBlank(image.getUrl())) {
			try {
				HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(image.getUrl()))
						.timeout(DOWNLOAD_TIMEOUT).GET().build();
				HttpResponse<byte[]> httpResponse =
						HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
				if (httpResponse.statusCode() == 200) {
					imageBytes = httpResponse.body();
				} else {
					logger.error("Download image error: {}, url: {}", httpResponse.statusCode(), image.getUrl());
				}
			} catch (IOException | InterruptedException e) {
				logger.error("Download image error: {}, url: {}", e.getMessage(), image.getUrl());
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt();
				}
			}
		}
		// 校验图片格式：防止把错误页等非图片内容保存为图片（表现为打开空白或损坏）
		if (imageBytes == null || imageBytes.length == 0 || !isImage(imageBytes)) {
			logger.error("Invalid image data: {}", imageBytes == null ? "empty"
					: StringUtils.abbr(EncodeUtils.encodeBase64(imageBytes), 100));
			return null;
		}
		return imageBytes;
	}

	/**
	 * 校验字节数据是否为支持的图片格式（PNG、JPEG、GIF、BMP、WEBP）
	 */
	public static boolean isImage(byte[] bytes) {
		if (bytes == null || bytes.length < 12) {
			return false;
		}
		// PNG: 89 50 4E 47
		if ((bytes[0] & 0xFF) == 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) {
			return true;
		}
		// JPEG: FF D8 FF
		if ((bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8 && (bytes[2] & 0xFF) == 0xFF) {
			return true;
		}
		// GIF: 47 49 46 38
		if (bytes[0] == 0x47 && bytes[1] == 0x49 && bytes[2] == 0x46 && bytes[3] == 0x38) {
			return true;
		}
		// BMP: 42 4D
		if (bytes[0] == 0x42 && bytes[1] == 0x4D) {
			return true;
		}
		// WEBP: 52 49 46 46 ... 57 45 42 50
		if (bytes[0] == 0x52 && bytes[1] == 0x49 && bytes[2] == 0x46 && bytes[3] == 0x46
				&& bytes[8] == 0x57 && bytes[9] == 0x45 && bytes[10] == 0x42 && bytes[11] == 0x50) {
			return true;
		}
		return false;
	}

	/**
	 * 按图片魔数获取扩展名（默认 png）
	 */
	public static String getImageExtension(byte[] bytes) {
		if (bytes == null || bytes.length < 4) {
			return ".png";
		}
		if ((bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8) {
			return ".jpg";
		}
		if (bytes[0] == 0x47 && bytes[1] == 0x49) {
			return ".gif";
		}
		if (bytes[0] == 0x42 && bytes[1] == 0x4D) {
			return ".bmp";
		}
		if (bytes[0] == 0x52 && bytes[1] == 0x49 && bytes.length >= 12
				&& bytes[8] == 0x57 && bytes[9] == 0x45 && bytes[10] == 0x42 && bytes[11] == 0x50) {
			return ".webp";
		}
		return ".png";
	}

	/**
	 * 图片字节数组，包装为 MultipartFile，用于保存到文件服务器
	 */
	protected static class ByteArrayMultipartFile implements MultipartFile {

		private final String name;
		private final String contentType;
		private final byte[] bytes;

		public ByteArrayMultipartFile(String name, String contentType, byte[] bytes) {
			this.name = name;
			this.contentType = contentType;
			this.bytes = bytes;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getOriginalFilename() {
			return name;
		}

		@Override
		public String getContentType() {
			return contentType;
		}

		@Override
		public boolean isEmpty() {
			return bytes == null || bytes.length == 0;
		}

		@Override
		public long getSize() {
			return bytes.length;
		}

		@Override
		public byte[] getBytes() {
			return bytes;
		}

		@Override
		public InputStream getInputStream() {
			return new ByteArrayInputStream(bytes);
		}

		@Override
		public void transferTo(File dest) throws IOException {
			Files.write(dest.toPath(), bytes);
		}
	}

}

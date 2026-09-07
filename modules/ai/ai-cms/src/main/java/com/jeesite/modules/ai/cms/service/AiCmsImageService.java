/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.service;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.Md5Utils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.entity.FileUploadParams;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.image.*;
import org.springframework.beans.factory.ObjectProvider;
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
import java.util.List;
import java.util.Map;

/**
 * AI 生图服务类，使用 Spring AI 的 ImageModel 生图。
 * <p>生图模型的超时等客户端参数见 AiCmsChatConfig#openAiImageModel，
 * 模型名、图片尺寸等在 spring.ai.openai.image.options 中配置。
 * @author ThinkGem
 */
@Service
public class AiCmsImageService extends BaseService {

	/** 下载图片地址的超时时间 */
	private static final Duration DOWNLOAD_TIMEOUT = Duration.ofMinutes(5);

	/** 共享的 HTTP 客户端（用于下载图片地址，默认使用系统代理设置） */
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(30))
			.build();

	private final ImageModel imageModel;

	private final ChatMemory chatMemory;

	public AiCmsImageService(ObjectProvider<ImageModel> imageModel, ObjectProvider<ChatMemory> chatMemory) {
		this.imageModel = imageModel.getIfAvailable();
		this.chatMemory = chatMemory.getIfAvailable();
	}

	/**
	 * 是否启用生图模型（spring.ai.model.image: openai）
	 */
	public boolean isEnabled() {
		return imageModel != null;
	}

	/**
	 * 文生图，生成图片并保存到文件服务器，返回图片访问地址等信息
	 * @param bizKey 业务关联键（如聊天会话 ID conversationId，为空时归属到当前用户）
	 * @param bizType 业务类型（如 "cms-chat"，为空时默认 "ai-image"）
	 * @param prompt 图片描述提示词
	 * @author ThinkGem
	 */
	public Map<String, Object> generateImage(String bizKey, String bizType, String prompt) {
		if (StringUtils.isBlank(prompt)) {
			throw new ServiceException("图片描述不能为空");
		}
		if (!isEnabled()) {
			throw new ServiceException("未启用图片生成模型，请配置 spring.ai.model.image: openai");
		}
		// 调用 Spring AI 生图 API，模型参数（模型名、图片尺寸等）在 spring.ai.openai.image 配置中设置
		// 免费模型有并发限流（429），失败时等待后重试
		ImageResponse response = null;
		RuntimeException last = null;
		for (int i = 0; i < 3; i++) {
			try {
				response = imageModel.call(new ImagePrompt(prompt));
				last = null;
				break;
			} catch (RuntimeException e) {
				last = e;
				if (e.getMessage() != null && e.getMessage().contains("429") && i < 2) {
					logger.warn("生图模型限流（第 " + (i + 1) + " 次调用），10 秒后重试");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						throw new ServiceException("生图模型限流，请稍后再试");
					}
				} else {
					throw e;
				}
			}
		}
		if (last != null) {
			throw last;
		}
		ImageGeneration generation = response.getResult();
		Image image = generation != null ? generation.getOutput() : null;
		if (image == null) {
			throw new ServiceException("生图失败，模型未返回图片数据");
		}
		byte[] imageBytes = getImageBytes(image);
		if (imageBytes == null) {
			throw new ServiceException("生图失败，未获取到有效的图片数据（详见后台日志）");
		}
		// 按实际图片格式确定文件名和类型
		String ext = getImageExtension(imageBytes);
		// 业务关联键：为空时归属到当前用户（未登录时归属到 system 用户）
		if (StringUtils.isBlank(bizKey)) {
			bizKey = "system";
			try {
				String userId = UserUtils.getUser().getId();
				if (StringUtils.isNotBlank(userId)) {
					bizKey = userId;
				}
			} catch (Exception e) {
				// 忽略，未登录场景
			}
		}
		// 业务类型：为空时默认 ai-image
		if (StringUtils.isBlank(bizType)) {
			bizType = "ai-image";
		}
		FileUploadParams params = new FileUploadParams();
		params.setFileMd5(EncodeUtils.encodeHex(Md5Utils.md5(imageBytes)));
		params.setFileName("ai-image-" + System.currentTimeMillis() + ext);
		params.setBizKey(bizKey);
		params.setBizType(bizType);
		params.setFile(new ByteArrayMultipartFile(params.getFileName(), "image/" + ext.replace(".", ""), imageBytes));
		Map<String, Object> res = FileUploadUtils.saveFileUpload(params);
		if (Global.FALSE.equals(res.get("result"))) {
			throw new ServiceException((String) res.get("message"));
		}
		FileUpload fileUpload = (FileUpload) res.get("fileUpload");
		// 写入会话记忆：聊天场景（bizType=cms-chat）下，记录生图动作，
		// 让后续对话的模型知道“用户要画什么、图片已生成”，实现追问衔接
		if ("cms-chat".equals(bizType) && chatMemory != null && bizKey.contains(":")) {
			try {
				chatMemory.add(bizKey, List.of(
						new UserMessage(prompt),
						new AssistantMessage("已为您生成图片：" + prompt + "，图片已保存到会话附件（"
								+ params.getFileName() + "），用户可以在对话中继续查看或追问该图片。")));
			} catch (Exception e) {
				logger.error("Write image to chat memory error: {}", e.getMessage());
			}
		}
		return Map.of(
				"prompt", prompt,
				"fileName", params.getFileName(),
				"fileUrl", Global.getCtxPath() + fileUpload.getFileUrl(),
				"fileRealPath", fileUpload.getFileEntity().getFileRealPath()
		);
	}

	/**
	 * 获取图片字节数据（优先 Base64 数据，否则下载图片地址），并校验图片格式有效性
	 */
	public byte[] getImageBytes(Image image) {
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
				HttpRequest request = HttpRequest.newBuilder(URI.create(image.getUrl()))
						.timeout(DOWNLOAD_TIMEOUT).GET().build();
				HttpResponse<byte[]> res = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
				if (res.statusCode() == 200) {
					imageBytes = res.body();
				} else {
					logger.error("Download image error: {}, url: {}", res.statusCode(), image.getUrl());
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
	private static class ByteArrayMultipartFile implements MultipartFile {

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

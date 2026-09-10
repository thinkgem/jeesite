/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.service;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.service.BaseService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.ai.cms.entity.AiChatCompletion;
import com.jeesite.modules.ai.cms.properties.AiCmsProperties;
import com.jeesite.modules.ai.cms.utils.AiRetryUtils;
import com.jeesite.modules.ai.cms.utils.AiThinkUtils;
import com.jeesite.modules.ai.tools.context.AiToolContextProvider;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.converter.AbstractMessageOutputConverter;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天服务类
 * @author ThinkGem
 */
@Service
public class AiCmsChatService extends BaseService {

	private static final String CMS_CHAT_CACHE = "cmsChatCache";

	/** 聊天会话上传图片的业务类型，用于关联、读取多模态识图文件 */
	public static final String BIZ_TYPE_CHAT = "cms-chat";

	private static final String[] USER_MESSAGE_SEARCH = new String[]{"{", "}"};
	private static final String[] USER_MESSAGE_REPLACE = new String[]{"\\{", "\\}"};

	/** 请求属性：流输出过程中累积的助手消息（异常中断时用于回写会话） */
	private static final String ASSISTANT_MESSAGE = "assistantMessage";

	/** 请求属性：流输出过程中累积的深度思考内容 */
	private static final String REASONING_CONTENT = "reasoningContent";

	/** 请求属性：流输出最后一条分片返回的 Token 用量统计 */
	private static final String USAGE = "usage";

	private final ChatClient chatClient;
	private final ChatMemory chatMemory;
	private final CacheChatMemoryRepository chatMemoryRepository;
	private final VectorStore vectorStore;
	private final AiCmsProperties properties;
	private final AiRetryUtils aiRetryUtils;

	public AiCmsChatService(ChatClient chatClient,
							ChatMemory chatMemory,
							CacheChatMemoryRepository chatMemoryRepository,
							ObjectProvider<VectorStore> vectorStore,
							AiCmsProperties properties,
							AiRetryUtils aiRetryUtils) {
		this.chatClient = chatClient;
		this.chatMemory = chatMemory;
		this.chatMemoryRepository = chatMemoryRepository;
		this.vectorStore = vectorStore.getIfAvailable();
		this.properties = properties;
		this.aiRetryUtils = aiRetryUtils;
	}

	/**
	 * 获取聊天对话消息
	 * @author ThinkGem
	 */
	public List<Message> getChatMessage(String conversationId) {
		if (StringUtils.isBlank(conversationId)) {
			return List.of();
		}
        return chatMemory.get(conversationId);
	}

	private static String getChatCacheKey() {
		String key = UserUtils.getUser().getId();
		if (StringUtils.isBlank(key)) {
			key = UserUtils.getSession().getId().toString();
		}
		return key;
	}

	public Map<String, Map<String, Object>> getChatCacheMap() {
		return CacheUtils.computeIfAbsent(CMS_CHAT_CACHE, getChatCacheKey(), k -> MapUtils.newHashMap());
	}

	/**
	 * 新建或更新聊天对话
	 * @author ThinkGem
	 */
	public Map<String, Object> saveChatConversation(String conversationId, String title) {
		if (StringUtils.isBlank(conversationId)) {
			conversationId = CacheChatMemoryRepository.genUserConversationId();
		}
		if (CacheChatMemoryRepository.checkPermiByConversationId(conversationId)){
			throw new ServiceException("越权操作！");
		}
		if (StringUtils.isBlank(title)) {
			title = "新对话 " + DateUtils.getTime();
		}
		Map<String, Object> map = MapUtils.newHashMap();
		map.put("id", conversationId);
		map.put("title", title);
		Map<String, Map<String, Object>> cache = getChatCacheMap();
		cache.put(conversationId, map);
		CacheUtils.put(CMS_CHAT_CACHE, getChatCacheKey(), cache);
		return map;
	}

	/**
	 * 删除聊天对话
	 * @author ThinkGem
	 */
	public void deleteChatConversation(String conversationId) {
		if (CacheChatMemoryRepository.checkPermiByConversationId(conversationId)){
			throw new ServiceException("越权操作！");
		}
		Map<String, Map<String, Object>> cache = getChatCacheMap();
		cache.remove(conversationId);
		CacheUtils.put(CMS_CHAT_CACHE, getChatCacheKey(), cache);
		chatMemory.clear(conversationId);
	}

	/**
	 * 聊天对话，流输出
	 * @author ThinkGem
	 */
	public Flux<ChatResponse> chatStream(String conversationId, String message, HttpServletRequest request) {
		if (CacheChatMemoryRepository.checkPermiByConversationId(conversationId)){
			throw new ServiceException("越权操作！");
		}
		String text = StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE);
		List<Media> media = ListUtils.newArrayList();
		// 识图：将对话上传的图片文件转换为多模态消息（注意：chat.model 需设置为多模态模型，如 qwen-vl-plus、gpt-4o）
		List<FileUpload> fileUploadList = FileUploadUtils.findFileUpload(conversationId, BIZ_TYPE_CHAT);
		// 只携带最近的图片（spring.ai.media-limit，默认 3 张，0 表示不限制），避免会话图片过多导致 token 消耗过大
		Integer mediaLimit = properties.getMediaLimit();
		if (mediaLimit == null || mediaLimit < 0) {
			mediaLimit = 3;
		}
		fileUploadList.sort(Comparator.comparing(FileUpload::getId, Comparator.nullsLast(Comparator.reverseOrder())));
		if (mediaLimit > 0 && fileUploadList.size() > mediaLimit) {
			fileUploadList = fileUploadList.subList(0, mediaLimit);
		}
		for (FileUpload fileUpload : fileUploadList) {
			File file = new File(fileUpload.getFileEntity().getFileRealPath());
			MediaType mediaType = MediaType.parseMediaType(FileUtils.getContentType(file.getName()));
			media.add(Media.builder().mimeType(mediaType).data(new FileSystemResource(file)).build());
		}
		UserMessage userMessage = UserMessage.builder().text(text).media(media).build();
		ChatClient.ChatClientRequestSpec spec = chatClient.prompt().messages(userMessage)
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
				.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
				// 开启 OpenAI 流式用量统计（stream_options.include_usage），确保尾块携带 usage；
				// 非 OpenAI 系 provider 会忽略该专有字段，不影响其他模型调用
				.options(org.springframework.ai.openai.OpenAiChatOptions.builder().streamUsage(true));
		if (vectorStore != null) {
			spec.advisors(QuestionAnswerAdvisor.builder(vectorStore)
					.searchRequest(SearchRequest.builder().similarityThreshold(0.6F).topK(6).build())
					.promptTemplate(new PromptTemplate(properties.getDefaultPromptTemplate()))
					.build());
		}
		// 深度思考：将各模型原生的思考字段（reasoning_content / thinking 等）累积后，
		// 统一归一化为 AssistantMessage.metadata.reasoningContent，随流输出下发给前端。
		// 累积器放 defer 内，保证每次订阅（含重试）都从空开始
		Flux<ChatResponse> chatResponseFlux = spec.stream().chatResponse();
		Flux<ChatResponse> thinkFlux = Flux.defer(() -> {
			StringBuilder reasoning = new StringBuilder();
			return chatResponseFlux.map(response -> AiThinkUtils.normalize(response, reasoning));
		});
		return aiRetryUtils.retry(thinkFlux)
			.doOnNext(response -> {
				// 用量统计：模型随最后一条分片返回，该分片可能没有正文输出，需在正文判断前捕获
				if (response.getMetadata() != null && response.getMetadata().getUsage() != null) {
					AiChatCompletion.Usage usage = AiChatCompletion.Usage.of(response.getMetadata().getUsage());
					if (usage != null) {
						request.setAttribute(USAGE, usage);
					}
				}
				Generation generation = response.getResult();
				if (generation == null || generation.getOutput() == null) {
					return;
				}
				AssistantMessage currMessage = generation.getOutput();
				// 上游 normalize 已按分片累积，这里直接取到的是完整的思考内容
				String reasoning = AiThinkUtils.mergeReasoningContent(
						(String) request.getAttribute(REASONING_CONTENT), AiThinkUtils.getReasoningContent(currMessage));
				request.setAttribute(REASONING_CONTENT, reasoning);
				String currText = StringUtils.defaultString(currMessage.getText());
				if (StringUtils.isBlank(currText) && StringUtils.isBlank(reasoning)) {
					return;
				}
				// 累积的消息用于异常中断时回写会话，metadata 中需带上完整的思考内容
				Map<String, Object> metadata = MapUtils.newHashMap(currMessage.getMetadata());
				metadata.put(AiThinkUtils.REASONING_CONTENT, reasoning);
				AssistantMessage assistantMessage = (AssistantMessage) request.getAttribute(ASSISTANT_MESSAGE);
				String content = assistantMessage == null ? currText
						: StringUtils.defaultString(assistantMessage.getText()) + currText;
				request.setAttribute(ASSISTANT_MESSAGE, AssistantMessage.builder()
						.content(content).properties(metadata).build());
			})
			.doFinally((signalType) -> {
				if (signalType != SignalType.ON_COMPLETE) {
					AssistantMessage assistantMessage = (AssistantMessage)request.getAttribute(ASSISTANT_MESSAGE);
					if (assistantMessage != null) {
						chatMemory.add(conversationId, assistantMessage);
					} else if (signalType == SignalType.CANCEL) {
						chatMemory.add(conversationId, new AssistantMessage(text("暂无消息，你已主动停止响应。")));
					}
				} else {
					// 正常结束时由 MessageChatMemoryAdvisor 写入会话，但其 metadata 为 last-wins 合并，
					// Ollama 等增量下发的思考字段会丢失；这里回填完整思考内容与用量统计，保证历史消息可回显
					chatMemoryRepository.fillLastAssistantMetadata(conversationId,
							(String) request.getAttribute(REASONING_CONTENT),
							(AiChatCompletion.Usage) request.getAttribute(USAGE));
				}
			})
			.onErrorResume(error -> {
				String errorMessage = error.getMessage();
				if (Global.getPropertyToBoolean("error.page.printErrorInfo", "true")){
					if (error instanceof WebClientResponseException webClientError) {
						errorMessage = webClientError.getResponseBodyAsString();
					} else if (error.getCause() instanceof WebClientResponseException webClientError) {
						errorMessage = webClientError.getResponseBodyAsString();
					}
				}
				AssistantMessage assistantMessage = new AssistantMessage(errorMessage);
				chatMemory.add(conversationId, assistantMessage);
				logger.error("Error message: {}", errorMessage);
				return Flux.just(ChatResponse.builder()
						.generations(List.of(new Generation(assistantMessage)))
						.build());
			})
			.contextWrite(AiToolContextProvider.contextWrite());
	}

	/**
	 * 聊天对话，文本输出
	 * @author ThinkGem
	 */
	public String chatText(String message) {
		return aiRetryUtils.execute(() -> chatClient.prompt()
			.messages(
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			)
			.call()
			.content());
    }

	private static final String SYSTEM_MESSAGE_TO_JSON = """
			；你是一个严格的数据提取和格式化工具。你的唯一任务是输出合法的、标准的 JSON 对象。
			必须遵守以下规则：
			1. 仅输出纯 JSON 字符串。
			2. 严禁使用 Markdown 代码块标记（即绝对不要包含 ```json 或 ```）。
			3. 严禁输出任何解释性文字、前言、后语或“好的，这是结果”之类的废话。
			4. 严禁输出换行符，除非是在 JSON 字符串值的内部。
			""";

	/**
	 * 聊天对话，结构化输出（Map）
	 * @author ThinkGem
	 */
	public Map<String, Object> chatJson(String message) {
		return aiRetryUtils.execute(() -> chatClient.prompt()
			.messages(
				// 注意：示例必须是 JSON 对象（不能是数组），否则严格的模型会跟随示例返回数组，
				new SystemMessage("{name:'张三', sex:'男', age:'17'}" + SYSTEM_MESSAGE_TO_JSON),
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			)
			.call()
			.responseEntity(
				new AbstractMessageOutputConverter<Map<String, Object>>(
					new JacksonJsonMessageConverter(JsonMapper.getInstance())
				) {
					final MapOutputConverter mapOutputConverter = new MapOutputConverter();
					@Override
					public Map<String, Object> convert(String source) {
						return mapOutputConverter.convert(source);
					}
					@Override
					public String getFormat() {
						return mapOutputConverter.getFormat();
					}
				}
			)
			.getEntity());
	}

	/**
	 * 聊天对话，结构化输出（Area）
	 * @author ThinkGem
	 */
	public List<Area> chatArea(String message) {
		Area where = new Area();
		where.setPage(new Page<>(1, 5, Page.COUNT_NOT_COUNT));
		List<Area> list = SpringUtils.getBean(AreaService.class).findList(where);
		ChatClient.ChatClientRequestSpec spec = chatClient.prompt()
			.messages(
				new SystemMessage(JsonMapper.toJson(list) + SYSTEM_MESSAGE_TO_JSON),
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			);
		if (vectorStore != null) {
			spec.advisors(QuestionAnswerAdvisor.builder(vectorStore)
					.searchRequest(SearchRequest.builder().similarityThreshold(0.6F).topK(6).build())
					.promptTemplate(new PromptTemplate(properties.getDefaultPromptTemplate()))
					.build());
		}
		return aiRetryUtils.execute(() -> spec.call()
			.responseEntity(
					new BeanOutputConverter<>(
							new ParameterizedTypeReference<List<Area>>() {},
							JsonMapper.getInstance()
					))
			.getEntity());
	}

//	public static void main(String[] args) {
//		String s = """
//				[{"id":"110000","isNewRecord":false,"createBy":"system","createDate":"2025-01-01T19:25:11Z","updateBy":"system","updateDate":"2025-01-01 19:25","childList":[{"id":"110100","isNewRecord":false,"createBy":"system","createDate":"2025-01-01 19:25","updateBy":"system","updateDate":"2025-01-01 19:25","childList":[{"id":"110101","isNewRecord":false,"areaCode":"110101","areaName":"东城区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110102","isNewRecord":false,"areaCode":"110102","areaName":"西城区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110105","isNewRecord":false,"areaCode":"110105","areaName":"朝阳区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110106","isNewRecord":false,"areaCode":"110106","areaName":"丰台区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110107","isNewRecord":false,"areaCode":"110107","areaName":"石景山区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110108","isNewRecord":false,"areaCode":"110108","areaName":"海淀区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110109","isNewRecord":false,"areaCode":"110109","areaName":"门头沟区","areaType":"3","isRoot":true,"isTreeLeaf":false},{"id":"110111","isNewRecord":false,"areaCode":"110111","areaName":"房山区","areaType":"3","isRoot":true,"isTreeLeaf":false}],"areaCode":"110100","areaName":"北京城区","areaType":"2","isRoot":true,"isTreeLeaf":false}],"areaCode":"110000","areaName":"北京市","areaType":"1","isRoot":true,"isTreeLeaf":false}]
//				""";
//		JsonMapper jsonMapper = JsonMapper.getInstance();
//		ParameterizedTypeReference<List<Area>> p = new ParameterizedTypeReference<List<Area>>() {};
//		List<Area> entity = jsonMapper.fromJsonString(s, jsonMapper.constructType(p.getType()));
//		System.out.println(entity);
//		String json = jsonMapper.toJsonString(entity);
//		System.out.println(json);
//	}
}

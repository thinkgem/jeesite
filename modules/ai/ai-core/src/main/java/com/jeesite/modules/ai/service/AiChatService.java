/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.ai.dao.AiChatDao;
import com.jeesite.modules.ai.entity.AiAgent;
import com.jeesite.modules.ai.entity.AiChat;
import com.jeesite.modules.ai.entity.AiChatMessage;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.utils.AiModelUtils;
import com.jeesite.modules.ai.utils.AiThinkUtils;
import com.jeesite.modules.ai.utils.AiToolContextUtils;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AI聊天对话 Service
 * <p>
 * 负责对话会话、对话消息上下文、多模态图片，以及同步/流式对话、重新生成回复。
 * 由 {@code AiCoreAutoConfiguration} 注入，业务可通过定义同类型 Bean 覆盖（二开：继承本类并注册 Bean）。
 * @author ThinkGem
 * @version 2026-11-12
 */
public class AiChatService extends CrudService<AiChatDao, AiChat> {

	/** 聊天会话上传图片的业务类型，用于关联、读取多模态识图文件 */
	public static final String BIZ_TYPE_CHAT = "ai-agent-chat";
	/** 对话类型：文本 */
	public static final String CHAT_TYPE_TEXT = "text";
	/** 参与上下文的历史消息最大条数 */
	public static final int MAX_HISTORY_SIZE = 20;
	/** 单次对话携带的最大图片数 */
	public static final int MAX_MEDIA_SIZE = 3;

	protected final AiAgentService aiAgentService;
	protected final AiChatMessageService aiChatMessageService;

	/**
	 * 构造注入
	 * <p>
	 * 模型通过 {@link AiModelUtils} 静态调用，不再注入 AI模型 Service，
	 * 避免与 Utils 中的懒加载 Bean 重复依赖。
	 * @param aiAgentService AI智能体 Service
	 * @param aiChatMessageService AI聊天消息 Service
	 */
	public AiChatService(AiAgentService aiAgentService, AiChatMessageService aiChatMessageService) {
		this.aiAgentService = aiAgentService;
		this.aiChatMessageService = aiChatMessageService;
	}

	// ==================== 对话会话 ====================

	/**
	 * 新建或更新对话会话（会话表）
	 * @param aiChat 会话对象（id 为空则新建；chatTitle 为空则自动生成）
	 */
	@Transactional
	public AiChat saveChatConversation(AiChat aiChat) {
		if (aiChat == null) {
			return null;
		}
		// 会话编号已存在时，必须是当前用户的会话，避免越权修改他人会话
		AiChat chat = StringUtils.isBlank(aiChat.getId())
				? null : checkChatConversation(aiChat.getId());
		boolean isNewRecord = (chat == null);
		if (isNewRecord) {
			chat = new AiChat();
			chat.setId(aiChat.getId());
			chat.setAgentId(aiChat.getAgentId());
			chat.setChatType(StringUtils.defaultIfBlank(aiChat.getChatType(), CHAT_TYPE_TEXT));
			chat.setWeight(aiChat.getWeight() != null ? aiChat.getWeight() : 0);
		} else {
			if (StringUtils.isNotBlank(aiChat.getAgentId())) {
				chat.setAgentId(aiChat.getAgentId());
			}
			// 权重用于会话置顶（999 置顶），未传递时保持原值
			if (aiChat.getWeight() != null) {
				chat.setWeight(aiChat.getWeight());
			}
		}
		if (StringUtils.isNotBlank(aiChat.getChatTitle())) {
			chat.setChatTitle(aiChat.getChatTitle());
		} else if (isNewRecord) {
			chat.setChatTitle("新对话 " + DateUtils.getDateTime());
		}
		save(chat);
		return chat;
	}

	/**
	 * 获取对话会话（仅限本人创建的会话）
	 * @param aiChat 会话（id 为会话编号）
	 */
	public AiChat getChatConversation(AiChat aiChat) {
		if (aiChat == null || StringUtils.isBlank(aiChat.getId())) {
			return null;
		}
		AiChat chat = get(aiChat.getId(), false);
		if (chat == null) {
			return null;
		}
		if (StringUtils.isNotBlank(chat.getCreateBy())
				&& !chat.getCreateBy().equals(getUserId())) {
			return null;
		}
		return chat;
	}

	/**
	 * 获取对话会话（仅限本人创建的会话）
	 * @param conversationId 会话编号
	 */
	public AiChat getChatConversation(String conversationId) {
		return StringUtils.isBlank(conversationId)
				? null : getChatConversation(new AiChat(conversationId));
	}

	/**
	 * 校验会话归属：会话必须存在且为当前登录用户创建，否则抛出异常
	 * <p>
	 * 按会话编号操作的方法（读写消息、关联图片、对话、重新生成）都应先经过本方法校验，
	 * 避免通过会话编号越权读取或修改他人的对话。
	 * @param conversationId 会话编号
	 */
	public AiChat checkChatConversation(String conversationId) {
		AiChat chat = getChatConversation(conversationId);
		if (chat == null) {
			throw new ServiceException(text("对话不存在或无权访问：") + conversationId);
		}
		return chat;
	}

	/**
	 * 查询当前用户的对话会话列表
	 */
	public List<AiChat> findChatConversationList() {
		return findChatConversationList(new AiChat());
	}

	/**
	 * 查询当前用户的对话会话列表
	 * @param aiChat 查询条件（agentId 为空则查询全部）
	 */
	public List<AiChat> findChatConversationList(AiChat aiChat) {
		String userId = getUserId();
		if (StringUtils.isBlank(userId)) {
			return ListUtils.newArrayList();
		}
		AiChat where = new AiChat();
		if (aiChat != null && StringUtils.isNotBlank(aiChat.getAgentId())) {
			where.setAgentId(aiChat.getAgentId());
		}
		// 只查询本人创建的会话
		where.sqlMap().getWhere().and("create_by", QueryType.EQ, userId);
		return findList(where);
	}

	/**
	 * 查询当前用户的对话会话列表
	 * @param agentId 智能体编号，为空则查询全部
	 */
	public List<AiChat> findChatConversationList(String agentId) {
		AiChat where = new AiChat();
		where.setAgentId(agentId);
		return findChatConversationList(where);
	}

	/**
	 * 删除对话会话（含消息）
	 * @param aiChat 会话（id 为会话编号）
	 */
	@Transactional
	public void deleteChatConversation(AiChat aiChat) {
		AiChat chat = getChatConversation(aiChat);
		if (chat == null) {
			return;
		}
		AiChatMessage where = new AiChatMessage();
		where.setConvId(chat.getId());
		aiChatMessageService.clearChatMessages(where);
		delete(chat);
	}

	/**
	 * 删除对话会话（含消息）
	 * @param conversationId 会话编号
	 */
	@Transactional
	public void deleteChatConversation(String conversationId) {
		deleteChatConversation(new AiChat(conversationId));
	}

	// ==================== 对话上下文 ====================

	/**
	 * 获取对话消息（Spring AI 消息，最多取最近 {@link #MAX_HISTORY_SIZE} 条）
	 * @param aiChat 会话（id 为会话编号）
	 */
	public List<Message> getChatMessages(AiChat aiChat) {
		if (aiChat == null || StringUtils.isBlank(aiChat.getId())) {
			return ListUtils.newArrayList();
		}
		// 校验会话归属，避免通过会话编号读取他人对话上下文
		checkChatConversation(aiChat.getId());
		AiChatMessage where = new AiChatMessage();
		where.setConvId(aiChat.getId());
		return toChatMessages(aiChatMessageService.findChatMessageList(where));
	}

	/**
	 * 查询对话消息列表（按创建时间正序，仅限本人会话）
	 * @param conversationId 会话编号
	 */
	public List<AiChatMessage> findChatMessageList(String conversationId) {
		if (StringUtils.isBlank(conversationId)) {
			return ListUtils.newArrayList();
		}
		// 校验会话归属，避免通过会话编号读取他人对话消息
		checkChatConversation(conversationId);
		AiChatMessage where = new AiChatMessage();
		where.setConvId(conversationId);
		return aiChatMessageService.findChatMessageList(where);
	}

	/**
	 * 获取消息的 Token 用量（元数据 JSON）
	 * @param aiChatMessage 消息
	 */
	public Map<String, Object> getChatMessageUsage(AiChatMessage aiChatMessage) {
		return aiChatMessageService.getChatMessageUsage(aiChatMessage);
	}

	/**
	 * 获取消息单价快照中的币种（调用当时的价格配置）
	 * @param aiChatMessage 消息
	 */
	public String getChatMessageCurrency(AiChatMessage aiChatMessage) {
		return aiChatMessageService.getChatMessageCurrency(aiChatMessage);
	}

	/**
	 * 获取消息的深度思考内容（兼容早期存于元数据的数据）
	 * @param aiChatMessage 消息
	 */
	public String getChatMessageReasoningContent(AiChatMessage aiChatMessage) {
		return aiChatMessageService.getChatMessageReasoningContent(aiChatMessage);
	}

	/**
	 * 对话消息转换为 Spring AI 消息（最多取最近 {@link #MAX_HISTORY_SIZE} 条）
	 * @param list 对话消息列表（按创建时间正序）
	 */
	protected List<Message> toChatMessages(List<AiChatMessage> list) {
		List<Message> messages = ListUtils.newArrayList();
		int fromIndex = Math.max(0, list.size() - MAX_HISTORY_SIZE);
		for (AiChatMessage chatMessage : list.subList(fromIndex, list.size())) {
			String content = chatMessage.getMsgText();
			if (StringUtils.isBlank(content)) {
				continue;
			}
			String role = chatMessage.getMsgType();
			if (AiChatMessageService.ROLE_SYSTEM.equals(role)) {
				messages.add(new SystemMessage(content));
			} else if (AiChatMessageService.ROLE_ASSISTANT.equals(role)) {
				messages.add(new AssistantMessage(content));
			} else {
				messages.add(new UserMessage(content));
			}
		}
		return messages;
	}

	// ==================== 对话 ====================

	/**
	 * 文本对话
	 * @param aiModel 模型
	 * @param aiChatMessage 用户消息（convId 为空则不保存对话记录）
	 */
	public String chatText(AiModel aiModel, AiChatMessage aiChatMessage) {
		return chatText(AiModelUtils.getChatClient(aiModel), aiModel, aiChatMessage);
	}

	/**
	 * 文本对话
	 * @param aiModel 模型
	 * @param systemPrompt 系统提示词
	 * @param aiChatMessage 用户消息（convId 为空则不保存对话记录）
	 */
	public String chatText(AiModel aiModel, String systemPrompt, AiChatMessage aiChatMessage) {
		return chatText(AiModelUtils.getChatClient(aiModel, systemPrompt), aiModel, aiChatMessage);
	}

	/**
	 * 文本对话（按智能体配置，convId 为空则不保存对话记录）
	 * @param aiAgent 智能体
	 * @param aiChatMessage 用户消息（convId 为空则不保存对话记录）
	 */
	public String chatText(AiAgent aiAgent, AiChatMessage aiChatMessage) {
		AiModel aiModel = aiAgentService.getAgentModel(aiAgent);
		// 按智能体构建 ChatClient（含工具调用与知识库检索）
		return chatText(aiAgentService.getChatClient(aiAgent), aiModel, aiChatMessage);
	}

	/**
	 * 文本对话
	 * @param chatClient ChatClient
	 * @param aiChatMessage 用户消息（convId 为空则不保存对话记录）
	 */
	public String chatText(ChatClient chatClient, AiChatMessage aiChatMessage) {
		return chatText(chatClient, null, aiChatMessage);
	}

	/**
	 * 文本对话（记录本次调用使用的模型与金额，用于历史消息的成本核算）
	 * @param chatClient ChatClient
	 * @param aiModel 模型，可为空
	 * @param aiChatMessage 用户消息（convId 为空则不保存对话记录）
	 */
	protected String chatText(ChatClient chatClient, AiModel aiModel, AiChatMessage aiChatMessage) {
		String conversationId = aiChatMessage.getConvId();
		List<Message> messages = getChatMessages(new AiChat(conversationId));
		messages.add(new UserMessage(aiChatMessage.getMsgText()));
		ChatResponse response = chatClient.prompt().messages(messages).call().chatResponse();
		String answer = getText(response);
		if (StringUtils.isNotBlank(conversationId)) {
			// 保存用户消息，保存后回填编号，作为助手回复的父消息编号
			aiChatMessage.setMsgType(AiChatMessageService.ROLE_USER);
			aiChatMessageService.saveChatMessage(aiChatMessage);
			if (StringUtils.isNotBlank(answer)) {
				AiChatMessage assistantMessage = new AiChatMessage();
				assistantMessage.setConvId(conversationId);
				assistantMessage.setParentId(aiChatMessage.getId());
				assistantMessage.setMsgType(AiChatMessageService.ROLE_ASSISTANT);
				assistantMessage.setModelCode(getModelCode(aiModel));
				assistantMessage.setMsgText(answer);
				aiChatMessageService.setChatUsage(assistantMessage, aiModel,
						aiChatMessageService.getChatUsage(response), getReasoningContent(response));
				aiChatMessageService.saveChatMessage(assistantMessage);
			}
		}
		return answer;
	}

	/**
	 * 流式对话（按智能体配置，保存对话记录）
	 * <p>
	 * 注意：回答内容在流结束时（含取消、异常）保存，调用方无需重复保存。
	 * @param aiAgent 智能体
	 * @param aiChatMessage 用户消息（convId 为空则不保存对话记录）
	 */
	public Flux<ChatResponse> chatStream(AiAgent aiAgent, AiChatMessage aiChatMessage) {
		AiChat aiChat = new AiChat(aiChatMessage.getConvId());
		List<Message> messages = getChatMessages(aiChat);
		// 本轮提交的图片：先取出，用于多模态识图，并记录到消息上供历史回显（前端提交后会清空会话上的图片关联）
		List<FileUpload> fileUploadList = findChatFileUploadList(aiChat);
		// 识图：将对话上传的图片转换为多模态消息（注意：智能体使用的模型需支持多模态，如 qwen-vl-plus、gpt-4o）
		messages.add(UserMessage.builder().text(aiChatMessage.getMsgText()).media(toChatMedia(fileUploadList)).build());
		// 用户消息在流结束时保存（含取消、异常），保存后回填编号，作为助手回复的父消息编号
		aiChatMessage.setMsgType(AiChatMessageService.ROLE_USER);
		aiChatMessageService.setChatMessageFiles(aiChatMessage, fileUploadList);
		return streamChat(aiAgent, messages, aiChatMessage, null);
	}

	/**
	 * 重新生成回复（流式）：删除该回复及其之后的对话，基于其父用户消息及之前的历史重新生成
	 * <p>
	 * 注意：回答内容在流结束时（含取消、异常）保存，调用方无需重复保存。
	 * @param aiAgent 智能体
	 * @param aiChatMessage 要重新生成的助手消息（convId 为会话编号，id 为空则取会话最后一条助手消息）
	 */
	public Flux<ChatResponse> regenerateChatStream(AiAgent aiAgent, AiChatMessage aiChatMessage) {
		String conversationId = aiChatMessage.getConvId();
		// 校验会话归属，避免通过会话编号删除他人消息并重新生成
		checkChatConversation(conversationId);
		String messageId = aiChatMessage.getId();
		AiChatMessage where = new AiChatMessage();
		where.setConvId(conversationId);
		List<AiChatMessage> list = aiChatMessageService.findChatMessageList(where);
		int index = -1;
		if (StringUtils.isNotBlank(messageId)) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (messageId.equals(list.get(i).getId())) {
					index = i;
					break;
				}
			}
		} else {
			// 未指定消息时，默认重新生成最后一条助手回复
			for (int i = list.size() - 1; i >= 0; i--) {
				if (AiChatMessageService.ROLE_ASSISTANT.equals(list.get(i).getMsgType())) {
					index = i;
					break;
				}
			}
		}
		if (index < 0) {
			throw new ServiceException("未找到要重新生成的回复消息");
		}
		AiChatMessage assistantMessage = list.get(index);
		if (!AiChatMessageService.ROLE_ASSISTANT.equals(assistantMessage.getMsgType())) {
			throw new ServiceException("只能重新生成助手的回复消息");
		}
		// 定位父用户消息：优先使用 parentId，历史数据（无 parentId）回退为往前最近的一条用户消息
		int userIndex = -1;
		if (StringUtils.isNotBlank(assistantMessage.getParentId())) {
			for (int i = index - 1; i >= 0; i--) {
				if (assistantMessage.getParentId().equals(list.get(i).getId())) {
					userIndex = i;
					break;
				}
			}
		}
		if (userIndex < 0) {
			for (int i = index - 1; i >= 0; i--) {
				if (AiChatMessageService.ROLE_USER.equals(list.get(i).getMsgType())) {
					userIndex = i;
					break;
				}
			}
		}
		if (userIndex < 0) {
			throw new ServiceException("未找到该回复对应的用户消息");
		}
		AiChatMessage userMessage = list.get(userIndex);
		// 被替换的旧回复及其之后的对话：新回复保存成功后再删除，避免取消生成时丢失历史
		List<AiChatMessage> discardMessages = ListUtils.newArrayList(list.subList(index, list.size()));
		// 上下文取该用户消息及之前的对话；图片（多模态识图）随本轮重新生成一并携带
		List<Message> messages = toChatMessages(list.subList(0, userIndex + 1));
		List<Media> media = getChatMedia(new AiChat(conversationId));
		if (!media.isEmpty() && !messages.isEmpty()
				&& messages.get(messages.size() - 1) instanceof UserMessage) {
			messages.set(messages.size() - 1, UserMessage.builder()
					.text(StringUtils.defaultString(userMessage.getMsgText())).media(media).build());
		}
		// 用户消息已存在（有编号），保存助手回复时直接关联该编号
		return streamChat(aiAgent, messages, userMessage, discardMessages);
	}

	/**
	 * 流式对话（按会话编号，保存对话记录）
	 * @param aiAgent 智能体
	 * @param conversationId 会话编号
	 * @param message 用户消息
	 */
	public Flux<ChatResponse> chatStream(AiAgent aiAgent, String conversationId, String message) {
		AiChatMessage aiChatMessage = new AiChatMessage();
		aiChatMessage.setConvId(conversationId);
		aiChatMessage.setMsgText(message);
		return chatStream(aiAgent, aiChatMessage);
	}

	/**
	 * 重新生成回复（流式，按会话编号）
	 * @param aiAgent 智能体
	 * @param conversationId 会话编号
	 * @param messageId 要重新生成的助手消息编号，为空则取会话最后一条助手消息
	 */
	public Flux<ChatResponse> regenerateChatStream(AiAgent aiAgent, String conversationId, String messageId) {
		AiChatMessage aiChatMessage = new AiChatMessage(messageId);
		aiChatMessage.setConvId(conversationId);
		return regenerateChatStream(aiAgent, aiChatMessage);
	}

	/**
	 * 流式对话（内部）：对话结束后保存助手回复，并关联其父用户消息
	 * @param aiAgent 智能体
	 * @param messages 发送给模型的消息（含本轮用户消息）
	 * @param userMessage 本轮用户消息（编号为空则保存；为空则不保存用户消息）
	 * @param discardMessages 新回复保存成功后需要删除的旧消息（重新生成时传入）
	 */
	protected Flux<ChatResponse> streamChat(AiAgent aiAgent, List<Message> messages,
			AiChatMessage userMessage, List<AiChatMessage> discardMessages) {
		AiModel aiModel = aiAgentService.getAgentModel(aiAgent);
		// 按智能体构建 ChatClient（含工具调用与知识库检索）
		ChatClient chatClient = aiAgentService.getChatClient(aiAgent);
		// 记录本次调用实际使用的模型（智能体后续切换模型不影响历史消息的成本核算）
		String modelCode = getModelCode(aiModel);
		String conversationId = userMessage != null ? userMessage.getConvId() : null;
		StringBuilder answer = new StringBuilder();
		// Token 用量可能在首个或最后一个分片，取最后一次有效值
		AtomicReference<Map<String, Object>> usageRef = new AtomicReference<>();
		// 深度思考内容（跨分片累积），流结束时保存到消息的思考内容字段，供历史消息回显
		AtomicReference<String> reasoningRef = new AtomicReference<>(StringUtils.EMPTY);
		// 累积器放 defer 内，保证每次订阅（含重新订阅）都从空开始
		Flux<ChatResponse> flux = Flux.defer(() -> {
			StringBuilder reasoning = new StringBuilder();
			return chatClient.prompt().messages(messages).stream().chatResponse()
				// 深度思考：将各模型原生的思考字段统一归一化为本分片增量，随流下发给前端
				.map(response -> AiThinkUtils.normalize(response, reasoning))
				.doOnNext(response -> reasoningRef.set(reasoning.toString()));
		});
		return flux
			// 配置类异常转为可读提示（如工具名称重复），避免前端只显示通用错误
			.onErrorMap(e -> {
				String message = e.getMessage();
				if (message != null && message.contains("Multiple tools with the same name")) {
					return new ServiceException("工具名称重复，请在智能体中去掉重复选择的工具或 MCP 服务", e);
				}
				return e;
			})
			.doOnNext(response -> {
				String text = getText(response);
				if (StringUtils.isNotBlank(text)) {
					answer.append(text);
				}
				Map<String, Object> usage = aiChatMessageService.getChatUsage(response);
				if (usage != null) {
					usageRef.set(usage);
				}
			})
			.doFinally(signalType -> {
				if (StringUtils.isNotBlank(conversationId)) {
					try {
						// 保存用户消息（重新生成时用户消息已存在），保存后回填编号，作为助手回复的父消息编号
						if (userMessage != null && StringUtils.isBlank(userMessage.getId())) {
							aiChatMessageService.saveChatMessage(userMessage);
						}
						if (!answer.isEmpty()) {
							AiChatMessage assistantMessage = new AiChatMessage();
							assistantMessage.setConvId(conversationId);
							assistantMessage.setParentId(userMessage != null ? userMessage.getId() : null);
							assistantMessage.setMsgType(AiChatMessageService.ROLE_ASSISTANT);
							assistantMessage.setModelCode(modelCode);
							assistantMessage.setMsgText(answer.toString());
							aiChatMessageService.setChatUsage(assistantMessage, aiModel,
									usageRef.get(), reasoningRef.get());
							aiChatMessageService.saveChatMessage(assistantMessage);
							// 重新生成成功：删除被替换的旧回复及其之后的对话
							if (discardMessages != null) {
								for (AiChatMessage discardMessage : discardMessages) {
									aiChatMessageService.delete(discardMessage);
								}
							}
						}
						// 一轮对话结束后刷新会话更新时间（不随每条消息更新，避免频繁写库）
						touchChatUpdateDate(conversationId);
					} catch (Exception e) {
						logger.error("保存对话消息失败：{}", e.getMessage(), e);
					}
				}
			})
			// 工具调用的会话同步：流式输出在异步线程执行，工具方法无法取到当前登录用户，
			// 这里（请求线程）把当前会话用户写入 Reactor 上下文，供 AiToolSubjectAspect 绑定
			.contextWrite(AiToolContextUtils.contextWrite());
	}

	/**
	 * 刷新会话更新时间（一轮对话结束后调用一次）
	 * <p>
	 * 会话列表按更新时间排序，可体现最近对话。
	 * @param conversationId 会话编号
	 */
	protected void touchChatUpdateDate(String conversationId) {
		try {
			AiChat aiChat = get(conversationId);
			// 全量更新，避免部分字段（如 isUpdateForce 列）被置空
			if (aiChat != null && !aiChat.getIsNewRecord()) {
				update(aiChat);
			}
		} catch (Exception e) {
			logger.warn("更新会话更新时间失败：{}，{}", conversationId, e.getMessage());
		}
	}

	/**
	 * 提取响应文本
	 * @param response 响应
	 */
	protected String getText(ChatResponse response) {
		if (response != null && response.getResult() != null
				&& response.getResult().getOutput() != null) {
			return response.getResult().getOutput().getText();
		}
		return null;
	}

	/**
	 * 提取响应的深度思考内容
	 * @param response 响应
	 */
	public String getReasoningContent(ChatResponse response) {
		if (response == null || response.getResult() == null) {
			return StringUtils.EMPTY;
		}
		return AiThinkUtils.getReasoningContent(response.getResult().getOutput());
	}

	// ==================== 对话图片（多模态识图） ====================

	/**
	 * 保存上传图片与聊天会话的关系，供后端多模态识图读取、以及切换会话时回显
	 * @param conversationId 会话编号
	 * @param fileUploadIds 新增的图片编号（多个逗号分隔）
	 * @param delFileUploadIds 删除的图片编号（多个逗号分隔）
	 */
	public void saveChatFile(String conversationId, String fileUploadIds, String delFileUploadIds) {
		if (StringUtils.isBlank(conversationId)) {
			return;
		}
		// 校验会话归属，避免把图片关联到他人会话或删除他人会话的图片
		checkChatConversation(conversationId);
		FileUploadUtils.saveFileUpload(conversationId, BIZ_TYPE_CHAT, fileUploadIds, delFileUploadIds);
	}

	/**
	 * 获取对话上传的图片文件（按上传倒序，最多取最近 {@link #MAX_MEDIA_SIZE} 张，避免 token 消耗过大）
	 * @param aiChat 会话（id 为会话编号）
	 */
	protected List<FileUpload> findChatFileUploadList(AiChat aiChat) {
		List<FileUpload> fileUploadList = new ArrayList<>();
		if (aiChat == null || StringUtils.isBlank(aiChat.getId())) {
			return fileUploadList;
		}
		fileUploadList.addAll(FileUploadUtils.findFileUpload(aiChat.getId(), BIZ_TYPE_CHAT));
		fileUploadList.sort(Comparator.comparing(FileUpload::getId,
				Comparator.nullsLast(Comparator.reverseOrder())));
		if (fileUploadList.size() > MAX_MEDIA_SIZE) {
			return new ArrayList<>(fileUploadList.subList(0, MAX_MEDIA_SIZE));
		}
		return fileUploadList;
	}

	/**
	 * 获取对话上传的图片（按上传倒序，最多取最近 {@link #MAX_MEDIA_SIZE} 张，避免 token 消耗过大）
	 * @param aiChat 会话（id 为会话编号）
	 */
	protected List<Media> getChatMedia(AiChat aiChat) {
		return toChatMedia(findChatFileUploadList(aiChat));
	}

	/**
	 * 图片文件转换为多模态媒体
	 * @param fileUploadList 图片文件列表
	 */
	protected List<Media> toChatMedia(List<FileUpload> fileUploadList) {
		List<Media> media = ListUtils.newArrayList();
		for (FileUpload fileUpload : fileUploadList) {
			if (fileUpload.getFileEntity() == null) {
				continue;
			}
			File file = new File(fileUpload.getFileEntity().getFileRealPath());
			MediaType mediaType = MediaType.parseMediaType(FileUtils.getContentType(file.getName()));
			media.add(Media.builder().mimeType(mediaType).data(new FileSystemResource(file)).build());
		}
		return media;
	}

	/**
	 * 获取消息携带的图片或文件（消息元数据 JSON，记录本轮提交的文件，供历史消息回显）
	 * @param aiChatMessage 消息
	 */
	public List<Map<String, Object>> getChatMessageFiles(AiChatMessage aiChatMessage) {
		return aiChatMessageService.getChatMessageFiles(aiChatMessage);
	}

	// ==================== 私有方法 ====================

	/**
	 * 获取模型编码（记录到消息上，用于历史消息的成本核算）
	 * @param aiModel 模型，可为空
	 */
	protected String getModelCode(AiModel aiModel) {
		return aiModel != null ? aiModel.getModelCode() : null;
	}

	/**
	 * 获取当前登录用户编号，取不到返回 null
	 */
	protected String getUserId() {
		return UserUtils.getUser().getId();
	}

	/**
	 * 获取单条数据
	 * @param aiChat 主键
	 */
	@Override
	public AiChat get(AiChat aiChat) {
		return super.get(aiChat);
	}

	/**
	 * 查询列表数据
	 * @param aiChat 查询条件
	 */
	@Override
	public List<AiChat> findList(AiChat aiChat) {
		return super.findList(aiChat);
	}

	/**
	 * 查询分页数据
	 * @param aiChat 查询条件
	 * @param aiChat page 分页对象
	 */
	@Override
	public Page<AiChat> findPage(AiChat aiChat) {
		return super.findPage(aiChat);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param aiChat 数据对象
	 */
	@Override
	@Transactional
	public void save(AiChat aiChat) {
		if (aiChat.getIsNewRecord()){
			dao.insert(aiChat);
		}else{
			dao.update(aiChat);
		}
	}

	/**
	 * 更新状态
	 * @param aiChat 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(AiChat aiChat) {
		if (StringUtils.isBlank(aiChat.getId())){
			return;
		}
		dao.updateStatus(aiChat);
	}

	/**
	 * 删除数据
	 * @param aiChat 数据对象
	 */
	@Override
	@Transactional
	public void delete(AiChat aiChat) {
		if (StringUtils.isBlank(aiChat.getId())){
			return;
		}
		dao.delete(aiChat);
	}

}

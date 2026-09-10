/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.service;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ai.cms.entity.AiChatCompletion;
import com.jeesite.modules.ai.cms.utils.AiThinkUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AI 对话消息存储
 * @author ThinkGem
 */
@Service
public class CacheChatMemoryRepository implements ChatMemoryRepository {

	private static final String CMS_CHAT_MSG_CACHE = "cmsChatMsgCache";

	/**
	 * 消息时间，存放在 Message 的 metadata 中，随消息一起返回给前端
	 */
	public static final String MESSAGE_CREATE_DATE = "createDate";

	/**
	 * Token 用量统计，存放在 Message 的 metadata 中，随消息一起返回给前端
	 */
	public static final String MESSAGE_USAGE = "usage";

	/**
	 * 构建用户维度的复合缓存键。
	 * Spring AI 在异步线程中执行消息，必须在请求线程上调用
	 */
	public static String genUserConversationId() {
		String userCode = UserUtils.getUser().getId();
		return userCode + ":" + IdGen.nextId();
	}

	/**
	 * 检查用户维度的复合缓存键权限
	 */
	public static boolean checkPermiByConversationId(String conversationId) {
		String userCode = UserUtils.getUser().getId();
		return StringUtils.startsWith(userCode + ":", conversationId);
	}

	@Override
	public @NotNull List<String> findConversationIds() {
		return CacheUtils.getCache(CMS_CHAT_MSG_CACHE).keys().stream().map(Object::toString).toList();
	}

	@Override
	public @NotNull List<Message> findByConversationId(@NotNull String conversationId) {
		return CacheUtils.computeIfAbsent(CMS_CHAT_MSG_CACHE, conversationId, k -> List.of());
	}

	@Override
	public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
		CacheUtils.put(CMS_CHAT_MSG_CACHE, conversationId, fillCreateDate(messages));
	}

	@Override
	public void deleteByConversationId(@NotNull String conversationId) {
		CacheUtils.remove(CMS_CHAT_MSG_CACHE, conversationId);
	}

	/**
	 * 为会话最后一条助手消息回填深度思考内容与 Token 用量统计。
	 * <p>
	 * Spring AI 的 MessageAggregator 对消息 metadata 采用 last-wins 合并，
	 * 而 Ollama 等模型的思考字段是逐分片增量下发的，聚合后仅剩最后一个分片的一小段
	 * （非空但不完整），因此不能在已存在时跳过；这里始终用流输出过程中累积的
	 * 完整思考内容覆盖，保证历史消息可以回显「深度思考」。
	 * <p>
	 * Token 用量由模型随流式最后一条分片返回，MessageChatMemoryAdvisor 写入会话时并不携带，
	 * 这里一并回填到 metadata 中，保证历史消息可以回显用量统计。
	 * @param conversationId 会话 ID
	 * @param reasoningContent 完整的思考内容（为空则不写入，但仍会清理原生思考字段）
	 * @param usage Token 用量统计（为空则不写入）
	 * @author ThinkGem
	 */
	public void fillLastAssistantMetadata(String conversationId, String reasoningContent,
			AiChatCompletion.Usage usage) {
		if (StringUtils.isBlank(conversationId)
				|| (StringUtils.isBlank(reasoningContent) && usage == null)) {
			return;
		}
		List<Message> messages = findByConversationId(conversationId);
		if (messages.isEmpty()) {
			return;
		}
		int index = -1;
		for (int i = messages.size() - 1; i >= 0; i--) {
			if (messages.get(i).getMessageType() == MessageType.ASSISTANT) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			return;
		}
		Message message = messages.get(index);
		// 清理各模型原生思考字段（Ollama 增量碎片），仅保留归一化后的完整思考内容
		Map<String, Object> newMetadata = AiThinkUtils.cleanMetadata(message.getMetadata(), reasoningContent);
		if (usage != null) {
			newMetadata.put(MESSAGE_USAGE, usage);
		}
		List<Message> list = ListUtils.newArrayList(messages);
		list.set(index, ((AssistantMessage) message).mutate().properties(newMetadata).build());
		CacheUtils.put(CMS_CHAT_MSG_CACHE, conversationId, list);
	}

	/**
	 * 为消息补充发送时间，已存在的不再覆盖，保证刷新页面后时间保持不变
	 * @author ThinkGem
	 */
	private static List<Message> fillCreateDate(List<Message> messages) {
		List<Message> list = ListUtils.newArrayList();
		for (Message message : messages) {
			list.add(fillCreateDate(message));
		}
		return list;
	}

	private static Message fillCreateDate(Message message) {
		Map<String, Object> metadata = message.getMetadata();
		if (metadata != null && metadata.get(MESSAGE_CREATE_DATE) != null) {
			return message;
		}
		Map<String, Object> newMetadata = metadata == null ? MapUtils.newHashMap() : MapUtils.newHashMap(metadata);
		newMetadata.put(MESSAGE_CREATE_DATE, DateUtils.formatDateTime(new Date()));
		return switch (message.getMessageType()) {
			case USER -> ((UserMessage) message).mutate().metadata(newMetadata).build();
			case ASSISTANT -> ((AssistantMessage) message).mutate().properties(newMetadata).build();
			default -> message;
		};
	}
}

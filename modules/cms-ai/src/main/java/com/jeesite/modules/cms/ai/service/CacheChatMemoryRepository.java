/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.service;

import com.jeesite.common.cache.CacheUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 对话消息存储
 * @author ThinkGem
 */
@Service
public class CacheChatMemoryRepository implements ChatMemoryRepository {

	private static final String CMS_CHAT_MSG_CACHE = "cmsChatMsgCache";

	@Override
	public @NotNull List<String> findConversationIds() {
		return CacheUtils.getCache(CMS_CHAT_MSG_CACHE).keys().stream().map(Object::toString).toList();
	}

	@Override
	public @NotNull List<Message> findByConversationId(@NotNull String conversationId) {
		List<Message> all = CacheUtils.get(CMS_CHAT_MSG_CACHE, conversationId);
		return all != null ? all : List.of();
	}

	@Override
	public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
		CacheUtils.put(CMS_CHAT_MSG_CACHE, conversationId, messages);
	}

	@Override
	public void deleteByConversationId(@NotNull String conversationId) {
		CacheUtils.remove(CMS_CHAT_MSG_CACHE, conversationId);
	}
}

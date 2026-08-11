/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.service;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import jakarta.validation.constraints.NotNull;
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

	/**
	 * 构建用户维度的复合缓存键，防止越权访问
	 */
	private static String buildUserKey(String conversationId) {
		String userId = UserUtils.getUser().getId();
		if (StringUtils.isBlank(userId)) {
			userId = UserUtils.getSession().getId().toString();
		}
		return userId + ":" + conversationId;
	}

	@Override
	public @NotNull List<String> findConversationIds() {
		return CacheUtils.getCache(CMS_CHAT_MSG_CACHE).keys().stream().map(Object::toString).toList();
	}

	@Override
	public @NotNull List<Message> findByConversationId(@NotNull String conversationId) {
		return CacheUtils.computeIfAbsent(CMS_CHAT_MSG_CACHE, buildUserKey(conversationId), k -> List.of());
	}

	@Override
	public void saveAll(@NotNull String conversationId, @NotNull List<Message> messages) {
		CacheUtils.put(CMS_CHAT_MSG_CACHE, buildUserKey(conversationId), messages);
	}

	@Override
	public void deleteByConversationId(@NotNull String conversationId) {
		CacheUtils.remove(CMS_CHAT_MSG_CACHE, buildUserKey(conversationId));
	}
}

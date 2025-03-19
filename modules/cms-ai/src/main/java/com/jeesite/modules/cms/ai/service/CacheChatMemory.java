/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.service;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.ListUtils;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 对话消息存储
 * @author ThinkGem
 */
@Service
public class CacheChatMemory implements ChatMemory {

	private static final String CMS_CHAT_MSG_CACHE = "cmsChatMsgCache";

	@Override
	public void add(String conversationId, List<Message> messages) {
		List<Message> conversationHistory = CacheUtils.get(CMS_CHAT_MSG_CACHE, conversationId);
		if (conversationHistory == null) {
			conversationHistory = ListUtils.newArrayList();
		}
		conversationHistory.addAll(messages);
		CacheUtils.put(CMS_CHAT_MSG_CACHE, conversationId, conversationHistory);
	}

	@Override
	public List<Message> get(String conversationId, int lastN) {
		List<Message> all = CacheUtils.get(CMS_CHAT_MSG_CACHE, conversationId);
		return all != null ? all.stream().skip(Math.max(0, all.size() - lastN)).toList() : List.of();
	}

	@Override
	public void clear(String conversationId) {
		CacheUtils.remove(CMS_CHAT_MSG_CACHE, conversationId);
	}

}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.service;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * AI 聊天服务类
 * @author ThinkGem
 */
@Service
public class CmsAiChatService extends BaseService {

	private static final String CMS_CHAT_CACHE = "cmsChatCache";

	@Autowired
    private ChatClient chatClient;
	@Autowired
    private ChatMemory chatMemory;
	@Autowired
	private VectorStore vectorStore;

	/**
	 * 获取聊天对话消息
	 * @author ThinkGem
	 */
	public List<Message> getChatMessage(String conversationId) {
        return chatMemory.get(conversationId, 100);
	}

	private static String getChatCacheKey() {
		String key = UserUtils.getUser().getId();
		if (StringUtils.isBlank(key)) {
			key = UserUtils.getSession().getId().toString();
		}
		return key;
	}

	public Map<String, Map<String, Object>> getChatCacheMap() {
		Map<String, Map<String, Object>> cache = CacheUtils.get(CMS_CHAT_CACHE, getChatCacheKey());
		if (cache == null) {
			cache = MapUtils.newHashMap();
		}
		return cache;
	}

	/**
	 * 新建或更新聊天对话
	 * @author ThinkGem
	 */
	public Map<String, Object> saveChatConversation(String conversationId, String title) {
		if (StringUtils.isBlank(conversationId)) {
			conversationId = IdGen.nextId();
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
		Map<String, Map<String, Object>> cache = getChatCacheMap();
		cache.remove(conversationId);
		CacheUtils.put(CMS_CHAT_CACHE, getChatCacheKey(), cache);
		chatMemory.clear(conversationId);
	}

	/**
	 * 聊天对话，流输出
	 * @author ThinkGem
	 */
	public Flux<ChatResponse> chatStream(String conversationId, String message) {
		return chatClient.prompt()
            .messages(new UserMessage(message))
            .advisors(
				new MessageChatMemoryAdvisor(chatMemory, conversationId, 1024),
				new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().similarityThreshold(0.6F).topK(6).build()))
            .stream()
			.chatResponse();
    }

}

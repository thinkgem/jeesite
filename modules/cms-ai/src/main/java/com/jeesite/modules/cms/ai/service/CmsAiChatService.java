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
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.service.BaseService;
import com.jeesite.modules.cms.ai.properties.CmsAiProperties;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.utils.AreaUtils;
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
import org.springframework.ai.converter.AbstractMessageOutputConverter;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.List;
import java.util.Map;

/**
 * AI 聊天服务类
 * @author ThinkGem
 */
@Service
public class CmsAiChatService extends BaseService {

	private static final String CMS_CHAT_CACHE = "cmsChatCache";
	private static final String[] USER_MESSAGE_SEARCH = new String[]{"{", "}"};
	private static final String[] USER_MESSAGE_REPLACE = new String[]{"\\{", "\\}"};

	@Autowired
	private ChatClient chatClient;
	@Autowired
	private ChatMemory chatMemory;
	@Autowired
	private VectorStore vectorStore;
	@Autowired
	private CmsAiProperties properties;

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
	public Flux<ChatResponse> chatStream(String conversationId, String message, HttpServletRequest request) {
		return chatClient.prompt()
			.messages(
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			)
			.advisors(
				MessageChatMemoryAdvisor.builder(chatMemory)
						.conversationId(conversationId)
						.build(),
				QuestionAnswerAdvisor.builder(vectorStore)
						.searchRequest(SearchRequest.builder().similarityThreshold(0.6F).topK(6).build())
						.promptTemplate(new PromptTemplate(properties.getDefaultPromptTemplate()))
						.build()
			)
			.stream()
			.chatResponse()
			.doOnNext(response -> {
				if (response.getResult() != null && StringUtils.isNotBlank(response.getResult().getOutput().getText())) {
					AssistantMessage assistantMessage = (AssistantMessage)request.getAttribute("assistantMessage");
					AssistantMessage currAssistantMessage = response.getResult().getOutput();
					if (assistantMessage == null) {
						request.setAttribute("assistantMessage", currAssistantMessage);
					} else {
						request.setAttribute("assistantMessage", new AssistantMessage(
								assistantMessage.getText() + currAssistantMessage.getText(),
								currAssistantMessage.getMetadata()));
					}
				}
			})
			.doFinally((signalType) -> {
				if (signalType != SignalType.ON_COMPLETE) {
					AssistantMessage assistantMessage = (AssistantMessage)request.getAttribute("assistantMessage");
					if (assistantMessage != null) {
						chatMemory.add(conversationId, assistantMessage);
					} else if (signalType == SignalType.CANCEL) {
						chatMemory.add(conversationId, new AssistantMessage(text("暂无消息，你已主动停止响应。")));
					}
				}
			})
			.onErrorResume(error -> {
				String errorMessage = error.getMessage();
				if (error instanceof WebClientResponseException webClientError) {
					errorMessage = webClientError.getResponseBodyAsString();
				}
				AssistantMessage assistantMessage = new AssistantMessage(errorMessage);
				chatMemory.add(conversationId, assistantMessage);
				logger.error("Error message: {}", errorMessage);
				return Flux.just(ChatResponse.builder()
						.generations(List.of(new Generation(assistantMessage)))
						.build());
			});
	}

	/**
	 * 聊天对话，文本输出
	 * @author ThinkGem
	 */
	public String chatText(String message) {
		return chatClient.prompt()
			.messages(
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			)
			.call()
			.content();
    }

	/**
	 * 聊天对话，结构化输出（Map）
	 * @author ThinkGem
	 */
	public Map<String, Object> chatJson(String message) {
		return chatClient.prompt()
			.messages(
				new SystemMessage("""
						[ {name:'张三', sex:'男', age:'17'}, {name:'李四', sex:'女', age:'18'} ]，返回 json。
						"""),
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			)
			.call()
			.responseEntity(new AbstractMessageOutputConverter<Map<String, Object>>(
					new MappingJackson2MessageConverter(JsonMapper.getInstance())
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
			.getEntity();
	}

	/**
	 * 聊天对话，结构化输出（Area）
	 * @author ThinkGem
	 */
	public List<Area> chatArea(String message) {
		List<Area> list = AreaUtils.getAreaAllList();
		if (list.size() > 10) list = list.subList(0, 10);
		return chatClient.prompt()
            .messages(
				new SystemMessage(JsonMapper.toJson(list)),
				new UserMessage(StringUtils.replaceEach(message, USER_MESSAGE_SEARCH, USER_MESSAGE_REPLACE))
			)
            .advisors(
				QuestionAnswerAdvisor.builder(vectorStore)
						.searchRequest(SearchRequest.builder().similarityThreshold(0.6F).topK(6).build())
						.promptTemplate(new PromptTemplate(properties.getDefaultPromptTemplate()))
						.build()
			)
			.call()
			.responseEntity(new BeanOutputConverter<>(new ParameterizedTypeReference<List<Area>>() {},
					JsonMapper.getInstance()))
			.getEntity();
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

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.ai.service.CmsAiChatService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 聊天控制器类
 * @author ThinkGem
 */
@RestController
@RequestMapping("${adminPath}/cms/chat")
public class CmsAiChatController extends BaseController {

	@Autowired
	private CmsAiChatService cmsAiChatService;

	/**
	 * 获取聊天对话消息
	 * @author ThinkGem
	 */
    @RequestMapping("/message")
    public List<Message> message(String id) {
        return cmsAiChatService.getChatMessage(id);
    }

	/**
	 * 聊天对话列表
	 * @author ThinkGem
	 */
    @RequestMapping("/list")
    public Collection<Map<String, Object>> list() {
		return cmsAiChatService.getChatCacheMap().values().stream()
            .sorted(Comparator.comparing(map -> (String) map.get("id"),
                    Comparator.reverseOrder())).collect(Collectors.toList());
    }

	/**
	 * 新建或更新聊天对话
	 * @author ThinkGem
	 */
	@RequestMapping("/save")
    public String save(String id, String title) {
		Map<String, Object> map = cmsAiChatService.saveChatConversation(id, title);
        return renderResult(Global.TRUE, "保存成功", map);
    }

	/**
	 * 删除聊天对话
	 * @author ThinkGem
	 */
    @RequestMapping("/delete")
    public String delete(String id) {
		cmsAiChatService.deleteChatConversation(id);
        return renderResult(Global.TRUE, "删除成功", id);
    }

	/**
	 * 聊天对话，流输出
	 * @author ThinkGem
	 */
    @RequestMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> stream(String id, String message) {
		return cmsAiChatService.chatStream(id, message);
    }

}

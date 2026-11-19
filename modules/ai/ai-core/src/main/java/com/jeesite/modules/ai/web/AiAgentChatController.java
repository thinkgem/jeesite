/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ai.entity.AiAgent;
import com.jeesite.modules.ai.entity.AiChat;
import com.jeesite.modules.ai.entity.AiChatCompletion;
import com.jeesite.modules.ai.entity.AiChatMessage;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.service.AiAgentService;
import com.jeesite.modules.ai.service.AiChatMessageService;
import com.jeesite.modules.ai.service.AiChatService;
import com.jeesite.modules.ai.utils.AiModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * AI智能体对话 Controller
 * <p>
 * 接口参数均为 query 参数，与前端 defHttp.post({ params }) 保持一致。
 * @author ThinkGem
 * @version 2025-11-12
 */
@ConditionalOnProperty(prefix = "ai", name = "enabled", havingValue = "true", matchIfMissing = true)
@RestController
@RequestMapping("${adminPath}/ai/agentChat")
public class AiAgentChatController extends BaseController {

	@Autowired
	private AiAgentService aiAgentService;

	@Autowired
	private AiChatService aiChatService;

	/**
	 * 对话消息列表（消息表）
	 */
	@GetMapping("message")
	public List<Map<String, Object>> message(String conversationId) {
		List<Map<String, Object>> list = ListUtils.newArrayList();
		// 会话不存在或不属于当前用户时，返回空列表
		if (aiChatService.getChatConversation(conversationId) == null) {
			return list;
		}
		for (AiChatMessage aiChatMessage : aiChatService.findChatMessageList(conversationId)) {
			Map<String, Object> item = MapUtils.newHashMap();
			item.put("id", aiChatMessage.getId());
			// 回复的消息编号：助手消息指向对应的用户消息，前端用于问答配对、重新生成等
			item.put("parentId", aiChatMessage.getParentId());
			item.put("messageType",
					AiChatMessageService.ROLE_USER.equals(aiChatMessage.getMsgType()) ? "USER" : "ASSISTANT");
			item.put("text", aiChatMessage.getMsgText());
			item.put("createDate", aiChatMessage.getCreateDate());
			// Token 用量（元数据 JSON），前端显示输入、输出、合计、缓存命中等
			Map<String, Object> usage = aiChatService.getChatMessageUsage(aiChatMessage);
			if (usage != null) {
				item.put("usage", usage);
			}
			// 本条回复实际使用的模型编码（智能体切换模型后仍可准确定位、核算成本）
			if (StringUtils.isNotBlank(aiChatMessage.getModelCode())) {
				item.put("modelCode", aiChatMessage.getModelCode());
			}
			// 本条回复消耗的 Token 合计与金额（按调用当时的价格计算）
			if (aiChatMessage.getTotalTokens() != null) {
				item.put("totalTokens", aiChatMessage.getTotalTokens());
			}
			if (aiChatMessage.getTotalCost() != null) {
				item.put("totalCost", aiChatMessage.getTotalCost());
				// 金额币种（取自调用当时的单价快照）
				String currency = aiChatService.getChatMessageCurrency(aiChatMessage);
				if (StringUtils.isNotBlank(currency)) {
					item.put("totalCostCurrency", currency);
				}
			}
			// 深度思考内容（元数据 JSON），前端历史消息回显「深度思考」面板
			String reasoningContent = aiChatService.getChatMessageReasoningContent(aiChatMessage);
			if (StringUtils.isNotBlank(reasoningContent)) {
				item.put("reasoningContent", reasoningContent);
			}
			// 本轮提交的图片或文件（元数据 JSON），前端显示在消息气泡中
			List<Map<String, Object>> fileList = aiChatService.getChatMessageFiles(aiChatMessage);
			if (fileList != null) {
				item.put("fileList", fileList);
			}
			list.add(item);
		}
		return list;
	}

	/**
	 * 对话会话列表（会话表）
	 */
	@GetMapping("list")
	public List<AiChat> list(String agentId) {
		return aiChatService.findChatConversationList(agentId);
	}

	/**
	 * 新建或更新对话会话（会话表）
	 */
	@PostMapping("save")
	public AiChat save(AiChat aiChat) {
		// 演示模式下也允许对话（会话、消息均为当前用户自己的数据）
		return aiChatService.saveChatConversation(aiChat);
	}

	/**
	 * 删除对话会话（含消息）
	 */
	@GetMapping("delete")
	public String delete(String id) {
		if (aiChatService.getChatConversation(id) == null) {
			throw new ServiceException("对话不存在或无权访问：" + id);
		}
		aiChatService.deleteChatConversation(id);
		return renderResult(Global.TRUE, text("删除成功"), id);
	}

	/**
	 * 保存上传图片与聊天会话的关系，供后端多模态识图读取、以及切换会话时回显
	 */
	@PostMapping("file/save")
	public String fileSave(@RequestParam String id, String fileUploadIds, String delFileUploadIds) {
		aiChatService.saveChatFile(id, fileUploadIds, delFileUploadIds);
		return renderResult(Global.TRUE, text("保存成功"), id);
	}

	/**
	 * 可选模型列表（启用状态）
	 */
	@GetMapping("modelList")
	public List<Map<String, Object>> modelList() {
		List<Map<String, Object>> list = ListUtils.newArrayList();
		for (AiModel model : AiModelUtils.findModelList(null)) {
			Map<String, Object> item = MapUtils.newHashMap();
			item.put("modelCode", model.getModelCode());
			item.put("modelDesc", model.getModelDesc());
			item.put("modelName", model.getModelName());
			item.put("modelProvider", model.getModelProvider());
			item.put("modelType", model.getModelType());
			list.add(item);
		}
		return list;
	}

	/**
	 * 切换智能体使用的模型（保存到智能体，所有对话共用）
	 */
	@PostMapping("saveModel")
	public String saveModel(String id, String modelCode) {
		// 演示模式下也允许切换模型，保证对话界面可正常使用
		if (StringUtils.isBlank(modelCode)) {
			throw new ServiceException("请选择模型");
		}
		AiAgent agent = aiAgentService.get(id, false);
		if (agent == null) {
			throw new ServiceException("智能体不存在：" + id);
		}
		AiModel model = AiModelUtils.getModel(modelCode);
		if (model == null) {
			throw new ServiceException("AI模型不存在：" + modelCode);
		}
		agent.setModel(model);
		aiAgentService.save(agent);
		return renderResult(Global.TRUE, text("模型已切换"), modelCode);
	}

	/**
	 * 对话（流式输出），输出 OpenAI v1 Chat Completions 兼容的 SSE 分片
	 * <p>
	 * 分片结构：{@code { id, object: "chat.completion.chunk", created, model, choices: [{ index, delta, finish_reason }], usage }}，
	 * 正文增量见 {@code choices[0].delta.content}，思考增量见 {@code choices[0].delta.reasoning_content}，
	 * 尾块携带 {@code finish_reason: "stop"} 与 Token 用量。
	 */
	@PostMapping(value = "completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<AiChatCompletion> completions(@RequestParam String id,
			String agentId, @RequestParam String message) {
		if (StringUtils.isBlank(agentId)) {
			throw new ServiceException("请选择智能体");
		}
		if (aiChatService.getChatConversation(id) == null) {
			throw new ServiceException("对话不存在或无权访问：" + id);
		}
		AiAgent agent = aiAgentService.get(agentId, false);
		if (agent == null) {
			throw new ServiceException("智能体不存在：" + agentId);
		}
		return AiChatCompletion.streamOf(aiChatService.chatStream(agent, id, message));
	}

	/**
	 * 重新生成回复（流式输出），输出格式与 {@link #completions} 一致
	 * <p>
	 * 会删除该回复及其之后的对话，基于其父用户消息及之前的历史重新回答。
	 * @param id 会话编号
	 * @param agentId 智能体编号
	 * @param messageId 要重新生成的助手消息编号，为空则重新生成最后一条助手回复
	 */
	@PostMapping(value = "regenerate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<AiChatCompletion> regenerate(@RequestParam String id,
			@RequestParam String agentId, String messageId) {
		if (StringUtils.isBlank(agentId)) {
			throw new ServiceException("请选择智能体");
		}
		if (aiChatService.getChatConversation(id) == null) {
			throw new ServiceException("对话不存在或无权访问：" + id);
		}
		AiAgent agent = aiAgentService.get(agentId, false);
		if (agent == null) {
			throw new ServiceException("智能体不存在：" + agentId);
		}
		return AiChatCompletion.streamOf(aiChatService.regenerateChatStream(agent, id, messageId));
	}

}

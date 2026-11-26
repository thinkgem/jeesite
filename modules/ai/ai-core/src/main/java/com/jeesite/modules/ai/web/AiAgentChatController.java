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
import com.jeesite.modules.ai.entity.AiChatMessage;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.response.AiChatCompletion;
import com.jeesite.modules.ai.service.AiAgentService;
import com.jeesite.modules.ai.service.AiChatMessageService;
import com.jeesite.modules.ai.service.AiChatService;
import com.jeesite.modules.ai.service.AiToolConfirmService;
import com.jeesite.modules.ai.utils.AiModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

	@Autowired
	private AiToolConfirmService aiToolConfirmService;

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
			// 已执行的工具步骤与任务列表（元数据 JSON），前端回显工具卡片与任务进度
			List<Map<String, Object>> steps = aiChatService.getChatMessageToolSteps(aiChatMessage);
			if (steps != null) {
				item.put("steps", steps);
			}
			List<Map<String, Object>> taskList = aiChatService.getChatMessageTaskList(aiChatMessage);
			if (taskList != null) {
				item.put("taskList", taskList);
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
	 * 尾块携带 {@code finish_reason: "stop"}，用量在随后独立的 {@code choices: []} 分片中下发，
	 * 流以 {@code data: [DONE]} 结束。
	 */
	@PostMapping(value = "completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<?>> completions(@RequestParam String id,
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
		// 本轮回复的费用：流式过程中由 Service 按模型单价算出，随用量分片下发，前端无需等历史消息
		AtomicReference<AiChatCompletion.Cost> costRef = new AtomicReference<>();
		return toSse(AiChatCompletion.streamOf(aiChatService.chatStream(agent, id, message, costRef), costRef::get));
	}

	/**
	 * 工具执行确认（用户对「等待确认」的工具调用提交决策）
	 * <p>
	 * 工具执行在流式响应的异步线程中挂起等待，本接口只负责唤醒，SSE 连接保持不中断，
	 * 因此返回 {@code continue: false}（表示前端无需再发起新的对话请求）。
	 * @param runId 本轮运行编号（confirm_required 事件下发）
	 * @param toolCallId 工具调用编号
	 * @param decision 决策：once 本次运行、always 始终运行、deny 拒绝
	 */
	@PostMapping("confirm")
	public String confirm(String runId, String toolCallId, String decision) {
		if (StringUtils.isBlank(runId) || StringUtils.isBlank(toolCallId)) {
			throw new ServiceException("确认参数不完整");
		}
		if (!aiToolConfirmService.confirm(runId, toolCallId, decision)) {
			throw new ServiceException("该确认已超时或已处理，请等待本轮对话结束");
		}
		Map<String, Object> data = MapUtils.newHashMap();
		data.put("continue", Boolean.FALSE);
		return renderResult(Global.TRUE, text("已提交"), data);
	}

	/**
	 * 工具确认配置：是否开启执行前确认（ai.tool.confirmMode 不为 none）
	 * <p>
	 * 未开启时工具调用不会产生确认请求，「工具确认设置」入口没有意义，前端不显示。
	 */
	@GetMapping("toolConfirm/config")
	public String toolConfirmConfig() {
		Map<String, Object> data = MapUtils.newHashMap();
		data.put("confirmMode", aiToolConfirmService.getConfirmMode());
		data.put("confirmEnabled", aiToolConfirmService.isConfirmEnabled());
		return renderResult(Global.TRUE, text(""), data);
	}

	/**
	 * 当前用户已允许「始终运行」的工具编码列表
	 */
	@GetMapping("toolConfirm/list")
	public List<String> toolConfirmList() {
		return aiToolConfirmService.getAlwaysAllowTools();
	}

	/**
	 * 撤销某个工具的「始终运行」，恢复为执行前确认
	 * @param toolCode 工具编码
	 */
	@PostMapping("toolConfirm/remove")
	public String toolConfirmRemove(String toolCode) {
		if (StringUtils.isBlank(toolCode)) {
			throw new ServiceException("请指定工具编码");
		}
		boolean removed = aiToolConfirmService.removeAlwaysAllow(toolCode);
		return renderResult(Global.TRUE, text(removed ? "已恢复执行前确认" : "该工具未设置始终运行"), removed);
	}

	/**
	 * 撤销当前用户全部「始终运行」
	 */
	@PostMapping("toolConfirm/clear")
	public String toolConfirmClear() {
		int count = aiToolConfirmService.clearAlwaysAllow();
		return renderResult(Global.TRUE, text("已恢复执行前确认：" + count + " 个工具"), count);
	}

	/**
	 * 重新生成回复（流式输出），输出格式与 {@link #completions} 一致
	 * <p>
	 * 会删除该回复及其之后的对话，基于其父用户消息及之前的历史重新回答。
	 * @param id 会话编号
	 * @param agentId 智能体编号
	 * @param messageId 要重新生成的助手消息编号，为空则重新生成最后一条助手回复
	 * @param context 本轮隐藏上下文（如可填写表单的字段结构），与首次发送一致，保证输出格式不变
	 */
	@PostMapping(value = "regenerate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<?>> regenerate(@RequestParam String id,
			@RequestParam String agentId, String messageId, String context) {
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
		// 本轮回复的费用：流式过程中由 Service 按模型单价算出，随用量分片下发，前端无需等历史消息
		AtomicReference<AiChatCompletion.Cost> costRef = new AtomicReference<>();
		return toSse(AiChatCompletion.streamOf(
				aiChatService.regenerateChatStream(agent, id, messageId, context, costRef), costRef::get));
	}

	/**
	 * 包装为 SSE 事件流，并按 OpenAI v1 协议在末尾追加 {@code data: [DONE]} 结束标记
	 * @param chunks OpenAI 兼容的流式分片
	 * @author ThinkGem
	 */
	private Flux<ServerSentEvent<?>> toSse(Flux<AiChatCompletion> chunks) {
		Flux<ServerSentEvent<?>> body = chunks.map(chunk -> (ServerSentEvent<?>) ServerSentEvent.builder(chunk).build());
		// 结束标记为字符串类型，先按通配类型持有，避免 concatWith 泛型下限冲突
		ServerSentEvent<?> done = ServerSentEvent.builder("[DONE]").build();
		return body.concatWith(Flux.just(done));
	}

}

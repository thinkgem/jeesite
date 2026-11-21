/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ai.dao.AiChatMessageDao;
import com.jeesite.modules.ai.entity.AiChat;
import com.jeesite.modules.ai.entity.AiChatMessage;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.response.AiReasoningContent;
import com.jeesite.modules.file.entity.FileUpload;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * AI聊天消息内容 Service
 * <p>
 * 负责消息的存取，以及消息上 Token 用量的解析、单价快照与消耗金额计算。
 * 由 {@code AiCoreAutoConfiguration} 注入，业务可通过定义同类型 Bean 覆盖（二开：继承本类并注册 Bean）。
 * @author ThinkGem
 * @version 2026-11-12
 */
public class AiChatMessageService extends CrudService<AiChatMessageDao, AiChatMessage> {

	/** 消息角色：用户 */
	public static final String ROLE_USER = "user";
	/** 消息角色：助手 */
	public static final String ROLE_ASSISTANT = "assistant";
	/** 消息角色：系统 */
	public static final String ROLE_SYSTEM = "system";
	/** 消息元数据：本轮提交的图片或文件列表 */
	public static final String METADATA_FILES = "files";
	/** 消息元数据：已执行的工具步骤 */
	public static final String METADATA_STEPS = "steps";
	/** 消息元数据：任务列表 */
	public static final String METADATA_TASKS = "taskList";

	/**
	 * 获取单条数据
	 * @param aiChatMessage 主键
	 */
	@Override
	public AiChatMessage get(AiChatMessage aiChatMessage) {
		return super.get(aiChatMessage);
	}

	/**
	 * 查询列表数据
	 * @param aiChatMessage 查询条件
	 */
	@Override
	public List<AiChatMessage> findList(AiChatMessage aiChatMessage) {
		return super.findList(aiChatMessage);
	}

	/**
	 * 查询分页数据
	 * @param aiChatMessage 查询条件
	 * @param aiChatMessage page 分页对象
	 */
	@Override
	public Page<AiChatMessage> findPage(AiChatMessage aiChatMessage) {
		return super.findPage(aiChatMessage);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param aiChatMessage 数据对象
	 */
	@Override
	@Transactional
	public void save(AiChatMessage aiChatMessage) {
		if (aiChatMessage.getIsNewRecord()){
			dao.insert(aiChatMessage);
		}else{
			dao.update(aiChatMessage);
		}
	}

	/**
	 * 更新状态
	 * @param aiChatMessage 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(AiChatMessage aiChatMessage) {
		if (StringUtils.isBlank(aiChatMessage.getId())){
			return;
		}
		dao.updateStatus(aiChatMessage);
	}

	/**
	 * 删除数据
	 * @param aiChatMessage 数据对象
	 */
	@Override
	@Transactional
	public void delete(AiChatMessage aiChatMessage) {
		if (StringUtils.isBlank(aiChatMessage.getId())){
			return;
		}
		dao.delete(aiChatMessage);
	}

	// ==================== 消息存储 ====================

	/**
	 * 查询对话消息列表（按创建时间正序）
	 * @param aiChatMessage 查询条件（convId 必填）
	 */
	public List<AiChatMessage> findChatMessageList(AiChatMessage aiChatMessage) {
		if (aiChatMessage == null || StringUtils.isBlank(aiChatMessage.getConvId())) {
			return ListUtils.newArrayList();
		}
		// 按创建时间正序，保证对话上下文顺序正确
		aiChatMessage.sqlMap().getOrder().setOrderBy("create_date asc");
		return findList(aiChatMessage);
	}

	/**
	 * 查询某条消息的回复消息列表（按创建时间正序），用于问答配对、重新生成等场景
	 * @param aiChatMessage 查询条件（convId、parentId 必填）
	 */
	public List<AiChatMessage> findChatMessageListByParentId(AiChatMessage aiChatMessage) {
		if (aiChatMessage == null || StringUtils.isBlank(aiChatMessage.getConvId())
				|| StringUtils.isBlank(aiChatMessage.getParentId())) {
			return ListUtils.newArrayList();
		}
		aiChatMessage.sqlMap().getOrder().setOrderBy("create_date asc");
		return findList(aiChatMessage);
	}

	/**
	 * 保存对话消息（消息表）
	 * @param aiChatMessage 消息内容（convId、msgType、msgText 必填）
	 * @return 保存后的消息（已回填编号、创建时间等），参数不合法时返回 null
	 */
	@Transactional
	public AiChatMessage saveChatMessage(AiChatMessage aiChatMessage) {
		if (aiChatMessage == null || StringUtils.isBlank(aiChatMessage.getConvId())
				|| StringUtils.isBlank(aiChatMessage.getMsgText())) {
			return null;
		}
		save(aiChatMessage);
		return aiChatMessage;
	}

	/**
	 * 保存对话消息（Spring AI 消息）
	 * @param aiChat 对话（id 为会话编号）
	 * @param message 消息
	 * @return 保存后的消息（已回填编号、创建时间等），参数不合法时返回 null
	 */
	@Transactional
	public AiChatMessage saveChatMessage(AiChat aiChat, Message message) {
		if (message == null) {
			return null;
		}
		AiChatMessage aiChatMessage = new AiChatMessage();
		aiChatMessage.setConvId(aiChat != null ? aiChat.getId() : null);
		aiChatMessage.setMsgType(getRole(message));
		aiChatMessage.setMsgText(message.getText());
		return saveChatMessage(aiChatMessage);
	}

	/**
	 * 清空对话消息（消息表）
	 * @param aiChatMessage 查询条件（convId 必填）
	 */
	@Transactional
	public void clearChatMessages(AiChatMessage aiChatMessage) {
		if (aiChatMessage == null || StringUtils.isBlank(aiChatMessage.getConvId())) {
			return;
		}
		for (AiChatMessage message : findChatMessageList(aiChatMessage)) {
			delete(message);
		}
	}

	/**
	 * 获取消息角色
	 * @param message Spring AI 消息
	 */
	protected String getRole(Message message) {
		if (message instanceof SystemMessage) {
			return ROLE_SYSTEM;
		}
		if (message instanceof AssistantMessage) {
			return ROLE_ASSISTANT;
		}
		return ROLE_USER;
	}

	// ==================== Token 用量与费用 ====================

	/**
	 * 提取响应的 Token 用量（输入、输出、合计、缓存命中等等，原始字段以厂商返回为准）
	 * @param response 响应
	 */
	public Map<String, Object> getChatUsage(ChatResponse response) {
		if (response == null || response.getMetadata() == null) {
			return null;
		}
		Usage usage = response.getMetadata().getUsage();
		// 空用量（EmptyUsage 返回 0）不参与统计，避免覆盖已获取的有效用量
		if (usage == null || usage.getTotalTokens() == null || usage.getTotalTokens() <= 0) {
			return null;
		}
		Map<String, Object> map = MapUtils.newHashMap();
		// 优先使用厂商原生用量，保留缓存命中等扩展字段
		Object nativeUsage = getNativeUsage(usage);
		if (nativeUsage != null) {
			try {
				Map<?, ?> nativeMap = JsonMapper.getInstance().readValue(
						JsonMapper.getInstance().writeValueAsString(nativeUsage), Map.class);
				if (nativeMap != null) {
					nativeMap.forEach((key, value) -> map.put(String.valueOf(key), value));
				}
			} catch (Exception e) {
				logger.debug("解析厂商 Token 用量失败：{}", e.getMessage());
			}
		}
		map.putIfAbsent("prompt_tokens", usage.getPromptTokens());
		map.putIfAbsent("completion_tokens", usage.getCompletionTokens());
		map.putIfAbsent("total_tokens", usage.getTotalTokens());
		return map;
	}

	/**
	 * 设置消息的 Token 用量、消耗金额与深度思考内容
	 * <p>
	 * 合计 Token 与金额存入消息字段（便于直接统计），
	 * 原始用量与调用当时的单价快照存入消息元数据（便于审计与重算，后续调价不影响历史金额）。
	 * @param aiChatMessage 消息
	 * @param aiModel 模型（用于取价格 options.price），可为空
	 * @param usage Token 用量，可为空
	 * @param reasoningContent 深度思考内容，可为空
	 */
	public void setChatUsage(AiChatMessage aiChatMessage, AiModel aiModel,
			Map<String, Object> usage, String reasoningContent) {
		if (aiChatMessage == null) {
			return;
		}
		// 深度思考内容单独存储，不再放进消息元数据
		if (StringUtils.isNotBlank(reasoningContent)) {
			aiChatMessage.setMsgThink(reasoningContent);
		}
		// 清理各模型的原生思考字段，保留厂商原始用量
		Map<String, Object> cleanUsage = AiReasoningContent.cleanMetadata(usage, null);
		Map<String, Object> metadata = MapUtils.newLinkedHashMap();
		if (!cleanUsage.isEmpty()) {
			metadata.put("usage", cleanUsage);
			Integer totalTokens = getUsageTokens(cleanUsage, "total_tokens");
			if (totalTokens != null) {
				aiChatMessage.setTotalTokens(totalTokens);
			}
			// 按调用当时的模型单价计算金额（输入、输出、缓存命中分别计价）
			// 未配置价格时不写金额，避免记成 0 造成“免费”的误解
			Map<String, Object> price = getModelPrice(aiModel);
			BigDecimal cost = calculateCost(cleanUsage, price);
			if (cost != null) {
				metadata.put("price", price);
				aiChatMessage.setTotalCost(cost);
			}
		}
		if (!metadata.isEmpty()) {
			try {
				aiChatMessage.setMsgMetadata(JsonMapper.getInstance().writeValueAsString(metadata));
			} catch (Exception e) {
				logger.debug("序列化消息元数据失败：{}", e.getMessage());
			}
		}
	}

	/**
	 * 设置消息携带的图片或文件（消息元数据 JSON）
	 * <p>
	 * 前端提交后不再保留会话上的图片关联，历史消息按消息上的记录回显提交过的图片或文件。
	 * @param aiChatMessage 消息
	 * @param fileUploadList 本轮提交的文件列表，可为空
	 */
	public void setChatMessageFiles(AiChatMessage aiChatMessage, List<FileUpload> fileUploadList) {
		if (aiChatMessage == null || fileUploadList == null || fileUploadList.isEmpty()) {
			return;
		}
		List<Map<String, Object>> files = ListUtils.newArrayList();
		for (FileUpload fileUpload : fileUploadList) {
			if (fileUpload == null || StringUtils.isBlank(fileUpload.getId())) {
				continue;
			}
			Map<String, Object> item = MapUtils.newHashMap();
			item.put("id", fileUpload.getId());
			item.put("fileName", fileUpload.getFileName());
			item.put("fileUrl", fileUpload.getFileUrl());
			files.add(item);
		}
		if (files.isEmpty()) {
			return;
		}
		Map<String, Object> metadata = MapUtils.newLinkedHashMap();
		metadata.put(METADATA_FILES, files);
		try {
			aiChatMessage.setMsgMetadata(JsonMapper.getInstance().writeValueAsString(metadata));
		} catch (Exception e) {
			logger.debug("序列化消息文件列表失败：{}", e.getMessage());
		}
	}

	/**
	 * 获取消息携带的图片或文件（消息元数据 JSON）
	 * @param aiChatMessage 消息
	 */
	public List<Map<String, Object>> getChatMessageFiles(AiChatMessage aiChatMessage) {
		Map<String, Object> metadata = getChatMessageMetadata(aiChatMessage);
		if (metadata == null) {
			return null;
		}
		Object files = metadata.get(METADATA_FILES);
		if (files instanceof List<?> fileList) {
			List<Map<String, Object>> result = ListUtils.newArrayList();
			for (Object item : fileList) {
				if (item instanceof Map<?, ?> fileMap) {
					Map<String, Object> file = MapUtils.newHashMap();
					fileMap.forEach((key, value) -> file.put(String.valueOf(key), value));
					result.add(file);
				}
			}
			return result.isEmpty() ? null : result;
		}
		return null;
	}

	/**
	 * 设置消息的已执行工具步骤（消息元数据 JSON，历史消息回显工具卡片）
	 * @param aiChatMessage 消息
	 * @param steps 工具步骤（stepId、title、toolName、args、status、result 等）
	 */
	public void setChatMessageToolSteps(AiChatMessage aiChatMessage, List<Map<String, Object>> steps) {
		if (steps == null || steps.isEmpty()) {
			return;
		}
		appendChatMessageMetadata(aiChatMessage, METADATA_STEPS, steps);
	}

	/**
	 * 设置消息的任务列表（消息元数据 JSON，历史消息回显任务进度）
	 * @param aiChatMessage 消息
	 * @param tasks 任务项（id、title、status）
	 */
	public void setChatMessageTaskList(AiChatMessage aiChatMessage, List<Map<String, Object>> tasks) {
		if (tasks == null || tasks.isEmpty()) {
			return;
		}
		appendChatMessageMetadata(aiChatMessage, METADATA_TASKS, tasks);
	}

	/**
	 * 获取消息的已执行工具步骤（消息元数据 JSON）
	 * @param aiChatMessage 消息
	 */
	public List<Map<String, Object>> getChatMessageToolSteps(AiChatMessage aiChatMessage) {
		return getChatMessageList(aiChatMessage, METADATA_STEPS);
	}

	/**
	 * 获取消息的任务列表（消息元数据 JSON）
	 * @param aiChatMessage 消息
	 */
	public List<Map<String, Object>> getChatMessageTaskList(AiChatMessage aiChatMessage) {
		return getChatMessageList(aiChatMessage, METADATA_TASKS);
	}

	/**
	 * 追加消息元数据（保留已有的用量、价格等字段）
	 */
	protected void appendChatMessageMetadata(AiChatMessage aiChatMessage, String key, Object value) {
		if (aiChatMessage == null || StringUtils.isBlank(key) || value == null) {
			return;
		}
		Map<String, Object> metadata = getChatMessageMetadata(aiChatMessage);
		if (metadata == null) {
			metadata = MapUtils.newLinkedHashMap();
		}
		metadata.put(key, value);
		try {
			aiChatMessage.setMsgMetadata(JsonMapper.getInstance().writeValueAsString(metadata));
		} catch (Exception e) {
			logger.debug("序列化消息元数据失败：{}", e.getMessage());
		}
	}

	/**
	 * 读取元数据中的列表字段（JSON 转 List）
	 */
	protected List<Map<String, Object>> getChatMessageList(AiChatMessage aiChatMessage, String key) {
		Map<String, Object> metadata = getChatMessageMetadata(aiChatMessage);
		if (metadata == null) {
			return null;
		}
		Object value = metadata.get(key);
		List<?> list;
		if (value instanceof List<?> valueList) {
			list = valueList;
		} else if (value instanceof String text && StringUtils.isNotBlank(text)) {
			// 兼容被序列化为 JSON 字符串的列表
			try {
				list = JsonMapper.getInstance().readValue(text, List.class);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
		List<Map<String, Object>> result = ListUtils.newArrayList();
		for (Object item : list) {
			if (item instanceof Map<?, ?> itemMap) {
				Map<String, Object> row = MapUtils.newHashMap();
				itemMap.forEach((mapKey, mapValue) -> row.put(String.valueOf(mapKey), mapValue));
				result.add(row);
			}
		}
		return result.isEmpty() ? null : result;
	}

	/**
	 * 获取模型的价格配置（options.price）
	 * <p>
	 * 格式示例：{@code {"input":0.002,"output":0.008,"cache":0.0005,"unit":"1K","currency":"CNY"}}，
	 * unit 可为 1K、1M、token，表示每多少 Token 的价格。
	 * @param aiModel 模型，可为空
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getModelPrice(AiModel aiModel) {
		if (aiModel == null || aiModel.getOptionMap() == null) {
			return MapUtils.newHashMap();
		}
		Object price = aiModel.getOptionMap().get("price");
		if (price instanceof Map<?, ?> priceMap) {
			Map<String, Object> result = MapUtils.newHashMap();
			priceMap.forEach((key, value) -> result.put(String.valueOf(key), value));
			return result;
		}
		return MapUtils.newHashMap();
	}

	/**
	 * 按模型单价计算本次消耗金额（缓存命中部分按缓存价，未配置缓存价时按输入价）
	 * @param usage Token 用量
	 * @param price 单价配置
	 */
	protected BigDecimal calculateCost(Map<String, Object> usage, Map<String, Object> price) {
		if (usage == null || price == null || price.isEmpty()) {
			return null;
		}
		Integer promptTokens = getUsageTokens(usage, "prompt_tokens");
		Integer completionTokens = getUsageTokens(usage, "completion_tokens");
		if (promptTokens == null && completionTokens == null) {
			return null;
		}
		BigDecimal inputPrice = getPriceValue(price.get("input"));
		BigDecimal outputPrice = getPriceValue(price.get("output"));
		BigDecimal cachePrice = getPriceValue(price.get("cache"));
		if (cachePrice == null) {
			cachePrice = inputPrice;
		}
		// 未配置任何单价时不计算金额
		if ((inputPrice == null || inputPrice.signum() <= 0) && (outputPrice == null || outputPrice.signum() <= 0)
				&& (cachePrice == null || cachePrice.signum() <= 0)) {
			return null;
		}
		int prompt = promptTokens != null ? promptTokens : 0;
		int completion = completionTokens != null ? completionTokens : 0;
		Integer cacheTokens = getUsageCacheTokens(usage);
		int cache = cacheTokens != null ? Math.min(cacheTokens, prompt) : 0;
		BigDecimal cost = BigDecimal.valueOf(prompt - cache)
				.multiply(inputPrice != null ? inputPrice : BigDecimal.ZERO)
				.add(BigDecimal.valueOf(cache).multiply(cachePrice != null ? cachePrice : BigDecimal.ZERO))
				.add(BigDecimal.valueOf(completion).multiply(outputPrice != null ? outputPrice : BigDecimal.ZERO));
		return cost.divide(getPriceDivisor(price.get("unit")), 6, RoundingMode.HALF_UP);
	}

	/**
	 * 获取单价值（兼容数字与字符串）
	 * @param value 单价值
	 */
	protected BigDecimal getPriceValue(Object value) {
		if (value instanceof Number number) {
			return new BigDecimal(number.toString());
		}
		if (value != null && StringUtils.isNotBlank(ObjectUtils.toString(value))) {
			try {
				return new BigDecimal(ObjectUtils.toString(value).trim());
			} catch (NumberFormatException e) {
				logger.debug("解析模型单价失败：{}", value);
			}
		}
		return null;
	}

	/**
	 * 获取单价单位对应的除数（1K=1000、1M=1000000，默认按每个 Token 计价）
	 * @param unit 单价单位
	 */
	protected BigDecimal getPriceDivisor(Object unit) {
		String value = StringUtils.lowerCase(ObjectUtils.toString(unit));
		if (StringUtils.isBlank(value)) {
			return BigDecimal.ONE;
		}
		return switch (value.replace("_", "").trim()) {
			case "1k", "k", "thousand" -> BigDecimal.valueOf(1000);
			case "1m", "m", "million" -> BigDecimal.valueOf(1000000);
			default -> BigDecimal.ONE;
		};
	}

	/**
	 * 获取用量中的指定 Token 字段值
	 * @param usage Token 用量
	 * @param key 字段名
	 */
	protected Integer getUsageTokens(Map<String, Object> usage, String key) {
		if (usage == null) {
			return null;
		}
		Object value = usage.get(key);
		if (value instanceof Number number) {
			return number.intValue();
		}
		if (value != null) {
			try {
				return Integer.valueOf(String.valueOf(value));
			} catch (NumberFormatException e) {
				logger.debug("解析 Token 用量字段失败：{}={}", key, value);
			}
		}
		return null;
	}

	/**
	 * 获取输入中的缓存命中 Token 数（兼容各厂商原生字段）
	 * <ul>
	 *   <li>OpenAI 系：{@code prompt_tokens_details.cached_tokens}、{@code prompt_tokens_details.cache_read_tokens}</li>
	 *   <li>DeepSeek 系：{@code prompt_cache_hit_tokens}</li>
	 *   <li>Anthropic：{@code cache_read_input_tokens}、{@code cache_read_tokens}</li>
	 * </ul>
	 * @param usage Token 用量
	 */
	protected Integer getUsageCacheTokens(Map<String, Object> usage) {
		if (usage == null) {
			return null;
		}
		Object details = usage.get("prompt_tokens_details");
		if (details instanceof Map<?, ?> detailMap) {
			for (String key : new String[]{"cached_tokens", "cache_read_tokens"}) {
				Object value = detailMap.get(key);
				if (value instanceof Number number) {
					return number.intValue();
				}
			}
		}
		for (String key : new String[]{"prompt_cache_hit_tokens", "cache_read_input_tokens", "cache_read_tokens"}) {
			Object value = usage.get(key);
			if (value instanceof Number number) {
				return number.intValue();
			}
		}
		return null;
	}

	/**
	 * 获取厂商原生 Token 用量（通过反射调用，兼容不同版本的 Spring AI）
	 * @param usage Token 用量
	 */
	protected Object getNativeUsage(Usage usage) {
		try {
			return usage.getClass().getMethod("getNativeUsage").invoke(usage);
		} catch (Exception e) {
			return null;
		}
	}

	// ==================== 消息元数据读取 ====================

	/**
	 * 获取消息的 Token 用量（元数据 JSON）
	 * @param aiChatMessage 消息
	 */
	public Map<String, Object> getChatMessageUsage(AiChatMessage aiChatMessage) {
		Map<String, Object> metadata = getChatMessageMetadata(aiChatMessage);
		if (metadata == null) {
			return null;
		}
		// 新结构：{"usage":{...},"price":{...}}，取厂商原始用量返回给前端
		Object usage = metadata.get("usage");
		if (usage instanceof Map<?, ?> usageMap) {
			Map<String, Object> result = MapUtils.newHashMap();
			usageMap.forEach((key, value) -> result.put(String.valueOf(key), value));
			return result;
		}
		// 兼容早期直接平铺用量、且可能混入思考内容、文件列表、工具步骤的元数据
		metadata.remove(AiReasoningContent.REASONING_CONTENT);
		metadata.remove(METADATA_FILES);
		metadata.remove(METADATA_STEPS);
		metadata.remove(METADATA_TASKS);
		return metadata.isEmpty() ? null : metadata;
	}

	/**
	 * 获取消息单价快照中的币种（消息元数据 JSON，调用当时的价格配置）
	 * @param aiChatMessage 消息
	 */
	public String getChatMessageCurrency(AiChatMessage aiChatMessage) {
		Map<String, Object> metadata = getChatMessageMetadata(aiChatMessage);
		if (metadata == null) {
			return null;
		}
		Object price = metadata.get("price");
		if (price instanceof Map<?, ?> priceMap) {
			return StringUtils.trimToNull(ObjectUtils.toString(priceMap.get("currency")));
		}
		return null;
	}

	/**
	 * 获取消息的深度思考内容（消息的思考内容字段，兼容早期存于元数据的数据）
	 * @param aiChatMessage 消息
	 */
	public String getChatMessageReasoningContent(AiChatMessage aiChatMessage) {
		if (aiChatMessage == null) {
			return StringUtils.EMPTY;
		}
		if (StringUtils.isNotBlank(aiChatMessage.getMsgThink())) {
			return aiChatMessage.getMsgThink();
		}
		Map<String, Object> metadata = getChatMessageMetadata(aiChatMessage);
		if (metadata == null) {
			return StringUtils.EMPTY;
		}
		return StringUtils.defaultString(ObjectUtils.toString(metadata.get(AiReasoningContent.REASONING_CONTENT)));
	}

	/**
	 * 获取消息的元数据（JSON 转 Map）
	 * @param aiChatMessage 消息
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getChatMessageMetadata(AiChatMessage aiChatMessage) {
		if (aiChatMessage == null || StringUtils.isBlank(aiChatMessage.getMsgMetadata())) {
			return null;
		}
		try {
			return JsonMapper.getInstance().readValue(aiChatMessage.getMsgMetadata(), Map.class);
		} catch (Exception e) {
			logger.debug("解析消息元数据失败：{}", e.getMessage());
			return null;
		}
	}

}

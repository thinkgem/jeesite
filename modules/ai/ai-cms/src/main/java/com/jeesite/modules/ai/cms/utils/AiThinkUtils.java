/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.utils;

import com.jeesite.common.lang.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 深度思考（推理内容）工具类。
 * <p>
 * 各模型返回思考内容的原生字段名称并不统一，例如：
 * <ul>
 *   <li>OpenAI / DeepSeek / 智谱 / 硅基流动 / 阿里百炼：{@code reasoning_content}</li>
 *   <li>Ollama（qwen3、deepseek-r1 等）：{@code thinking}</li>
 *   <li>Anthropic Claude：{@code thinking} / {@code reasoning}</li>
 *   <li>部分模型：不返回独立字段，而是把思考内容写在正文的 &lt;think&gt;...&lt;/think&gt; 标签中</li>
 * </ul>
 * 本类负责把上述原生字段统一归一化到 {@link #REASONING_CONTENT}，
 * 存放在 {@code AssistantMessage.metadata} 中随流输出下发给前端，
 * 前端据此渲染「深度思考」折叠面板（同时仍兼容正文中的 &lt;think&gt; 标签）。
 *
 * @author ThinkGem
 */
public class AiThinkUtils {

	/**
	 * 归一化后的思考内容字段名，位于 AssistantMessage.metadata 中
	 */
	public static final String REASONING_CONTENT = "reasoningContent";

	/**
	 * 各模型的原生思考字段名称（按优先级排列）
	 */
	private static final List<String> THINKING_KEYS = List.of( //
			"reasoningContent", // Spring AI OpenAI 系（原始字段为 reasoning_content）
			"reasoning_content", // OpenAI 兼容原始字段（DeepSeek、智谱、硅基流动、百炼等）
			"thinkingContent", //
			"thinking_content", //
			"thinking", // Ollama、Anthropic Claude
			"reasoning" // Anthropic Claude、部分 OpenAI 兼容网关
	);

	/**
	 * 从消息中读取原生思考内容
	 * @author ThinkGem
	 */
	public static String getReasoningContent(AssistantMessage message) {
		if (message == null) {
			return StringUtils.EMPTY;
		}
		return getReasoningContent(message.getMetadata());
	}

	/**
	 * 按优先级从元数据中查找第一个非空的思考字段
	 * @author ThinkGem
	 */
	public static String getReasoningContent(Map<String, Object> metadata) {
		if (metadata == null || metadata.isEmpty()) {
			return StringUtils.EMPTY;
		}
		for (String key : THINKING_KEYS) {
			Object value = metadata.get(key);
			if (value instanceof String text && StringUtils.isNotEmpty(text)) {
				return text;
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 归一化思考字段：累积各模型的原生思考内容，并统一写入 AssistantMessage.metadata.reasoningContent。
	 * <p>
	 * 流输出时只下发本分片的<b>增量</b>：OpenAI 系每个分片返回的是完整累计值，若原样下发
	 * 会导致思考内容随每个分片重复传输（长度随累积线性增长，整体流量为 O(n²)），
	 * 因此这里截取新增部分，由前端自行累积拼接。
	 * <p>
	 * 同时会移除 metadata 中各模型的原生思考字段（reasoning_content / thinking 等），
	 * 只保留归一化后的 reasoningContent。因为 Ollama 等模型是逐分片增量下发 thinking 的，
	 * 最后一个分片往往只剩一个句号或换行之类的碎片，若不清理会随响应下发形成脏数据。
	 * <p>
	 * 注意：正文内容不做任何改写，若模型把思考内容写在 &lt;think&gt; 标签中，则由前端负责解析渲染，
	 * 这样原生思考字段也不会随正文回传给模型，避免污染会话上下文。
	 * @param response 模型原始响应
	 * @param reasoning 流输出过程中的思考内容累积器（每个订阅需新建，保证重试时重新开始）
	 * @return 归一化后的响应（本分片无思考内容增量时原样返回）
	 * @author ThinkGem
	 */
	public static ChatResponse normalize(ChatResponse response, StringBuilder reasoning) {
		if (response == null || response.getResult() == null) {
			return response;
		}
		Generation generation = response.getResult();
		AssistantMessage output = generation.getOutput();
		if (output == null) {
			return response;
		}
		String previous = reasoning.toString();
		// 累积后的完整思考内容，用于写入会话存储
		String merged = mergeReasoningContent(previous, getReasoningContent(output));
		// 本分片的增量，随流下发给前端
		String delta = deltaReasoningContent(previous, getReasoningContent(output));
		reasoning.setLength(0);
		reasoning.append(merged);
		if (StringUtils.isEmpty(delta) && !hasReasoningKey(output.getMetadata())) {
			return response;
		}
		List<Generation> generations = new ArrayList<>(response.getResults());
		generations.set(0, new Generation(newMessage(output, delta), generation.getMetadata()));
		return ChatResponse.builder() //
				.from(response) //
				.generations(List.copyOf(generations)) //
				.build();
	}

	/**
	 * 清理各模型的原生思考字段，仅保留归一化后的 reasoningContent。
	 * <p>
	 * 用于写入会话存储前整理 metadata，避免 Ollama 增量下发的 thinking 碎片残留。
	 * @param metadata 原始元数据
	 * @param reasoningContent 完整的思考内容，为空则只清理不写入
	 * @return 整理后的元数据
	 * @author ThinkGem
	 */
	public static Map<String, Object> cleanMetadata(Map<String, Object> metadata, String reasoningContent) {
		Map<String, Object> map = new LinkedHashMap<>(metadata);
		for (String key : THINKING_KEYS) {
			map.remove(key);
		}
		if (StringUtils.isNotEmpty(reasoningContent)) {
			map.put(REASONING_CONTENT, reasoningContent);
		}
		return map;
	}

	/**
	 * 元数据中是否存在任一模型的原生思考字段
	 * @author ThinkGem
	 */
	private static boolean hasReasoningKey(Map<String, Object> metadata) {
		if (metadata == null || metadata.isEmpty()) {
			return false;
		}
		for (String key : THINKING_KEYS) {
			if (metadata.containsKey(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 复制消息，写入归一化后的思考字段，并清理各模型的原生思考字段
	 * @author ThinkGem
	 */
	private static AssistantMessage newMessage(AssistantMessage output, String reasoning) {
		Map<String, Object> metadata = cleanMetadata(output.getMetadata(), reasoning);
		return AssistantMessage.builder() //
				.content(output.getText()) //
				.properties(metadata) //
				.media(output.getMedia()) //
				.toolCalls(output.getToolCalls()) //
				.build();
	}

	/**
	 * 累计思考内容：OpenAI 系模型每个分片返回的是完整累计值，Ollama 系返回的是增量值，
	 * 这里自动识别两种情况，始终返回累计后的完整思考内容。
	 * @param previous 已累计的思考内容
	 * @param current 当前分片中的思考内容
	 * @return 累计后的思考内容
	 * @author ThinkGem
	 */
	public static String mergeReasoningContent(String previous, String current) {
		if (StringUtils.isEmpty(current)) {
			return StringUtils.defaultString(previous);
		}
		if (StringUtils.isEmpty(previous)) {
			return current;
		}
		// 当前值以已累计值开头，说明是完整累计值（OpenAI 系），直接覆盖
		if (current.startsWith(previous)) {
			return current;
		}
		// 否则视为增量值（Ollama 系），追加
		return previous + current;
	}

	/**
	 * 取本分片的思考内容增量，用于流输出，避免把完整的思考内容随每个分片重复下发。
	 * @param previous 已累计的思考内容
	 * @param current 当前分片中的思考内容（OpenAI 系为完整累计值，Ollama 系为增量值）
	 * @return 本分片新增的思考内容
	 * @author ThinkGem
	 */
	public static String deltaReasoningContent(String previous, String current) {
		if (StringUtils.isEmpty(current)) {
			return StringUtils.EMPTY;
		}
		if (StringUtils.isEmpty(previous)) {
			return current;
		}
		// 当前值是完整累计值（OpenAI 系），截取新增部分
		if (current.startsWith(previous)) {
			return current.substring(previous.length());
		}
		// 当前值本就是增量值（Ollama 系），原样返回
		return current;
	}

}

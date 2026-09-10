/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.ai.cms.utils.AiThinkUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

/**
 * AI 聊天流输出响应对象（OpenAI v1 Chat Completions 流式兼容）。
 * <p>
 * 输出与 OpenAI v1 {@code /v1/chat/completions} 的 server-sent events 分片完全一致：
 * <pre>{@code
 * {
 *   "id": "nextId",
 *   "object": "chat.completion.chunk",
 *   "created": 1699000000,
 *   "model": "gpt-4o-mini",
 *   "choices": [
 *     { "index": 0, "delta": { "role": "assistant", "content": "...", "reasoning_content": "..." }, "finish_reason": null }
 *   ],
 *   "usage": { "prompt_tokens": 1, "completion_tokens": 2, "total_tokens": 3,
 *              "prompt_tokens_details": { "cached_tokens": 1 } }
 * }
 * }</pre>
 * 约定：
 * <ul>
 *   <li>首块 {@code delta} 携带 {@code role: "assistant"}；</li>
 *   <li>正文增量放 {@code delta.content}，思考增量放 {@code delta.reasoning_content}（DeepSeek 等兼容约定）；</li>
 *   <li>尾块 {@code finish_reason: "stop"} 并附 {@code usage}（仅尾块携带）；</li>
 *   <li>{@code content} 与 {@code reasoning_content} 均为本分片增量，由前端累积拼接，
 *       避免思考内容随每个分片重复下发（否则流量随累积长度呈 O(n²) 增长）。</li>
 * </ul>
 * 字段统一使用 snake_case，与 OpenAI 官方保持一致；前端可同时兼容本格式、Spring AI 原生
 * ChatResponse 以及历史轻量格式。
 *
 * @author ThinkGem
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AiChatCompletion implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 本次会话分片唯一 ID */
	private String id;

	/** 固定为 chat.completion.chunk */
	private String object = "chat.completion.chunk";

	/** Unix 时间戳（秒） */
	private Long created;

	/** 实际使用的模型名 */
	private String model;

	/** 选择项列表 */
	private List<Choice> choices;

	/** 用量统计（仅尾块携带） */
	private Usage usage;

	private AiChatCompletion() {
	}

	/**
	 * 将模型单分片响应转换为 OpenAI v1 流式分片
	 * @param first 是否为首块（首块 delta 携带 role）
	 * @param last 是否为尾块（尾块携带 finish_reason）
	 * @author ThinkGem
	 */
	public static AiChatCompletion of(ChatResponse response, boolean first, boolean last) {
		AiChatCompletion res = new AiChatCompletion();
		if (response == null) {
			return res;
		}
		Choice choice = new Choice();
		Delta delta = choice.getDelta();
		if (first) {
			delta.setRole("assistant");
		}
		Generation generation = response.getResult();
		if (generation != null && generation.getOutput() != null) {
			String text = generation.getOutput().getText();
			if (StringUtils.isNotEmpty(text)) {
				delta.setContent(text);
			}
			// 上游 AiThinkUtils.normalize 已把思考内容归一化为本分片增量，存于 metadata.reasoningContent
			String reasoning = AiThinkUtils.getReasoningContent(generation.getOutput());
			if (StringUtils.isNotEmpty(reasoning)) {
				delta.setReasoningContent(reasoning);
			}
		}
		if (last) {
			// 尾块补上结束原因，前端据此结束渲染（非尾块不输出 finish_reason）
			choice.setFinishReason("stop");
			// 本分片自带的 usage（OpenAI 开启 stream_usage 后最后一条即携带完整用量）一并下发，
			// 前端取最后一个非空分片；Usage.of 在无有效用量时返回 null，配合 NON_EMPTY 不输出空统计
			if (response.getMetadata() != null) {
				res.setUsage(Usage.of(response.getMetadata().getUsage()));
			}
		}
		res.setChoices(List.of(choice));
		return res;
	}

	/**
	 * 将 Spring AI 流输出整体转换为 OpenAI v1 流式分片
	 * <p>
	 * 会话级 id / created / model 必须跨分片保持一致，且首块要带 role、末块要带 finish_reason，
	 * 因此整条流需要用 defer 包裹：每次订阅（含重试）都生成独立的会话状态，避免复用旧值。
	 * <p>
	 * 流式响应无法预知最后一片，故暂存一片、待下一片到达时再下发，流结束时把最后一片标记为尾块下发，
	 * 保证尾块一定携带 {@code finish_reason: "stop"}（不依赖 provider 是否返回 usage）。
	 * @param source Spring AI 原始响应流
	 * @author ThinkGem
	 */
	public static Flux<AiChatCompletion> streamOf(Flux<ChatResponse> source) {
		return Flux.defer(() -> {
			// id / created 与首块内容无关，可直接生成；model 需等首个分片返回后才能确定
			String chunkId = IdGen.nextId();
			long createTime = System.currentTimeMillis() / 1000;
			AtomicReference<String> modelRef = new AtomicReference<>();
			// 暂存上一片响应与分片下标，用于判断首块、尾块
			AtomicReference<ChatResponse> pendingRef = new AtomicReference<>();
			AtomicLong indexRef = new AtomicLong();
			BiFunction<ChatResponse, Boolean, AiChatCompletion> toChunk = (response, last) -> {
				var metadata = response.getMetadata();
				if (metadata != null) {
					// 部分 provider 只在后续分片才返回 model，取首个非空值即可
					if (modelRef.get() == null) {
						modelRef.set(metadata.getModel());
					}
				}
				AiChatCompletion chunk = AiChatCompletion.of(response, indexRef.getAndIncrement() == 0, last);
				chunk.setId(chunkId);
				chunk.setCreated(createTime);
				chunk.setModel(modelRef.get());
				return chunk;
			};
			return source.concatMap(response -> {
				// 上一片此时才能确定不是尾块，直接下发；最后一片留到流结束后下发
				ChatResponse previous = pendingRef.getAndSet(response);
				return previous == null ? Flux.empty() : Flux.just(toChunk.apply(previous, false));
			}).concatWith(Flux.defer(() -> {
				ChatResponse previous = pendingRef.getAndSet(null);
				return previous == null ? Flux.empty() : Flux.just(toChunk.apply(previous, true));
			}));
		});
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	/**
	 * 选择项（OpenAI choices 元素）
	 * @author ThinkGem
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Choice implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 分片序号，始终为 0（需要常驻输出） */
		@JsonInclude(JsonInclude.Include.ALWAYS)
		private Integer index = 0;

		/** 增量内容 */
		private Delta delta = new Delta();

		/** 结束原因，仅尾块为 stop（NON_EMPTY：非尾块不输出） */
		@JsonProperty("finish_reason")
		private String finishReason;

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public Delta getDelta() {
			return delta;
		}

		public void setDelta(Delta delta) {
			this.delta = delta;
		}

		public String getFinishReason() {
			return finishReason;
		}

		public void setFinishReason(String finishReason) {
			this.finishReason = finishReason;
		}
	}

	/**
	 * 增量内容（OpenAI delta 对象）
	 * @author ThinkGem
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Delta implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 角色，仅首块出现 */
		private String role;

		/** 正文增量 */
		private String content;

		/** 思考内容增量（深度思考，DeepSeek 等 OpenAI 兼容约定） */
		@JsonProperty("reasoning_content")
		private String reasoningContent;

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getReasoningContent() {
			return reasoningContent;
		}

		public void setReasoningContent(String reasoningContent) {
			this.reasoningContent = reasoningContent;
		}
	}

	/**
	 * Token 用量统计（OpenAI usage 对象，snake_case）
	 * @author ThinkGem
	 */
	public static class Usage implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 提示词 Token 数 */
		@JsonProperty("prompt_tokens")
		private Integer promptTokens;

		/** 生成 Token 数 */
		@JsonProperty("completion_tokens")
		private Integer completionTokens;

		/** 总 Token 数 */
		@JsonProperty("total_tokens")
		private Integer totalTokens;

		/** 提示词 Token 明细（OpenAI 约定，含缓存命中 Token） */
		@JsonProperty("prompt_tokens_details")
		private PromptTokensDetails promptTokensDetails;

		public static Usage of(org.springframework.ai.chat.metadata.Usage usage) {
			if (usage == null) {
				return null;
			}
			Usage res = new Usage();
			res.promptTokens = usage.getPromptTokens();
			res.completionTokens = usage.getCompletionTokens();
			res.totalTokens = usage.getTotalTokens();
			// 缓存命中的提示词 Token（Spring AI 统一抽象，OpenAI 返回 prompt_tokens_details.cached_tokens）
			Long cachedTokens = usage.getCacheReadInputTokens();
			if (cachedTokens != null && cachedTokens > 0) {
				res.promptTokensDetails = new PromptTokensDetails(cachedTokens);
			}
			// 全部为空或 0 时视为无有效用量，返回 null，避免下发 {"prompt_tokens":0,...} 空统计
			if (isEmpty(res.promptTokens) && isEmpty(res.completionTokens) && isEmpty(res.totalTokens)
					&& res.promptTokensDetails == null) {
				return null;
			}
			return res;
		}

		private static boolean isEmpty(Integer value) {
			return value == null || value == 0;
		}

		public Integer getPromptTokens() {
			return promptTokens;
		}

		public void setPromptTokens(Integer promptTokens) {
			this.promptTokens = promptTokens;
		}

		public Integer getCompletionTokens() {
			return completionTokens;
		}

		public void setCompletionTokens(Integer completionTokens) {
			this.completionTokens = completionTokens;
		}

		public Integer getTotalTokens() {
			return totalTokens;
		}

		public void setTotalTokens(Integer totalTokens) {
			this.totalTokens = totalTokens;
		}

		public PromptTokensDetails getPromptTokensDetails() {
			return promptTokensDetails;
		}

		public void setPromptTokensDetails(PromptTokensDetails promptTokensDetails) {
			this.promptTokensDetails = promptTokensDetails;
		}
	}

	/**
	 * 提示词 Token 明细（OpenAI prompt_tokens_details 对象，snake_case）
	 * @author ThinkGem
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class PromptTokensDetails implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 命中提示词缓存的 Token 数（OpenAI cached_tokens） */
		@JsonProperty("cached_tokens")
		private Long cachedTokens;

		public PromptTokensDetails() {
		}

		public PromptTokensDetails(Long cachedTokens) {
			this.cachedTokens = cachedTokens;
		}

		public Long getCachedTokens() {
			return cachedTokens;
		}

		public void setCachedTokens(Long cachedTokens) {
			this.cachedTokens = cachedTokens;
		}
	}

}

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.entity;

import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * AI聊天消息内容 Entity
 * @author ThinkGem
 * @version 2026-11-12
 */
@Table(name = "${_prefix}ai_chat_message", alias = "a", label = "AI聊天消息内容", columns = {
		@Column(name = "id", attrName = "id", label = "编号", isPK = true),
		@Column(name = "conv_id", attrName = "convId", label = "对话编号"),
		@Column(name = "parent_id", attrName = "parentId", label = "回复的消息编号"),
		@Column(name = "msg_type", attrName = "msgType", label = "消息类型"),
		@Column(name = "model_code", attrName = "modelCode", label = "模型编码"),
		@Column(name = "msg_text", attrName = "msgText", label = "消息内容"),
		@Column(name = "msg_think", attrName = "msgThink", label = "思考内容"),
		@Column(name = "msg_metadata", attrName = "msgMetadata", label = "消息元数据"),
		@Column(name = "total_tokens", attrName = "totalTokens", label = "消耗Token数", isUpdateForce = true),
		@Column(name = "total_cost", attrName = "totalCost", label = "消耗金额"),
		@Column(includeEntity = DataEntity.class),
		@Column(includeEntity = BaseEntity.class),
	}, orderBy = "a.update_date DESC"
)
public class AiChatMessage extends DataEntity<AiChatMessage> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	protected String convId;		// 对话编号
	protected String parentId;		// 回复的消息编号
	protected String msgType;		// 消息类型
	protected String modelCode;		// 模型编码
	protected String msgText;		// 消息内容
	protected String msgFullText;	// 完整消息内容（含隐藏上下文，仅用于模型推理，不入库）
	protected String msgThink;		// 思考内容
	protected String msgMetadata;		// 消息元数据
	protected Integer totalTokens;		// 消耗Token数
	protected BigDecimal totalCost;		// 消耗金额

	public AiChatMessage() {
		this(null);
	}
	
	public AiChatMessage(String id){
		super(id);
	}
	
	@Size(min=0, max=64, message="对话编号长度不能超过 64 个字符")
	public String getConvId() {
		return convId;
	}

	public void setConvId(String convId) {
		this.convId = convId;
	}
	
	@Size(min=0, max=64, message="回复的消息编号长度不能超过 64 个字符")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Size(min=0, max=100, message="消息类型长度不能超过 100 个字符")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Size(min=0, max=64, message="模型编码长度不能超过 64 个字符")
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	
	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	/**
	 * 完整消息内容：含前端附加的隐藏上下文（如可填写表单的字段结构），仅用于模型推理，不入库。
	 * 消息内容 msgText 只保存用户原文。
	 */
	public String getMsgFullText() {
		return msgFullText;
	}

	public void setMsgFullText(String msgFullText) {
		this.msgFullText = msgFullText;
	}
	
	public String getMsgThink() {
		return msgThink;
	}

	public void setMsgThink(String msgThink) {
		this.msgThink = msgThink;
	}
	
	public String getMsgMetadata() {
		return msgMetadata;
	}

	public void setMsgMetadata(String msgMetadata) {
		this.msgMetadata = msgMetadata;
	}
	
	public Integer getTotalTokens() {
		return totalTokens;
	}

	public void setTotalTokens(Integer totalTokens) {
		this.totalTokens = totalTokens;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	
}

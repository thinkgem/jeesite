/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

import java.io.Serial;

/**
 * 智能体与向量库关联 Entity
 * <p>
 * 关联表为联合主键（agent_id、vector_biz_type），无独立主键与状态列。
 * @author ThinkGem
 * @version 2026-11-17
 */
@Table(name = "${_prefix}ai_agent_vector", alias = "a", label = "智能体与向量库关联表", columns = {
		@Column(name = "agent_id", attrName = "agentId", label = "智能体编号", isPK = true),
		@Column(name = "vector_biz_type", attrName = "vectorBizType", label = "向量库业务类型", isPK = true),
}, orderBy = "a.agent_id DESC, a.vector_biz_type DESC")
public class AiAgentVector extends DataEntity<AiAgentVector> {

	@Serial
	private static final long serialVersionUID = 1L;
	protected String agentId;		// 智能体编号
	protected String vectorBizType;		// 向量库业务类型（ai_vector_biz.biz_type）

	public AiAgentVector() {
		this(null, null);
	}

	public AiAgentVector(String agentId, String vectorBizType) {
		this.agentId = agentId;
		this.vectorBizType = vectorBizType;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getVectorBizType() {
		return vectorBizType;
	}

	public void setVectorBizType(String vectorBizType) {
		this.vectorBizType = vectorBizType;
	}

}

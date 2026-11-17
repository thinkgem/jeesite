/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.entity;


import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.io.Serial;

/**
 * 智能体与工具关联表 Entity
 * @author ThinkGem
 * @version 2026-11-16
 */
@Table(name = "${_prefix}ai_agent_tool", alias = "a", label = "智能体与工具关联表", columns = {
		@Column(name = "agent_id", attrName = "agentId", label = "智能体编号", isPK = true),
		@Column(name = "tool_code", attrName = "toolCode", label = "工具编码", isPK = true),
	}, orderBy = "a.agent_id DESC, a.tool_code DESC"
)
public class AiAgentTool extends DataEntity<AiAgentTool> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	protected String agentId;		// 智能体编号
	protected String toolCode;		// 工具编码

	public AiAgentTool() {
		this(null, null);
	}
	
	public AiAgentTool(String agentId, String toolCode){
		this.agentId = agentId;
		this.toolCode = toolCode;
	}
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}
	
}
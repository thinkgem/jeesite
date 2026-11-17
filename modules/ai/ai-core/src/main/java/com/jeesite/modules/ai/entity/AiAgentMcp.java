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
 * 智能体与MCP服务关联表 Entity
 * @author ThinkGem
 * @version 2026-11-16
 */
@Table(name = "${_prefix}ai_agent_mcp", alias = "a", label = "智能体与MCP服务关联表", columns = {
		@Column(name = "agent_id", attrName = "agentId", label = "智能体编号", isPK = true),
		@Column(name = "mcp_code", attrName = "mcpCode", label = "MCP编码", isPK = true),
	}, orderBy = "a.agent_id DESC, a.mcp_code DESC"
)
public class AiAgentMcp extends DataEntity<AiAgentMcp> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	protected String agentId;		// 智能体编号
	protected String mcpCode;		// MCP编码

	public AiAgentMcp() {
		this(null, null);
	}
	
	public AiAgentMcp(String agentId, String mcpCode){
		this.agentId = agentId;
		this.mcpCode = mcpCode;
	}
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	public String getMcpCode() {
		return mcpCode;
	}

	public void setMcpCode(String mcpCode) {
		this.mcpCode = mcpCode;
	}
	
}
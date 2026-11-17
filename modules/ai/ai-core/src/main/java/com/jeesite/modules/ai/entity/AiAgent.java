/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.entity;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.mybatis.type.JsonTypeHandler;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.util.Map;

/**
 * AI智能体 Entity
 * @author ThinkGem
 * @version 2025-07-04
 */
@Table(name = "${_prefix}ai_agent", alias = "a", label = "AI智能体信息", columns = {
		@Column(name = "id", attrName = "id", label = "编号", isPK = true),
		@Column(name = "agent_name", attrName = "agentName", label = "智能体名称", queryType = QueryType.LIKE),
		@Column(name = "agent_avatar", attrName = "agentAvatar", label = "智能体头像"),
		@Column(name = "agent_intro", attrName = "agentIntro", label = "智能体简介"),
		@Column(name = "model_code", attrName = "model.modelCode", label = "模型编码"),
		@Column(name = "system_prompt", attrName = "systemPrompt", label = "系统提示词"),
		@Column(name = "welcome", attrName = "welcome", label = "欢迎语"),
		@Column(name = "problem", attrName = "problem", label = "预设问题"),
		@Column(name = "shortcut", attrName = "shortcut", label = "快捷指令"),
		@Column(name = "placeholder", attrName = "placeholder", label = "输入框提示"),
		@Column(name = "options", attrName = "optionMap", label = "配置选项", comment = "配置选项（JSON）",
				isQuery = false, javaType = Map.class, typeHandler = JsonTypeHandler.class),
		@Column(name = "weight", attrName = "weight", label = "权重", isUpdateForce = true),
		@Column(includeEntity = DataEntity.class),
		@Column(includeEntity = BaseEntity.class),
}, joinTable = {
		@JoinTable(type = Type.LEFT_JOIN, entity = AiModel.class, attrName = "model", alias = "m",
			on = "m.model_code = a.model_code", columns = {
				@Column(name = "model_code", label = "模型编码", isPK = true),
				@Column(name = "model_name", label = "模型名称", isQuery = false),
				@Column(name = "model_desc", label = "模型描述", isQuery = false),
				@Column(name = "model_provider", label = "模型提供商", isQuery = false),
		}),
}, orderBy = "a.weight DESC, a.update_date DESC")
public class AiAgent extends DataEntity<AiAgent> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	protected String agentName;		// 智能体名称
	protected String agentAvatar;	// 智能体头像
	protected String agentIntro;	// 智能体简介
	protected AiModel model;		// 模型
	protected String systemPrompt;	// 系统提示词
	protected String welcome;		// 欢迎语
	protected String problem;		// 预设问题
	protected String shortcut;		// 快捷指令
	protected String placeholder;	// 输入框提示
	protected Map<String, Object> optionMap;	// 配置选项（JSON）
	protected Integer weight;			// 权重
	
	protected String toolCodes;			// 关联工具编码（非表字段，逗号分隔，存 ai_agent_tool）
	protected String mcpCodes;			// 关联MCP编码（非表字段，逗号分隔，存 ai_agent_mcp）
	protected String vectorBizTypes;	// 关联向量库业务类型（非表字段，逗号分隔，存 ai_agent_vector）
	protected String toolNames;			// 关联工具名称（非表字段，逗号分隔，仅用于表单回显）
	protected String mcpNames;			// 关联MCP名称（非表字段，逗号分隔，仅用于表单回显）
	protected String vectorBizNames;	// 关联向量库业务名称（非表字段，逗号分隔，仅用于表单回显）

	public AiAgent() {
		this(null);
	}
	
	public AiAgent(String id){
		super(id);
	}
	
	@Size(min=0, max=100, message="智能体名称长度不能超过 100 个字符")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	@Size(min=0, max=100, message="智能体头像长度不能超过 100 个字符")
	public String getAgentAvatar() {
		return agentAvatar;
	}

	public void setAgentAvatar(String agentAvatar) {
		this.agentAvatar = agentAvatar;
	}
	
	@Size(min=0, max=100, message="智能体简介长度不能超过 100 个字符")
	public String getAgentIntro() {
		return agentIntro;
	}

	public void setAgentIntro(String agentIntro) {
		this.agentIntro = agentIntro;
	}
	
	public AiModel getModel() {
		if (model == null){
			model = new AiModel();
		}
		return model;
	}

	public void setModel(AiModel model) {
		this.model = model;
	}
	
	public String getSystemPrompt() {
		return systemPrompt;
	}

	public void setSystemPrompt(String systemPrompt) {
		this.systemPrompt = systemPrompt;
	}
	
	@Size(min=0, max=200, message="欢迎语长度不能超过 200 个字符")
	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
	
	@Size(min=0, max=500, message="预设问题长度不能超过 500 个字符")
	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}
	
	@Size(min=0, max=500, message="快捷指令长度不能超过 500 个字符")
	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}
	
	@Size(min=0, max=200, message="输入框提示长度不能超过 200 个字符")
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	public Map<String, Object> getOptionMap() {
		if (this.optionMap == null){
			optionMap = MapUtils.newHashMap();
		}
		return optionMap;
	}

	public void setOptionMap(Map<String, Object> optionMap) {
		this.optionMap = optionMap;
	}
	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * 关联工具编码（来源 ai_agent_tool，逗号分隔，仅用于表单传递与回显）
	 */
	public String getToolCodes() {
		return toolCodes;
	}

	public void setToolCodes(String toolCodes) {
		this.toolCodes = toolCodes;
	}

	/**
	 * 关联 MCP 编码（来源 ai_agent_mcp，逗号分隔，仅用于表单传递与回显）
	 */
	public String getMcpCodes() {
		return mcpCodes;
	}

	public void setMcpCodes(String mcpCodes) {
		this.mcpCodes = mcpCodes;
	}

	/**
	 * 关联向量库业务类型（来源 ai_agent_vector，逗号分隔，仅用于表单传递与回显）
	 */
	public String getVectorBizTypes() {
		return vectorBizTypes;
	}

	public void setVectorBizTypes(String vectorBizTypes) {
		this.vectorBizTypes = vectorBizTypes;
	}

	/**
	 * 关联工具名称（逗号分隔，与 toolCodes 一一对应，仅用于表单回显）
	 */
	public String getToolNames() {
		return toolNames;
	}

	public void setToolNames(String toolNames) {
		this.toolNames = toolNames;
	}

	/**
	 * 关联 MCP 名称（逗号分隔，与 mcpCodes 一一对应，仅用于表单回显）
	 */
	public String getMcpNames() {
		return mcpNames;
	}

	public void setMcpNames(String mcpNames) {
		this.mcpNames = mcpNames;
	}

	/**
	 * 关联向量库业务名称（逗号分隔，与 vectorBizTypes 一一对应，仅用于表单回显）
	 */
	public String getVectorBizNames() {
		return vectorBizNames;
	}

	public void setVectorBizNames(String vectorBizNames) {
		this.vectorBizNames = vectorBizNames;
	}
	
}

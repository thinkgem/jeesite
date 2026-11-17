/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ai.dao.AiAgentMcpDao;
import com.jeesite.modules.ai.entity.AiAgentMcp;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 智能体与 MCP 服务关联 Service
 * <p>
 * 关联表为联合主键（agent_id、mcp_code），无独立主键与状态列，
 * 因此按 agentId 条件批量删除后重建，不使用 {@link #delete} 方法。
 * @author ThinkGem
 * @version 2026-11-16
 */
public class AiAgentMcpService extends CrudService<AiAgentMcpDao, AiAgentMcp> {

	/**
	 * 查询智能体关联的 MCP 编码
	 * @param agentId 智能体编号
	 */
	public List<String> findMcpCodes(String agentId) {
		List<String> list = ListUtils.newArrayList();
		if (StringUtils.isBlank(agentId)) {
			return list;
		}
		AiAgentMcp where = new AiAgentMcp();
		where.setAgentId(agentId);
		for (AiAgentMcp entity : findList(where)) {
			String mcpCode = StringUtils.trimToNull(entity.getMcpCode());
			if (mcpCode != null) {
				list.add(mcpCode);
			}
		}
		return list;
	}

	/**
	 * 保存智能体关联的 MCP 服务（先清空再写入）
	 * @param agentId 智能体编号
	 * @param mcpCodes MCP 编码集合，为空则仅清空
	 */
	@Transactional
	public void saveAgentMcp(String agentId, List<String> mcpCodes) {
		if (StringUtils.isBlank(agentId)) {
			return;
		}
		deleteByAgentId(agentId);
		if (ListUtils.isEmpty(mcpCodes)) {
			return;
		}
		for (String mcpCode : mcpCodes) {
			String code = StringUtils.trimToNull(mcpCode);
			if (code != null) {
				dao.insert(new AiAgentMcp(agentId, code));
			}
		}
	}

	/**
	 * 删除智能体关联的全部 MCP 服务
	 * @param agentId 智能体编号
	 */
	@Transactional
	public void deleteByAgentId(String agentId) {
		if (StringUtils.isBlank(agentId)) {
			return;
		}
		AiAgentMcp where = new AiAgentMcp();
		where.setAgentId(agentId);
		dao.deleteByEntity(where);
	}

}

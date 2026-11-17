/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ai.dao.AiAgentToolDao;
import com.jeesite.modules.ai.entity.AiAgentTool;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 智能体与工具关联 Service
 * <p>
 * 关联表为联合主键（agent_id、tool_code），无独立主键与状态列，
 * 因此按 agentId 条件批量删除后重建，不使用 {@link #delete} 方法。
 * @author ThinkGem
 * @version 2026-11-16
 */
public class AiAgentToolService extends CrudService<AiAgentToolDao, AiAgentTool> {

	/**
	 * 查询智能体关联的工具编码
	 * @param agentId 智能体编号
	 */
	public List<String> findToolCodes(String agentId) {
		List<String> list = ListUtils.newArrayList();
		if (StringUtils.isBlank(agentId)) {
			return list;
		}
		AiAgentTool where = new AiAgentTool();
		where.setAgentId(agentId);
		for (AiAgentTool entity : findList(where)) {
			String toolCode = StringUtils.trimToNull(entity.getToolCode());
			if (toolCode != null) {
				list.add(toolCode);
			}
		}
		return list;
	}

	/**
	 * 保存智能体关联的工具（先清空再写入）
	 * @param agentId 智能体编号
	 * @param toolCodes 工具编码集合，为空则仅清空
	 */
	@Transactional
	public void saveAgentTools(String agentId, List<String> toolCodes) {
		if (StringUtils.isBlank(agentId)) {
			return;
		}
		deleteByAgentId(agentId);
		if (ListUtils.isEmpty(toolCodes)) {
			return;
		}
		for (String toolCode : toolCodes) {
			String code = StringUtils.trimToNull(toolCode);
			if (code != null) {
				dao.insert(new AiAgentTool(agentId, code));
			}
		}
	}

	/**
	 * 删除智能体关联的全部工具
	 * @param agentId 智能体编号
	 */
	@Transactional
	public void deleteByAgentId(String agentId) {
		if (StringUtils.isBlank(agentId)) {
			return;
		}
		AiAgentTool where = new AiAgentTool();
		where.setAgentId(agentId);
		dao.deleteByEntity(where);
	}

}

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.ai.dao.AiAgentVectorDao;
import com.jeesite.modules.ai.entity.AiAgentVector;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 智能体与向量库关联 Service
 * <p>
 * 关联表为联合主键（agent_id、vector_biz_type），无独立主键与状态列，
 * 因此按 agentId 条件批量删除后重建，不使用 {@link #delete} 方法。
 * @author ThinkGem
 * @version 2026-11-17
 */
public class AiAgentVectorService extends CrudService<AiAgentVectorDao, AiAgentVector> {

	/**
	 * 查询智能体关联的向量库业务类型
	 * @param agentId 智能体编号
	 */
	public List<String> findVectorBizTypes(String agentId) {
		List<String> list = ListUtils.newArrayList();
		if (StringUtils.isBlank(agentId)) {
			return list;
		}
		AiAgentVector where = new AiAgentVector();
		where.setAgentId(agentId);
		for (AiAgentVector entity : findList(where)) {
			String vectorBizType = StringUtils.trimToNull(entity.getVectorBizType());
			if (vectorBizType != null) {
				list.add(vectorBizType);
			}
		}
		return list;
	}

	/**
	 * 保存智能体关联的向量库业务类型（先清空再写入）
	 * @param agentId 智能体编号
	 * @param vectorBizTypes 向量库业务类型集合，为空则仅清空
	 */
	@Transactional
	public void saveAgentVectors(String agentId, List<String> vectorBizTypes) {
		if (StringUtils.isBlank(agentId)) {
			return;
		}
		deleteByAgentId(agentId);
		if (ListUtils.isEmpty(vectorBizTypes)) {
			return;
		}
		for (String vectorBizType : vectorBizTypes) {
			String bizType = StringUtils.trimToNull(vectorBizType);
			if (bizType != null) {
				dao.insert(new AiAgentVector(agentId, bizType));
			}
		}
	}

	/**
	 * 删除智能体关联的全部向量库
	 * @param agentId 智能体编号
	 */
	@Transactional
	public void deleteByAgentId(String agentId) {
		if (StringUtils.isBlank(agentId)) {
			return;
		}
		AiAgentVector where = new AiAgentVector();
		where.setAgentId(agentId);
		dao.deleteByEntity(where);
	}

	/**
	 * 删除引用了指定业务类型的全部智能体关联（业务配置删除时调用）
	 * @param vectorBizType 向量库业务类型
	 */
	@Transactional
	public void deleteByVectorBizType(String vectorBizType) {
		if (StringUtils.isBlank(vectorBizType)) {
			return;
		}
		AiAgentVector where = new AiAgentVector();
		where.setVectorBizType(vectorBizType);
		dao.deleteByEntity(where);
	}

}

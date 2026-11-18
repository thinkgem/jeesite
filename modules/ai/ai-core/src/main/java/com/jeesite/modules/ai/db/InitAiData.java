/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.db;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.ai.entity.*;
import com.jeesite.modules.ai.service.*;
import com.jeesite.modules.gen.utils.GenUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 初始化 AI 表及数据
 * @author ThinkGem
 * @version 2026-11-18
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitAiData extends BaseInitDataTests {

	public InitAiData(AiModelService modelService, AiMcpService mcpService,
					  AiToolService toolService, AiVectorService vectorService,
					  AiVectorBizService vectorBizService, AiAgentService agentService,
					  AiAgentToolService agentToolService, AiAgentMcpService agentMcpService,
					  AiAgentVectorService agentVectorService) {
		this.modelService = modelService;
		this.mcpService = mcpService;
		this.toolService = toolService;
		this.vectorService = vectorService;
		this.vectorBizService = vectorBizService;
		this.agentService = agentService;
		this.agentToolService = agentToolService;
		this.agentMcpService = agentMcpService;
		this.agentVectorService = agentVectorService;
	}

	@Override
	public boolean initData() throws Exception {
		if (GenUtils.isTableExists(Global.getTablePrefix() + "ai_agent")) {
			return true; // 如果表已存在，则无需初始化
		}
		this.runCreateScript("ai.sql");
		this.initModuleInfo("ai");
		this.initModuleMenu("/ai/agent/list");
		this.initModuleDict("ai_model_provider");
		// 智能体依赖模型、向量库、工具、MCP 服务，按依赖顺序初始化
		this.initModel();
		this.initMcp();
		this.initTool();
		this.initVector();
		this.initVectorBiz();
		this.initAgent();
		this.initAgentTool();
		this.initAgentMcp();
		this.initAgentVector();
		return true;
	}

	private final AiModelService modelService;
	/**
	 * AI 模型表
	 */
	public void initModel() throws Exception {
//		clearTable(AiModel.class);
		initExcelData(AiModel.class, params -> {
			String action = (String)params[0];
			if("set".equals(action)){
				AiModel entity = (AiModel)params[1];
				if ("optionMap".equals(params[2])){
					entity.setOptionMap(parseOptionMap(params[3]));
					return true;
				}
				return null;
			}
			else if("save".equals(action)){
				AiModel entity = (AiModel)params[1];
				entity.setIsNewRecord(true);
				modelService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final AiMcpService mcpService;
	/**
	 * AI MCP 服务表
	 */
	public void initMcp() throws Exception {
//		clearTable(AiMcp.class);
		initExcelData(AiMcp.class, params -> {
			String action = (String)params[0];
			if("set".equals(action)){
				AiMcp entity = (AiMcp)params[1];
				if ("optionMap".equals(params[2])){
					entity.setOptionMap(parseOptionMap(params[3]));
					return true;
				}
				return null;
			}
			else if("save".equals(action)){
				AiMcp entity = (AiMcp)params[1];
				entity.setIsNewRecord(true);
				mcpService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final AiToolService toolService;
	/**
	 * AI 工具注册表
	 */
	public void initTool() throws Exception {
//		clearTable(AiTool.class);
		initExcelData(AiTool.class, params -> {
			String action = (String)params[0];
			if("set".equals(action)){
				AiTool entity = (AiTool)params[1];
				if ("optionMap".equals(params[2])){
					entity.setOptionMap(parseOptionMap(params[3]));
					return true;
				}
				return null;
			}
			else if("save".equals(action)){
				AiTool entity = (AiTool)params[1];
				entity.setIsNewRecord(true);
				toolService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final AiVectorService vectorService;
	/**
	 * AI 向量库表
	 */
	public void initVector() throws Exception {
//		clearTable(AiVector.class);
		initExcelData(AiVector.class, params -> {
			String action = (String)params[0];
			if("set".equals(action)){
				AiVector entity = (AiVector)params[1];
				if ("optionMap".equals(params[2])){
					entity.setOptionMap(parseOptionMap(params[3]));
					return true;
				}
				return null;
			}
			else if("save".equals(action)){
				AiVector entity = (AiVector)params[1];
				entity.setIsNewRecord(true);
				vectorService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final AiVectorBizService vectorBizService;
	/**
	 * AI 向量库业务配置表
	 */
	public void initVectorBiz() throws Exception {
//		clearTable(AiVectorBiz.class);
		initExcelData(AiVectorBiz.class, params -> {
			String action = (String)params[0];
			if("set".equals(action)){
				AiVectorBiz entity = (AiVectorBiz)params[1];
				if ("optionMap".equals(params[2])){
					entity.setOptionMap(parseOptionMap(params[3]));
					return true;
				}
				return null;
			}
			else if("save".equals(action)){
				AiVectorBiz entity = (AiVectorBiz)params[1];
				entity.setIsNewRecord(true);
				vectorBizService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final AiAgentService agentService;
	/**
	 * AI 智能体表（关联的工具、MCP、向量库见 initAgentTool、initAgentMcp、initAgentVector）
	 */
	public void initAgent() throws Exception {
//		clearTable(AiAgent.class);
		initExcelData(AiAgent.class, params -> {
			String action = (String)params[0];
			if("set".equals(action)){
				AiAgent entity = (AiAgent)params[1];
				String header = (String)params[2];
				Object val = params[3];
				// 模型编码为关联对象属性（model.modelCode），需转换后赋值
				if ("model.modelCode".equals(header)){
					entity.getModel().setModelCode(ObjectUtils.toString(val));
					return true;
				}
				else if ("optionMap".equals(header)){
					entity.setOptionMap(parseOptionMap(val));
					return true;
				}
				return null;
			}
			else if("save".equals(action)){
				AiAgent entity = (AiAgent)params[1];
				entity.setIsNewRecord(true);
				agentService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final AiAgentToolService agentToolService;
	/**
	 * AI 智能体与工具关联表（按智能体分组后重建关联）
	 */
	public void initAgentTool() throws Exception {
//		clearTable(AiAgentTool.class);
		Map<String, List<String>> agentToolMap = MapUtils.newHashMap();
		initExcelData(AiAgentTool.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				AiAgentTool entity = (AiAgentTool)params[1];
				agentToolMap.computeIfAbsent(entity.getAgentId(), k -> ListUtils.newArrayList())
						.add(entity.getToolCode());
				return null;
			}
			return null;
		});
		agentToolMap.forEach(agentToolService::saveAgentTools);
	}

	private final AiAgentMcpService agentMcpService;
	/**
	 * AI 智能体与 MCP 服务关联表（按智能体分组后重建关联）
	 */
	public void initAgentMcp() throws Exception {
//		clearTable(AiAgentMcp.class);
		Map<String, List<String>> agentMcpMap = MapUtils.newHashMap();
		initExcelData(AiAgentMcp.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				AiAgentMcp entity = (AiAgentMcp)params[1];
				agentMcpMap.computeIfAbsent(entity.getAgentId(), k -> ListUtils.newArrayList())
						.add(entity.getMcpCode());
				return null;
			}
			return null;
		});
		agentMcpMap.forEach(agentMcpService::saveAgentMcp);
	}

	private final AiAgentVectorService agentVectorService;
	/**
	 * AI 智能体与向量库业务关联表（按智能体分组后重建关联）
	 */
	public void initAgentVector() throws Exception {
//		clearTable(AiAgentVector.class);
		Map<String, List<String>> agentVectorMap = MapUtils.newHashMap();
		initExcelData(AiAgentVector.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				AiAgentVector entity = (AiAgentVector)params[1];
				agentVectorMap.computeIfAbsent(entity.getAgentId(), k -> ListUtils.newArrayList())
						.add(entity.getVectorBizType());
				return null;
			}
			return null;
		});
		agentVectorMap.forEach(agentVectorService::saveAgentVectors);
	}

	/**
	 * 解析 Excel 中的配置选项列（options），为 JSON 文本
	 */
	private Map<String, Object> parseOptionMap(Object val) {
		String json = ObjectUtils.toString(val);
		if (StringUtils.isBlank(json)) {
			return null;
		}
		return JsonMapper.fromJson(json, Map.class);
	}

}

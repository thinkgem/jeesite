/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { BasicModel, Page } from '@jeesite/core/api/model/baseModel';
import { AiModel } from '@jeesite/ai';

const { adminPath } = useGlobSetting();

export interface AiAgent extends BasicModel<AiAgent> {
  agentName?: string; // 智能体名称
  agentAvatar?: string; // 智能体头像
  agentIntro?: string; // 智能体简介
  model?: AiModel; // 模型（关联模型表）
  systemPrompt?: string; // 系统提示词
  welcome?: string; // 欢迎语
  problem?: string; // 预设问题
  shortcut?: string; // 快捷指令
  placeholder?: string; // 输入框提示
  optionMap?: any; // 配置选项（JSON）
  weight?: number; // 权重
  toolCodes?: string; // 关联工具编码（逗号分隔，存 ai_agent_tool）
  mcpCodes?: string; // 关联MCP编码（逗号分隔，存 ai_agent_mcp）
  vectorBizTypes?: string; // 关联向量库业务类型（逗号分隔，存 ai_agent_vector）
  toolNames?: string; // 关联工具名称（逗号分隔，表单回显用）
  mcpNames?: string; // 关联MCP名称（逗号分隔，表单回显用）
  vectorBizNames?: string; // 关联向量库业务名称（逗号分隔，表单回显用）
}

export const aiAgentList = (params?: AiAgent | any) =>
  defHttp.get<AiAgent>({ url: adminPath + '/ai/agent/list', params });

export const aiAgentListData = (params?: AiAgent | any) =>
  defHttp.post<Page<AiAgent>>({ url: adminPath + '/ai/agent/listData', params });

export const aiAgentForm = (params?: AiAgent | any) =>
  defHttp.get<AiAgent>({ url: adminPath + '/ai/agent/form', params });

export const aiAgentSave = (params?: any, data?: AiAgent | any) =>
  defHttp.postJson<AiAgent>({ url: adminPath + '/ai/agent/save', params, data });

export const aiAgentDisable = (params?: AiAgent | any) =>
  defHttp.get<AiAgent>({ url: adminPath + '/ai/agent/disable', params });

export const aiAgentEnable = (params?: AiAgent | any) =>
  defHttp.get<AiAgent>({ url: adminPath + '/ai/agent/enable', params });

export const aiAgentDelete = (params?: AiAgent | any) =>
  defHttp.get<AiAgent>({ url: adminPath + '/ai/agent/delete', params });

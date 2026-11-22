/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { AxiosProgressEvent, GenericAbortSignal } from 'axios';

const { adminPath } = useGlobSetting();

export const aiAgentChatMessage = (params?: Recordable | any) =>
  defHttp.get<Recordable[]>({ url: adminPath + '/ai/agentChat/message', params });

export const aiAgentChatList = (params?: Recordable | any) =>
  defHttp.get<Recordable[]>({ url: adminPath + '/ai/agentChat/list', params });

export const aiAgentChatSave = (params?: Recordable | any) =>
  defHttp.post<Recordable>({ url: adminPath + '/ai/agentChat/save', params });

export const aiAgentChatModelList = () => defHttp.get<Recordable[]>({ url: adminPath + '/ai/agentChat/modelList' });

export const aiAgentChatSaveModel = (params?: Recordable | any) =>
  defHttp.post<Recordable>({ url: adminPath + '/ai/agentChat/saveModel', params });

// 保存上传图片与聊天会话的关联关系，供后端多模态识图读取、以及切换会话时回显
export const aiAgentChatFileSave = (params?: Recordable | any) =>
  defHttp.post<Recordable>({ url: adminPath + '/ai/agentChat/file/save', params });

export const aiAgentChatDelete = (params?: Recordable | any) =>
  defHttp.get<Recordable>({ url: adminPath + '/ai/agentChat/delete', params });

export const aiAgentChatCompletions = (
  params?: Recordable | any,
  signal?: GenericAbortSignal,
  onDownloadProgress?: (progressEvent: AxiosProgressEvent) => void,
) =>
  defHttp.post<Recordable>({
    url: adminPath + '/ai/agentChat/completions',
    params,
    signal,
    onDownloadProgress,
    responseType: 'stream',
    // 流式响应可能长时间无结束（深度思考、工具调用、多轮 Loop），不能沿用默认超时
    timeout: 0,
    headers: {
      'x-ajax': 'event-stream',
    },
  });

// 工具执行确认：工具在后端流式响应的异步线程中挂起，本接口只负责唤醒，SSE 连接保持不中断
// 决策：once 本次运行、always 始终运行、deny 拒绝执行
export const aiAgentChatConfirm = (params?: Recordable | any) =>
  defHttp.post<Recordable>({ url: adminPath + '/ai/agentChat/confirm', params });

// 工具确认配置：后端是否开启执行前确认（ai.tool.confirmMode 不为 none 时前端才显示设置入口）
export const aiAgentChatToolConfirmConfig = () =>
  defHttp.get<Recordable>({ url: adminPath + '/ai/agentChat/toolConfirm/config' });

// 当前用户已允许「始终运行」的工具编码（保存在服务端内存，重启后清空）
export const aiAgentChatToolConfirmList = () =>
  defHttp.get<string[]>({ url: adminPath + '/ai/agentChat/toolConfirm/list' });

// 撤销某个工具的「始终运行」，恢复为执行前确认
export const aiAgentChatToolConfirmRemove = (params?: Recordable | any) =>
  defHttp.post<Recordable>({ url: adminPath + '/ai/agentChat/toolConfirm/remove', params });

// 撤销当前用户全部「始终运行」
export const aiAgentChatToolConfirmClear = () =>
  defHttp.post<Recordable>({ url: adminPath + '/ai/agentChat/toolConfirm/clear' });

// 重新生成回复：删除该回复及其之后的对话，基于其父用户消息及之前的历史重新回答
// messageId 为空时，重新生成最后一条助手回复
export const aiAgentChatRegenerate = (
  params?: Recordable | any,
  signal?: GenericAbortSignal,
  onDownloadProgress?: (progressEvent: AxiosProgressEvent) => void,
) =>
  defHttp.post<Recordable>({
    url: adminPath + '/ai/agentChat/regenerate',
    params,
    signal,
    onDownloadProgress,
    responseType: 'stream',
    // 流式响应可能长时间无结束（深度思考、工具调用、多轮 Loop），不能沿用默认超时
    timeout: 0,
    headers: {
      'x-ajax': 'event-stream',
    },
  });

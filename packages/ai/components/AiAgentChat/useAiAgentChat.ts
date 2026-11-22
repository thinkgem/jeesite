/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { nextTick, onMounted, reactive } from 'vue';
import { useRoute } from 'vue-router';

import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { useMessage } from '@jeesite/core/hooks/web/useMessage';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';
import { router } from '@jeesite/core/router';

import defaultAgentAvatar from '@jeesite/assets/images/logo.png';

import { aiAgentListData } from '@jeesite/ai/api/ai/agent';
import {
  aiAgentChatConfirm,
  aiAgentChatDelete,
  aiAgentChatFileSave,
  aiAgentChatList,
  aiAgentChatMessage,
  aiAgentChatModelList,
  aiAgentChatSave,
  aiAgentChatSaveModel,
  aiAgentChatToolConfirmClear,
  aiAgentChatToolConfirmConfig,
  aiAgentChatToolConfirmList,
  aiAgentChatToolConfirmRemove,
} from '@jeesite/ai/api/ai/agentChat';

// 会话置顶权重
export const TOP_WEIGHT = 9999;

export interface UseAiAgentChatOptions {
  // 指定智能体（如路由参数中的 id）
  agentId?: string;
  // 抽屉紧凑模式：不做路由同步等页面级行为
  compact?: boolean;
  // 切换智能体时是否同步到路由参数（页面模式）
  syncRoute?: boolean;
  // 是否在 onMounted 时自动初始化智能体与会话
  autoInit?: boolean;
}

/**
 * AI 智能体对话的公共逻辑（页面与抽屉助手共用）
 *
 * - 智能体列表、模型、会话列表、消息、图片上传、工具确认、重新生成等状态与动作；
 * - 通过 props 传递给 AiChatHeader、AiChatSidebar、AiChatMain 等视图组件。
 */
export function useAiAgentChat(options: UseAiAgentChatOptions = {}) {
  const { t } = useI18n('ai.chat');
  const { showMessage, createConfirm } = useMessage();
  const { ctxPath } = useGlobSetting();
  const route = options.syncRoute ? useRoute() : null;

  // ChatMessage 组件实例：由视图组件通过 setMessageApi 注入，避免放进响应式对象
  let messageApi: any = null;
  let ensureConversationTask: Promise<string> | null = null;

  const chat = reactive({
    // ============ 状态 ============
    compact: !!options.compact,
    agentList: [] as Recordable[],
    agentId: options.agentId as string | undefined,
    modelList: [] as Recordable[],
    modelCode: undefined as string | undefined,
    chatList: [] as Recordable[],
    conversationIds: [] as string[],
    conversationTitle: '',
    messages: [] as Recordable[],
    loading: false,
    inputText: '',
    sendBtnHover: false,
    confirmLoadingStepId: '',
    askAnswerLoading: false,
    toolConfirmEnabled: false,
    toolConfirmOpen: false,
    toolConfirmLoading: false,
    alwaysAllowTools: [] as string[],
    uploadDataMap: {} as Recordable,
    uploadFileList: [] as Recordable[],
    uploadLoadTime: 0,
    shortcutValue: null as string | null,
    // 随消息附加给模型的隐藏上下文（如当前可填写表单的字段结构），不显示在用户消息气泡中
    formContext: '',

    // ============ 计算属性 ============
    get conversationId(): string | undefined {
      return this.conversationIds[0];
    },

    get agentOptions() {
      return this.agentList.map((item) => ({ label: item.agentName, value: item.id }));
    },

    get currentAgent(): Recordable | undefined {
      return this.agentList.find((item) => item.id === this.agentId);
    },

    get currentAgentAvatar(): string {
      const avatar = this.currentAgent?.agentAvatar;
      return avatar ? this.getImageUrl({ fileUrl: avatar }) : defaultAgentAvatar;
    },

    get modelOptions() {
      const options = this.modelList.map((item) => ({
        label: item.modelName || item.modelDesc || item.modelCode,
        value: item.modelCode,
      }));
      // 当前模型不在可用列表（如已停用）时补一项，避免下拉直接回显模型编码
      const model = this.currentAgent?.model;
      if (model?.modelCode && !options.some((item) => item.value === model.modelCode)) {
        options.unshift({
          label: model.modelName || model.modelDesc || model.modelCode,
          value: model.modelCode,
        });
      }
      return options;
    },

    // 预设问题：支持换行分隔
    get problems(): string[] {
      return (this.currentAgent?.problem || '')
        .split(/[\r\n]/)
        .map((item: string) => item.trim())
        .filter(Boolean);
    },

    // 快捷指令：输入框工具栏的下拉指令模板
    get shortcutOptions() {
      return (this.currentAgent?.shortcut || '')
        .split(/[\r\n]/)
        .map((item: string) => item.trim())
        .filter(Boolean)
        .map((item: string) => ({ label: item, value: item }));
    },

    get hasMessages(): boolean {
      return this.messages.length > 0;
    },

    // ============ 方法 ============
    setMessageApi(api: any) {
      messageApi = api;
    },

    // 滚动消息列表到底部
    scrollMessages() {
      messageApi?.scrollBottom?.();
    },

    // 初始化：智能体、模型、当前智能体的会话列表
    async init(agentId?: string) {
      const [agentRes, modelRes] = await Promise.all([
        aiAgentListData().catch(() => null),
        // 模型列表接口异常时不阻塞聊天
        aiAgentChatModelList().catch(() => []),
      ]);
      this.agentList = agentRes?.list || [];
      this.modelList = modelRes || [];
      await this.selectAgent(agentId ?? this.agentId);
      // 工具确认配置：不阻塞聊天，加载失败时按未开启处理（不显示设置入口）
      this.loadToolConfirmConfig();
    },

    // 重新加载智能体列表（编辑智能体后同步）
    async refreshAgents() {
      const res = await aiAgentListData();
      this.agentList = res?.list || [];
      this.modelCode = this.currentAgent?.model?.modelCode;
    },

    // 选中智能体并加载其会话列表
    async selectAgent(agentId?: string) {
      if (agentId && this.agentList.some((item) => item.id === agentId)) {
        this.agentId = agentId;
      } else if (this.agentList.length > 0) {
        this.agentId = this.agentList[0].id;
      }
      this.modelCode = this.currentAgent?.model?.modelCode;
      if (!this.agentId) {
        return;
      }
      this.conversationIds = [];
      this.conversationTitle = '';
      this.messages = [];
      this.uploadFileList = [];
      this.uploadDataMap = {};
      await this.loadChatList();
    },

    async loadChatList() {
      this.chatList = (await aiAgentChatList({ agentId: this.agentId }).catch(() => [])) || [];
    },

    // 切换智能体：清空当前会话
    async handleAgentChange() {
      this.conversationIds = [];
      this.conversationTitle = '';
      this.messages = [];
      this.uploadFileList = [];
      this.uploadDataMap = {};
      this.modelCode = this.currentAgent?.model?.modelCode;
      await this.loadChatList();
      // 同步路由参数，刷新后仍停留在当前智能体
      if (route && this.agentId) {
        router.replace({ path: route.path, query: { ...route.query, id: this.agentId } });
      }
    },

    // 切换模型并保存到智能体
    async handleModelChange() {
      if (!this.agentId || !this.modelCode) {
        return;
      }
      const oldCode = this.currentAgent?.model?.modelCode;
      try {
        const res = await aiAgentChatSaveModel({ id: this.agentId, modelCode: this.modelCode });
        const agent = this.currentAgent;
        if (agent) {
          agent.model = { ...(agent.model || {}), modelCode: this.modelCode };
        }
        showMessage(res?.message || t('模型已切换'));
      } catch {
        // 保存失败时回滚到原模型，避免界面与智能体配置不一致
        this.modelCode = oldCode;
      }
    },

    // 新建对话
    async handleAdd() {
      if (!this.agentId) {
        showMessage(t('请先选择智能体'));
        return;
      }
      // 已有空会话时不再重复创建，避免产生多个空对话
      const latest = this.chatList[0];
      if (latest) {
        const list = (await aiAgentChatMessage({ conversationId: latest.id }).catch(() => [])) || [];
        if (list.length == 0) {
          this.selectConversation(latest);
          showMessage(t('当前已是新对话'));
          return;
        }
      }
      const res = await aiAgentChatSave({ agentId: this.agentId, chatTitle: t('新对话') });
      this.chatList.unshift(res);
      this.selectConversation(res);
    },

    // 确保存在会话（首次提问、图片上传等都需要会话编号），并发调用复用同一个创建过程
    async ensureConversation(): Promise<string> {
      if (this.conversationId) {
        return this.conversationId;
      }
      if (ensureConversationTask) {
        return ensureConversationTask;
      }
      ensureConversationTask = (async () => {
        if (!this.agentId) {
          showMessage(t('请先选择智能体'));
          return '';
        }
        // 复用最近的空会话，避免产生多个空对话
        const latest = this.chatList[0];
        if (latest) {
          const list = (await aiAgentChatMessage({ conversationId: latest.id }).catch(() => [])) || [];
          if (list.length == 0) {
            this.selectConversation(latest);
            return this.conversationId || '';
          }
        }
        const res = await aiAgentChatSave({ agentId: this.agentId, chatTitle: t('新对话') });
        this.chatList.unshift(res);
        this.selectConversation(res);
        return this.conversationId || '';
      })().finally(() => {
        ensureConversationTask = null;
      });
      return ensureConversationTask;
    },

    // 只切换会话标识、清空消息与图片预览，不加载历史消息
    selectConversation(item: Recordable) {
      this.conversationIds = [item.id];
      this.conversationTitle = item.chatTitle;
      this.uploadFileList = [];
      this.uploadDataMap = {};
      this.messages = [];
    },

    // 切换会话并加载历史消息
    async handleSelect(item: Recordable) {
      this.selectConversation(item);
      const list = (await aiAgentChatMessage({ conversationId: item.id }).catch(() => [])) || [];
      // 历史消息兜底：后端已裁剪隐藏上下文，这里再剥一次，避免旧数据把标记显示出来
      this.messages = list.map((msg: Recordable) =>
        msg?.messageType == 'USER' && typeof msg.text === 'string' ? { ...msg, text: msg.text } : msg,
      );
    },

    // 按会话编号切换（下拉选择）
    async handleSelectById(id: string) {
      const item = this.chatList.find((chatItem) => chatItem.id === id);
      if (item) {
        await this.handleSelect(item);
      }
    },

    // 置顶 / 取消置顶会话
    async handleTop(item: Recordable) {
      if (this.loading) {
        return;
      }
      const weight = item.weight == TOP_WEIGHT ? 0 : TOP_WEIGHT;
      await aiAgentChatSave({ id: item.id, weight, agentId: this.agentId });
      await this.loadChatList();
    },

    // 会话标题改名（edit 为 true 时进入编辑态，由视图聚焦输入框）
    async handleEdit(item: Recordable, edit: boolean) {
      if (this.loading) {
        return;
      }
      item.edit = edit;
      if (!edit && item.chatTitle !== item.oldTitle) {
        delete item.oldTitle;
        await aiAgentChatSave({ id: item.id, chatTitle: item.chatTitle, agentId: this.agentId });
      }
      item.oldTitle = item.chatTitle;
    },

    async handleDelete(item: Recordable, index: number) {
      const res = await aiAgentChatDelete({ id: item.id });
      this.chatList.splice(index, 1);
      showMessage(res?.message);
      this.conversationIds = [''];
      this.conversationTitle = '';
      this.messages = [];
      this.uploadFileList = [];
      this.uploadDataMap = {};
    },

    // 图片地址：上传组件返回的是相对地址，需拼接应用上下文路径
    getImageUrl(file: Recordable) {
      const url = file?.fileUrl || '';
      if (!url || url.startsWith('data:') || url.indexOf('://') > -1) {
        return url;
      }
      return ctxPath + url;
    },

    // 上传图片前，若还没有会话则先创建，保证图片关联到会话ID
    async handleUploadClick() {
      await this.ensureConversation();
    },

    // 删除本地预览的图片（同时解除与会话的关联）
    async handleRemoveUpload(file: Recordable) {
      const conversationId = this.conversationId;
      if (conversationId && file.id) {
        await aiAgentChatFileSave({ id: conversationId, delFileUploadIds: file.id });
      }
      this.uploadFileList = this.uploadFileList.filter((item) => item.id !== file.id);
      this.uploadLoadTime = new Date().getTime();
    },

    // 上传组件内删除图片时，同步移除本地预览
    handleUploadDelete(record: Recordable) {
      this.uploadFileList = this.uploadFileList.filter((item) => item.id !== record.id);
    },

    // 上传图片保存后，将图片与当前会话建立关联，供后端多模态识图读取、切换会话时回显
    async handleUploadChange(dataMap: Recordable, records: Recordable[] = []) {
      const fileList = (records || []).filter((item) => !!item);
      const ids = fileList.map((item) => item.id).filter(Boolean);
      const prevIds = this.uploadFileList.filter((item) => !!item).map((item) => item.id);
      this.uploadFileList = fileList;
      const conversationId = this.conversationId;
      if (!conversationId) {
        return;
      }
      // 本次删除了图片：解除图片与会话的关联
      const delFileUploadIds = dataMap?.['ai-agent-chat__del'];
      if (delFileUploadIds) {
        dataMap['ai-agent-chat__del'] = '';
        await aiAgentChatFileSave({ id: conversationId, delFileUploadIds });
        return;
      }
      // 本次新增了图片：建立图片与会话的关联
      if (ids.length > prevIds.length && prevIds.every((id) => ids.includes(id))) {
        await aiAgentChatFileSave({ id: conversationId, fileUploadIds: ids.join(',') });
      }
    },

    // 快捷指令选中：填入输入框，不直接发送
    handleShortcutChange(value: string) {
      this.shortcutValue = value;
      this.inputText = value || '';
    },

    handleEnter(event: KeyboardEvent) {
      if (event.key === 'Enter' && !event.shiftKey && !event.isComposing) {
        event.preventDefault();
        if (!this.loading) {
          this.handleSend();
        }
      }
    },

    handleSendOrStop() {
      if (this.loading) {
        messageApi?.stopLoading?.();
        return;
      }
      this.handleSend();
    },

    // 直接发送一段文本（预设问题、表单填充提示词等）
    async sendText(text: string) {
      this.inputText = text;
      await this.handleSend();
    },

    async handleSend() {
      const message = (this.inputText || '').trim();
      if (!message) {
        showMessage(t('请填写你的问题'));
        return;
      }
      if (this.loading) {
        return;
      }
      if (!this.agentId) {
        showMessage(t('请先选择智能体'));
        return;
      }
      // 首次提问：静默准备会话（复用最近的空会话或新建），不加载历史消息
      if (!this.conversationId) {
        await this.ensureConversation();
      }
      if (!this.conversationId) {
        return;
      }
      this.loading = true;
      // 本次提交的图片：随消息一起显示在对话中，提交后清空
      const fileList = this.uploadFileList
        .filter((item) => !!item)
        .map((item) => ({ id: item.id, fileName: item.fileName, fileUrl: item.fileUrl }));
      const params = {
        id: this.conversationId,
        agentId: this.agentId,
        message,
        fileList,
        // 隐藏上下文：让模型一次对话就按可填写表单的字段输出，避免再多问一轮
        context: this.formContext || undefined,
      };
      this.inputText = '';
      try {
        // 首条消息时，用消息内容作为会话标题
        this.chatList
          .filter((item) => item.id == params.id)
          .forEach((item) => {
            if ((item.chatTitle || '').startsWith(t('新对话'))) {
              item.chatTitle = message.substring(0, 30);
              aiAgentChatSave({ id: item.id, chatTitle: item.chatTitle, agentId: this.agentId });
            }
          });
        // 不等待输出：消息已显示在对话中，立即清空输入框上的附件
        const sendTask = messageApi?.sendMessage?.(params);
        this.uploadFileList = [];
        this.uploadDataMap = {};
        await sendTask;
      } finally {
        this.loading = false;
        // 输出完成后解除图片与会话的关联，避免下一条消息再次携带
        await this.clearSentFiles(fileList);
      }
    },

    // 清空已提交的图片：解除与会话的关联，并刷新上传组件
    async clearSentFiles(fileList: Recordable[]) {
      this.uploadFileList = [];
      this.uploadDataMap = {};
      const conversationId = this.conversationId;
      const ids = (fileList || [])
        .filter((item) => !!item)
        .map((item) => item.id)
        .filter(Boolean);
      try {
        if (conversationId && ids.length > 0) {
          await aiAgentChatFileSave({ id: conversationId, delFileUploadIds: ids.join(',') });
        }
      } finally {
        // 即使解绑失败也刷新，服务端为准
        this.uploadLoadTime = new Date().getTime();
      }
    },

    // 重新生成回复：仅助手消息可重新生成；非最后一条时会删除其后的对话，需用户确认
    async handleRegenerate(data: Recordable) {
      if (this.loading || !this.agentId || !this.conversationId) {
        return;
      }
      let message = data?.value;
      let index = Number(data?.index ?? -1);
      if (!(index >= 0)) {
        return;
      }
      // 新生成的回复本地没有编号，先从服务端刷新一次，确保能精确指定要重新生成的回复
      if (message && !message.id) {
        const list = (await aiAgentChatMessage({ conversationId: this.conversationId }).catch(() => [])) || [];
        if (list[index]?.messageType != 'ASSISTANT') {
          index = list.map((item) => item.messageType).lastIndexOf('ASSISTANT');
        }
        this.messages = list;
        message = this.messages[index];
        // 等待消息列表同步到聊天组件后再触发，避免组件内仍持有旧数组
        await nextTick();
      }
      if (!message || message.messageType != 'ASSISTANT' || !(index >= 0)) {
        return;
      }
      const params = { id: this.conversationId, agentId: this.agentId, messageId: message.id, index };
      // 重新生成中间回复会丢弃其后的所有对话，需用户确认
      if (index < this.messages.length - 1) {
        createConfirm({
          iconType: 'warning',
          title: t('重新生成'),
          content: t('重新生成该回复将删除其后的对话，是否继续？'),
          // 高于 AI 浮窗（1010），保证确认框不被浮窗遮挡
          zIndex: 2100,
          onOk: () => {
            this.doRegenerate(params);
          },
        });
        return;
      }
      await this.doRegenerate(params);
    },

    async doRegenerate(params: Recordable) {
      this.loading = true;
      try {
        await messageApi?.regenerateMessage?.(params);
      } finally {
        this.loading = false;
      }
    },

    /**
     * 工具执行确认
     * 工具在后端流式响应的异步线程中挂起，本接口只负责唤醒，SSE 连接不中断，
     * 后续事件继续由原流式请求推送，前端无需重新发起对话
     */
    async handleToolConfirm(data: Recordable) {
      const step = data?.step;
      const decision = data?.decision;
      const index = Number(data?.index ?? -1);
      const message = index >= 0 ? this.messages[index] : undefined;
      if (!step || !decision || !message || this.confirmLoadingStepId) {
        return;
      }
      this.confirmLoadingStepId = step.stepId;
      try {
        await aiAgentChatConfirm({ runId: message.runId, toolCallId: step.toolCallId, decision });
        // 回写状态：拒绝置为已拒绝，其余回到运行中等待工具结果
        step.status = decision === 'deny' ? 'rejected' : 'running';
        step.endedAt = decision === 'deny' ? Date.now() : undefined;
      } catch {
        // 提交失败回滚为待确认，允许用户重新选择
        step.status = 'waiting_confirm';
        message.status = 'waiting_confirm';
        showMessage(t('确认失败，请稍后重试'));
      } finally {
        this.confirmLoadingStepId = '';
      }
    },

    // 任务方向确认：提交用户选择，后端唤醒等待中的 ask_user 工具
    async handleAskAnswer(data: Recordable) {
      const ask = data?.ask;
      const answer = data?.answer;
      if (!ask || !answer || this.askAnswerLoading) {
        return;
      }
      this.askAnswerLoading = true;
      try {
        await aiAgentChatConfirm({ runId: ask.runId, toolCallId: ask.questionId, decision: answer });
        // 提交成功后移除卡片，避免用户重复提交
        const index = Number(data?.index ?? -1);
        const message = index >= 0 ? this.messages[index] : undefined;
        if (message) {
          message.askUser = undefined;
        }
      } catch {
        showMessage(t('提交失败，请稍后重试'));
      } finally {
        this.askAnswerLoading = false;
      }
    },

    // 工具确认配置：后端开启执行前确认（ai.tool.confirmMode 不为 none）时才显示设置入口
    async loadToolConfirmConfig() {
      try {
        const res = await aiAgentChatToolConfirmConfig();
        this.toolConfirmEnabled = !!res?.confirmEnabled;
      } catch {
        this.toolConfirmEnabled = false;
      }
    },

    // 工具确认设置：列出当前用户已允许「始终运行」的工具
    async handleToolConfirmSetting() {
      this.toolConfirmOpen = true;
      await this.loadAlwaysAllowTools();
    },

    async loadAlwaysAllowTools() {
      this.toolConfirmLoading = true;
      try {
        this.alwaysAllowTools = (await aiAgentChatToolConfirmList()) || [];
      } catch {
        this.alwaysAllowTools = [];
      } finally {
        this.toolConfirmLoading = false;
      }
    },

    // 撤销单个工具的「始终运行」，下次执行前重新询问
    async handleRemoveAlwaysAllow(toolCode: string) {
      try {
        await aiAgentChatToolConfirmRemove({ toolCode });
        showMessage(t('已恢复执行前确认'));
        await this.loadAlwaysAllowTools();
      } catch {
        showMessage(t('操作失败，请稍后重试'));
      }
    },

    // 撤销当前用户全部「始终运行」
    async handleClearAlwaysAllow() {
      try {
        await aiAgentChatToolConfirmClear();
        showMessage(t('已全部恢复执行前确认'));
        await this.loadAlwaysAllowTools();
      } catch {
        showMessage(t('操作失败，请稍后重试'));
      }
    },
  });

  if (options.autoInit !== false) {
    onMounted(() => {
      chat.init();
    });
  }

  return chat;
}

// 对话上下文类型：用于 AiChatHeader、AiChatSidebar、AiChatMain 的 props
export type AiAgentChatContext = ReturnType<typeof useAiAgentChat>;

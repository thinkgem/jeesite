<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="flex items-center min-w-0 pl-3 py-2 min-h-12">
    <Avatar :src="chat.currentAgentAvatar" :size="32" shape="circle" class="bg-white shrink-0" />
    <Select
      v-model:value="chat.agentId"
      class="jeesite-ai-chat-agent shrink-0"
      :options="chat.agentOptions"
      :disabled="chat.loading"
      :placeholder="t('选择智能体')"
      :title="t('选择智能体')"
      variant="borderless"
      :popupMatchSelectWidth="false"
      @change="chat.handleAgentChange()"
    />
    <div class="ml-2 mr-10 text-sm c-gray-5 truncate">
      {{ chat.currentAgent?.agentIntro || t('暂无简介') }}
    </div>
    <!-- 工具确认设置：后端开启执行前确认（confirmMode）时才显示 -->
    <Button
      v-if="chat.toolConfirmEnabled"
      class="ml-auto shrink-0"
      type="text"
      size="small"
      :title="t('工具确认设置')"
      :disabled="!chat.agentId"
      @click="chat.handleToolConfirmSetting()"
    >
      <Icon icon="i-ant-design:safety-certificate-outlined" />
    </Button>
    <!-- 编辑当前智能体 -->
    <Button
      class="shrink-0"
      :class="chat.toolConfirmEnabled ? '' : 'ml-auto'"
      type="text"
      size="small"
      :title="t('编辑智能体')"
      :disabled="chat.loading || !chat.agentId"
      v-auth="'ai:agent:edit'"
      @click="handleEditAgent"
    >
      <Icon icon="i-ant-design:edit-outlined" />
    </Button>

    <!-- 工具确认设置：已选择「始终运行」的工具可恢复为每次确认 -->
    <Modal v-model:open="chat.toolConfirmOpen" :title="t('工具确认设置')" :footer="null" :width="460" :zIndex="2100">
      <div class="text-xs c-gray-5 mb-2">
        {{ t('以下工具已选择「始终运行」，执行时不再询问；可随时恢复为执行前确认。') }}
      </div>
      <div v-if="chat.toolConfirmLoading" class="py-4 text-center c-gray-5">{{ t('加载中') }}...</div>
      <div v-else-if="chat.alwaysAllowTools.length == 0" class="py-4 text-center c-gray-5">
        {{ t('暂无已允许始终运行的工具') }}
      </div>
      <div
        v-for="tool in chat.alwaysAllowTools"
        :key="tool"
        class="flex items-center justify-between py-1.5 border-b border-gray-1"
      >
        <code class="text-xs c-gray-7">{{ tool }}</code>
        <Button size="small" @click="chat.handleRemoveAlwaysAllow(tool)">{{ t('恢复确认') }}</Button>
      </div>
      <div v-if="chat.alwaysAllowTools.length > 0" class="mt-3 flex justify-between items-center">
        <span class="text-xs c-gray-5">{{ t('恢复后，该工具下次执行前会重新询问。') }}</span>
        <Button size="small" danger @click="chat.handleClearAlwaysAllow()">{{ t('全部恢复确认') }}</Button>
      </div>
    </Modal>

    <!-- 编辑智能体抽屉 -->
    <InputForm @register="registerAgentDrawer" @success="handleAgentFormSuccess" />
  </div>
</template>
<script lang="ts" setup name="AiChatHeader">
  import type { PropType } from 'vue';

  import type { AiAgentChatContext } from './useAiAgentChat';

  import { Avatar, Button, Modal, Select } from 'antdv-next';

  import { Icon } from '@jeesite/core/components/Icon';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import InputForm from '../../views/ai/agent/form.vue';

  const props = defineProps({
    chat: { type: Object as PropType<AiAgentChatContext>, required: true },
  });

  const { t } = useI18n('ai.chat');

  // 编辑当前智能体：复用智能体管理表单抽屉
  const [registerAgentDrawer, { openDrawer: openAgentDrawer }] = useDrawer();

  function handleEditAgent() {
    if (!props.chat.agentId) {
      return;
    }
    openAgentDrawer(true, { id: props.chat.agentId });
  }

  // 编辑保存后同步智能体信息（名称、头像、简介、模型、预设问题、快捷指令等）
  async function handleAgentFormSuccess() {
    await props.chat.refreshAgents();
  }
</script>
<style lang="less">
  // 标题区的智能体选择：无边框，视觉上作为页面标题使用
  .jeesite-ai-chat-agent {
    // 负外边距抵消内边距，悬停底色有左右留白但文字仍与头像对齐
    padding-inline: 6px !important;
    margin-left: 6px;
    border-radius: 6px;
    transition: background-color 0.2s;

    .ant-select-selection-item,
    .ant-select-content-value {
      overflow: hidden;
      max-width: 320px;
      padding-right: 16px !important;
      color: #333;
      font-size: 16px;
      font-weight: 600;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    &:hover {
      background: #f5f5f5 !important;
    }
  }

  html[data-theme='dark'] {
    .jeesite-ai-chat-agent {
      .ant-select-selection-item,
      .ant-select-content-value {
        color: #ddd;
      }

      &:hover {
        background: #333 !important;
      }
    }
  }
</style>

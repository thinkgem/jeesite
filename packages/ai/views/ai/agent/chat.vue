<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <!-- cssFullHeight：高度完全由 CSS（flex）决定，首帧就是最终高度，不等 JS 计算后再调整 -->
  <PageWrapper cssFullHeight :sidebarWidth="220" :contentFullHeight="true" :contentMinHeight="400">
    <!-- 智能体基本信息：标题区（头像 + 可切换的智能体名称 + 简介） -->
    <template #headerTitle>
      <AiChatHeader :chat="chat" />
    </template>
    <!-- 会话列表 -->
    <template #sidebar>
      <AiChatSidebar :chat="chat" />
    </template>
    <!-- 对话区（消息列表 + 输入框），与右下角 AI 助手抽屉共用 -->
    <AiChatMain :chat="chat">
      <!-- AI 生成内容提示：位于消息列表末尾，随消息滚动 -->
      <template #footer>
        <div class="pt-2 pb-1 c-gray-4 text-xs text-center">
          {{ t('服务生成的所有内容均由人工智能模型生成，准确和完整性无法保证，不代表我们的态度或观点。') }}
        </div>
      </template>
    </AiChatMain>
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsAiAgentChat">
  import { watch } from 'vue';
  import { useRoute } from 'vue-router';

  import { PageWrapper } from '@jeesite/core/components/Page';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import { AiChatHeader, AiChatMain, AiChatSidebar, useAiAgentChat } from '@jeesite/ai/components/AiAgentChat';

  const { t } = useI18n('ai.chat');
  const route = useRoute();

  // 从智能体管理打开时（/ai/agent/chat?id=xxx），默认选中该智能体，页内仍可切换
  const chat = useAiAgentChat({
    agentId: (route.query.id as string) || undefined,
    // 页内切换智能体时同步路由参数，刷新后仍停留在当前智能体
    syncRoute: true,
  });

  watch(
    () => route.query.id,
    (id) => {
      // 页内切换智能体时已同步更新路由，值与当前一致则无需重复初始化
      if (id && id !== chat.agentId) {
        chat.selectAgent(id as string);
      }
    },
  );
</script>

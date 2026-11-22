<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="jeesite-ai-chat-main h-full rounded-2 flex flex-col overflow-hidden pt-2">
    <!-- 空状态：欢迎语 + 预设问题（min-h-0 + 内部滚动，弹窗变小时不挤占输入框） -->
    <div v-if="!chat.hasMessages" class="flex-1 min-h-0 overflow-y-auto text-center">
      <!-- 抽屉模式下宽度受限，左右留出边距，避免预设问题卡片贴边 -->
      <div class="flex min-h-full items-center justify-center">
        <div class="overflow-hidden max-w-600px mx-auto px-3 py-4">
          <div class="text-xl c-gray-5 mx-10 leading-relaxed">
            {{ chat.currentAgent?.welcome || t('我是你的 AI 助手，我可以帮你解答一些问题') }}
          </div>
          <div v-if="chat.problems.length > 0" class="mt-5 bg-white rounded-2xl text-left px-5 pt-3 pb-2 op-70">
            <div class="font-size-3.5 font-bold pb-2 op-90">{{ t('猜测您想问：') }}</div>
            <!-- 预设问题可能较长：flex 换行 + 按钮内文本自动换行，避免超出卡片被裁切 -->
            <div class="flex flex-wrap gap-2">
              <Button
                v-for="problem in chat.problems"
                :key="problem"
                class="!h-auto max-w-full !whitespace-normal !text-left break-all"
                shape="round"
                @click="chat.sendText(problem)"
              >
                {{ problem }}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <ChatMessage
      ref="messageRef"
      v-model:value="chat.messages"
      :class="['min-h-0', chat.hasMessages ? '' : 'hidden']"
      :chatCompletionsApi="aiAgentChatCompletions"
      :chatRegenerateApi="aiAgentChatRegenerate"
      :showRegenerate="true"
      :systemAvatar="chat.currentAgentAvatar"
      :showThinking="true"
      :showTasks="true"
      :askAnswerLoading="chat.askAnswerLoading"
      :showTools="true"
      :showToolConfirm="true"
      :confirmLoadingStepId="chat.confirmLoadingStepId"
      v-model:loading="chat.loading"
      @regenerate="(data) => chat.handleRegenerate(data)"
      @tool-confirm="(data) => chat.handleToolConfirm(data)"
      @ask-answer="(data) => chat.handleAskAnswer(data)"
    >
      <!-- 消息动作扩展：如「填充到表单」 -->
      <template #msgActionLeftAfter="data">
        <slot name="messageAction" v-bind="data || {}"></slot>
      </template>
      <template #footer>
        <slot name="footer"></slot>
      </template>
    </ChatMessage>
    <div :class="[chat.compact ? 'px-2' : 'px-14', 'mb-2 w-full mt-3 shrink-0']">
      <div class="jeesite-ai-chat-input rounded-2 p-2 bg-white text-[15px] leading-7">
        <!-- 已上传图片预览，支持 Ctrl+V 粘贴上传 -->
        <div v-if="chat.uploadFileList.length > 0" class="flex flex-wrap items-center gap-2 mb-2">
          <div v-for="file in chat.uploadFileList" :key="file.id" class="relative">
            <img
              :src="chat.getImageUrl(file)"
              :title="file.fileName"
              class="w-14 h-14 rounded-2 object-cover border border-gray-2 bg-white"
            />
            <Icon
              icon="i-ant-design:close-circle-filled"
              class="absolute -top-2 -right-2 bg-white rounded-full c-gray-5 cursor-pointer"
              :size="16"
              :title="t('删除')"
              @click="chat.handleRemoveUpload(file)"
            />
          </div>
        </div>
        <textarea
          ref="inputRef"
          v-model="chat.inputText"
          class="w-full outline-none no-scrollbar resize-none border-none bg-transparent leading-6"
          rows="1"
          :placeholder="
            chat.currentAgent?.placeholder || t('你有什么想知道的，快来问问我，Shift+Enter 换行，Enter 发送。')
          "
          @input="handleInput"
          @keypress="(e) => chat.handleEnter(e)"
          @paste="handlePaste"
        ></textarea>
        <div class="flex items-center -mt-0.5">
          <BasicUpload
            ref="uploadRef"
            v-model:value="chat.uploadDataMap"
            :bizKey="chat.conversationId"
            bizType="ai-agent-chat"
            :loadTime="chat.uploadLoadTime"
            class="shrink-0"
            size="small"
            uploadType="image"
            :showUploadText="false"
            uploadButtonType="default"
            :maxNumber="3"
            :disabled="chat.loading"
            :accept="['jpg', 'jpeg', 'png']"
            @click="chat.handleUploadClick()"
            @change="(dataMap, records) => chat.handleUploadChange(dataMap, records)"
            @delete="(record) => chat.handleUploadDelete(record)"
          />
          <!-- 快捷指令：下拉选择后填入输入框，可继续编辑再发送 -->
          <Select
            v-if="chat.shortcutOptions.length > 0"
            :value="chat.shortcutValue"
            class="jeesite-ai-chat-shortcut shrink-0 ml-2"
            :options="chat.shortcutOptions"
            :disabled="chat.loading"
            :placeholder="t('快捷指令')"
            :title="t('快捷指令')"
            :popupMatchSelectWidth="false"
            size="small"
            variant="borderless"
            @change="handleShortcutChange"
          />
          <div class="flex-auto"></div>
          <Select
            v-model:value="chat.modelCode"
            class="jeesite-ai-chat-model shrink-0"
            :options="chat.modelOptions"
            :disabled="chat.loading || !chat.agentId"
            :placeholder="t('选择模型')"
            :title="t('选择模型')"
            :popupMatchSelectWidth="false"
            size="small"
            show-search
            option-filter-prop="label"
            variant="borderless"
            @change="chat.handleModelChange()"
          />
          <Button
            class="ml-2 shrink-0"
            type="primary"
            shape="circle"
            :loading="chat.loading && !chat.sendBtnHover"
            :title="chat.loading ? t('停止响应') : ''"
            @mouseenter="chat.sendBtnHover = true"
            @mouseleave="chat.sendBtnHover = false"
            @click="chat.handleSendOrStop()"
          >
            <Icon
              v-if="!chat.loading || chat.sendBtnHover"
              :icon="chat.loading ? 'i-ant-design:x-filled' : 'i-ant-design:arrow-up-outlined'"
            />
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup name="AiChatMain">
  import type { PropType } from 'vue';

  import type { AiAgentChatContext } from './useAiAgentChat';

  import { nextTick, onMounted, ref, shallowRef, watch } from 'vue';
  import { Button, Select } from 'antdv-next';

  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicUpload, getClipboardImageFiles } from '@jeesite/core/components/Upload';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import { aiAgentChatCompletions, aiAgentChatRegenerate } from '@jeesite/ai/api/ai/agentChat';

  import { ChatMessage } from '@jeesite/ai';

  const props = defineProps({
    chat: { type: Object as PropType<AiAgentChatContext>, required: true },
  });

  const { t } = useI18n('ai.chat');
  const messageRef = shallowRef<InstanceType<typeof ChatMessage>>();
  const uploadRef = shallowRef<InstanceType<typeof BasicUpload>>();
  const inputRef = ref<HTMLTextAreaElement>();

  onMounted(() => {
    // 把聊天组件实例交给公共逻辑（发送、重新生成、停止、滚动都由它统一处理）
    props.chat.setMessageApi(messageRef.value);
  });

  // 输入框最多撑到两行（行高 24px），超出部分内部滚动，避免多行输入把对话区挤小
  const MAX_INPUT_HEIGHT = 300;

  function handleInput() {
    if (inputRef.value) {
      inputRef.value.style.height = 'auto';
      const height = inputRef.value.scrollHeight;
      inputRef.value.style.height = (height > MAX_INPUT_HEIGHT ? MAX_INPUT_HEIGHT : height) + 'px';
    }
  }

  // 程序化改动输入内容（发送后清空、点击预设问题回填）不会触发 input 事件，
  // 这里统一重新计算高度，避免多行回车后发送，输入框仍停留在多行高度
  watch(
    () => props.chat.inputText,
    () => nextTick(handleInput),
  );

  // 快捷指令选中：回填输入框（下一帧清空下拉值，恢复占位文字）
  function handleShortcutChange(value: string) {
    props.chat.handleShortcutChange(value);
    nextTick(() => {
      props.chat.shortcutValue = null;
    });
    inputRef.value?.focus();
    nextTick(handleInput);
  }

  // 输入框粘贴：粘贴前确保存在会话（已有则复用，不再新建），再调用上传组件的上传逻辑
  async function handlePaste(event: ClipboardEvent) {
    const files = getClipboardImageFiles(event);
    if (files.length == 0) {
      return;
    }
    event.preventDefault();
    await props.chat.ensureConversation();
    await uploadRef.value?.uploadFiles(files);
  }
</script>
<style lang="less">
  // 输入框内的模型选择、快捷指令：无边框，作为工具栏文字按钮使用
  .jeesite-ai-chat-input {
    .jeesite-ai-chat-model,
    .jeesite-ai-chat-shortcut {
      width: auto;
      padding-inline: 4px;
      border-radius: 6px;

      .ant-select-selection-item,
      .ant-select-content-value {
        max-width: 180px;
        padding-right: 16px !important;
        color: #666;
        font-size: 13px;
      }

      .ant-select-selection-placeholder,
      .ant-select-placeholder {
        color: #999;
        font-size: 13px;
      }

      .ant-select-arrow {
        color: #999;
        font-size: 10px;
      }

      &:hover {
        background: #f5f5f5 !important;
      }
    }

    // 快捷指令下拉：指令可能较长，限制宽度避免挤占工具栏
    .jeesite-ai-chat-shortcut .ant-select-selection-item,
    .jeesite-ai-chat-shortcut .ant-select-content-value {
      max-width: 120px;
    }
  }

  html[data-theme='dark'] {
    .jeesite-ai-chat-input {
      .jeesite-ai-chat-model,
      .jeesite-ai-chat-shortcut {
        .ant-select-selection-item,
        .ant-select-content-value {
          color: #ddd;
        }

        &:hover {
          background: #333 !important;
        }
      }
    }
  }
</style>

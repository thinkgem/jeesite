<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <!-- 侧栏用 flex 纵向布局：新建按钮固定高度，会话列表占满剩余高度，底部留出空隙，避免列表被裁切贴底 -->
  <div class="flex flex-col h-full">
    <div class="p-2 shrink-0">
      <Button type="primary" class="w-full" :disabled="chat.loading || !chat.agentId" @click="chat.handleAdd()">
        <Icon icon="i-ant-design:plus-outlined" /> {{ t('新建对话') }}
      </Button>
    </div>
    <ScrollContainer class="jeesite-ai-chat p-2 bg-white rounded-2 flex-1 min-h-0 mb-2">
      <Menu class="jeesite-ai-chat-menu" v-model:selectedKeys="chat.conversationIds" :disabled="chat.loading">
        <template v-for="(item, index) in chat.chatList" :key="item.id">
          <MenuItem @click="handleSelect(item)">
            <div class="flex items-center justify-end">
              <span v-if="item.edit" class="flex-1 mr-2">
                <Input
                  v-model:value="item.chatTitle"
                  size="small"
                  class="mr-2"
                  @blur="handleEdit(item, false)"
                  :ref="(el) => setEditInputRef(el, item)"
                />
              </span>
              <span v-else class="flex-1 truncate">{{ item.chatTitle }}</span>
              <!-- 置顶标记常显，点击取消置顶 -->
              <Icon
                v-if="item.weight == TOP_WEIGHT"
                icon="i-ant-design:pushpin-filled"
                class="ml-1 c-primary shrink-0"
                :title="t('取消置顶')"
                @click.stop="chat.handleTop(item)"
              />
              <span class="actions c-gray flex items-center shrink-0">
                <Icon
                  v-if="item.weight != TOP_WEIGHT"
                  icon="i-ant-design:pushpin-outlined"
                  class="ml-1"
                  :title="t('置顶')"
                  @click.stop="chat.handleTop(item)"
                />
                <Icon icon="i-ant-design:edit" class="ml-1" @click.stop="handleEdit(item, true)" />
                <Popconfirm
                  :title="t('是否确认删除该对话吗？')"
                  @confirm="chat.handleDelete(item, index)"
                  :disabled="chat.loading"
                >
                  <Icon icon="i-ant-design:delete" class="ml-1" />
                </Popconfirm>
              </span>
            </div>
          </MenuItem>
        </template>
      </Menu>
      <div class="h-10"></div>
    </ScrollContainer>
  </div>
</template>
<script lang="ts" setup name="AiChatSidebar">
  import type { PropType } from 'vue';

  import type { AiAgentChatContext } from './useAiAgentChat';

  import { nextTick, ref } from 'vue';
  import { Button, Input, Menu, MenuItem, Popconfirm } from 'antdv-next';

  import { Icon } from '@jeesite/core/components/Icon';
  import { ScrollContainer } from '@jeesite/core/components/Container';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import { TOP_WEIGHT } from './useAiAgentChat';

  const props = defineProps({
    chat: { type: Object as PropType<AiAgentChatContext>, required: true },
  });

  const { t } = useI18n('ai.chat');
  const editInputRefs = ref<Recordable>({});

  function setEditInputRef(el: any, item: Recordable) {
    if (el) {
      editInputRefs.value[item.id] = el;
    }
  }

  async function handleSelect(item: Recordable) {
    await props.chat.handleSelect(item);
    props.chat.scrollMessages();
  }

  async function handleEdit(item: Recordable, edit: boolean) {
    await props.chat.handleEdit(item, edit);
    if (edit) {
      await nextTick();
      editInputRefs.value[item.id]?.focus?.();
    }
  }
</script>
<style lang="less">
  .jeesite-ai-chat {
    &-menu.ant-menu.ant-menu-light {
      border-right: 0 !important;
      padding: 2px !important;
      margin-right: -8px !important;

      .ant-menu-item {
        height: 34px;
        margin: 2px 0 !important;
        padding-right: 8px;
        color: #333 !important;
        font-size: 14px;
        line-height: 34px;

        &-selected {
          background-color: #f0f5ff !important;
          color: #333 !important;
        }

        .actions {
          display: none;
        }

        &-selected,
        &-active {
          .actions {
            display: block;
          }
        }
      }
    }
  }

  html[data-theme='dark'] {
    .jeesite-ai-chat {
      .ant-menu-light .ant-menu-item {
        color: #ddd !important;

        &-selected {
          background-color: #333 !important;
          color: #ddd !important;
        }
      }
    }
  }
</style>

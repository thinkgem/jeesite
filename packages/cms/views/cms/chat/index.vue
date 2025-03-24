<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230" :contentFullHeight="true">
    <template #sidebar>
      <div class="p-2 pt-1">
        <a-button type="primary" class="w-full" @click="handleAdd">
          <Icon icon="i-ant-design:plus-outlined" /> {{ t('新建对话') }}
        </a-button>
      </div>
      <ScrollContainer class="p-2 bg-white rounded-2 h-full">
        <Menu style="border-right: 0; padding: 2px" v-model:selectedKeys="conversationIds">
          <template v-for="(item, index) in chatList" :key="item.id">
            <Menu.Item @click="handleSelect(item)" style="padding-right: 8px">
              <div class="flex justify-end">
                <span v-if="item.edit" class="flex-1 mr-2">
                  <a-input
                    v-model:value="item.title"
                    size="small"
                    class="mr-2"
                    @blur="handleEdit(item, false, $event)"
                  />
                </span>
                <span v-else class="flex-1 truncate">{{ item.title }}</span>
                <span v-if="item.id == conversationIds[0]" class="c-gray">
                  <Icon icon="i-ant-design:edit" class="pt-3" @click="handleEdit(item, true)" />
                  <Popconfirm
                    :title="t('是否确认删除该对话吗？')"
                    @confirm="handleDelete(item, index)"
                  >
                    <Icon icon="i-ant-design:delete" class="pt-3" />
                  </Popconfirm>
                </span>
              </div>
            </Menu.Item>
          </template>
        </Menu>
        <div class="h-10"></div>
      </ScrollContainer>
    </template>
    <div class="h-full rounded-2 flex flex-col overflow-hidden">
      <ChatMessage
        ref="messageRef"
        v-model:value="messages"
        :chatStreamApi="cmsChatStream"
        :conversationId="conversationIds[0]"
        :inputMessage="inputMessageRef?.value"
        v-model:loading="loading"
      >
        <template #header>
          <div
            v-if="messages.length == 0"
            class="h-[90%] flex justify-center items-center c-gray-4 text-xl"
          >
            {{ t('我是你的 AI 助手，我可以帮你解答一些问题') }}
          </div>
        </template>
      </ChatMessage>
      <div class="pl-14 pr-16 w-full flex justify-end mt-3">
        <div class="flex-1 rounded-2 p-3 pb-1 bg-white text-[15px] leading-7">
          <textarea
            ref="inputMessageRef"
            class="outline-none no-scrollbar resize-none w-full h-full border-none bg-transparent"
            rows="1"
            :placeholder="t('你有什么想知道的，快来问问我，Shift+Enter 换行，Enter 发送。')"
            @input="handleInput"
            @keypress="handleEnter"
          ></textarea>
        </div>
        <a-button
          style="width: 110px; height: 100%; margin-left: 10px"
          type="primary"
          :loading="loading"
          @click="handleSend"
        >
          <Icon icon="i-fa:send" /> {{ t('发送') }}
        </a-button>
      </div>
      <div class="pt-2 pr-8 c-gray-4 text-xs text-center">
        {{
          t(
            '服务生成的所有内容均由人工智能模型生成，准确和完整性无法保证，不代表我们的态度或观点。',
          )
        }}
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsCmsChatIndex">
  import { nextTick, onMounted, ref } from 'vue';
  import { Menu, Popconfirm } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { ScrollContainer } from '@jeesite/core/components/Container';
  import {
    cmsChatDelete,
    cmsChatList,
    cmsChatMessage,
    cmsChatSave,
    cmsChatStream,
  } from '@jeesite/cms/api/cms/chat';
  import { ChatMessage } from '@jeesite/cms';

  const { t } = useI18n('cms.chat');
  const { showMessage } = useMessage();
  const conversationIds = ref<string[]>([]);
  const conversationTitle = ref<string>('');

  const loading = ref(false);
  const messageRef = ref<InstanceType<typeof ChatMessage>>();
  const inputMessageRef = ref<HTMLTextAreaElement>();
  const messages = ref<Recordable[]>([]);

  const chatList = ref<Recordable[]>([]);

  onMounted(async () => {
    chatList.value = await cmsChatList();
    if (chatList.value.length == 0) {
      const res = await cmsChatSave();
      chatList.value.unshift(res);
    }
    await nextTick(async () => {
      await handleSelect(chatList.value[0]);
    });
  });

  async function handleAdd() {
    if (chatList.value.length > 0) {
      await handleSelect(chatList.value[0]);
      if (messages.value.length == 0) {
        showMessage(t('当前已是新对话'));
        return;
      }
    }
    const res = await cmsChatSave();
    chatList.value.unshift(res);
    await handleSelect(chatList.value[0]);
  }

  async function handleSelect(item: Recordable) {
    conversationIds.value = [item.id];
    conversationTitle.value = item.title;
    messages.value = await cmsChatMessage({ id: item.id });
    messageRef.value?.scrollBottom();
  }

  async function handleEdit(item: Recordable, edit: boolean, event?: Event) {
    item.edit = edit;
    if (!edit) {
      const res = await cmsChatSave(item);
      showMessage(res.message);
    }
  }

  async function handleDelete(item: Recordable, idx: number) {
    const res = await cmsChatDelete({ id: item.id });
    chatList.value.splice(idx, 1);
    showMessage(res.message);
    if (idx == 0 && chatList.value[idx]) {
      await handleSelect(chatList.value[idx]);
    } else if (chatList.value[idx - 1]) {
      await handleSelect(chatList.value[idx - 1]);
    }
  }

  function handleInput() {
    if (inputMessageRef.value) {
      inputMessageRef.value.style.height = 'auto';
      const height = inputMessageRef.value.scrollHeight;
      inputMessageRef.value.style.height = (height > 300 ? 300 : height) + 'px';
    }
  }

  function handleInputMessage(value: string) {
    if (inputMessageRef.value) {
      inputMessageRef.value.value = value;
      inputMessageRef.value.style.height = 'auto';
    }
  }

  function handleEnter(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      handleSend();
    }
  }

  async function handleSend() {
    if (inputMessageRef.value && inputMessageRef.value.value) {
      loading.value = true;
      if (!conversationIds.value[0]) {
        await handleAdd();
      }
      const params = {
        id: conversationIds.value[0],
        message: inputMessageRef.value.value,
      };
      inputMessageRef.value.value = '';
      inputMessageRef.value.style.height = 'auto';
      try {
        await messageRef.value?.sendMessage(params);
      } finally {
        loading.value = false;
      }
    } else {
      showMessage(t('请填写你的问题'));
    }
  }

  function handleStopLoading() {
    loading.value = false;
  }
</script>

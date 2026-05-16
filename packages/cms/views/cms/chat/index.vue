<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="200" :contentFullHeight="true" :contentMinHeight="400">
    <template #sidebar>
      <div class="p-2 pt-1">
        <a-button type="primary" class="w-full" @click="handleAdd" :disabled="loading">
          <Icon icon="i-ant-design:plus-outlined" /> {{ t('新建对话') }}
        </a-button>
      </div>
      <ScrollContainer ref="chatListRef" class="jeesite-cms-ai p-2 bg-white rounded-2 h-full">
        <Menu class="jeesite-cms-ai-menu" v-model:selectedKeys="conversationIds" :disabled="loading">
          <template v-for="(item, index) in chatList" :key="item.id">
            <Menu.Item @click="handleSelect(item)">
              <div class="flex justify-end">
                <span v-if="item.edit" class="flex-1 mr-2">
                  <a-input
                    v-model:value="item.title"
                    size="small"
                    class="mr-2"
                    @blur="handleEdit(item, false, $event)"
                    :ref="(el) => setEditInputRef(el, item)"
                  />
                </span>
                <span v-else class="flex-1 truncate">{{ item.title }}</span>
                <span class="actions c-gray">
                  <Icon icon="i-ant-design:edit" class="pt-3" @click="handleEdit(item, true)" />
                  <Popconfirm
                    :title="t('是否确认删除该对话吗？')"
                    @confirm="handleDelete(item, index)"
                    :disabled="loading"
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
      <div v-if="messages.length == 0" class="h-full flex justify-center items-center text-center">
        <div class="overflow-hidden">
          <div class="text-xl c-gray-5">
            {{ t('我是你的 AI 助手，我可以帮你解答一些问题') }}
          </div>
          <div class="mt-5 line-height-loose" v-if="userStore.getPageCacheByKey('demoMode')">
            <div class="font-size-3.7 c-gray-4 p-3">
              提示：当前对接的是 DeepSeek-R1-8B 小模型，仅作为当前演示使用，AI 回答结果可能不够理想，<br />
              由于硬件资源有限，当前 AI 模型未接入 Tool、MPC 等工具服务调用，如想体验与业务联动功能，<br />
              推荐本地部署，对接 AI 模型 [<a href="https://jeesite.com/docs/ai-cms/" target="_blank">部署指南</a>]。
              此外当前向量库中只含了几篇关于 jeesite 的文章，<br />
              知识库数据来源，自 JeeSite 内容管理模块，进入菜单：扩展功能 -> 内容管理 -> 内容发布<br />
            </div>
            <div class="mt-5 bg-white rounded-2xl text-left px-5 py-3 op-70">
              <div class="font-size-4 font-bold pb-1 op-90">猜测您想问：</div>
              <a-button class="mr-3" shape="round" @click="handleQuick">jeesite 简介</a-button>
              <a-button class="mr-3" shape="round" @click="handleQuick">jeesite 优势</a-button>
              <a-button class="mr-3" shape="round" @click="handleQuick">jeesite 技术栈</a-button>
            </div>
          </div>
        </div>
      </div>
      <ChatMessage
        ref="messageRef"
        v-model:value="messages"
        :chatStreamApi="cmsChatStream"
        :conversationId="conversationIds[0]"
        :inputMessage="inputMessageRef?.value"
        v-model:loading="loading"
      />
      <div class="pl-14 pr-16 w-full flex justify-end mt-3">
        <div class="flex items-end flex-1 rounded-2 p-2 bg-white text-[15px] leading-7">
          <textarea
            ref="inputMessageRef"
            class="flex-auto ml-1 outline-none no-scrollbar resize-none w-full h-full border-none bg-transparent"
            rows="1"
            :placeholder="t('你有什么想知道的，快来问问我，Shift+Enter 换行，Enter 发送。')"
            @input="handleInput"
            @keypress="handleEnter"
          ></textarea>
          <a-button class="ml-2 shrink-0" type="primary" shape="circle" :loading="loading" @click="handleSend">
            <Icon v-if="!loading" icon="i-ant-design:arrow-up-outlined" />
          </a-button>
        </div>
      </div>
      <div class="pt-2 pr-8 c-gray-4 text-xs text-center">
        {{ t('服务生成的所有内容均由人工智能模型生成，准确和完整性无法保证，不代表我们的态度或观点。') }}
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsCmsChatIndex">
  import { nextTick, onMounted, ref, shallowRef } from 'vue';
  import { Menu, Popconfirm } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useUserStore } from '@jeesite/core/store/modules/user';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { ScrollContainer } from '@jeesite/core/components/Container';
  import { cmsChatDelete, cmsChatList, cmsChatMessage, cmsChatSave, cmsChatStream } from '@jeesite/cms/api/cms/chat';
  import { ChatMessage } from '@jeesite/cms';

  const { t } = useI18n('cms.chat');
  const { showMessage } = useMessage();
  const conversationIds = ref<string[]>([]);
  const conversationTitle = ref<string>('');
  const userStore = useUserStore();

  const loading = ref(false);
  const chatListRef = shallowRef<InstanceType<typeof ScrollContainer>>();
  const chatList = ref<Recordable[]>([]);
  const messageRef = shallowRef<InstanceType<typeof ChatMessage>>();
  const inputMessageRef = ref<HTMLTextAreaElement>();
  const messages = ref<Recordable[]>([]);
  const editInputRefs = ref<Recordable>({});

  onMounted(async () => {
    chatList.value = await cmsChatList();
  });

  async function handleAdd() {
    chatListRef.value?.scrollTo(0);
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

  function setEditInputRef(el: any, item: Recordable) {
    if (el) {
      editInputRefs.value[item.id] = el;
    }
  }

  async function handleEdit(item: Recordable, edit: boolean, event?: Event) {
    if (loading.value) return;
    item.edit = edit;
    if (edit) {
      await nextTick(() => {
        const inputRef = editInputRefs.value[item.id];
        if (inputRef && inputRef.focus) {
          inputRef.focus();
        }
      });
    } else if (item.title !== item.oldTitle) {
      delete item.oldTitle;
      const res = await cmsChatSave(item);
      showMessage(res.message);
    }
    item.oldTitle = item.title;
  }

  async function handleDelete(item: Recordable, idx: number) {
    const res = await cmsChatDelete({ id: item.id });
    chatList.value.splice(idx, 1);
    showMessage(res.message);
    // if (idx == 0 && chatList.value[idx]) {
    //   await handleSelect(chatList.value[idx]);
    // } else if (chatList.value[idx - 1]) {
    //   await handleSelect(chatList.value[idx - 1]);
    // }
    conversationIds.value = [''];
    conversationTitle.value = '';
    messages.value = [];
  }

  function handleInput() {
    if (inputMessageRef.value) {
      inputMessageRef.value.style.height = 'auto';
      const height = inputMessageRef.value.scrollHeight;
      inputMessageRef.value.style.height = (height > 300 ? 300 : height) + 'px';
    }
  }

  function handleEnter(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      if (!loading.value) {
        handleSend();
      }
    }
  }

  async function handleSend() {
    if (inputMessageRef.value && inputMessageRef.value.value) {
      if (!conversationIds.value[0]) {
        await handleAdd();
      }
      loading.value = true;
      const params = {
        id: conversationIds.value[0],
        message: inputMessageRef.value.value,
      };
      inputMessageRef.value.value = '';
      inputMessageRef.value.style.height = 'auto';
      try {
        chatList.value
          .filter((item) => item.id == params.id)
          .forEach((item) => {
            if (item.title.startsWith('新对话')) {
              item.title = params.message.substring(0, 30);
              cmsChatSave(item);
            }
          });
        await messageRef.value?.sendMessage(params);
      } finally {
        loading.value = false;
      }
    } else {
      showMessage(t('请填写你的问题'));
    }
  }

  function handleQuick(e: MouseEvent) {
    const target = e.target as HTMLElement;
    if (inputMessageRef.value) {
      inputMessageRef.value.value = target.textContent;
      if (!loading.value) {
        handleSend();
      }
    }
  }
</script>
<style lang="less">
  .jeesite-cms-ai {
    &-menu.ant-menu.ant-menu-light {
      border-right: 0 !important;
      padding: 2px !important;

      .ant-menu-item {
        padding-right: 8px;
        color: #333 !important;

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
    .jeesite-cms-ai {
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

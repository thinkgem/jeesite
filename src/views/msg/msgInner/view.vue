<!--
  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
  * No deletion without permission, or be held responsible to law.
  * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230" title="false">
    <div class="jeesite-msg-title mb-5 ml-2 mr-2 pb-6 pt-4 text-center text-xl">
      {{ record.msgTitle }}
    </div>
    <BasicForm @register="registerForm">
      <template #readList="{ model, field }">
        <span v-for="(e, i) in model[field]" :key="i">
          <Tag color="blue" :title="e.receiveUserCode">{{ e.receiveUserName }}</Tag>
        </span>
        <span v-if="model[field] == 0">
          <Tag>{{ t('还没有人阅读') }}</Tag>
        </span>
      </template>
      <template #unReadList="{ model, field }">
        <span v-for="(e, i) in model[field]" :key="i">
          <Tag color="purple" :title="e.receiveUserCode">{{ e.receiveUserName }}</Tag>
        </span>
        <span v-if="model[field] == 0">
          <Tag>{{ t('没有了') }}</Tag>
        </span>
      </template>
    </BasicForm>
    <div class="flex justify-center">
      <a-button type="primary" @click="closeCurrent">
        <Icon icon="ant-design:close-outlined" /> {{ t('关闭') }}
      </a-button>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsMsgMsgInnerView">
  import { ref, computed, onMounted } from 'vue';
  import { Tag } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { PageWrapper } from '/@/components/Page';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { MsgInner, msgInnerView } from '/@/api/msg/msgInner';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { useQuery } from '/@/hooks/web/usePage';

  const { t } = useI18n('msg.msgInner');
  const { closeCurrent } = useTabs();
  const getQuery = useQuery();
  const record = ref<MsgInner>({ id: getQuery.value.id } as MsgInner);

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('等级'),
      field: 'contentLevel',
      component: 'Text',
      componentProps: {
        dictType: 'msg_inner_content_level',
      },
    },
    {
      label: t('类型'),
      field: 'contentType',
      component: 'Text',
      componentProps: {
        dictType: 'msg_inner_content_type',
      },
    },
    {
      label: t('内容'),
      field: 'msgContent',
      component: 'Text',
      componentProps: {
        isHtml: true,
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('附件'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'msgInner_file',
        uploadType: 'all',
        readonly: true,
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('发送者'),
      field: 'sendUserName',
      component: 'Text',
    },
    {
      label: t('发送时间'),
      field: 'sendDate',
      component: 'Text',
    },
    {
      label: t('阅读状态'),
      field: 'receiveInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('已读用户'),
      field: 'readList',
      component: 'Text',
      colProps: { lg: 24, md: 24 },
      slot: 'readList',
    },
    {
      label: t('未读用户'),
      field: 'unReadList',
      component: 'Text',
      colProps: { lg: 24, md: 24 },
      slot: 'unReadList',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  onMounted(async () => {
    await resetFields();
    const res = await msgInnerView(record.value);
    record.value = (res.msgInner || {}) as MsgInner;
    record.value.__t = new Date().getTime();
    record.value.readList = res.readList;
    record.value.unReadList = res.unReadList;
    setFieldsValue(record.value);
  });
</script>
<style lang="less">
  .jeesite-msg-title {
    border-bottom: 1px solid #ddd;
  }
  [data-theme='dark'] {
    .jeesite-msg-title {
      border-bottom: 1px solid #303030;
    }
  }
</style>

<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button type="primary" @click="handleForm({})" v-auth="'ai:agent:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })" :title="record.agentName">
          {{ record.agentName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsAiAgentList">
  import { computed, h, onMounted, ref, unref } from 'vue';
  import { Avatar } from 'antdv-next';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { aiAgentDelete, aiAgentListData } from '@jeesite/ai/api/ai/agent';
  import { aiAgentDisable, aiAgentEnable } from '@jeesite/ai/api/ai/agent';
  import { aiModelListData } from '@jeesite/ai';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import defaultAgentAvatar from '@jeesite/assets/images/logo.png';
  import InputForm from './form.vue';

  const { t } = useI18n('ai.agent');
  const { showMessage } = useMessage();
  const { ctxPath } = useGlobSetting();
  const { meta } = unref(router.currentRoute);

  // 头像地址：相对路径需拼接应用上下文路径
  function getAvatarUrl(avatar?: string) {
    if (!avatar || avatar.startsWith('data:') || avatar.indexOf('://') > -1) {
      return avatar || '';
    }
    return ctxPath + avatar;
  }

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('AI智能体管理'),
  };

  // 模型下拉选项
  const modelList = ref<Recordable[]>([]);
  const modelOptions = computed(() =>
    modelList.value.map((item) => ({
      label: item.modelDesc || item.modelName || item.modelCode,
      value: item.modelCode,
    })),
  );

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('名称'),
        field: 'agentName',
        component: 'Input',
      },
      {
        label: t('AI 模型'),
        field: 'model.modelCode',
        component: 'Select',
        componentProps: {
          options: [],
          allowClear: true,
          showSearch: true,
          optionFilterProp: 'label',
          placeholder: t('请选择'),
        },
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: 'sys_search_status',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('头像'),
      dataIndex: 'agentAvatar',
      key: 'a.agent_avatar',
      sorter: false,
      width: 100,
      align: 'center',
      customRender: ({ record }) => {
        // 未设置头像时显示默认头像，与编辑表单的默认头像保持一致
        const url = getAvatarUrl(record.agentAvatar);
        return h(Avatar, { src: url || defaultAgentAvatar, size: 32, class: 'bg-white' });
      },
    },
    {
      title: t('名称'),
      dataIndex: 'agentName',
      key: 'a.agent_name',
      sorter: true,
      width: 200,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('简介'),
      dataIndex: 'agentIntro',
      key: 'a.agent_intro',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('AI 模型'),
      dataIndex: 'model.modelName',
      key: 'a.model_code',
      sorter: true,
      width: 130,
      align: 'left',
      format: (_, record) => record.model?.modelName || record.model?.modelCode || '',
    },
    {
      title: t('系统提示词'),
      dataIndex: 'systemPrompt',
      key: 'a.system_prompt',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('欢迎语'),
      dataIndex: 'welcome',
      key: 'a.welcome',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('预设问题'),
      dataIndex: 'problem',
      key: 'a.problem',
      sorter: true,
      width: 200,
      align: 'left',
      // 多行数据以顿号展示，避免表格行高过大
      format: (val) => (val || '').replace(/[\r\n,，]+/g, '、'),
    },
    {
      title: t('快捷指令'),
      dataIndex: 'shortcut',
      key: 'a.shortcut',
      sorter: true,
      width: 200,
      align: 'left',
      format: (val) => (val || '').replace(/[\r\n,，]+/g, '、'),
    },
    {
      title: t('输入框提示'),
      dataIndex: 'placeholder',
      key: 'a.placeholder',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('权重'),
      dataIndex: 'weight',
      key: 'a.weight',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_search_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      key: 'a.remarks',
      sorter: true,
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 190,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑AI智能体'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'ai:agent:edit',
      },
      {
        icon: 'i-ant-design:message-outlined',
        title: t('智能体对话'),
        onClick: handleChat.bind(this, record),
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用AI智能体'),
        popConfirm: {
          title: t('是否确认停用AI智能体'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'ai:agent:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用AI智能体'),
        popConfirm: {
          title: t('是否确认启用AI智能体'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'ai:agent:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除AI智能体'),
        popConfirm: {
          title: t('是否确认删除AI智能体'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'ai:agent:edit',
      },
    ],
  };

  const [registerTable, { reload, getForm }] = useTable({
    api: aiAgentListData,
    beforeFetch: (params) => {
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  // 模型下拉数据（接口异常时不阻塞列表）
  onMounted(async () => {
    const res = await aiModelListData().catch(() => null);
    modelList.value = res?.list || [];
    getForm().updateSchema({ field: 'model.modelCode', componentProps: { options: modelOptions.value } });
  });

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  // 跳转到智能体对话页，通过 id 参数锁定该智能体
  function handleChat(record: Recordable) {
    // 路由列表中存在 path 为空的路由（如布局、重定向），需先过滤再匹配，避免 path 为 undefined 报错
    const chatRoute = router
      .getRoutes()
      .find((item) => typeof item.path === 'string' && item.path.endsWith('/ai/agent/chat'));
    router.push({
      path: chatRoute?.path || unref(router.currentRoute).path.replace(/\/list$/, '/chat'),
      query: { id: record.id },
    });
  }

  async function handleDisable(record: Recordable) {
    const params = { id: record.id };
    const res = await aiAgentDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { id: record.id };
    const res = await aiAgentEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { id: record.id };
    const res = await aiAgentDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

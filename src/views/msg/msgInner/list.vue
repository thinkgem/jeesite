<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
        <a-button type="primary" @click="handleForm({ status: '9' })" v-auth="'msg:msgInner:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id, status: record.status })">
          {{ record.msgTitle }}
          <Icon v-if="record.isAttac == '1'" icon="i-fa:paperclip" />
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsMsgMsgInnerList">
  import { unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { msgInnerDelete, msgInnerListData } from '/@/api/msg/msgInner';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';
  import { useGo } from '/@/hooks/web/usePage';

  const { t } = useI18n('msg.msgInner');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('站内消息'),
  };
  const go = useGo();

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 3 },
    labelWidth: 50,
    schemas: [
      {
        label: t('标题'),
        field: 'msgTitle',
        component: 'Input',
      },
      {
        label: t('等级'),
        field: 'contentLevel',
        component: 'Select',
        componentProps: {
          dictType: 'msg_inner_content_level',
        },
      },
      {
        label: t('类型'),
        field: 'contentType',
        component: 'Select',
        componentProps: {
          dictType: 'msg_inner_content_type',
        },
      },
      {
        label: t('发送时间'),
        field: 'dateRange',
        component: 'RangePicker',
        componentProps: {},
        colProps: { lg: 6 },
        labelWidth: 80,
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: 'msg_inner_msg_status',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
    ],
    fieldMapToTime: [['dateRange', ['sendDate_gte', 'sendDate_lte']]],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('标题'),
      dataIndex: 'msgTitle',
      key: 'a.msg_title',
      sorter: true,
      width: 280,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('等级'),
      dataIndex: 'contentLevel',
      key: 'a.content_level',
      sorter: true,
      width: 80,
      align: 'center',
      dictType: 'msg_inner_content_level',
    },
    {
      title: t('类型'),
      dataIndex: 'contentType',
      key: 'a.content_type',
      sorter: true,
      width: 80,
      align: 'center',
      dictType: 'msg_inner_content_type',
    },
    {
      title: t('发送者'),
      dataIndex: 'sendUserName',
      key: 'a.send_user_name',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('发送时间'),
      dataIndex: 'sendDate',
      key: 'a.send_date',
      sorter: true,
      width: 150,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 80,
      align: 'center',
      dictType: 'msg_inner_msg_status',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-ant-design:file-text-outlined',
        title: t('查看消息'),
        onClick: handleForm.bind(this, { id: record.id, status: record.status }),
        auth: 'msg:msgInner:view',
        ifShow: () => record.status !== '9',
        enable: true,
      },
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑消息'),
        onClick: handleForm.bind(this, { id: record.id, status: record.status }),
        auth: 'msg:msgInner:edit',
        ifShow: () => record.status === '9',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除消息'),
        popConfirm: {
          title: t('是否确认删除消息'),
          confirm: handleDelete.bind(this, { id: record.id }),
        },
        auth: 'msg:msgInner:edit',
        ifShow: () => record.status === '9',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: msgInnerListData,
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

  function handleForm(record: Recordable) {
    if (record.status !== '9') {
      go('/msg/msgInner/view?id=' + record.id);
      return;
    }
    openDrawer(true, record);
  }

  async function handleDelete(record: Recordable) {
    const res = await msgInnerDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

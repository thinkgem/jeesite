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
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })">
          {{ record.logTitle }}
        </a>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts" setup name="ViewsSysOnlineList">
  import { h, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { onlineListData, onlineTickOut } from '/@/api/sys/online';
  import { FormProps } from '/@/components/Form';

  const { t } = useI18n('sys.online');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('在线用户'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('操作用户'),
        field: 'userCode',
        // fieldLabel: 'userName',
        component: 'ListSelect',
        componentProps: {
          selectType: 'userSelect',
        },
      },
      {
        label: t('查询所有在线'),
        field: 'isAllOnline',
        helpMessage: t('包含3分钟以上未操作的用户'),
        component: 'Switch',
        componentProps: {
          checkedValue: '1',
        },
        labelWidth: 170,
        colProps: { lg: 5 },
      },
      {
        label: t('包含游客用户'),
        field: 'isVisitor',
        helpMessage: t('包含未登录的用户'),
        component: 'Switch',
        componentProps: {
          checkedValue: '1',
        },
        labelWidth: 160,
        colProps: { lg: 5 },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('用户名称'),
      dataIndex: 'userName',
      key: 'userName',
      sorter: true,
      width: 100,
      align: 'center',
      customRender: ({ record }) => {
        const tit = '' + t('账号') + '：' + (record.userCode || '');
        return h('span', { title: tit }, record.userName || t('游客'));
      },
    },
    {
      title: t('创建时间'),
      dataIndex: 'startTimestamp',
      key: 'startTimestamp',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('最后访问'),
      dataIndex: 'lastAccessTime',
      key: 'lastAccessTime',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('超时时间'),
      dataIndex: 'timeout',
      key: 'timeout',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('客户主机'),
      dataIndex: 'host',
      key: 'host',
      sorter: true,
      width: 70,
      align: 'center',
    },
    {
      title: t('用户类型'),
      dataIndex: 'userType',
      key: 'userType',
      sorter: true,
      width: 70,
      align: 'center',
      dictType: 'sys_user_type',
      defaultValue: t('未设置'),
    },
    {
      title: t('设备类型'),
      dataIndex: 'deviceType',
      key: 'deviceType',
      sorter: true,
      width: 70,
      align: 'center',
      dictType: 'sys_device_type',
      defaultValue: t('未设置'),
    },
  ];

  const actionColumn: BasicColumn = {
    width: 70,
    actions: (record: Recordable) => [
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('踢出在线用户'),
        popConfirm: {
          title: t('是否要踢出在线状态'),
          confirm: handleTickOut.bind(this, { sessionId: record.id }),
        },
        auth: 'sys:online:edit',
      },
    ],
  };

  const [registerTable, { reload }] = useTable({
    api: onlineListData,
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

  async function handleTickOut(record: Recordable) {
    const res = await onlineTickOut(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:config:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })">
          {{ record.configName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysConfigList">
  import { unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { configDelete, configListData } from '/@/api/sys/config';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.config');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('参数管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('参数名称'),
        field: 'configName',
        component: 'Input',
      },
      {
        label: t('参数键名'),
        field: 'configKey_like',
        component: 'Input',
      },
      {
        label: t('系统内置'),
        field: 'isSys',
        component: 'Select',
        componentProps: {
          dictType: 'sys_yes_no',
          allowClear: true,
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('参数名称'),
      dataIndex: 'configName',
      key: 'a.config_name',
      sorter: true,
      width: 130,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('参数键名'),
      dataIndex: 'configKey',
      key: 'a.config_key',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('参数键值'),
      dataIndex: 'configValue',
      key: 'a.config_value',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('系统内置'),
      dataIndex: 'isSys',
      key: 'a.is_sys',
      sorter: true,
      width: 80,
      align: 'center',
      dictType: 'sys_yes_no',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 100,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑参数'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'sys:config:edit',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除参数'),
        popConfirm: {
          title: t('是否确认删除参数'),
          confirm: handleDelete.bind(this, { id: record.id }),
        },
        auth: 'sys:config:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: configListData,
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
    openDrawer(true, record);
  }

  async function handleDelete(record: Recordable) {
    const res = await configDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

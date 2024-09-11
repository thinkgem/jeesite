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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:dictType:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })" :title="record.dictName">
          {{ record.dictName }}
        </a>
      </template>
      <template #dictTypeColumn="{ record }">
        <a @click="handleDictData(record)" :title="record.dictType">
          {{ record.dictType }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysDictTypeList">
  import { unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { dictTypeDelete, dictTypeListData } from '/@/api/sys/dictType';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.dictType');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('字典管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 4 },
    labelWidth: 90,
    schemas: [
      {
        label: t('字典名称'),
        field: 'dictName',
        component: 'Input',
      },
      {
        label: t('字典类型'),
        field: 'dictType_like',
        component: 'Input',
      },
      {
        label: t('系统字典'),
        field: 'isSys',
        component: 'Select',
        componentProps: {
          dictType: 'sys_yes_no',
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
      title: t('字典名称'),
      dataIndex: 'dictName',
      key: 'a.dict_name',
      sorter: true,
      width: 260,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('字典类型'),
      dataIndex: 'dictType',
      key: 'a.dict_type',
      sorter: true,
      width: 260,
      align: 'left',
      slot: 'dictTypeColumn',
    },
    {
      title: t('系统字典'),
      dataIndex: 'isSys',
      key: 'a.is_sys',
      sorter: true,
      width: 80,
      dictType: 'sys_yes_no',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 130,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 80,
      dictType: 'sys_status',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      key: 'a.remarks',
      sorter: true,
      width: 130,
    },
  ];

  const actionColumn: BasicColumn = {
    width: 130,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑字典'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'sys:dictType:edit',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除字典'),
        popConfirm: {
          title: t('是否确认删除字典'),
          confirm: handleDelete.bind(this, { id: record.id }),
        },
        auth: 'sys:dictType:edit',
      },
      {
        icon: 'i-ant-design:unordered-list-outlined',
        title: t('字典数据'),
        onClick: handleDictData.bind(this, record),
        auth: 'sys:dictData:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: dictTypeListData,
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
    const res = await dictTypeDelete(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleDictData(record: Recordable) {
    router.push({
      path: '/sys/dictData/list',
      query: {
        dictType: record.dictType,
      },
    });
  }

  function handleSuccess(_record: Recordable) {
    reload();
  }
</script>

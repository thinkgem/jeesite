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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:module:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ moduleCode: record.moduleCode })">
          {{ record.moduleName }}
        </a>
      </template>
      <template #statusColumn="{ column, record }">
        <DictLabel
          v-if="record.isLoader"
          :dictType="column.dictType"
          :dictValue="record[column.dataIndex]"
        />
        <span v-else style="color: red">{{ t('未安装') }}</span>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysModuleList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { DictLabel } from '/@/components/Dict';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { moduleDelete, moduleListData } from '/@/api/sys/module';
  import { moduleDisable, moduleEnable } from '/@/api/sys/module';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.module');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: router.currentRoute.value.meta.title || t('模块管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('模块名称'),
        field: 'moduleName',
        component: 'Input',
      },
      {
        label: t('主类全名'),
        field: 'mainClassName',
        component: 'Input',
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: 'sys_search_status',
          allowClear: true,
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('模块名称'),
      dataIndex: 'moduleName',
      key: 'a.module_name',
      sorter: true,
      width: 130,
      align: 'center',
      slots: { customRender: 'firstColumn' },
    },
    {
      title: t('模块编码'),
      dataIndex: 'moduleCode',
      key: 'a.module_code',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('模块描述'),
      dataIndex: 'description',
      key: 'a.description',
      sorter: true,
      width: 230,
      align: 'left',
    },
    {
      title: t('版本'),
      dataIndex: 'currentVersion',
      key: 'a.current_version',
      sorter: true,
      width: 60,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 60,
      align: 'center',
      dictType: 'sys_status',
      slots: { customRender: 'statusColumn' },
    },
  ];

  const actionColumn: BasicColumn = {
    width: 130,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑模块'),
        onClick: handleForm.bind(this, { moduleCode: record.moduleCode }),
        auth: 'sys:module:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用模块'),
        popConfirm: {
          title: t('是否确认停用模块'),
          confirm: handleDisable.bind(this, { moduleCode: record.moduleCode }),
        },
        auth: 'sys:module:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用模块'),
        popConfirm: {
          title: t('是否确认启用模块'),
          confirm: handleEnable.bind(this, { moduleCode: record.moduleCode }),
        },
        auth: 'sys:module:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除模块'),
        popConfirm: {
          title: t('是否确认删除模块'),
          confirm: handleDelete.bind(this, { moduleCode: record.moduleCode }),
        },
        auth: 'sys:module:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: moduleListData,
    beforeFetch: (params) => {
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    pagination: true,
    canResize: true,
  });

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const res = await moduleDisable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleEnable(record: Recordable) {
    const res = await moduleEnable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleDelete(record: Recordable) {
    const res = await moduleDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

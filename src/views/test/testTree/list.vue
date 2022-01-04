<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @fetchSuccess="fetchSuccess">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('展开全部')">
          <Icon icon="bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <a-button type="primary" @click="handleForm({})" v-auth="'test:testTree:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <span class="cursor-pointer" @click="expandCollapse(record)">
          ( {{ record.treeCode }} )
        </span>
        <a @click="handleForm({ treeCode: record.treeCode })">
          {{ record.treeName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsTestTestTreeList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, watch, nextTick } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { testTreeDelete, testTreeListData } from '/@/api/test/testTree';
  import { testTreeDisable, testTreeEnable } from '/@/api/test/testTree';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCode: String,
  });

  const { t } = useI18n('test.testTree');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: router.currentRoute.value.meta.title || t('数据管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('节点名称'),
        field: 'treeName',
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
      {
        label: t('备注信息'),
        field: 'remarks',
        component: 'Input',
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('节点名称'),
      dataIndex: 'treeName',
      width: 230,
      align: 'left',
      slots: { customRender: 'firstColumn' },
    },
    {
      title: t('排序号'),
      dataIndex: 'treeSort',
      width: 130,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 130,
      align: 'center',
      dictType: 'sys_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      width: 130,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑数据'),
        onClick: handleForm.bind(this, { treeCode: record.treeCode }),
        auth: 'test:testTree:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用数据'),
        popConfirm: {
          title: t('是否确认停用数据'),
          confirm: handleDisable.bind(this, { treeCode: record.treeCode }),
        },
        auth: 'test:testTree:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用数据'),
        popConfirm: {
          title: t('是否确认启用数据'),
          confirm: handleEnable.bind(this, { treeCode: record.treeCode }),
        },
        auth: 'test:testTree:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除数据'),
        popConfirm: {
          title: t('是否确认删除数据'),
          confirm: handleDelete.bind(this, { treeCode: record.treeCode }),
        },
        auth: 'test:testTree:edit',
      },
      {
        icon: 'fluent:add-circle-24-regular',
        title: t('新建下级数据'),
        onClick: handleForm.bind(this, {
          parentCode: record.treeCode,
          parentName: record.treeName,
        }),
        auth: 'test:testTree:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse }] = useTable({
    api: testTreeListData,
    beforeFetch: (params) => {
      params.id = props.treeCode;
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    isTreeTable: true,
    pagination: false,
    canResize: true,
  });

  watch(
    () => props.treeCode,
    () => {
      reload();
    },
  );

  function fetchSuccess() {
    if (props.treeCode) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const res = await testTreeDisable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleEnable(record: Recordable) {
    const res = await testTreeEnable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleDelete(record: Recordable) {
    const res = await testTreeDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

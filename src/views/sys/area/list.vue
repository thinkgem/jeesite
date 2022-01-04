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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:area:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <span class="cursor-pointer" @click="expandCollapse(record)">
          ( {{ record.areaCode }} )
        </span>
        <a @click="handleForm({ areaCode: record.areaCode })">
          {{ record.areaName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysAreaList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, watch, nextTick } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { areaDelete, areaListData } from '/@/api/sys/area';
  import { areaDisable, areaEnable } from '/@/api/sys/area';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCode: String,
  });

  const { t } = useI18n('sys.area');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: router.currentRoute.value.meta.title || t('行政区划'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('区域名称'),
        field: 'areaName',
        component: 'Input',
      },
      {
        label: t('区域类型'),
        field: 'areaType',
        component: 'Select',
        componentProps: {
          dictType: 'sys_area_type',
          allowClear: true,
        },
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
      title: t('区域名称'),
      dataIndex: 'areaName',
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
      title: t('区域类型'),
      dataIndex: 'areaType',
      width: 130,
      align: 'center',
      dictType: 'sys_area_type',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 80,
      align: 'center',
      dictType: 'sys_search_status',
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
        title: t('编辑区域'),
        onClick: handleForm.bind(this, { areaCode: record.areaCode }),
        auth: 'sys:area:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用区域'),
        popConfirm: {
          title: t('是否确认停用区域'),
          confirm: handleDisable.bind(this, { areaCode: record.areaCode }),
        },
        auth: 'sys:area:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用区域'),
        popConfirm: {
          title: t('是否确认启用区域'),
          confirm: handleEnable.bind(this, { areaCode: record.areaCode }),
        },
        auth: 'sys:area:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除区域'),
        popConfirm: {
          title: t('是否确认删除区域'),
          confirm: handleDelete.bind(this, { areaCode: record.areaCode }),
        },
        auth: 'sys:area:edit',
      },
      {
        icon: 'fluent:add-circle-24-regular',
        title: t('新建下级区域'),
        onClick: handleForm.bind(this, {
          parentCode: record.id,
          parentName: record.areaName,
        }),
        auth: 'sys:area:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse }] = useTable({
    api: areaListData,
    beforeFetch: (params) => {
      params.areaCode = props.treeCode;
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    isTreeTable: true,
    pagination: true,
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
    const res = await areaDisable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleEnable(record: Recordable) {
    const res = await areaEnable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleDelete(record: Recordable) {
    const res = await areaDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

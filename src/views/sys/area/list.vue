<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="fetchSuccess">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="i-bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('折叠全部')">
          <Icon icon="i-bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:area:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
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
<script lang="ts" setup name="ViewsSysAreaList">
  import { watch, nextTick, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { areaDelete, areaListData } from '/@/api/sys/area';
  import { areaDisable, areaEnable } from '/@/api/sys/area';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import { isEmpty } from '/@/utils/is';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCodes: Array as PropType<String[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('sys.area');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('行政区划'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('区域名称'),
        field: 'areaName',
        component: 'Input',
      },
      {
        label: t('区域代码'),
        field: 'areaCode',
        component: 'Input',
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
      {
        label: t('备注信息'),
        field: 'remarks',
        component: 'Input',
      },
    ],
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('区域名称'),
      dataIndex: 'areaName',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
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
        icon: 'i-clarity:note-edit-line',
        title: t('编辑区域'),
        onClick: handleForm.bind(this, { areaCode: record.areaCode }),
        auth: 'sys:area:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用区域'),
        popConfirm: {
          title: t('是否确认停用区域'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'sys:area:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用区域'),
        popConfirm: {
          title: t('是否确认启用区域'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'sys:area:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除区域'),
        popConfirm: {
          title: t('是否确认删除区域'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'sys:area:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级区域'),
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
      params.areaCode = !isEmpty(props.treeCodes) ? props.treeCodes[0] : '';
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
    () => props.treeCodes,
    () => {
      if (!isEmpty(props.treeCodes)) {
        reload();
      }
    },
  );

  function fetchSuccess() {
    if (!isEmpty(props.treeCodes)) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { areaCode: record.areaCode };
    const res = await areaDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { areaCode: record.areaCode };
    const res = await areaEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { areaCode: record.areaCode };
    const res = await areaDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

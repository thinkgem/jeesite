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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:company:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <span class="cursor-pointer" @click="expandCollapse(record)"> ( {{ record.viewCode }} ) </span>
        <a @click="handleForm({ companyCode: record.companyCode })">
          {{ record.companyName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysCompanyList">
  import { watch, nextTick, unref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { companyDelete, companyListData } from '@jeesite/core/api/sys/company';
  import { companyDisable, companyEnable } from '@jeesite/core/api/sys/company';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import { isEmpty } from '@jeesite/core/utils/is';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCodes: Array as PropType<String[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('sys.company');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('公司管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('公司代码'),
        field: 'viewCode_like',
        component: 'Input',
      },
      {
        label: t('公司名称'),
        field: 'companyName',
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
        label: t('公司全称'),
        field: 'fullName',
        component: 'Input',
      },
    ],
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('公司名称'),
      dataIndex: 'companyName',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('公司全称'),
      dataIndex: 'fullName',
      width: 130,
      align: 'left',
    },
    {
      title: t('排序号'),
      dataIndex: 'treeSort',
      width: 90,
      align: 'center',
    },
    {
      title: t('归属区域'),
      dataIndex: 'area.areaName',
      width: 100,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 80,
      align: 'center',
      dictType: 'sys_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      width: 100,
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
        title: t('编辑公司'),
        onClick: handleForm.bind(this, { companyCode: record.companyCode }),
        auth: 'sys:company:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用公司'),
        popConfirm: {
          title: t('是否确认停用公司'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'sys:company:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用公司'),
        popConfirm: {
          title: t('是否确认启用公司'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'sys:company:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除公司'),
        popConfirm: {
          title: t('是否确认删除公司'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'sys:company:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级公司'),
        onClick: handleForm.bind(this, {
          parentCode: record.viewCode,
          parentName: record.companyName,
        }),
        auth: 'sys:company:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse }] = useTable({
    api: companyListData,
    beforeFetch: (params) => {
      params.companyCode = !isEmpty(props.treeCodes) ? props.treeCodes[0] : '';
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
    const params = { companyCode: record.companyCode };
    const res = await companyDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { companyCode: record.companyCode };
    const res = await companyEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { companyCode: record.companyCode };
    const res = await companyDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:menu:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <Icon
          :icon="record.menuIcon || 'ant-design:file-outlined'"
          @click="expandCollapse(record)"
          class="text-base w-5 mr-2"
        />
        <a @click="handleForm({ menuCode: record.menuCode })" :title="record.menuNameRaw">
          {{ record.menuNameRaw }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysMenuList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, watch, nextTick } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { menuDelete, menuDisable, menuEnable, menuListData } from '/@/api/sys/menu';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCode: String,
    sysCode: String,
  });

  const { t } = useI18n('sys.menu');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: router.currentRoute.value.meta.title || t('菜单管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('菜单名称'),
        field: 'menuNameRaw',
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
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('菜单名称'),
      dataIndex: 'menuNameRaw',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('归属模块'),
      dataIndex: 'moduleCodes',
      width: 110,
    },
    {
      title: t('地址'),
      dataIndex: 'menuHref',
      align: 'left',
      width: 130,
    },
    {
      title: t('排序'),
      dataIndex: 'treeSort',
      width: 80,
    },
    {
      title: t('类型'),
      dataIndex: 'menuType',
      width: 70,
      dictType: 'sys_menu_type',
    },
    {
      title: t('可见'),
      dataIndex: 'isShow',
      width: 70,
      dictType: 'sys_show_hide',
    },
    {
      title: t('权限标识'),
      dataIndex: 'permission',
      width: 130,
      align: 'left',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 70,
      dictType: 'sys_status',
    },
    {
      title: t('菜单权重'),
      dataIndex: 'weight',
      width: 120,
      dictType: 'sys_menu_weight',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑菜单'),
        onClick: handleForm.bind(this, { menuCode: record.menuCode }),
        auth: 'sys:menu:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用菜单'),
        popConfirm: {
          title: t('是否确认停用菜单'),
          confirm: handleDisable.bind(this, { menuCode: record.menuCode }),
        },
        auth: 'sys:menu:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用菜单'),
        popConfirm: {
          title: t('是否确认启用菜单'),
          confirm: handleEnable.bind(this, { menuCode: record.menuCode }),
        },
        auth: 'sys:menu:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除菜单'),
        popConfirm: {
          title: t('是否确认删除菜单'),
          confirm: handleDelete.bind(this, { menuCode: record.menuCode }),
        },
        auth: 'sys:menu:edit',
      },
      {
        icon: 'fluent:add-circle-24-regular',
        title: t('新建下级菜单'),
        onClick: handleForm.bind(this, {
          parentCode: record.menuCode,
          parentName: record.menuNameRaw,
        }),
        auth: 'sys:menu:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse }] = useTable({
    api: menuListData,
    beforeFetch: (params) => {
      params.sysCode = props.sysCode || 'default';
      params.menuCode = props.treeCode;
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

  watch([() => props.treeCode, () => props.sysCode], () => {
    reload();
  });

  function fetchSuccess() {
    if (props.treeCode) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    record.sysCode = props.sysCode;
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const res = await menuDisable(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const res = await menuEnable(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const res = await menuDelete(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    if (record.parentCode) {
      // showMessage('刷新节点：' + record.parentCode);
    }
    reload();
  }
</script>

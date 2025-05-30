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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:menu:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <Icon
          :icon="record.menuIcon || 'i-ant-design:file-outlined'"
          @click="expandCollapse(record)"
          class="mr-2 w-5 text-base"
        />
        <a @click="handleForm({ menuCode: record.menuCode })" :title="record.menuNameRaw">
          {{ record.menuNameRaw }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysMenuList">
  import { unref, watch, nextTick } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { menuDelete, menuDisable, menuEnable, menuListData } from '@jeesite/core/api/sys/menu';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import { isEmpty } from '@jeesite/core/utils/is';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCodes: Array as PropType<String[]>,
    sysCode: String,
    isLeaf: Boolean,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('sys.menu');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('菜单管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('菜单名称'),
        field: 'menuNameRaw',
        component: 'Input',
      },
      {
        label: t('菜单地址'),
        field: 'menuHref',
        component: 'Input',
      },
      {
        label: t('权限标识'),
        field: 'permission',
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
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
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
      title: t('组件'),
      dataIndex: 'component',
      align: 'center',
      width: 80,
    },
    {
      title: t('排序号'),
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
    width: 180,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑菜单'),
        onClick: handleForm.bind(this, { menuCode: record.menuCode }),
        auth: 'sys:menu:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用菜单'),
        popConfirm: {
          title: t('是否确认停用菜单'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'sys:menu:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用菜单'),
        popConfirm: {
          title: t('是否确认启用菜单'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'sys:menu:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除菜单'),
        popConfirm: {
          title: t('是否确认删除菜单'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'sys:menu:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级菜单'),
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
      params.menuCode = !isEmpty(props.treeCodes) ? props.treeCodes[0] : '';
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

  watch([() => props.treeCodes, () => props.sysCode], () => {
    if (!isEmpty(props.treeCodes) && props.sysCode) {
      reload();
    }
    // if (props.isLeaf) {
    //   handleForm({ menuCode: props.treeCode });
    // }
  });

  function fetchSuccess() {
    if (!isEmpty(props.treeCodes)) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    record.sysCode = props.sysCode;
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { menuCode: record.menuCode };
    const res = await menuDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { menuCode: record.menuCode };
    const res = await menuEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { menuCode: record.menuCode };
    const res = await menuDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

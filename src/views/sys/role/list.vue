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
        <a-button type="primary" @click="handleForm({ op: 'add' })" v-auth="'sys:role:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ roleCode: record.roleCode, op: 'edit' })">
          {{ record.roleName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
    <FormAuthDataScope @register="registerAuthDataSourceDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysRoleList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { roleDelete, roleListData } from '/@/api/sys/role';
  import { roleDisable, roleEnable } from '/@/api/sys/role';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';
  import FormAuthDataScope from './formAuthDataScope.vue';

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: router.currentRoute.value.meta.title || t('角色管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('角色名称'),
        field: 'roleName',
        component: 'Input',
      },
      {
        label: t('角色编码'),
        field: 'roleCode_like',
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
        label: t('用户类型'),
        field: 'userType',
        component: 'Select',
        componentProps: {
          dictType: 'sys_user_type',
          allowClear: true,
        },
      },
      {
        label: t('系统角色'),
        field: 'isSys',
        component: 'Select',
        componentProps: {
          dictType: 'sys_yes_no',
          allowClear: true,
        },
      },
      {
        label: t('角色分类'),
        field: 'roleType',
        component: 'Select',
        componentProps: {
          dictType: 'sys_role_type',
          allowClear: true,
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('角色名称'),
      dataIndex: 'roleName',
      key: 'a.role_name',
      sorter: true,
      width: 130,
      align: 'center',
      slots: { customRender: 'firstColumn' },
    },
    {
      title: t('角色编码'),
      dataIndex: 'roleCode',
      key: 'a.role_code',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('排序号'),
      dataIndex: 'roleSort',
      key: 'a.role_sort',
      sorter: true,
      width: 90,
      align: 'center',
    },
    {
      title: t('用户类型'),
      dataIndex: 'userType',
      key: 'a.user_type',
      sorter: true,
      width: 90,
      align: 'center',
      dictType: 'sys_user_type',
    },
    {
      title: t('系统角色'),
      dataIndex: 'isSys',
      key: 'a.is_sys',
      sorter: true,
      width: 90,
      align: 'center',
      dictType: 'sys_yes_no',
    },
    {
      title: t('角色分类'),
      dataIndex: 'roleType',
      key: 'a.role_type',
      sorter: true,
      width: 90,
      align: 'center',
      dictType: 'sys_role_type',
    },
    {
      title: t('数据范围'),
      dataIndex: 'dataScope',
      key: 'a.data_scope',
      sorter: true,
      width: 100,
      align: 'center',
      dictType: 'sys_role_data_scope',
    },
    {
      title: t('业务范围'),
      dataIndex: 'bizScope',
      key: 'a.biz_scope',
      sorter: true,
      width: 100,
      align: 'center',
      dictType: 'sys_role_biz_scope',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 90,
      align: 'left',
      dictType: 'sys_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      key: 'a.remarks',
      sorter: true,
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑角色'),
        onClick: handleForm.bind(this, { roleCode: record.roleCode, op: 'edit' }),
        auth: 'sys:role:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用角色'),
        popConfirm: {
          title: t('是否确认停用角色'),
          confirm: handleDisable.bind(this, { roleCode: record.roleCode }),
        },
        auth: 'sys:role:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用角色'),
        popConfirm: {
          title: t('是否确认启用角色'),
          confirm: handleEnable.bind(this, { roleCode: record.roleCode }),
        },
        auth: 'sys:role:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除角色'),
        popConfirm: {
          title: t('是否确认删除角色'),
          confirm: handleDelete.bind(this, { roleCode: record.roleCode }),
        },
        auth: 'sys:role:edit',
      },
    ],
    dropDownActions: (record: Recordable) => [
      {
        icon: 'ant-design:check-square-outlined',
        label: t('授权菜单'),
        onClick: handleForm.bind(this, { roleCode: record.roleCode, op: 'auth' }),
        auth: 'sys:role:edit',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        label: t('数据权限'),
        onClick: handleFormAuthDataScope.bind(this, { roleCode: record.roleCode, op: 'auth' }),
        auth: 'sys:role:edit',
      },
      // {
      //   icon: 'ant-design:user-outlined',
      //   label: t('分配用户'),
      //   onClick: handleForm.bind(this, { roleCode: record.roleCode }),
      //   auth: 'sys:role:edit',
      // },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerAuthDataSourceDrawer, { openDrawer: openAuthDataScopeDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: roleListData,
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

  async function handleDisable(record: Recordable) {
    const res = await roleDisable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleEnable(record: Recordable) {
    const res = await roleEnable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleDelete(record: Recordable) {
    const res = await roleDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleFormAuthDataScope(record: Recordable) {
    openAuthDataScopeDrawer(true, record);
  }

  function handleSuccess() {
    reload();
  }
</script>

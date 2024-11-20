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
        <a-button type="primary" @click="handleForm({ op: 'add' })" v-auth="'sys:corpAdmin:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ userCode: record.userCode, op: 'edit' })" :title="record.loginCode">
          {{ record.loginCode }}
        </a>
      </template>
      <template #corpColumn="{ record }">
        <a
          @click="reload({ searchInfo: { corpCode_: record.corpCode_ } })"
          :title="record.corpCode_"
        >
          {{ record.corpCode_ }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysCorpAdminList">
  import { onMounted, ref, unref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import {
    corpAdminDelete,
    corpAdminListData,
    corpAdminList,
  } from '@jeesite/core/api/sys/corpAdmin';
  import {
    corpAdminResetpwd,
    corpAdminDisable,
    corpAdminEnable,
  } from '@jeesite/core/api/sys/corpAdmin';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'simple-line-icons:badge',
    value: meta.title || t('租户管理员'),
  };
  const useCorpModel = ref<boolean>(false);
  const currentCorpCode = ref<string>('');
  const currentCorpName = ref<string>('');

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 100,
    schemas: [
      {
        label: t('登录账号'),
        field: 'loginCode',
        component: 'Input',
      },
      {
        label: t('用户昵称'),
        field: 'userName',
        component: 'Input',
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: 'sys_user_status',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('登录账号'),
      dataIndex: 'loginCode',
      key: 'a.login_code',
      sorter: true,
      width: 100,
      slot: 'firstColumn',
    },
    {
      title: t('用户昵称'),
      dataIndex: 'userName',
      key: 'a.user_name',
      sorter: true,
      width: 100,
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
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 100,
    },
    {
      title: t('电子邮箱'),
      dataIndex: 'email',
      key: 'a.email',
      sorter: true,
      width: 100,
    },
    {
      title: t('手机号码'),
      dataIndex: 'mobile',
      key: 'a.mobile',
      sorter: true,
      width: 100,
    },
    {
      title: t('办公电话'),
      dataIndex: 'phone',
      key: 'a.phone',
      sorter: true,
      width: 100,
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑用户'),
        onClick: handleForm.bind(this, { userCode: record.userCode, op: 'edit' }),
        auth: 'sys:corpAdmin:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用用户'),
        popConfirm: {
          title: t('是否确认停用用户'),
          confirm: handleDisable.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:corpAdmin:updateStatus',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用用户'),
        popConfirm: {
          title: t('是否确认启用用户'),
          confirm: handleEnable.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:corpAdmin:updateStatus',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除用户'),
        popConfirm: {
          title: t('是否确认删除用户'),
          confirm: handleDelete.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:corpAdmin:edit',
      },
      {
        icon: 'i-fluent:add-12-filled',
        title: t('新增管理员'),
        onClick: handleForm.bind(this, {
          corpCode_: record.corpCode_,
          corpName_: record.corpName_,
          op: 'addAdmin',
        }),
        auth: 'sys:corpAdmin:edit',
        ifShow: () => useCorpModel.value,
      },
    ],
    dropDownActions: (record: Recordable) => [
      {
        icon: 'i-ant-design:reload-outlined',
        label: t('重置密码'),
        onClick: handleResetpwd.bind(this, { userCode: record.userCode }),
        auth: 'sys:corpAdmin:resetpwd',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: corpAdminListData,
    beforeFetch: (params) => {
      return params;
    },
    immediate: false,
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
  });

  onMounted(async () => {
    const res = await corpAdminList();
    useCorpModel.value = res.useCorpModel;
    currentCorpCode.value = res.currentCorpCode;
    currentCorpName.value = res.currentCorpName;
    reload();
  });

  function handleForm(record: Recordable) {
    if (record.op == 'add') {
      if (useCorpModel.value) {
        record.op = 'addCorp';
      } else {
        record.corpCode_ = currentCorpCode.value;
        record.corpName_ = currentCorpName.value;
        record.op = 'addAdmin';
      }
    }
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const res = await corpAdminDisable(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const res = await corpAdminEnable(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const res = await corpAdminDelete(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleResetpwd(record: Recordable) {
    const res = await corpAdminResetpwd(record);
    showMessage(res.message);
  }

  function handleSuccess(_record: Recordable) {
    reload();
  }
</script>

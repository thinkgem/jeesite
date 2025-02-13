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
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:secAdmin:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ userCode: record.userCode })" :title="record.loginCode">
          {{ record.loginCode }}
        </a>
      </template>
      <template #form-formTop>
        <div class="mx-2 mb-3">
          <Alert
            v-if="showAlert"
            :afterClose="handleAlertClose"
            :message="`二级管理员是系统管理员的下级管理员，其控制权限类型包括：
                    （1）拥有的权限：您的上级给您分配什么权限，您就能看什么数据，包括您所属机构及下级数据或其它树形结构数据的本级及下级数据；
                    （2）管理的权限：您能将某些数据的权限分配给他人，但自己没有该数据的读取权限，一般用于管理者身份的权限控制。
                    当前控制权限类型为：${ctrlPermi == '1' ? '拥有' : '管理'}的权限，可在后端配置文件修改 user.adminCtrlPermi 参数更改类型。`"
            type="info"
            banner
            closable
          />
        </div>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysSecAdminList">
  import { onMounted, watch, ref, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { secAdminDelete, secAdminListData, secAdminList } from '/@/api/sys/secAdmin';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';
  import { Alert } from 'ant-design-vue';

  const props = defineProps({
    treeCode: String,
  });

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'simple-line-icons:user-female',
    value: meta.title || t('二级管理员'),
  };
  const ctrlPermi = ref('');
  const postList = ref<Recordable>([]);
  const roleList = ref<Recordable>([]);

  const showAlert = ref<boolean>(false);

  onMounted(async () => {
    const days = 1000 * 60 * 60 * 24;
    const lastClosedTime = localStorage.getItem('jeesite-alert-time-sec-admin');
    if (!lastClosedTime || (new Date().getTime() - Number(lastClosedTime)) / days >= 100) {
      showAlert.value = true;
    }
  });

  function handleAlertClose() {
    showAlert.value = false;
    localStorage.setItem('jeesite-alert-time-sec-admin', String(new Date().getTime()));
    redoHeight();
  }

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 100,
    schemas: [
      {
        label: t('账号'),
        field: 'loginCode',
        component: 'Input',
      },
      {
        label: t('昵称'),
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
      {
        label: t('姓名'),
        field: 'refName',
        component: 'Input',
      },
      {
        label: t('手机'),
        field: 'mobile',
        component: 'Input',
      },
      {
        label: t('邮箱'),
        field: 'email',
        component: 'Input',
      },
      {
        label: t('电话'),
        field: 'phone',
        component: 'Input',
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
        onClick: handleForm.bind(this, { userCode: record.userCode }),
        auth: 'sys:secAdmin:edit',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除用户'),
        popConfirm: {
          title: t('是否确认删除用户'),
          confirm: handleDelete.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:secAdmin:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, getForm, redoHeight }] = useTable({
    api: secAdminListData,
    beforeFetch: (params) => {
      params.ctrlPermi = ctrlPermi.value;
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
    const res = await secAdminList();
    postList.value = res.postList?.map((item) => ({
      label: item.postName,
      value: item.postCode,
    }));
    roleList.value = res.roleList?.map((item) => ({
      label: item.roleName,
      value: item.roleCode,
    }));
    ctrlPermi.value = res.ctrlPermi || '2';
    reload();
  });

  watch(
    () => props.treeCode,
    async () => {
      await getForm().setFieldsValue({
        'employee.office.officeCode': props.treeCode,
      });
      reload();
    },
  );

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDelete(record: Recordable) {
    const res = await secAdminDelete(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(_record: Recordable) {
    reload();
  }
</script>

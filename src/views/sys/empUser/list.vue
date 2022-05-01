<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="pr-1 m-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button type="default" @click="handleExport()">
          <Icon icon="ant-design:download-outlined" /> {{ t('导出') }}
        </a-button>
        <a-button type="default" @click="handleImport()">
          <Icon icon="ant-design:upload-outlined" /> {{ t('导入') }}
        </a-button>
        <a-button type="primary" @click="handleForm({ op: 'add' })" v-auth="'sys:empUser:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ userCode: record.userCode, op: 'edit' })" :title="record.loginCode">
          {{ record.loginCode }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
    <FormAuthDataScope @register="registerAuthDataSourceDrawer" @success="handleSuccess" />
    <FormImport @register="registerImportModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysEmpUserList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, onMounted, watch, ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGlobSetting } from '/@/hooks/setting';
  import { downloadByUrl } from '/@/utils/file/download';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { empUserDelete, empUserListData, empUserList } from '/@/api/sys/empUser';
  import { resetpwd, empUserDisable, empUserEnable } from '/@/api/sys/empUser';
  import { officeTreeData } from '/@/api/sys/office';
  import { companyTreeData } from '/@/api/sys/company';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';
  import FormAuthDataScope from './formAuthDataScope.vue';
  import FormImport from './formImport.vue';

  const props = defineProps({
    treeCode: String,
  });

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: router.currentRoute.value.meta.title || t('字典管理'),
  };
  const ctrlPermi = ref('');
  const postList = ref<Recordable>([]);
  const roleList = ref<Recordable>([]);

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 60,
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
        label: t('机构'),
        field: 'employee.office.officeCode',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
        },
      },
      {
        label: t('公司'),
        field: 'employee.company.companyCode',
        component: 'TreeSelect',
        componentProps: {
          api: companyTreeData,
        },
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
      {
        label: t('岗位'),
        field: 'employee.postCode',
        component: 'Select',
        componentProps: {
          options: postList,
          allowClear: true,
        },
      },
      {
        label: t('角色'),
        field: 'roleCode',
        component: 'Select',
        componentProps: {
          options: roleList,
          allowClear: true,
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
      slots: { customRender: 'firstColumn' },
    },
    {
      title: t('用户昵称'),
      dataIndex: 'userName',
      key: 'a.user_name',
      sorter: true,
      width: 100,
    },
    {
      title: t('员工姓名'),
      dataIndex: 'refName',
      key: 'a.ref_name',
      sorter: true,
      width: 100,
    },
    {
      title: t('归属机构'),
      dataIndex: 'employee.office.officeName',
      key: 'o.office_name',
      sorter: true,
      width: 100,
    },
    {
      title: t('归属公司'),
      dataIndex: 'employee.company.companyName',
      key: 'c.company_name',
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
      width: 130,
    },
    {
      title: t('电子邮箱'),
      dataIndex: 'email',
      key: 'a.email',
      sorter: true,
      width: 130,
    },
    {
      title: t('手机号码'),
      dataIndex: 'mobile',
      key: 'a.mobile',
      sorter: true,
      width: 130,
    },
    {
      title: t('办公电话'),
      dataIndex: 'phone',
      key: 'a.phone',
      sorter: true,
      width: 130,
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑用户'),
        onClick: handleForm.bind(this, { userCode: record.userCode, op: 'edit' }),
        auth: 'sys:empUser:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用用户'),
        popConfirm: {
          title: t('是否确认停用用户'),
          confirm: handleDisable.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:empUser:updateStatus',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用用户'),
        popConfirm: {
          title: t('是否确认启用用户'),
          confirm: handleEnable.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:empUser:updateStatus',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除用户'),
        popConfirm: {
          title: t('是否确认删除用户'),
          confirm: handleDelete.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:empUser:edit',
      },
    ],
    dropDownActions: (record: Recordable) => [
      {
        icon: 'ant-design:check-square-outlined',
        label: t('分配角色'),
        onClick: handleForm.bind(this, { userCode: record.userCode, op: 'auth' }),
        auth: 'sys:empUser:authRole',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        label: t('数据权限'),
        onClick: handleFormAuthDataScope.bind(this, { userCode: record.userCode, op: 'auth' }),
        auth: 'sys:empUser:authDataScope',
      },
      {
        icon: 'ant-design:reload-outlined',
        label: t('重置密码'),
        onClick: handleResetpwd.bind(this, { userCode: record.userCode }),
        auth: 'sys:empUser:resetpwd',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerAuthDataSourceDrawer, { openDrawer: openAuthDataScopeDrawer }] = useDrawer();
  const [registerTable, { reload, getForm }] = useTable({
    api: empUserListData,
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
    const res = await empUserList();
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

  async function handleExport() {
    const { ctxAdminPath } = useGlobSetting();
    downloadByUrl({
      url: ctxAdminPath + '/sys/empUser/exportData',
      target: '_self',
    });
  }

  const [registerImportModal, { openModal: importModal }] = useModal();

  function handleImport() {
    importModal(true, {});
  }

  async function handleDisable(record: Recordable) {
    const res = await empUserDisable(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const res = await empUserEnable(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const res = await empUserDelete(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleFormAuthDataScope(record: Recordable) {
    openAuthDataScopeDrawer(true, record);
  }

  async function handleResetpwd(record: Recordable) {
    const res = await resetpwd(record);
    showMessage(res.message);
  }

  function handleSuccess(_record: Recordable) {
    reload();
  }
</script>

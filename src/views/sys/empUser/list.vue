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
        <Popconfirm :title="t('是否确认删除选中的用户吗？')" @confirm="handleDeleteSelected()">
          <a-button danger class="ml-4" type="default" v-if="selectedRowKeysRef.length > 0" v-auth="'sys:empUser:edit'">
            <Icon icon="i-ant-design:delete-outlined" color="error" /> {{ t('删除') }} ({{ selectedRowKeysRef.length }})
          </a-button>
        </Popconfirm>
      </template>
      <template #toolbar>
        <a-button type="default" :loading="loading" @click="handleExport()">
          <Icon icon="i-ant-design:download-outlined" /> {{ t('导出') }}
        </a-button>
        <a-button type="default" @click="handleImport()">
          <Icon icon="i-ant-design:import-outlined" /> {{ t('导入') }}
        </a-button>
        <a-button type="primary" @click="handleForm({ op: 'add' })" v-auth="'sys:empUser:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
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
<script lang="ts" setup name="ViewsSysEmpUserList">
  import { onMounted, watch, ref, unref } from 'vue';
  import { Popconfirm } from 'ant-design-vue';
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
  import { isEmpty } from '/@/utils/is';
  import InputForm from './form.vue';
  import FormAuthDataScope from './formAuthDataScope.vue';
  import FormImport from './formImport.vue';
  import { useLocaleStore } from '/@/store/modules/locale';
  import { LOCALE } from '/@/settings/localeSetting';

  const props = defineProps({
    treeCodes: Array as PropType<String[]>,
    treeName: String,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'simple-line-icons:user',
    value: meta.title || t('用户管理'),
  };
  const ctrlPermi = ref('');
  const postList = ref<Recordable>([]);
  const roleList = ref<Recordable>([]);
  const localeStore = useLocaleStore();
  const loading = ref(false);

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: localeStore.getLocale == LOCALE.ZH_CN ? 60 : 100,
    showAdvancedButton: true,
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
        fieldLabel: 'employee.office.officeName',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
          allowClear: true,
        },
      },
      {
        label: t('公司'),
        field: 'employee.company.companyCode',
        fieldLabel: 'employee.company.companyName',
        component: 'TreeSelect',
        componentProps: {
          api: companyTreeData,
          allowClear: true,
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
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
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
        icon: 'i-clarity:note-edit-line',
        title: t('编辑用户'),
        onClick: handleForm.bind(this, { userCode: record.userCode, op: 'edit' }),
        auth: 'sys:empUser:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
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
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t((record.status === '3' ? '解冻' : '启用') + '用户'),
        popConfirm: {
          title: t('是否确认' + (record.status === '3' ? '解冻' : '启用') + '用户'),
          confirm: handleEnable.bind(this, {
            userCode: record.userCode,
            freeze: record.status === '3',
          }),
        },
        auth: 'sys:empUser:updateStatus',
        ifShow: () => record.status === '2' || record.status === '3' || record.status === '4',
      },
      {
        icon: 'i-ant-design:delete-outlined',
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
        icon: 'i-ant-design:check-square-outlined',
        label: t('分配角色'),
        onClick: handleForm.bind(this, { userCode: record.userCode, op: 'auth' }),
        auth: 'sys:empUser:authRole',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        label: t('数据权限'),
        onClick: handleFormAuthDataScope.bind(this, { userCode: record.userCode, op: 'auth' }),
        auth: 'sys:empUser:authDataScope',
      },
      {
        icon: 'i-ant-design:reload-outlined',
        label: t('重置密码'),
        popConfirm: {
          title: t('是否确认重置密码'),
          confirm: handleResetpwd.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:empUser:resetpwd',
      },
    ],
  };

  const selectedRowKeysRef = ref<string[]>([]);
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
    showSearchForm: true,
    canResize: true,
    // pagination: { defaultPageSize: 10 },
    defaultRowSelection: {
      onChange: (selectedRowKeys: string[], _selectedRows: Recordable[]) => {
        selectedRowKeysRef.value = selectedRowKeys;
      },
    },
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
    () => props.treeCodes,
    async () => {
      if (!isEmpty(props.treeCodes)) {
        await getForm().setFieldsValue({
          'employee.office.officeCode': props.treeCodes[0],
          'employee.office.officeName': props.treeName,
        });
        await reload();
      }
    },
  );

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleExport() {
    loading.value = true;
    const { ctxAdminPath } = useGlobSetting();
    await downloadByUrl({
      url: ctxAdminPath + '/sys/empUser/exportData',
      params: getForm().getFieldsValue(),
    });
    loading.value = false;
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

  async function handleDeleteSelected() {
    let message: any[] = [];
    for (const userCode of selectedRowKeysRef.value) {
      try {
        await empUserDelete({ userCode });
      } catch (e: any) {
        const msg = e.response?.data?.message || e.message;
        if (msg === '演示模式，不允许操作！') {
          showMessage(msg);
          return;
        }
        message.push(msg);
      }
    }
    if (message.length == 0) {
      message.push(t('批量删除成功！'));
    }
    selectedRowKeysRef.value = [];
    showMessage(message.join(', '));
    handleSuccess({});
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

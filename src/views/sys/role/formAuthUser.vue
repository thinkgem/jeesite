<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :showOkBtn="false"
    @register="registerDrawer"
    @close="handleClose"
    width="80%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <template #centerFooter>
      <a-button type="primary" @click="handleForm" v-auth="'sys:role:edit'">
        <Icon icon="i-fluent:add-12-filled" /> {{ t('添加用户') }}
      </a-button>
      <ListSelect
        ref="listSelectRef"
        selectType="empUserSelect"
        :checkbox="true"
        @select="handleUserSelect"
        v-show="false"
      />
      <Popconfirm :title="t('是否确认取消选中用户的角色')" @confirm="handleDelete">
        <a-button type="error" v-auth="'sys:role:edit'">
          <Icon icon="i-ant-design:close-outlined" /> {{ t('批量取消') }}
        </a-button>
      </Popconfirm>
    </template>
    <BasicTable @register="registerTable" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref, unref, computed } from 'vue';
  import { Popconfirm } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicTable, BasicColumn, useTable, TableRowSelection } from '/@/components/Table';
  import { FormProps } from '/@/components/Form';
  import { ListSelect } from '/@/components/ListSelect';
  import { formAuthUser, saveAuthUser, deleteAuthUser } from '/@/api/sys/role';
  import { userListData } from '/@/api/sys/user';
  import { useDict } from '/@/components/Dict';

  //const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({});
  const getTitle = computed(() => {
    const r = record.value;
    const { getDictLabel } = useDict();
    return {
      icon: meta.icon || 'ant-design:book-outlined',
      value:
        t('角色分配用户') +
        ' (' +
        r.roleName +
        '-' +
        r.viewCode +
        '-' +
        getDictLabel('sys_user_type', r.userType, t('未设置')) +
        ')',
    };
  });
  const listSelectRef = ref<any>(null);

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
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
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
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
  ];

  const actionColumn: BasicColumn = {
    width: 60,
    actions: (record: Recordable) => [
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('取消授权'),
        popConfirm: {
          title: t('是否取消该用户角色'),
          confirm: handleDelete.bind(this, { userCode: record.userCode }),
        },
        auth: 'sys:secAdmin:edit',
      },
    ],
  };

  const rowSelection: TableRowSelection = {
    type: 'checkbox',
  };

  const [registerTable, { reload, getSelectRowKeys, setSelectedRowKeys }] = useTable({
    api: userListData,
    beforeFetch: (params) => {
      params.roleCode = record.value.roleCode;
      params.userType = record.value.userType;
      return params;
    },
    immediate: false,
    columns: tableColumns,
    actionColumn: actionColumn,
    rowSelection: rowSelection,
    formConfig: searchForm,
    showTableSetting: false,
    useSearchForm: true,
    canResize: true,
    resizeHeightOffset: 60,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    const res = await formAuthUser(data);
    record.value = (res.role || {}) as Recordable;
    handleSuccess();
    setDrawerProps({ loading: false });
  });

  function handleForm() {
    listSelectRef.value.openSelectModal();
  }

  async function handleUserSelect(values: Recordable[]) {
    if (values.length == 0) {
      showMessage(t('请选中要授权角色的用户'));
      return;
    }
    const params = {
      roleCode: record.value.roleCode,
      userRoleString: values.map((e) => e.userCode).join(','),
    };
    const res = await saveAuthUser(params);
    showMessage(res.message);
    handleSuccess();
    listSelectRef.value.setSelectList([]);
  }

  async function handleDelete(event: any) {
    let userRoleString: string;
    if (event && event.userCode) {
      userRoleString = event.userCode;
    } else {
      const selectedRowKeys = getSelectRowKeys();
      if (selectedRowKeys.length == 0) {
        showMessage(t('请选中要取消角色的用户'));
        return;
      }
      userRoleString = selectedRowKeys.join(',');
    }
    const params = { roleCode: record.value.roleCode, userRoleString };
    const res = await deleteAuthUser(params);
    showMessage(res.message);
    handleSuccess();
    setSelectedRowKeys([]);
  }

  function handleSuccess() {
    reload();
  }

  async function handleClose() {
    closeDrawer();
  }
</script>

<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:empUser:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="pr-1 m-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #employeeOfficeList>
        <BasicTable
          @register="registerEmployeeOfficeTable"
          @row-click="handleEmployeeOfficeRowClick"
        />
        <a-button class="mt-2" @click="handleEmployeeOfficeAdd">
          <Icon icon="ant-design:plus-circle-outlined" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #userRoleString>
        <BasicTable @register="registerUserRoleTable" />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysEmpUserForm',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { checkEmpNo, empUserSave, empUserForm } from '/@/api/sys/empUser';
  import { checkLoginCode } from '/@/api/sys/user';
  import { officeTreeData } from '/@/api/sys/office';
  import { companyTreeData } from '/@/api/sys/company';
  import { roleTreeData } from '/@/api/sys/role';
  import { BasicTable, useTable } from '/@/components/Table';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const record = ref<Recordable>({});
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增用户') : t('编辑用户'),
  };
  const ctrlPermi = ref<String>('');
  const op = ref<String>('');

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('归属机构'),
      field: 'employee.office.officeCode',
      fieldLabel: 'employee.office.officeName',
      component: 'TreeSelect',
      required: true,
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('归属公司'),
      field: 'employee.company.companyCode',
      fieldLabel: 'employee.company.companyName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('登录账号'),
      field: 'loginCode',
      component: 'Input',
      componentProps: {
        maxlength: 20,
      },
      rules: [
        { required: true },
        { min: 4, max: 20, message: t('请输入长度在 4 到 20 个字符之间') },
        { pattern: /^[\u0391-\uFFE5\w]+$/, message: t('不能输入特殊字符') },
        {
          validator(_rule, value) {
            return new Promise((resolve, reject) => {
              if (!value || value === '') return resolve();
              checkLoginCode(record.value.loginCode || '', value)
                .then((res) => (res ? resolve() : reject(t('登录账号已存在'))))
                .catch((err) => reject(err.message || t('验证失败')));
            });
          },
        },
      ],
    },
    {
      label: t('用户昵称'),
      field: 'userName',
      component: 'Input',
      componentProps: {
        maxlength: 32,
      },
      required: true,
    },
    {
      label: t('电子邮箱'),
      field: 'email',
      component: 'Input',
      componentProps: {
        maxlength: 300,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
      rules: [{ type: 'email', message: t('请填写正确的邮箱地址') }],
    },
    {
      label: t('手机号码'),
      field: 'mobile',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('办公电话'),
      field: 'phone',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('权重'),
      field: 'userWeight',
      helpMessage: '排序，权重越大排名越靠前，请填写数字。',
      component: 'InputNumber',
      defaultValue: 0,
      componentProps: {
        maxlength: 8,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },

    {
      label: t('员工信息'),
      field: 'employeeInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },

    {
      label: t('员工工号'),
      field: 'employee.empNo',
      component: 'Input',
      componentProps: {
        maxlength: 32,
      },
      rules: [
        { required: false },
        {
          validator(_rule, value) {
            return new Promise((resolve, reject) => {
              if (!value || value === '') return resolve();
              checkEmpNo(record.value.employee?.empNo || '', value)
                .then((res) => (res ? resolve() : reject(t('员工工号已存在'))))
                .catch((err) => reject(err.message || t('验证失败')));
            });
          },
        },
      ],
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('员工姓名'),
      field: 'employee.empName',
      component: 'Input',
      componentProps: {
        maxlength: 32,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('员工姓名'),
      field: 'employee.empName',
      component: 'Input',
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('所在岗位'),
      field: 'employee.employeePosts',
      component: 'Select',
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('英文名'),
      field: 'employee.empNameEn',
      component: 'Input',
      componentProps: {
        maxlength: 32,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('附属机构'),
      field: 'employee.employeeOfficeList',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'employeeOfficeList',
      show: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { lg: 24, md: 24 },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },

    {
      label: t('角色信息'),
      field: 'roleInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
      ifShow: () => op.value === 'add' || op.value === 'auth',
    },
    {
      label: t('分配角色'),
      field: 'userRoleString',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'userRoleString',
      show: () => op.value === 'add' || op.value === 'auth',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
    labelWidth: 120,
  });

  const [registerEmployeeOfficeTable, employeeOfficeTable] = useTable({
    actionColumn: {
      width: 60,
      actions: (record: Recordable) => [
        {
          icon: 'ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: '是否确认删除',
            confirm: handleEmployeeOfficeDelete.bind(this, record),
          },
          auth: 'sys:empUser:edit',
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
  });

  async function setEmployeeOfficeTableData(res: Recordable) {
    employeeOfficeTable.setColumns([
      {
        title: t('附属机构'),
        dataIndex: 'officeCode',
        dataLabel: 'officeName',
        width: 100,
        align: 'left',
        editRow: true,
        editComponent: 'TreeSelect',
        editComponentProps: {
          api: officeTreeData,
          params: { ctrlPermi },
          canSelectParent: false,
        },
        editRule: true,
      },
      {
        title: t('附属岗位'),
        dataIndex: 'postCode',
        dataLabel: 'postName',
        width: 100,
        align: 'left',
        editRow: true,
        editComponent: 'Select',
        editComponentProps: {
          options: res.postList?.map((item) => ({
            label: item.postName,
            value: item.postCode,
          })),
        },
        editRule: true,
      },
    ]);
    employeeOfficeTable.setTableData(record.value.employee?.employeeOfficeList || []);
  }

  function handleEmployeeOfficeRowClick(record: Recordable) {
    record.onEdit?.(true, false);
  }

  function handleEmployeeOfficeAdd() {
    employeeOfficeTable.insertTableDataRecord({
      id: 'rid_' + new Date().getTime(),
      editable: true,
    });
  }

  function handleEmployeeOfficeDelete(record: Recordable) {
    employeeOfficeTable.deleteTableDataRecord(record);
  }

  async function getEmployeeOfficeList() {
    let employeeOfficeListValid = true;
    let employeeOfficeList = employeeOfficeTable.getDataSource();
    for (const record of employeeOfficeList) {
      if (!(await record.onEdit?.(false, true))) {
        employeeOfficeListValid = false;
      }
    }
    if (!employeeOfficeListValid) {
      throw new Error('employeeOfficeList valid.');
    }
    return employeeOfficeList;
  }

  const [registerUserRoleTable, userRoleTable] = useTable({
    columns: [
      {
        title: t('角色名称'),
        dataIndex: 'name',
        width: 260,
        align: 'center',
      },
      {
        title: t('角色编码'),
        dataIndex: 'id',
        width: 260,
        align: 'center',
      },
    ],
    rowSelection: { type: 'checkbox' },
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
  });

  async function setUserRoleTableData(res: Recordable) {
    const dataSource = await roleTreeData({
      userType: 'employee',
      ctrlPermi: ctrlPermi.value,
    });
    userRoleTable.setTableData(dataSource || []);
    userRoleTable.setSelectedRowKeys(res.roleList?.map((item) => item.id) || []);
  }

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    resetFields();
    employeeOfficeTable.setTableData([]);
    userRoleTable.setTableData([]);
    userRoleTable.setSelectedRowKeys([]);
    setDrawerProps({ loading: true });
    op.value = data.op || 'add';
    const res = await empUserForm(data);
    record.value = (res.empUser || {}) as Recordable;
    ctrlPermi.value = res.ctrlPermi || '2';
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'loginCode',
        componentProps: {
          disabled: op.value === 'auth',
        },
      },
      {
        field: 'userName',
        componentProps: {
          disabled: op.value === 'auth',
        },
      },
    ]);
    if (op.value === 'add' || op.value === 'edit') {
      updateSchema([
        {
          field: 'employee.office.officeCode',
          componentProps: {
            api: officeTreeData,
            params: { ctrlPermi },
            canSelectParent: false,
          },
        },
        {
          field: 'employee.company.companyCode',
          componentProps: {
            api: companyTreeData,
            params: { ctrlPermi },
            canSelectParent: false,
          },
        },
        {
          field: 'employee.employeePosts',
          componentProps: {
            options: res.postList.map((item) => ({
              label: item.postName,
              value: item.postCode,
            })),
            mode: 'multiple',
          },
        },
      ]);
      setEmployeeOfficeTableData(res);
    }
    if (op.value === 'add' || op.value === 'auth') {
      setUserRoleTableData(res);
    }
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        userCode: record.value.userCode,
        op: op.value,
      };
      data.userType = 'employee';
      if (op.value === 'add' || op.value === 'edit') {
        if (!data.employee.empNo) {
          data.employee.empNo = record.value.loginCode;
        }
        data.employee.employeeOfficeList = await getEmployeeOfficeList();
      }
      if (op.value === 'add' || op.value === 'auth') {
        data.userRoleString = userRoleTable.getSelectRowKeys().join(',');
      }
      // console.log('submit', params, data, record);
      const res = await empUserSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('您填写的信息有误，请根据提示修正。'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>

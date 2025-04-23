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
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #employeeOfficeList>
        <FormEmployeeOfficeList ref="formEmployeeOfficeListRef" />
      </template>
      <template #userRoleString>
        <div v-if="postRolePermi" class="mb-3">
          <Alert
            :message="`启用岗位角色权限权限后，角色不会保存，请在用户关联岗位中关联角色。`"
            type="info"
            banner
            closable
          />
        </div>
        <FormUserRoleList ref="formUserRoleListRef" />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysEmpUserForm">
  import { ref, unref, computed } from 'vue';
  import { Alert } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { EmpUser, checkEmpNo, empUserSave, empUserForm } from '@jeesite/core/api/sys/empUser';
  import { checkLoginCode } from '@jeesite/core/api/sys/user';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { companyTreeData } from '@jeesite/core/api/sys/company';
  import FormEmployeeOfficeList from './formEmployeeOfficeList.vue';
  import FormUserRoleList from './formUserRoleList.vue';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<EmpUser>({} as EmpUser);
  const formEmployeeOfficeListRef = ref<InstanceType<typeof FormEmployeeOfficeList>>();
  const formUserRoleListRef = ref<InstanceType<typeof FormUserRoleList>>();

  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增用户') : t('编辑用户'),
  }));
  const ctrlPermi = ref<string>('');
  const postRolePermi = ref<boolean>(false);
  const op = ref<string>('');

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
          trigger: 'blur',
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
      helpMessage: t('排序，权重越大排名越靠前，请填写数字。'),
      component: 'InputNumber',
      defaultValue: 0,
      componentProps: {
        maxlength: 8,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('性别'),
      field: 'sex',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_user_sex',
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },

    {
      label: t('详细信息'),
      field: 'employeeInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
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
          trigger: 'blur',
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
      colProps: { md: 24, lg: 24 },
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
      colProps: { md: 24, lg: 24 },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },

    {
      label: t('用户分配角色'),
      field: 'roleInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => op.value === 'add' || op.value === 'auth',
    },
    {
      label: t('分配角色'),
      field: 'userRoleString',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'userRoleString',
      show: () => op.value === 'add' || op.value === 'auth',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
    labelWidth: 120,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await resetFields();
    formEmployeeOfficeListRef.value?.clearTableData();
    formUserRoleListRef.value?.clearTableData();
    setDrawerProps({ loading: true });
    op.value = data.op || 'add';
    const res = await empUserForm(data);
    record.value = (res.empUser || {}) as EmpUser;
    ctrlPermi.value = res.ctrlPermi || '2';
    postRolePermi.value = res.postRolePermi || false;
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
      const postListOptions = res.postList.map((item) => ({
        label: item.postName,
        value: item.postCode,
      }));
      updateSchema([
        {
          field: 'employee.office.officeCode',
          componentProps: {
            api: officeTreeData,
            params: { ctrlPermi },
            canSelectParent: true,
          },
        },
        {
          field: 'employee.company.companyCode',
          componentProps: {
            api: companyTreeData,
            params: { ctrlPermi },
            canSelectParent: true,
          },
        },
        {
          field: 'employee.employeePosts',
          componentProps: {
            options: postListOptions,
            mode: 'multiple',
          },
        },
      ]);
      formEmployeeOfficeListRef.value?.setTableData(record.value, ctrlPermi, postListOptions);
    }
    if (op.value === 'add' || op.value === 'auth') {
      formUserRoleListRef.value?.setTableData(res, ctrlPermi);
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
        await formEmployeeOfficeListRef.value?.getTableData(data);
      }
      if (op.value === 'add' || op.value === 'auth') {
        await formUserRoleListRef.value?.getSelectRowKeyString(data);
      }
      // console.log('submit', params, data, record);
      const res = await empUserSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>

<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:corpAdmin:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="pr-1 m-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #userRoleString>
        <BasicTable
          @register="registerUserRoleTable"
          @selection-change="handleUserRoleSelectionChange"
        />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysCorpAdminForm',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, ref, computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { corpAdminSave, corpAdminForm } from '/@/api/sys/corpAdmin';
  import { User, checkLoginCode } from '/@/api/sys/user';
  import { roleTreeData } from '/@/api/sys/role';
  import { BasicTable, useTable } from '/@/components/Table';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.corpAdmin');
  const { showMessage } = useMessage();
  const record = ref<User>({} as User);
  const getTitle = computed(() => ({
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增管理员') : t('编辑管理员'),
  }));
  const useCorpModel = ref<boolean>(false);
  const corpAdminRoleCode = ref<string>('');
  const op = ref<string>('');

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('租户代码'),
      field: 'corpCode_',
      component: 'Input',
      componentProps: {
        maxlength: 20,
      },
      rules: [
        { required: true },
        { pattern: /^[a-zA-Z0-9_]*$/, message: t('请输入字母数字或下划线') },
      ],
      show: () => useCorpModel.value,
    },
    {
      label: t('租户昵称'),
      field: 'corpName_',
      component: 'Input',
      componentProps: {
        maxlength: 32,
      },
      required: true,
      show: () => useCorpModel.value,
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
      rules: [{ type: 'email', message: t('请填写正确的邮箱地址') }],
    },
    {
      label: t('手机号码'),
      field: 'mobile',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('办公电话'),
      field: 'phone',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
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
    },

    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { lg: 24, md: 24 },
    },

    {
      label: t('角色信息'),
      field: 'roleInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('分配角色'),
      field: 'userRoleString',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'userRoleString',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
    labelWidth: 120,
  });

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

  function handleUserRoleSelectionChange({ keys }) {
    if (!keys.includes(corpAdminRoleCode.value)) {
      showMessage(t('管理员默认角色，不能取消。'));
      keys.push(corpAdminRoleCode.value);
      userRoleTable.setSelectedRowKeys(keys);
    }
  }

  async function setUserRoleTableData(res: Recordable) {
    const dataSource = await roleTreeData({ isAll: true });
    userRoleTable.setTableData(dataSource || []);
    userRoleTable.setSelectedRowKeys(res.roleList?.map((item) => item.id) || []);
  }

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    resetFields();
    userRoleTable.setTableData([]);
    userRoleTable.setSelectedRowKeys([]);
    setDrawerProps({ loading: true });
    op.value = data.op || 'addAdmin';
    const res = await corpAdminForm(data);
    record.value = (res.user || {}) as User;
    useCorpModel.value = res.useCorpModel;
    corpAdminRoleCode.value = res.corpAdminRoleCode;
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'corpCode_',
        componentProps: {
          disabled: !record.value.isNewRecord || op.value === 'addAdmin',
        },
      },
      {
        field: 'corpName_',
        componentProps: {
          disabled: !record.value.isNewRecord || op.value === 'addAdmin',
        },
      },
    ]);
    setUserRoleTableData(res);
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
      data.userRoleString = userRoleTable.getSelectRowKeys().join(',');
      // console.log('submit', params, data, record);
      const res = await corpAdminSave(params, data);
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

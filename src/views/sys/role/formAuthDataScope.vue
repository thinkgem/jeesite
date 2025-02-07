<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:role:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="80%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #dataScopeTrees>
        <CustomDataScope ref="customDataScopeRef" :api="roleCtrlDataTreeData" />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import {
    roleFormAuthDataScope,
    roleCtrlDataTreeData,
    roleSaveAuthDataScope,
  } from '/@/api/sys/role';
  import CustomDataScope from './components/CustomDataScope.vue';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({});
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: t('数据权限'),
  };
  const customDataScopeRef = ref<InstanceType<typeof CustomDataScope>>();

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('角色名称'),
      field: 'roleName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('角色编码'),
      field: 'roleCode',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('数据范围'),
      field: 'dataScope',
      helpMessage: t('指定数据权限范围类型，多个角色同时指定，之间为或者关系'),
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_role_data_scope',
        allowClear: true,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('业务范围'),
      field: 'bizScope',
      helpMessage: t(
        '在 addFilter 权限过滤的时候指定适应的业务范围，不指定代表所有生效，如：有的功能看本部门，有的功能看本公司；新的业务范围从字典 sys_role_biz_scope 类型添加。',
      ),
      component: 'Select',
      componentProps: {
        dictType: 'sys_role_biz_scope',
        allowClear: true,
        mode: 'multiple',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('授权数据权限'),
      field: 'authDataScopeInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      show: ({ values }) => values.dataScope === '2',
    },
    {
      field: 'roleDataScopeListJson',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'dataScopeTrees',
      show: ({ values }) => values.dataScope === '2',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await roleFormAuthDataScope(data);
    record.value = (res.role || {}) as Recordable;
    setFieldsValue(record.value);
    await customDataScopeRef.value?.loadDataScopeList({
      dataScopes: res.dataScopes || [],
      dataScopeList: res.roleDataScopeList || [],
      moduleCodes: res.moduleCodes || [],
      ctrlPermi: res.ctrlPermi || '2',
    });
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        ...data,
        isNewRecord: record.value.isNewRecord,
        roleCode: record.value.roleCode,
        roleDataScopeListJson: customDataScopeRef.value?.getDataScopeListJson(),
      };
      // console.log('submit', params, data, record);
      const res = await roleSaveAuthDataScope(params);
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

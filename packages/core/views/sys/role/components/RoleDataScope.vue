<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="pt-2">
    <BasicForm @register="registerForm">
      <template #dataScopeTrees>
        <CustomDataScope ref="customDataScopeRef" :api="roleCtrlDataTreeData" />
      </template>
    </BasicForm>
  </div>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  // import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { roleCtrlDataTreeData } from '@jeesite/core/api/sys/role';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import CustomDataScope from './CustomDataScope.vue';

  const props = defineProps({
    menuDataScope: propTypes.bool,
  });

  const { t } = useI18n('sys.role');
  // const { showMessage } = useMessage();
  const record = ref<Recordable>({});
  const customDataScope = ref<Recordable>({});

  const customDataScopeRef = ref<InstanceType<typeof CustomDataScope>>();

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('菜单名称'),
      field: 'menuName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      ifShow: () => !!props.menuDataScope,
    },
    {
      label: t('权限标识'),
      field: 'permission',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      ifShow: () => !!props.menuDataScope,
    },
    {
      label: t('角色名称'),
      field: 'roleName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      ifShow: () => !props.menuDataScope,
    },
    {
      label: t('角色编码'),
      field: 'roleCode',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      ifShow: () => !props.menuDataScope,
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
      ifShow: () => !props.menuDataScope,
    },
    {
      label: t('授权数据权限'),
      field: 'authDataScopeInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      show: ({ values }) => values.dataScope === '2',
    },
    {
      field: 'roleDataScopeList',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'dataScopeTrees',
      show: ({ values }) => values.dataScope === '2',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, getFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  async function loadDataScopeFormData(role: Recordable, res?: Recordable) {
    await resetFields();
    record.value = role;
    await setFieldsValue(record.value);
    if (res) {
      customDataScope.value = {
        dataScopes: res.dataScopes || [],
        moduleCodes: res.moduleCodes || [],
        dataScopeList: props.menuDataScope
          ? []
          : (res.roleDataScopeList || []).filter((item: Recordable) => item.menuCode === '0'),
        ctrlPermi: res.ctrlPermi || '2',
      };
    } else {
      customDataScope.value.menuCode = role.menuCode || '0';
      customDataScope.value.dataScopeList = role.roleDataScopeList || [];
    }
    await customDataScopeRef.value?.loadDataScopeList(customDataScope.value);
  }

  async function getDataScopeFormData() {
    return {
      ...(await validate()),
      menuCode: record.value.menuCode,
      roleDataScopeList: customDataScopeRef.value?.getDataScopeList(),
    };
  }

  defineExpose({
    loadDataScopeFormData,
    getDataScopeFormData,
  });
</script>

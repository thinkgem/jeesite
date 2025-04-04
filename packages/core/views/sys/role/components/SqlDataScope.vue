<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="pt-2">
    <BasicForm @register="registerForm" />
    <div class="ml-5 mt-2">
      <Alert message="提示：在后端配置文件开启 user.dataScopeRuleSql 参数后，方可使用该功能。" type="info" />
    </div>
  </div>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Alert } from 'ant-design-vue';
  // import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { trim } from 'lodash-es';

  const { t } = useI18n('sys.role');
  // const { showMessage } = useMessage();
  const record = ref<Recordable>({});

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('菜单名称'),
      field: 'menuName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('权限标识'),
      field: 'permission',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('SQL片段'),
      field: 'authDataScopeSql',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'sqlWhere',
      component: 'InputTextArea',
      componentProps: {
        rows: 8,
      },
      colProps: { md: 24, lg: 24 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, getFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  async function loadDataScopeFormData(data: Recordable, res?: Recordable) {
    await resetFields();
    record.value = data;
    await setFieldsValue(record.value);
  }

  async function getDataScopeFormData() {
    const data = {
      ...(await validate()),
      menuCode: record.value.menuCode,
    };
    if (data.sqlWhere) {
      data.sqlWhere = trim(data.sqlWhere);
    }
    return data;
  }

  defineExpose({
    loadDataScopeFormData,
    getDataScopeFormData,
  });
</script>

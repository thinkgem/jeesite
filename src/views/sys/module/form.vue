<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:module:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="pr-1 m-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysModuleForm',
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
  import { Module, moduleSave, moduleForm } from '/@/api/sys/module';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.module');
  const { showMessage } = useMessage();
  const record = ref<Module>({} as Module);
  const getTitle = computed(() => ({
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增模块') : t('编辑模块'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('模块名称'),
      field: 'moduleName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('模块编码'),
      field: 'moduleCode',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
      rules: [
        { required: true },
        {
          pattern: /^[a-zA-Z]([a-zA-Z0-9_\-])*[a-zA-Z0-9]$/,
          message: t('请输入2个以上字符，字母开头、允许字母数字下划线或减号、字母数字结尾'),
        },
      ],
    },
    {
      label: t('主类全名'),
      field: 'mainClassName',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('模块描述'),
      field: 'description',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
        rows: 3,
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('当前版本'),
      field: 'currentVersion',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
    },
    {
      label: t('升级信息'),
      field: 'upgradeInfo',
      component: 'Input',
      componentProps: {
        maxlength: 300,
        disabled: true,
      },
      ifShow: () => !record.value.isNewRecord,
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await moduleForm(data);
    record.value = (res.module || {}) as Module;
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'moduleCode',
        componentProps: {
          disabled: !record.value.isNewRecord,
        },
      },
      {
        field: 'currentVersion',
        componentProps: {
          disabled: !record.value.isNewRecord,
        },
      },
    ]);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        moduleCode: record.value.moduleCode,
      };
      // console.log('submit', params, data, record);
      const res = await moduleSave(params, data);
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

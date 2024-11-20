<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:config:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysConfigForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Config, configSave, configForm, checkConfigKey } from '@jeesite/web/api/sys/config';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.config');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Config>({} as Config);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增参数') : t('编辑参数'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('参数名称'),
      field: 'configName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('参数键名'),
      field: 'configKey',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      rules: [
        { required: true },
        {
          validator(_rule, value) {
            return new Promise((resolve, reject) => {
              if (!value || value === '') return resolve();
              checkConfigKey(record.value.configKey || '', value)
                .then((res) => (res ? resolve() : reject(t('参数键名已存在'))))
                .catch((err) => reject(err.message || t('验证失败')));
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      label: t('参数键值'),
      field: 'configValue',
      component: 'Input',
      componentProps: {
        maxlength: 1000,
      },
    },
    {
      label: t('系统内置'),
      field: 'isSys',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_yes_no',
      },
      required: true,
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
        rows: 4,
      },
      colProps: { md: 24, lg: 24 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 12, lg: 18 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await configForm(data);
    record.value = (res.config || {}) as Config;
    setFieldsValue(record.value);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id,
      };
      // console.log('submit', params, data, record);
      const res = await configSave(params, data);
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

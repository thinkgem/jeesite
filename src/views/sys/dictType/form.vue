<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:dictType:edit'"
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
    name: 'ViewsSysDictTypeForm',
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
  import { checkDictType, dictTypeSave, dictTypeForm } from '/@/api/sys/dictType';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.dictType');
  const { showMessage } = useMessage();
  const record = ref<Recordable>({});
  const getTitle = computed(() => ({
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增字典类型') : t('编辑字典类型'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('字典名称'),
      field: 'dictName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('字典类型'),
      field: 'dictType',
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
              checkDictType(record.value.dictType || '', value)
                .then((res) => (res ? resolve() : reject(t('字典类型已存在'))))
                .catch((err) => reject(err.message || t('验证失败')));
            });
          },
        },
      ],
    },
    {
      label: t('系统字典'),
      field: 'isSys',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_yes_no',
      },
      defaultValue: '0',
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: inputFormSchemas,
    baseColProps: { lg: 24, md: 24 },
    labelWidth: 120,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    resetFields();
    setDrawerProps({ loading: true });
    const res = await dictTypeForm(data);
    record.value = (res.dictType || {}) as Recordable;
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
      const res = await dictTypeSave(params, data);
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

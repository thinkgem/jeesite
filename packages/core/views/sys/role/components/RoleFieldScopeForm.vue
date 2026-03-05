<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
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
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysRecordableForm">
  import { ref, unref, computed, h } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import {
    roleEntityAttrListData,
    roleEntityListData,
    roleFormAuthFieldScope,
    roleSaveAuthFieldScope,
  } from '@jeesite/core/api/sys';
  import { Button } from '@jeesite/core/components/Button';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.roleFieldScope');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({} as Recordable);
  const entityList = ref<Recordable[]>([] as Recordable[]);
  const entityAttrList = ref<Recordable[]>([] as Recordable[]);

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增权限') : t('编辑权限'),
  }));

  const inputFormSchemas: FormSchema<Recordable>[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('实体名称'),
      field: 'entityName',
      component: 'Select',
      componentProps: ({ formModel }) => {
        return {
          options: entityList,
          onSelect: async (_val: string, item: Recordable) => {
            formModel.entityClass = item.entityClass;
            formModel.entityLabel = item.entityLabel;
            // 获取实体属性列表
            entityAttrList.value = await roleEntityAttrListData({ className: formModel.entityClass });
          },
          allowClear: true,
        };
      },
      required: true,
      dynamicDisabled: () => !record.value.isNewRecord,
    },
    {
      label: t('实体标签'),
      field: 'entityLabel',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('实体类名'),
      field: 'entityClass',
      component: 'Input',
      componentProps: {
        maxlength: 100,
        disabled: true,
      },
      required: true,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('查看权限'),
      field: 'fieldConfigViewInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('禁止查看'),
      helpMessage: '当禁止与允许设置相同属性的时候，禁止的优先级高于允许的属性',
      field: 'fieldConfigMap.viewExclude',
      component: 'Select',
      componentProps: {
        options: entityAttrList,
        mode: 'multiple',
        maxTagCount: 5,
        allowClear: true,
      },
      suffix,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('允许查看'),
      helpMessage: '当禁止与允许设置相同属性的时候，禁止的优先级高于允许的属性',
      field: 'fieldConfigMap.viewInclude',
      component: 'Select',
      componentProps: {
        options: entityAttrList,
        mode: 'multiple',
        maxTagCount: 5,
        allowClear: true,
      },
      suffix,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('编辑权限'),
      field: 'fieldConfigEditInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('禁止编辑'),
      helpMessage: '当禁止与允许设置相同属性的时候，禁止的优先级高于允许的属性',
      field: 'fieldConfigMap.editExclude',
      component: 'Select',
      componentProps: {
        options: entityAttrList,
        mode: 'multiple',
        maxTagCount: 5,
        allowClear: true,
      },
      suffix,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('允许编辑'),
      helpMessage: '当禁止与允许设置相同属性的时候，禁止的优先级高于允许的属性',
      field: 'fieldConfigMap.editInclude',
      component: 'Select',
      componentProps: {
        options: entityAttrList,
        mode: 'multiple',
        maxTagCount: 5,
        allowClear: true,
      },
      suffix,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('其它信息'),
      field: 'otherInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { md: 24, lg: 24 },
    },
  ];

  function suffix(e) {
    return [
      h(
        Button,
        {
          onClick: () => {
            const vals: string[] = [];
            entityAttrList.value.filter((item: any) => {
              vals.push(item.value);
              return true;
            });
            const values = {};
            values[e.field] = vals.join(',');
            setFieldsValue(values);
          },
        },
        { default: () => t('全选') },
      ),
      h(
        Button,
        {
          class: 'ml-1',
          onClick: () => {
            const values = {};
            values[e.field] = '';
            setFieldsValue(values);
          },
        },
        { default: () => t('清空') },
      ),
    ];
  }

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm<Recordable>({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await roleFormAuthFieldScope(data);
    record.value = (res.roleFieldScope || {}) as Recordable;
    // const role = (res.role || {}) as Recordable;
    // record.value.roleCode = role.roleCode;
    record.value.__t = new Date().getTime();
    await setFieldsValue(record.value);
    // 获取实体列表
    entityList.value = await roleEntityListData();
    // 获取实体属性列表
    if (record.value.entityClass != '') {
      entityAttrList.value = await roleEntityAttrListData({ className: record.value.entityClass });
    }
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id || data.id,
      };
      data.roleCode = record.value.roleCode;
      data.menuCode = record.value.menuCode;
      // console.log('submit', params, data, record);
      const res = await roleSaveAuthFieldScope(params, data);
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

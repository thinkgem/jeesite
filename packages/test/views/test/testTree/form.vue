<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'test:testTree:edit'"
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
<script lang="ts" setup name="ViewsTestTestTreeForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { TestTree, testTreeSave, testTreeForm, testTreeTreeData } from '@jeesite/test/api/test/testTree';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('test.testTree');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<TestTree>({} as TestTree);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增数据') : t('编辑数据'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('上级数据'),
      field: 'parentCode',
      fieldLabel: 'parentName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
      },
    },
    {
      label: t('节点编码'),
      field: 'treeCode',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
      rules: [{ required: true }, { pattern: /^[a-zA-Z0-9_]*$/, message: t('请输入字母数字下划线') }],
    },
    {
      label: t('节点名称'),
      field: 'treeName',
      component: 'Input',
      componentProps: {
        maxlength: 200,
      },
      required: true,
    },
    {
      label: t('排序号'),
      field: 'treeSort',
      helpMessage: '升序',
      component: 'InputNumber',
      componentProps: {
        maxlength: 10,
      },
      defaultValue: '30',
      required: true,
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

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await testTreeForm(data);
    record.value = (res.testTree || {}) as TestTree;
    if (data.parentCode && data.parentName) {
      record.value.parentCode = data.parentCode;
      record.value.parentName = data.parentName;
    }
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'parentCode',
        componentProps: {
          api: testTreeTreeData,
          params: {
            excludeCode: record.value.id,
            isShowRawName: true,
          },
        },
      },
      {
        field: 'treeCode',
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
        treeCode: record.value.treeCode,
      };
      data.oldParentCode = record.value.parentCode;
      // console.log('submit', params, data, record);
      const res = await testTreeSave(params, data);
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

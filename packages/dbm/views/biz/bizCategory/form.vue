<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicModal
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'biz:bizCategory:edit'"
    @register="registerModal"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup name="ViewsBizCategoryForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicModal, useModalInner } from '@jeesite/core/components/Modal';
  import { BizCategory, bizCategorySave, bizCategoryForm, bizCategoryTreeData } from '@jeesite/dbm/api/biz/bizCategory';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('biz.bizCategory');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<BizCategory>({} as BizCategory);

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增分类') : t('编辑分类'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('上级分类'),
      field: 'parentCode',
      fieldLabel: 'parentName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
        // style: 'width: calc(50% - 60px)',
      },
      // colProps: { md: 24, lg: 24 },
    },
    {
      label: t('分类代码'),
      field: 'viewCode',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
    },
    {
      label: t('分类名称'),
      field: 'categoryName',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
      required: true,
    },
    {
      label: t('排序号'),
      field: 'treeSort',
      helpMessage: '升序',
      component: 'InputNumber',
      defaultValue: '30',
      componentProps: {
        maxlength: 8,
      },
      rules: [{ required: true }, { pattern: /^\d+$/, message: t('请输入一个正整数') }],
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

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ loading: true });
    await resetFields();
    const res = await bizCategoryForm(data);
    record.value = (res.bizCategory || {}) as BizCategory;
    record.value.__t = new Date().getTime();
    if (data.parentCode && data.parentName) {
      record.value.parentCode = data.parentCode;
      record.value.parentName = data.parentName;
    }
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'parentCode',
        componentProps: {
          api: bizCategoryTreeData,
          params: {
            excludeCode: record.value.id,
            isShowRawName: true,
          },
        },
      },
    ]);
    setModalProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setModalProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        categoryCode: record.value.categoryCode,
      };
      data.oldParentCode = record.value.parentCode;
      // console.log('submit', params, data, record);
      const res = await bizCategorySave(params, data);
      showMessage(res.message);
      setTimeout(closeModal);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicForm @register="registerForm" />
</template>
<script lang="ts" setup name="ViewsCmsArticleFormView">
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { Article } from '@jeesite/cms/api/cms/article';
  import type { NamePath } from 'antdv-next/dist/form/types';
  import { ref } from 'vue';

  const { t } = useI18n('cms.article');

  const article_DEFAULT_TEMPLATE = ref('');
  const contentViewList = ref([]);

  const inputFormSchemas: FormSchema<Article>[] = [
    {
      label: t('自定义内容视图'),
      helpMessage: () => `自定义内容视图名称必须以 ${article_DEFAULT_TEMPLATE.value} 开始`,
      field: 'customContentView',
      component: 'Select',
      componentProps: {
        options: contentViewList,
        allowClear: true,
      },
    },
    {
      label: t('视图参数配置'),
      helpMessage:
        "视图参数例如: {count:2, title_show:'yes'} 则在视图文件中的获取方法是：${viewConfig_count}、${viewConfig_titleShow}",
      field: 'viewConfig',
      component: 'Input',
      componentProps: {
        placeholder:
          "视图参数例如: {count:2, title_show:'yes'} 则在视图文件中的获取方法是：${viewConfig_count}、${viewConfig_titleShow}",
        maxlength: 1000,
      },
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

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm<Article>({
    labelWidth: 130,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  defineExpose({
    resetFields: async () => {
      await resetFields();
    },
    setFieldsValue: async (values: Recordable, res: any) => {
      contentViewList.value = (res.contentViewList || []).map((item) => {
        return { label: item.dictLabel, value: item.dictValue };
      });
      await setFieldsValue(values);
    },
    validate: async (nameList?: NamePath[]) => {
      return await validate(nameList);
    },
  });
</script>

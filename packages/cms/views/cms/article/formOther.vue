<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicForm @register="registerForm" />
</template>
<script lang="ts" setup name="ViewsCmsArticleFormOther">
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { Article } from '@jeesite/cms/api/cms/article';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import type { NamePath } from 'antdv-next/dist/form/types';
  import { computed, ref } from 'vue';

  const { t } = useI18n('cms.article');
  const record = ref<Article>({} as Article);

  const inputFormSchemas: FormSchema<Article>[] = [
    {
      label: t('内容图片'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'article_image',
        uploadType: 'image',
        maxNumber: 1,
        // imageMaxWidth: 1024,
        // imageMaxHeight: 768,
        // imageThumbName: '150x150.jpg',
        showPreviewNumber: false,
        showPreviewList: true,
        emptyHidePreview: true,
        onChange: (dataMap, fileList) => {
          fileList.forEach((e) => {
            record.value.image = e.fileUrl;
          });
        },
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('关键字'),
      field: 'keywords',
      component: 'Input',
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
    setFieldsValue: async (values: Recordable) => {
      record.value = values as Article;
      await setFieldsValue(values);
    },
    validate: async (nameList?: NamePath[]) => {
      let data = await validate(nameList);
      data.image = record.value.image;
      return data;
    },
  });
</script>

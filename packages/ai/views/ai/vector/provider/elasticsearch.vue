<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicForm @register="registerForm" />
  </div>
</template>
<script lang="ts" setup name="ViewsAiVectorProviderElasticsearch">
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { AiVector } from '@jeesite/ai';

  defineProps<{
    model: AiVector;
  }>();

  const { t } = useI18n('ai.vector');

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('连接信息'),
      field: 'esLinkInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('服务地址'),
      helpMessage: t('如：http://127.0.0.1:9200；需 Elasticsearch 8.0 及以上'),
      field: 'baseUrl',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        placeholder: 'http://127.0.0.1:9200',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('用户名'),
      helpMessage: t('Basic 认证用户名，留空则不认证'),
      field: 'username',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('密码或密钥'),
      field: 'newPassword',
      component: 'Input',
      componentProps: {
        maxlength: 255,
      },
    },
    {
      label: t('索引选项'),
      field: 'esIndexInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('相似度函数'),
      helpMessage: t('cosine 适用于大多数场景；l2_norm 欧氏距离；dot_product 适合已归一化的向量'),
      field: 'optionMap.similarity',
      component: 'Select',
      defaultValue: 'cosine',
      componentProps: {
        options: [
          { label: 'cosine', value: 'cosine' },
          { label: 'l2_norm', value: 'l2_norm' },
          { label: 'dot_product', value: 'dot_product' },
        ],
      },
    },
    {
      label: t('向量字段名'),
      helpMessage: t('索引中存放向量的字段名，默认 embedding'),
      field: 'optionMap.embeddingFieldName',
      component: 'Input',
      componentProps: {
        maxlength: 64,
        placeholder: 'embedding',
      },
    },
    {
      label: t('初始化索引'),
      helpMessage: t('首次使用时自动创建索引与 mapping'),
      field: 'optionMap.initializeSchema',
      component: 'Switch',
      defaultValue: true,
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 150,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  defineExpose({
    resetFields,
    setFieldsValue,
    validate,
  });
</script>

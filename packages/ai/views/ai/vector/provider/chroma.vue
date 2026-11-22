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
<script lang="ts" setup name="ViewsAiVectorProviderChroma">
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
      field: 'chromaLinkInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('服务地址'),
      helpMessage: t(
        '如：http://127.0.0.1:8000；Chroma Cloud 为 https://api.trychroma.com:443；需 Chroma 1.0 及以上版本（使用 /api/v2 接口）',
      ),
      field: 'baseUrl',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        placeholder: 'http://127.0.0.1:8000',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('用户名'),
      helpMessage: t('Basic 认证用户名；留空则只使用下面的密码作为 API Key（x-chroma-token）'),
      field: 'username',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('密码或密钥'),
      helpMessage: t('Basic 认证密码，或 Chroma Cloud 的 API Key'),
      field: 'newPassword',
      component: 'Input',
      componentProps: {
        maxlength: 255,
      },
    },
    {
      label: t('集合选项'),
      field: 'chromaCollectionInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('初始化集合'),
      helpMessage: t('首次使用时自动创建租户、数据库与集合'),
      field: 'optionMap.initializeSchema',
      component: 'Switch',
      defaultValue: true,
    },
    {
      label: t('重建集合'),
      helpMessage: t(
        '删除同名集合并按当前向量模型的维度重新创建，慎用：会清空已有向量数据。集合维度在首次写入后不可修改，更换向量模型（维度不同）后必须重建集合或改用新的集合名称',
      ),
      field: 'optionMap.recreateCollection',
      component: 'Switch',
      defaultValue: false,
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

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
<script lang="ts" setup name="ViewsAiVectorProviderMilvus">
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
      field: 'milvusLinkInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('服务地址'),
      helpMessage: t('如：http://127.0.0.1:19530；使用 https 表示启用 TLS（如 Zilliz Cloud）'),
      field: 'baseUrl',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        placeholder: 'http://127.0.0.1:19530',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('用户名'),
      helpMessage: t('账号密码认证时填写；仅填写密码时按 Token（Zilliz Cloud API Key）认证'),
      field: 'username',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('密码或密钥'),
      helpMessage: t('账号密码认证时的密码，或 Zilliz Cloud 的 API Key'),
      field: 'newPassword',
      component: 'Input',
      componentProps: {
        maxlength: 255,
      },
    },
    {
      label: t('集合选项'),
      field: 'milvusCollectionInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('数据库名称'),
      helpMessage: t('默认 default'),
      field: 'optionMap.databaseName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
        placeholder: 'default',
      },
    },
    {
      label: t('分区名称'),
      helpMessage: t('限定插入、删除、检索的分区，留空使用默认分区'),
      field: 'optionMap.partitionName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('索引选项'),
      field: 'milvusIndexInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('索引类型'),
      helpMessage: t('默认 HNSW；大数据量也可用 IVF_FLAT、AUTOINDEX'),
      field: 'optionMap.indexType',
      component: 'Select',
      defaultValue: 'HNSW',
      componentProps: {
        options: [
          { label: 'IVF_FLAT', value: 'IVF_FLAT' },
          { label: 'FLAT', value: 'FLAT' },
          { label: 'IVF_SQ8', value: 'IVF_SQ8' },
          { label: 'IVF_PQ', value: 'IVF_PQ' },
          { label: 'HNSW', value: 'HNSW' },
          { label: 'AUTOINDEX', value: 'AUTOINDEX' },
          { label: 'SCANN', value: 'SCANN' },
          { label: 'DISKANN', value: 'DISKANN' },
        ],
      },
    },
    {
      label: t('距离度量'),
      helpMessage: t('仅支持 COSINE、L2、IP，默认 COSINE'),
      field: 'optionMap.metricType',
      component: 'Select',
      defaultValue: 'COSINE',
      componentProps: {
        options: [
          { label: 'COSINE', value: 'COSINE' },
          { label: 'L2', value: 'L2' },
          { label: 'IP', value: 'IP' },
        ],
      },
    },
    {
      label: t('索引参数'),
      helpMessage: t('JSON 格式，默认 {"nlist":1024}；IVF_FLAT 检索时建议同时在检索参数中设置 nprobe'),
      field: 'optionMap.indexParameters',
      component: 'Input',
      componentProps: {
        maxlength: 500,
        placeholder: '{"nlist":1024}',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('初始化集合'),
      helpMessage: t('首次使用时自动创建集合与索引'),
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

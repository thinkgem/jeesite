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
<script lang="ts" setup name="ViewsAiVectorProviderRedis">
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { AiVector } from '@jeesite/ai';

  defineProps<{
    model: AiVector;
  }>();

  const { t } = useI18n('ai.vector');

  // 向量算法：HNSW 时才显示 HNSW 参数（未配置时按默认 HNSW 处理）
  const isHnsw = ({ values }: any) => (values.optionMap?.vectorAlgorithm || 'HNSW') === 'HNSW';

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('连接信息'),
      field: 'redisLinkInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('服务地址'),
      helpMessage: t('如：redis://127.0.0.1:6379/0，TLS 连接使用 rediss://；需 Redis Stack（RediSearch + RedisJSON）'),
      field: 'baseUrl',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        placeholder: 'redis://127.0.0.1:6379/0',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('用户名'),
      helpMessage: t('Redis 6+ ACL 用户名，留空使用默认账号'),
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
      field: 'redisIndexInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('Key 前缀'),
      helpMessage: t('向量数据的 Redis key 前缀，默认 embedding:'),
      field: 'optionMap.prefix',
      component: 'Input',
      componentProps: {
        maxlength: 64,
        placeholder: 'embedding:',
      },
    },
    {
      label: t('向量算法'),
      helpMessage: t('HNSW：近似检索，速度快；FLAT：精确检索，适合小数据量'),
      field: 'optionMap.vectorAlgorithm',
      component: 'Select',
      defaultValue: 'HNSW',
      componentProps: {
        options: [
          { label: 'HNSW', value: 'HNSW' },
          { label: 'FLAT', value: 'FLAT' },
        ],
      },
    },
    {
      label: t('距离度量'),
      helpMessage: t('默认 COSINE，可选 L2（欧氏距离）、IP（内积）'),
      field: 'optionMap.distanceMetric',
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
      label: t('HNSW 参数'),
      field: 'redisHnswInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: isHnsw,
    },
    {
      label: t('每节点连接数'),
      helpMessage: t('hnswM：每个节点的最大出边数，默认 16，常用 5 到 100'),
      field: 'optionMap.hnswM',
      component: 'InputNumber',
      defaultValue: 16,
      componentProps: {
        min: 5,
        max: 100,
        class: 'w-full',
      },
      ifShow: isHnsw,
    },
    {
      label: t('建索引候选数'),
      helpMessage: t('hnswEfConstruction：建索引时的候选列表大小，默认 200，常用 50 到 500'),
      field: 'optionMap.hnswEfConstruction',
      component: 'InputNumber',
      defaultValue: 200,
      componentProps: {
        min: 50,
        max: 500,
        class: 'w-full',
      },
      ifShow: isHnsw,
    },
    {
      label: t('检索候选数'),
      helpMessage: t('hnswEfRuntime：检索时的候选列表大小，默认 10，常用 20 到 200'),
      field: 'optionMap.hnswEfRuntime',
      component: 'InputNumber',
      defaultValue: 10,
      componentProps: {
        min: 10,
        max: 200,
        class: 'w-full',
      },
      ifShow: isHnsw,
    },
    {
      label: t('其它选项'),
      field: 'redisOtherInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('初始化索引'),
      helpMessage: t('首次使用时自动创建 RediSearch 索引'),
      field: 'optionMap.initializeSchema',
      component: 'Switch',
      defaultValue: true,
    },
    {
      label: t('连接超时'),
      helpMessage: t('单位毫秒，默认 2000'),
      field: 'optionMap.timeoutMillis',
      component: 'InputNumber',
      defaultValue: 2000,
      componentProps: {
        min: 500,
        step: 500,
        class: 'w-full',
      },
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

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
<script lang="ts" setup name="ViewsAiVectorProviderPgvector">
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
      field: 'pgLinkInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('服务地址'),
      helpMessage: t(
        'JDBC 地址，如：jdbc:postgresql://127.0.0.1:5432/jeesite?currentSchema=public；留空则使用容器内已有的数据源',
      ),
      field: 'baseUrl',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        placeholder: 'jdbc:postgresql://host:5432/db?currentSchema=public',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('用户名'),
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
      label: t('主键类型'),
      helpMessage: t('向量表主键列类型，默认 TEXT；UUID 由框架生成，SERIAL/BIGSERIAL 为自增整型'),
      field: 'optionMap.idType',
      component: 'Select',
      defaultValue: 'TEXT',
      componentProps: {
        options: [
          { label: 'TEXT', value: 'TEXT' },
          { label: 'UUID', value: 'UUID' },
          { label: 'INTEGER', value: 'INTEGER' },
          { label: 'SERIAL', value: 'SERIAL' },
          { label: 'BIGSERIAL', value: 'BIGSERIAL' },
        ],
      },
    },
    {
      label: t('索引类型'),
      helpMessage: t('HNSW 查询性能好、占用内存大；IVFFLAT 建索引快；NONE 精确检索（小数据量）'),
      field: 'optionMap.indexType',
      component: 'Select',
      defaultValue: 'HNSW',
      componentProps: {
        options: [
          { label: 'HNSW', value: 'HNSW' },
          { label: 'IVFFLAT', value: 'IVFFLAT' },
          { label: 'NONE', value: 'NONE' },
        ],
      },
    },
    {
      label: t('距离类型'),
      helpMessage: t('COSINE_DISTANCE 余弦距离（默认）；EUCLIDEAN_DISTANCE 欧氏距离；NEGATIVE_INNER_PRODUCT 负内积'),
      field: 'optionMap.distanceType',
      component: 'Select',
      defaultValue: 'COSINE_DISTANCE',
      componentProps: {
        options: [
          { label: 'COSINE_DISTANCE', value: 'COSINE_DISTANCE' },
          { label: 'EUCLIDEAN_DISTANCE', value: 'EUCLIDEAN_DISTANCE' },
          { label: 'NEGATIVE_INNER_PRODUCT', value: 'NEGATIVE_INNER_PRODUCT' },
        ],
      },
    },
    {
      label: t('批量写入大小'),
      helpMessage: t('批量写入时的分批大小，默认 10000'),
      field: 'optionMap.maxDocumentBatchSize',
      component: 'InputNumber',
      defaultValue: 10000,
      componentProps: {
        min: 100,
        step: 100,
        class: 'w-full',
      },
    },
    {
      label: t('表结构选项'),
      field: 'pgSchemaInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('初始化表结构'),
      helpMessage: t('首次使用时自动创建向量表与索引（Spring AI 默认为 false，需手动建表；本系统默认开启）'),
      field: 'optionMap.initializeSchema',
      component: 'Switch',
      defaultValue: true,
    },
    {
      label: t('重建向量表'),
      helpMessage: t('保存连接时删除已存在的向量表并重新创建，慎用：会清空已有向量数据'),
      field: 'optionMap.removeExistingVectorStoreTable',
      component: 'Switch',
      defaultValue: false,
    },
    {
      label: t('模式名称'),
      helpMessage: t('数据库 schema 名称，默认 public'),
      field: 'optionMap.schemaName',
      component: 'Input',
      componentProps: {
        maxlength: 64,
        placeholder: 'public',
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

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
<script lang="ts" setup name="ViewsAiVectorProviderSimple">
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { AiVector } from '@jeesite/ai';

  defineProps<{
    model: AiVector;
  }>();

  const { t } = useI18n('ai.vector');

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('存储选项'),
      helpMessage: t(
        '内存向量库（SimpleVectorStore）无需外部服务，适合演示与测试；配置持久化文件后可保留数据，不支持多实例部署',
      ),
      field: 'simpleOptions',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('持久化文件'),
      helpMessage: t(
        '文件名，如：ai-vector.json；仅允许字母、数字、下划线、减号，文件固定保存于 {用户主目录}/jeesite/vector 目录；留空为纯内存存储，重启后数据丢失',
      ),
      field: 'optionMap.persistenceFile',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        placeholder: 'ai-vector.json',
      },
      colProps: { md: 24, lg: 24 },
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

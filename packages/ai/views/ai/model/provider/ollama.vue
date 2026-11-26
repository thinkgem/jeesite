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
<script lang="ts" setup name="ViewsAiAiModelForm">
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { AiModel } from '@jeesite/ai';

  const props = defineProps<{
    model: AiModel;
  }>();

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('ai.aiModel');
  const { showMessage } = useMessage();

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('模型选项'),
      field: 'modelOptionsTitle',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => ['chat', 'embedding'].includes(props.model.modelType),
    },
    {
      label: t('生成随机性'),
      helpMessage: [
        'temperature 采样温度值、发散度，取值范围 0 到 2，Ollama 默认 0.8。',
        '较高的值（如 0.8）会使输出更随机，而较低的值（如 0.2）则使输出更加集中和确定。',
        '我们通常建议调整此参数或 topP 参数中的一个，而不是同时调整两者。',
        '参考设置：精确模式 0.3；平衡模式 0.8；创意模式 1.2。',
        '不填写时使用模型自身的默认值（Spring AI 2.x 不再统一默认 0.7）。',
      ],
      field: 'optionMap.chat.temperature',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 2,
        step: 0.1,
      },
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('生成采样率'),
      helpMessage: [
        'topP 一种替代温度采样（temperature sampling）的方法，称为“核采样”（nucleus sampling）。',
        '在这种方法中，模型会考虑概率质量累计达到 topP 的那些 token。例如，0.1 表示只考虑',
        '概率总和为前 10% 的 token。我们通常建议修改这个参数或 temperature 参数中的一个，',
        '而不要同时调整两者。取值范围 0 到 1。',
      ],
      field: 'optionMap.chat.topP',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 1,
        step: 0.1,
      },
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('生成多样性'),
      helpMessage: 'topK 从每个时间步的前 K 个候选 token 中采样，默认 40，值越大结果越随机。',
      field: 'optionMap.chat.topK',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 100,
        step: 1,
      },
      defaultValue: 40,
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('重复惩罚'),
      helpMessage: 'repeatPenalty 重复惩罚，大于 1 时降低重复内容，Ollama 默认 1.1。',
      field: 'optionMap.chat.repeatPenalty',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 2,
        step: 0.1,
      },
      defaultValue: 1.1,
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('最大回复数'),
      helpMessage: [
        'maxTokens 对应 Ollama 的 num_predict，控制模型一次最多生成的 token 数。',
        '填 -1 表示不限制，由模型自行决定何时停止。',
        '不限制填写范围，按模型能力填写即可，例如 131072（128K）。',
        '不能超过模型的上下文长度（numCtx）。',
      ],
      field: 'optionMap.chat.maxTokens',
      component: 'InputNumber',
      componentProps: {
        min: -1,
        step: 1,
      },
      defaultValue: 32768,
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('上下文长度'),
      helpMessage: 'numCtx 上下文窗口大小（token 数），不填写使用模型或 Ollama 的默认值。',
      field: 'optionMap.chat.numCtx',
      component: 'InputNumber',
      componentProps: {
        min: 1,
        max: 1048576,
        step: 1,
      },
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('停止序列'),
      helpMessage: 'stop 模型遇到这些字符串时停止生成，多个值用英文逗号分隔。',
      field: 'optionMap.chat.stop',
      component: 'Input',
      componentProps: {
        maxlength: 500,
        placeholder: t('多个值用英文逗号分隔'),
      },
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('随机种子'),
      helpMessage: 'seed 设置后，在参数相同的情况下模型会尽量返回相同的结果。',
      field: 'optionMap.chat.seed',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 1,
      },
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('思考模式'),
      helpMessage: 'think 仅 Qwen3、DeepSeek-R1、GPT-OSS 等支持思考的模型有效。',
      field: 'optionMap.chat.think',
      component: 'Switch',
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('向量的维度'),
      helpMessage: 'dimensions 向量的维度大小，和模型要求的维度匹配，如：384、512、768、1024 等',
      field: 'optionMap.embedding.dimensions',
      component: 'InputNumber',
      componentProps: {
        min: 1,
        step: 1,
      },
      ifShow: () => props.model.modelType == 'embedding',
    },
    {
      label: t('其它选项'),
      field: 'otherOptionsTitle',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('超时时间'),
      helpMessage: 'timeout 模型拉取操作的超时时间',
      field: 'optionMap.timeout',
      component: 'InputNumber',
      componentProps: {
        min: 1,
        step: 1,
      },
      renderComponentContent: () => {
        return {
          addonAfter: () => t('分钟'),
        };
      },
      defaultValue: 5,
    },
    {
      label: t('重试次数'),
      helpMessage: 'maxRetries 模型拉取操作的最大重试次数。',
      field: 'optionMap.maxRetries',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 1,
      },
    },
    {
      label: t('拉取方式'),
      helpMessage: 'pullModelStrategy 启动时加载模型的方式',
      field: 'optionMap.pullModelStrategy',
      component: 'Select',
      componentProps: {
        options: [
          { label: '始终拉取，保持最新 (ALWAYS)', value: 'ALWAYS' },
          { label: '不存在的时候拉取 (WHEN_MISSING)', value: 'WHEN_MISSING' },
          { label: '不主动拉取模型 (NEVER)', value: 'NEVER' },
        ],
      },
      defaultValue: 'NEVER',
    },
    {
      label: t('附加模型'),
      helpMessage: 'additionalModels 除当前模型外，需要一并加载的模型名称，多个用英文逗号分隔。',
      field: 'optionMap.additionalModels',
      component: 'Input',
      componentProps: {
        maxlength: 500,
        placeholder: t('多个值用英文逗号分隔'),
      },
    },
    {
      label: t('模型权重'),
      helpMessage: '权重越大，排序越靠前',
      field: 'weight',
      component: 'InputNumber',
      componentProps: {
        maxlength: 8,
      },
      rules: [{ pattern: /^\d+$/, message: t('请输入一个正整数') }],
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  defineExpose({
    resetFields,
    setFieldsValue,
    validate,
  });
</script>

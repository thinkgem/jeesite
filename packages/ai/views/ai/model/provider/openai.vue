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
      ifShow: () => ['chat', 'embedding', 'image', 'speech', 'transcription'].includes(props.model.modelType),
    },
    {
      label: t('生成随机性'),
      helpMessage: [
        'temperature 采样温度值、发散度，取值范围 0 到 2。',
        '较高的值（如 0.8）会使输出更随机，而较低的值（如 0.2）则使输出更加集中和确定。',
        '我们通常建议调整此参数或 topP 参数中的一个，而不是同时调整两者。',
        '参考设置：精确模式 0.3；平衡模式 0.7；创意模式 1。',
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
      helpMessage: [
        'frequencyPenalty 频率惩罚，控制生成文本多样性的参数，取值范围 -2 到 2。',
        '它通过影响模型对已经生成过的词汇的选择倾向，来减少重复内容的出现。',
      ],
      field: 'optionMap.chat.frequencyPenalty',
      component: 'InputNumber',
      componentProps: {
        min: -2,
        max: 2,
        step: 0.1,
      },
      defaultValue: 0,
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('存在惩罚'),
      helpMessage: [
        'presencePenalty 存在惩罚，取值范围 -2 到 2。',
        '鼓励模型谈论新话题，正值会让模型更倾向于生成尚未出现过的内容。',
      ],
      field: 'optionMap.chat.presencePenalty',
      component: 'InputNumber',
      componentProps: {
        min: -2,
        max: 2,
        step: 0.1,
      },
      defaultValue: 0,
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('最大回复数'),
      helpMessage: [
        'maxTokens 用于控制模型在一次请求中最多可以生成多少个 token。',
        '这个参数直接影响生成文本的长度，不能超过模型的最大输出上限。',
        '推理模型（如 o1、o3）请使用“最大推理回复数”（maxCompletionTokens）。',
      ],
      field: 'optionMap.chat.maxTokens',
      component: 'InputNumber',
      componentProps: {
        min: 1,
        max: 32768,
        step: 1,
      },
      defaultValue: 4096,
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('最大推理回复数'),
      helpMessage: [
        'maxCompletionTokens 用于推理模型（如 o1、o3、gpt-5）的最大输出 token 数。',
        '与 maxTokens 互斥，设置后 maxTokens 不生效；非推理模型请留空。',
      ],
      field: 'optionMap.chat.maxCompletionTokens',
      component: 'InputNumber',
      componentProps: {
        min: 1,
        max: 32768,
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
      label: t('推理强度'),
      helpMessage: 'reasoningEffort 推理模型的思考强度，仅部分模型支持，不填使用模型默认值。',
      field: 'optionMap.chat.reasoningEffort',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: 'minimal', value: 'minimal' },
          { label: 'low', value: 'low' },
          { label: 'medium', value: 'medium' },
          { label: 'high', value: 'high' },
        ],
      },
      ifShow: () => props.model.modelType == 'chat',
    },
    {
      label: t('严格模式'),
      helpMessage: 'strict 工具调用参数严格校验，开启后工具参数 schema 会自动补全为严格模式。',
      field: 'optionMap.chat.strict',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: t('开启'), value: true },
          { label: t('关闭'), value: false },
        ],
      },
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
      label: t('图片尺寸'),
      helpMessage: 'size 生成图片的尺寸，与 width、height 二选一。',
      field: 'optionMap.image.size',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: '1024x1024', value: '1024x1024' },
          { label: '1792x1024', value: '1792x1024' },
          { label: '1024x1792', value: '1024x1792' },
          { label: 'auto', value: 'auto' },
        ],
      },
      ifShow: () => props.model.modelType == 'image',
    },
    {
      label: t('图片质量'),
      helpMessage: 'quality 生成图片的画质，gpt-image-1 为 low、medium、high，dall-e-3 为 standard、hd。',
      field: 'optionMap.image.quality',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: 'auto', value: 'auto' },
          { label: 'low', value: 'low' },
          { label: 'medium', value: 'medium' },
          { label: 'high', value: 'high' },
          { label: 'standard', value: 'standard' },
          { label: 'hd', value: 'hd' },
        ],
      },
      ifShow: () => props.model.modelType == 'image',
    },
    {
      label: t('图片风格'),
      helpMessage: 'style 仅 dall-e-3 支持：vivid 更鲜艳、natural 更自然。',
      field: 'optionMap.image.style',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: 'vivid', value: 'vivid' },
          { label: 'natural', value: 'natural' },
        ],
      },
      ifShow: () => props.model.modelType == 'image',
    },
    {
      label: t('生成数量'),
      helpMessage: 'n 一次请求生成的图片数量，dall-e-3 仅支持 1。',
      field: 'optionMap.image.n',
      component: 'InputNumber',
      componentProps: {
        min: 1,
        max: 10,
        step: 1,
      },
      defaultValue: 1,
      ifShow: () => props.model.modelType == 'image',
    },
    {
      label: t('图片格式'),
      helpMessage: 'responseFormat 返回图片的形式：url 为图片地址，b64_json 为 Base64 内容。',
      field: 'optionMap.image.responseFormat',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: 'url', value: 'url' },
          { label: 'b64_json', value: 'b64_json' },
        ],
      },
      ifShow: () => props.model.modelType == 'image',
    },
    {
      label: t('超时时间'),
      helpMessage: t('timeout 生图请求的超时时间，支持 15m、300s、600 等写法；生图耗时较长，默认 15 分钟'),
      field: 'optionMap.image.timeout',
      component: 'Input',
      componentProps: {
        maxlength: 16,
        placeholder: '15m',
      },
      ifShow: () => props.model.modelType == 'image',
    },
    {
      label: t('音色'),
      helpMessage: 'voice 语音合成音色：alloy、echo、fable、onyx、nova、shimmer 等。',
      field: 'optionMap.speech.voice',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: 'alloy', value: 'alloy' },
          { label: 'echo', value: 'echo' },
          { label: 'fable', value: 'fable' },
          { label: 'onyx', value: 'onyx' },
          { label: 'nova', value: 'nova' },
          { label: 'shimmer', value: 'shimmer' },
        ],
      },
      ifShow: () => props.model.modelType == 'speech',
    },
    {
      label: t('语速'),
      helpMessage: 'speed 语音合成语速，取值范围 0.25 到 4，默认 1。',
      field: 'optionMap.speech.speed',
      component: 'InputNumber',
      componentProps: {
        min: 0.25,
        max: 4,
        step: 0.05,
      },
      defaultValue: 1,
      ifShow: () => props.model.modelType == 'speech',
    },
    {
      label: t('音频格式'),
      helpMessage: 'responseFormat 合成音频的格式：mp3、opus、aac、flac、wav、pcm。',
      field: 'optionMap.speech.responseFormat',
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: 'mp3', value: 'mp3' },
          { label: 'opus', value: 'opus' },
          { label: 'aac', value: 'aac' },
          { label: 'flac', value: 'flac' },
          { label: 'wav', value: 'wav' },
          { label: 'pcm', value: 'pcm' },
        ],
      },
      ifShow: () => props.model.modelType == 'speech',
    },
    {
      label: t('发音指导'),
      helpMessage: 'instructions 发音指导，描述语气、情感、语速等，如“用温柔的语气朗读”。',
      field: 'optionMap.speech.instructions',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      ifShow: () => props.model.modelType == 'speech',
    },
    {
      label: t('转写语言'),
      helpMessage: 'language 音频的语言，ISO-639-1 编码，如 zh、en，不填写由模型自动识别。',
      field: 'optionMap.transcription.language',
      component: 'Input',
      componentProps: {
        maxlength: 20,
      },
      ifShow: () => props.model.modelType == 'transcription',
    },
    {
      label: t('转写提示词'),
      helpMessage: 'prompt 用于引导转写的提示词，可提升专业术语、人名的识别准确率。',
      field: 'optionMap.transcription.prompt',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      ifShow: () => props.model.modelType == 'transcription',
    },
    {
      label: t('转写温度'),
      helpMessage: 'temperature 转写温度，取值范围 0 到 1，越低结果越稳定。',
      field: 'optionMap.transcription.temperature',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 1,
        step: 0.1,
      },
      ifShow: () => props.model.modelType == 'transcription',
    },
    {
      label: t('其它选项'),
      field: 'optionMapTitle',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('组织ID'),
      helpMessage: 'organizationId OpenAI 组织 ID，用于区分账单与配额，一般无需填写。',
      field: 'optionMap.organizationId',
      component: 'Input',
      componentProps: {
        maxlength: 100,
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

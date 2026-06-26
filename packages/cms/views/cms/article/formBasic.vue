<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicForm @register="registerForm" />
</template>
<script lang="ts" setup name="ViewsCmsArticleFormBasic">
  import { h, ref, Ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { Article } from '@jeesite/cms/api/cms/article';
  import { categoryTreeData } from '@jeesite/cms/api/cms/category';
  import type { NamePath } from 'antdv-next/dist/form/types';
  import { Checkbox } from 'antdv-next';

  const { t } = useI18n('cms.article');
  const record = ref<Article>({} as Article);
  let isNeedAudit = ref(false);

  const inputFormSchemas: FormSchema<Article>[] = [
    {
      label: t('归属栏目'),
      field: 'category.categoryCode',
      component: 'TreeSelect',
      componentProps: {
        api: categoryTreeData,
        canSelectParent: false,
        allowClear: true,
        onSelect: (value, node) => {
          isNeedAudit.value = node.isNeedAudit == '1';
        },
      },
      required: true,
    },
    {
      label: t('内容来源'),
      field: 'source',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'cms_source',
        onChange: (val: any) => {
          record.value.source = val;
        },
      },
      defaultValue: '2',
      required: true,
    },
    {
      label: t('内容出处'),
      field: 'copyfrom',
      component: 'Input',
      componentProps: {
        maxlength: 255,
      },
      colProps: { md: 24, lg: 24 },
      show: () => record.value.source === '1',
    },
    {
      label: t('内容标题'),
      field: 'title',
      component: 'Input',
      componentProps: {
        maxlength: 255,
        class: 'text-ruler',
        addonAfter: () =>
          h('div', { style: 'position:relative;' }, [
            h('input', {
              style: 'border:0;width:28px;padding:0 2px',
              type: 'color',
              title: t('标题颜色'),
              value: record.value.color || '#555555',
              onChange: (e: any) => {
                record.value.color = e.target?.value || '';
              },
            }),
            record.value.color &&
              h(
                'a',
                {
                  style: 'position:absolute;cursor:pointer;top:3px;right:-8px;font-size:12px;opacity:0.5',
                  title: t('清除颜色'),
                  onClick: () => {
                    record.value.color = '';
                  },
                },
                () => '×',
              ),
          ]),
      },
      required: true,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('外部链接'),
      helpMessage: '如果填写外部链接，点击该文章会直接跳转到该地址，不想不跳转请留空。',
      field: 'href',
      component: 'Input',
      componentProps: {
        maxlength: 1000,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('权重/排序'),
      helpMessage: '数值越大排序越靠前，可设置权重过期时间。',
      field: 'weight',
      component: 'InputNumber',
      componentProps: {
        // style: 'max-width: 180px',
        maxlength: 8,
      },
      suffix: ({ model }) => {
        return h(
          Checkbox,
          {
            onChange: (e: any) => {
              model.weight = e.target?.checked ? 9999 : 0;
            },
          },
          () => t('置顶'),
        );
      },
      rules: [{ pattern: /^\d+$/, message: t('请输入一个正整数') }],
    },
    {
      label: t('权重过期时间'),
      helpMessage: '时间到期后，权重自动恢复为0，如果为空，则权重永不过期。',
      field: 'weightDate',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
      },
    },
    {
      label: t('内容摘要'),
      field: 'description',
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
    setFieldsValue: async (values: Recordable, _isNeedAudit: Ref) => {
      record.value = values as Article;
      isNeedAudit = _isNeedAudit;
      await setFieldsValue(values);
    },
    validate: async (nameList?: NamePath[]) => {
      let data = await validate(nameList);
      data.color = record.value.color;
      return data;
    },
  });
</script>
<style lang="less">
  .text-ruler .ant-input {
    background: url('../../../assets/images/ruler.png') repeat-x scroll 0 bottom transparent;
  }
</style>

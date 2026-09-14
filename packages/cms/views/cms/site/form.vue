<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'cms:site:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #copyright="{ model, field }">
        <WangEditor v-model:value="model[field]" :bizKey="record.id" :bizType="'site_' + field" :height="300" />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsCmsSiteForm">
  import { ref, unref, computed, h } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Site, siteSave, siteForm } from '@jeesite/cms/api/cms/site';
  import { WangEditor } from '@jeesite/core/components/WangEditor';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('cms.site');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Site>({} as Site);
  const indexViewList = ref([]);

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增站点') : t('编辑站点'),
  }));

  const inputFormSchemas: FormSchema<Site>[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('站点名称'),
      field: 'siteName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
        placeholder: '默认站点',
      },
      required: true,
    },
    {
      label: t('站点编码'),
      field: 'siteCode',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
      rules: [{ required: true }, { pattern: /^[a-zA-Z0-9_]*$/, message: t('请输入字母数字下划线') }],
    },
    {
      label: t('站点标题'),
      field: 'title',
      component: 'Input',
      componentProps: {
        maxlength: 100,
        placeholder: '演示站点',
      },
      required: true,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('站点域名'),
      field: 'domainName',
      component: 'Input',
      componentProps: {
        maxlength: 500,
        placeholder: 'https://jeesite.com',
      },
    },
    {
      label: t('站点排序'),
      field: 'siteSort',
      component: 'Input',
      componentProps: {
        maxlength: 8,
      },
      defaultValue: 10,
      rules: [{ required: true }, { pattern: /^\d+$/, message: t('请输入一个正整数') }],
    },
    {
      label: t('站点Logo'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'site_logo',
        uploadType: 'image',
        maxNumber: 1,
        maxSize: 5,
        imageMaxWidth: 1024,
        imageMaxHeight: 768,
        // imageThumbName: '150x150.jpg',
        showPreviewList: true,
        onChange: (dataMap, fileList) => {
          fileList.forEach((e) => {
            record.value.logo = e.fileUrl;
          });
        },
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('详细信息'),
      field: 'detailInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('描述'),
      field: 'description',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
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
    {
      label: t('主题风格'),
      field: 'theme',
      component: 'Select',
      componentProps: {
        dictType: 'cms_theme',
        allowClear: true,
      },
    },
    {
      label: t('首页视图'),
      field: 'customIndexView',
      component: 'Select',
      componentProps: {
        options: indexViewList,
        allowClear: true,
      },
    },
    {
      label: t('版权信息'),
      field: 'copyright',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 1000,
      },
      slot: 'copyright',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('其它信息'),
      field: 'otherInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { md: 24, lg: 24 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm<Site>({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await siteForm(data);
    record.value = (res.site || {}) as Site;
    record.value.__t = new Date().getTime();
    indexViewList.value = (res.indexViewList || []).map((item) => {
      return { label: item.dictLabel, value: item.dictValue };
    });
    await setFieldsValue(record.value);
    await updateSchema([
      {
        field: 'siteCode',
        componentProps: {
          disabled: !record.value.isNewRecord,
        },
      },
    ]);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        siteCode: data.siteCode || record.value.siteCode,
      };
      data.logo = record.value.logo;
      // console.log('submit', params, data, record);
      const res = await siteSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>

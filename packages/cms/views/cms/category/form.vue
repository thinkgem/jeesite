<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'cms:category:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #viewConfigHelp>
        <div class="mx-2 mb-3">
          <Alert
            :title="`例如视图参数设置为：{count:2,titleShow:'yes'} 则在视图文件中的获取方法是：\${viewConfig_count}、\${viewConfig_titleShow}。
                      设置栏目的管理地址：若设置【adminUrl:false】表示无管理地址，在内容发布栏目列表中不显示该栏目`"
            type="info"
            banner
          />
        </div>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsCmsCategoryForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Category, categorySave, categoryForm, categoryTreeData } from '@jeesite/cms/api/cms/category';
  import { Alert } from 'antdv-next';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('cms.category');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Category>({} as Category);

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增栏目') : t('编辑栏目'),
  }));

  const category_DEFAULT_TEMPLATE = ref('');
  const article_DEFAULT_TEMPLATE = ref('');
  const listViewList = ref([]);
  const contentViewList = ref([]);
  const siteList = ref([]);

  const inputFormSchemas: FormSchema<Category>[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('上级栏目'),
      field: 'parentCode',
      fieldLabel: 'parentName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
      },
    },
    {
      label: t('归属站点'),
      field: 'site.siteCode',
      component: 'Select',
      componentProps: {
        options: siteList,
      },
      required: true,
    },
    {
      label: t('栏目编码'),
      field: 'categoryCode',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
      rules: [{ required: true }, { pattern: /^[a-zA-Z0-9_]*$/, message: t('请输入字母数字下划线') }],
    },
    {
      label: t('内容模型'),
      field: 'moduleType',
      component: 'Select',
      componentProps: {
        dictType: 'cms_module_type',
        allowClear: true,
      },
    },
    {
      label: t('栏目名称'),
      field: 'categoryName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('排序号'),
      field: 'treeSort',
      helpMessage: '升序',
      component: 'InputNumber',
      defaultValue: '30',
      componentProps: {
        maxlength: 8,
      },
      rules: [{ required: true }, { pattern: /^\d+$/, message: t('请输入一个正整数') }],
    },
    {
      label: t('栏目图片'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'category_image',
        uploadType: 'image',
        maxNumber: 1,
        // imageMaxWidth: 1024,
        // imageMaxHeight: 768,
        // imageThumbName: '150x150.jpg',
        showPreviewNumber: false,
        showPreviewList: true,
        emptyHidePreview: true,
        onChange: (dataMap, fileList) => {
          fileList.forEach((e) => {
            record.value.image = e.fileUrl;
          });
        },
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('外部链接'),
      field: 'href',
      component: 'Input',
      componentProps: {
        placeholder: '栏目超链接地址，优先级“高”',
        maxlength: 255,
      },
    },
    {
      label: t('链接目标'),
      field: 'target',
      component: 'Input',
      componentProps: {
        placeholder: '栏目超链接打开的目标窗口，新窗口打开，请填写：“_blank”',
        maxlength: 20,
      },
    },
    {
      label: t('栏目描述'),
      field: 'description',
      component: 'Input',
      componentProps: {
        placeholder: '填写描述，有助于搜搜引擎优化',
        maxlength: 500,
      },
    },
    {
      label: t('关键字'),
      field: 'keywords',
      component: 'Input',
      componentProps: {
        placeholder: '填写描述，有助于搜搜引擎优化',
        maxlength: 500,
      },
    },
    {
      label: t('栏目配置'),
      field: 'detailInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('在导航中显示'),
      helpMessage: '是否在导航中显示该栏目',
      field: 'inMenu',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_show_hide',
      },
    },
    {
      label: t('是否允许评论'),
      field: 'isCanComment',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_yes_no',
      },
    },
    {
      label: t('在分类页显示'),
      helpMessage: '是否在分类页中显示该栏目的文章列表',
      field: 'inList',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_show_hide',
      },
    },
    {
      label: t('是否需要审核'),
      helpMessage: '如果需要审核，则启动 BPM 申请流程（专业版）',
      field: 'isNeedAudit',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_yes_no',
      },
    },
    {
      label: t('内容展现模式'),
      helpMessage: '默认展现方式,首栏目内容列表,栏目第一条内容',
      field: 'showModes',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'cms_show_modes',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('自定义列表视图'),
      helpMessage: () => `自定义列表视图名称必须以 ${category_DEFAULT_TEMPLATE.value} 开始`,
      field: 'customListView',
      component: 'Select',
      componentProps: {
        options: listViewList,
        allowClear: true,
      },
    },
    {
      label: t('自定义内容视图'),
      helpMessage: () => `自定义内容视图名称必须以 ${article_DEFAULT_TEMPLATE.value} 开始`,
      field: 'customContentView',
      component: 'Select',
      componentProps: {
        options: contentViewList,
        allowClear: true,
      },
    },
    {
      label: t('视图参数配置'),
      helpMessage:
        "视图参数例如: {count:2, title_show:'yes'} 则在视图文件中的获取方法是：${viewConfig_count}、${viewConfig_titleShow}",
      field: 'viewConfig',
      component: 'Input',
      componentProps: {
        placeholder:
          "视图参数例如: {count:2, title_show:'yes'} 则在视图文件中的获取方法是：${viewConfig_count}、${viewConfig_titleShow}",
        maxlength: 1000,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'viewConfigHelp',
      component: 'Text',
      colProps: { md: 24, lg: 24 },
      slot: 'viewConfigHelp',
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

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm<Category>({
    labelWidth: 140,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await categoryForm(data);
    record.value = (res.category || {}) as Category;
    record.value.__t = new Date().getTime();
    if (data.parentCode && data.parentName) {
      record.value.parentCode = data.parentCode;
      record.value.parentName = data.parentName;
    }
    category_DEFAULT_TEMPLATE.value = res.category_DEFAULT_TEMPLATE;
    article_DEFAULT_TEMPLATE.value = res.article_DEFAULT_TEMPLATE;
    listViewList.value = (res.listViewList || []).map((item) => {
      return { label: item.dictLabel, value: item.dictValue };
    });
    contentViewList.value = (res.contentViewList || []).map((item) => {
      return { label: item.dictLabel, value: item.dictValue };
    });
    siteList.value = (res.siteList || []).map((item) => {
      return { label: item.siteName, value: item.siteCode };
    });
    await setFieldsValue(record.value);
    await updateSchema([
      {
        field: 'parentCode',
        componentProps: {
          api: categoryTreeData,
          params: {
            excludeCode: record.value.id,
            isShowRawName: true,
          },
        },
      },
      {
        field: 'categoryCode',
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
        categoryCode: record.value.categoryCode || data.categoryCode,
      };
      data.oldParentCode = record.value.parentCode;
      data.image = record.value.image;
      // console.log('submit', params, data, record);
      const res = await categorySave(params, data);
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

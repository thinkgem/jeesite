<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:dictData:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysDictDataForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { DictData, dictDataSave, dictDataForm, dictDataTreeData } from '/@/api/sys/dictData';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.dictData');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<DictData>({} as DictData);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增选项') : t('编辑选项'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('上级选项'),
      field: 'parentCode',
      fieldLabel: 'parentName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
        // style: 'width: calc(50% - 60px)',
      },
      // colProps: { lg: 24, md: 24 },
    },
    {
      field: 'none',
      component: 'None',
    },

    {
      label: t('选项标签'),
      field: 'dictLabelRaw',
      component: 'Input',
      required: true,
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('选项键值'),
      field: 'dictValue',
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
      componentProps: {
        maxlength: 10,
      },
      defaultValue: '30',
      required: true,
    },
    {
      label: t('系统内置'),
      field: 'isSys',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_yes_no',
      },
      required: true,
    },

    {
      label: t('选项描述'),
      field: 'description',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
    },
    {
      label: t('选项图标'),
      field: 'menuIcon',
      component: 'IconPicker',
    },

    {
      label: t('CSS类名'),
      field: 'cssClass',
      helpMessage: [
        '支持以下风格内置字典样式：',
        'tag pink、tag red、tag orange、tag green、tag cyan、tag blue、tag purple',
        'tag error、tag success、tag warning、tag processing、tag default',
        'badge error、badge success、badge warning、badge processing',
      ],
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
    },
    {
      label: t('CSS样式'),
      field: 'cssStyle',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
    },

    // {
    //   label: t('其它信息'),
    //   field: 'otherInfo',
    //   component: 'Divider',
    //   colProps: { lg: 24, md: 24 },
    // },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { lg: 24, md: 24 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await dictDataForm(data);
    record.value = (res.dictData || {}) as DictData;
    if (data.dictType) {
      record.value.dictType = data.dictType;
    }
    if (data.parentCode && data.parentName) {
      record.value.parentCode = data.parentCode;
      record.value.parentName = data.parentName;
    }
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'parentCode',
        componentProps: {
          api: dictDataTreeData,
          params: {
            dictType: record.value.dictType,
            excludeCode: record.value.dictCode,
            isShowRawName: true,
          },
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
        dictCode: record.value.dictCode,
      };
      data.dictType = record.value.dictType;
      data.oldParentCode = record.value.parentCode;
      // console.log('submit', params, data, record);
      const res = await dictDataSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>

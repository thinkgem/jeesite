<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:office:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysOfficeForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Office, officeSave, officeForm, officeTreeData } from '@jeesite/core/api/sys/office';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.office');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Office>({} as Office);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增机构') : t('编辑机构'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('上级机构'),
      field: 'parentCode',
      fieldLabel: 'parentName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
        // style: 'width: calc(50% - 60px)',
      },
      // colProps: { md: 24, lg: 24 },
    },
    {
      field: 'none',
      component: 'None',
    },
    {
      label: t('机构名称'),
      field: 'officeName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('机构代码'),
      field: 'viewCode',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('机构全称'),
      field: 'fullName',
      component: 'Input',
      componentProps: {
        maxlength: 200,
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
        maxlength: 10,
      },
      required: true,
    },
    {
      label: t('机构类型'),
      field: 'officeType',
      component: 'Select',
      componentProps: {
        dictType: 'sys_office_type',
        allowClear: true,
      },
      required: true,
    },

    {
      label: t('详细信息'),
      field: 'officeInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('负责人'),
      field: 'leader',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('办公电话'),
      field: 'phone',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('联系地址'),
      field: 'address',
      component: 'Input',
      componentProps: {
        maxlength: 255,
      },
    },
    {
      label: t('邮政编码'),
      field: 'zipCode',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('电子邮箱'),
      field: 'email',
      component: 'Input',
      componentProps: {
        maxlength: 300,
      },
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

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await officeForm(data);
    record.value = (res.office || {}) as Office;
    if (data.parentCode && data.parentName) {
      record.value.parentCode = data.parentCode;
      record.value.parentName = data.parentName;
    }
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'parentCode',
        componentProps: {
          api: officeTreeData,
          params: {
            excludeCode: record.value.id,
            isShowRawName: true,
          },
        },
      },
      {
        field: 'viewCode',
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
        officeCode: record.value.officeCode,
      };
      data.oldParentCode = record.value.parentCode;
      // console.log('submit', params, data, record);
      const res = await officeSave(params, data);
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

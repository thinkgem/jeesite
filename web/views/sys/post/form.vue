<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:post:edit'"
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
<script lang="ts" setup name="ViewsSysPostForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Post, postSave, postForm } from '@jeesite/core/api/sys/post';
  import { roleTreeData } from '@jeesite/core/api/sys/role';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.post');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Post>({} as Post);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增岗位') : t('编辑岗位'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('岗位名称'),
      field: 'postName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('岗位代码'),
      field: 'viewCode',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
    },
    {
      label: t('岗位分类'),
      field: 'postType',
      component: 'Select',
      componentProps: {
        dictType: 'sys_post_type',
        allowClear: true,
      },
    },
    {
      label: t('排序号'),
      field: 'postSort',
      helpMessage: '升序',
      component: 'InputNumber',
      defaultValue: '30',
      componentProps: {
        maxlength: 10,
      },
      required: true,
    },
    {
      label: t('关联角色'),
      field: 'roleCodes',
      fieldLabel: 'roleNames',
      component: 'TreeSelect',
      componentProps: {
        api: roleTreeData,
        treeCheckable: true,
      },
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

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await postForm(data);
    record.value = (res.post || {}) as Post;
    record.value.roleCodes = res.roleCodes || '';
    record.value.roleNames = res.roleNames || '';
    setFieldsValue(record.value);
    updateSchema([
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
        postCode: record.value.postCode,
        oldRoleName: record.value.postName,
      };
      // console.log('submit', params, data, record);
      const res = await postSave(params, data);
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

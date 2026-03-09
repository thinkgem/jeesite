<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'dbm:data:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="90%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsDbmDataForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, useForm, FormProps } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { dbmDataForm, dbmDataSave } from '@jeesite/dbm/api/dbm/data';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('dbm.data');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({} as Recordable);

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增数据') : t('编辑数据'),
  }));

  const [registerForm, { resetFields, setFieldsValue, validate, setProps }] = useForm<Recordable>({
    labelWidth: 120,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await dbmDataForm(data);
    const formProps = (res.formProps || {}) as FormProps;
    const formKey = formProps.formKey || 'id';
    for (const schema of formProps.schemas || []) {
      if (schema.field == formKey) {
        schema.dynamicDisabled = formKeyDynamicDisabled;
        break;
      }
    }
    await setProps(formProps);
    record.value = (res.dbmDataEntity || {}) as Recordable;
    record.value.__t = new Date().getTime();
    await setFieldsValue(record.value);
    setDrawerProps({ loading: false });
  });

  function formKeyDynamicDisabled() {
    return !record.value?.isNewRecord;
  }

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      data.__entityId = record.value.__entityId;
      data.isNewRecord = record.value.isNewRecord;
      // console.log('submit', data, record);
      const res = await dbmDataSave(data);
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

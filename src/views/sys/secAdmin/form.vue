<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:empUser:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="80%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #dataScopeTrees>
        <CustomDataScope ref="customDataScopeRef" :api="ctrlDataTreeData" :ctrlPermis="['0', '2']" />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysEmpUserAuthDataScope">
  import { ref, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { ctrlDataTreeData } from '/@/api/sys/empUser';
  import { secAdminForm, secAdminSave } from '/@/api/sys/secAdmin';
  import CustomDataScope from '../role/components/CustomDataScope.vue';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({});
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: t('二级管理员'),
  };
  const customDataScopeRef = ref<InstanceType<typeof CustomDataScope>>();

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('登录账号'),
      field: 'userCode',
      fieldLabel: 'userName',
      // component: 'TreeSelect',
      // componentProps: {
      //   api: officeTreeData,
      //   params: { isLoadUser: true, userIdPrefix: '' },
      //   canSelectParent: false,
      //   allowClear: true,
      //   onChange: (userCode) => {
      //     loadData({ userCode });
      //   },
      // },
      component: 'ListSelect',
      componentProps: {
        // checkbox: true,
        selectType: 'empUserSelect',
        onChange: (userCode) => {
          loadData({ userCode });
        },
      },
    },
    {
      label: t('用户昵称'),
      field: 'userName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('可管理的数据权限'),
      field: 'dataScopeInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'userDataScopeList',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'dataScopeTrees',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
    labelWidth: 120,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await resetFields();
    loadData(data);
  });

  async function loadData(data) {
    setDrawerProps({ loading: true });
    const res = await secAdminForm(data);
    record.value = (res.user || {}) as Recordable;
    setFieldsValue(record.value);
    await customDataScopeRef.value?.loadDataScopeList({
      dataScopes: res.dataScopes || [],
      dataScopeList: res.userDataScopeList || [],
      moduleCodes: res.moduleCodes || [],
      ctrlPermi: res.ctrlPermi || '2',
    });
    setDrawerProps({ loading: false });
  }

  async function handleSubmit() {
    try {
      if (!record.value.userCode) {
        showMessage(t('请选择要设置二级管理员身份的登录账号'));
        return;
      }
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        ...data,
        isNewRecord: record.value.isNewRecord,
        userCode: record.value.userCode,
        userDataScopeListJson: customDataScopeRef.value?.getDataScopeListJson(),
      };
      // console.log('submit', params, data, record);
      const res = await secAdminSave(params);
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

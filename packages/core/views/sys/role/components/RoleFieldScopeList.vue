<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="pt-2">
    <BasicForm @register="registerForm">
      <template #fieldScopeList>
        <BasicTable @register="registerTable">
          <template #firstColumn="{ record, text, value }">
            <a @click="handleForm({ id: record.id })" :title="value">
              {{ text }}
            </a>
          </template>
        </BasicTable>
      </template>
    </BasicForm>
    <RoleFieldScopeForm @register="registerFormDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthFieldScope">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { BasicForm, FormProps, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { BasicColumn, BasicTable, useTable } from '@jeesite/core/components/Table';
  import { roleDeleteAuthFieldScope, roleFieldScopeListData } from '@jeesite/core/api/sys';
  import RoleFieldScopeForm from '@jeesite/core/views/sys/role/components/RoleFieldScopeForm.vue';
  import { useDrawer } from '@jeesite/core/components/Drawer';

  const props = defineProps({
    resizeHeightOffset: propTypes.number.def(60),
    menuFieldScope: propTypes.bool,
  });

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const record = ref<Recordable>({});

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => !!props.menuFieldScope,
    },
    {
      label: t('菜单名称'),
      field: 'menuName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      ifShow: () => !!props.menuFieldScope,
    },
    {
      label: t('权限标识'),
      field: 'permission',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      ifShow: () => !!props.menuFieldScope,
    },
    {
      label: t('权限列表'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => !!props.menuFieldScope,
    },
    {
      field: 'roleFieldScopeList',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'fieldScopeList',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, getFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const searchForm: FormProps<Recordable> = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('实体名称'),
        field: 'entityName',
        component: 'Input',
      },
      {
        label: t('实体标签'),
        field: 'entityLabel',
        component: 'Input',
      },
      {
        label: t('实体类名'),
        field: 'entityClass',
        component: 'Input',
      },
    ],
  };

  const tableColumns: BasicColumn<Recordable>[] = [
    {
      title: t('实体名称'),
      dataIndex: 'entityName',
      key: 'a.entity_name',
      sorter: true,
      width: 50,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('实体标签'),
      dataIndex: 'entityLabel',
      key: 'a.entity_label',
      sorter: true,
      width: 50,
      align: 'left',
    },
    {
      title: t('实体类名称'),
      dataIndex: 'entityClass',
      key: 'a.class_name',
      sorter: true,
      width: 100,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn<Recordable> = {
    width: 50,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑权限'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'sys:role:edit',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除权限'),
        popConfirm: {
          title: t('是否确认删除权限'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'sys:role:edit',
      },
    ],
  };

  const [registerTable, { reload, getForm, getDataSource, getRawDataSource }] = useTable<Recordable>({
    api: roleFieldScopeListData,
    beforeFetch: (params) => {
      params.roleCode = record.value.roleCode;
      params.menuCode = record.value.menuCode;
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    indexColumnProps: { width: 20 },
    formConfig: searchForm,
    showTableSetting: false,
    useSearchForm: true,
    canResize: true,
    resizeHeightOffset: props.resizeHeightOffset,
    immediate: false,
  });

  const [registerFormDrawer, formAction] = useDrawer();

  function handleForm(record: Recordable) {
    formAction.openDrawer(true, record);
  }

  async function handleDelete(record: Recordable) {
    const params = { id: record.id };
    const res = await roleDeleteAuthFieldScope(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }

  async function loadFieldScopeFormData(data: Recordable) {
    await resetFields();
    record.value = data;
    await setFieldsValue(record.value);
    await reload();
  }

  function isEmptyTable() {
    return getDataSource().length === 0;
  }

  defineExpose({
    loadFieldScopeFormData,
    isEmptyTable,
    handleForm,
  });
</script>

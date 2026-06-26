<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Think Gem
-->
<template>
  <BasicForm @register="registerForm">
    <template #extendMap>
      <BasicTable @register="registerTable" @row-click="handleRowClick" />
      <a-button class="mt-2" @click="handleRowAdd">
        <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
      </a-button>
    </template>
  </BasicForm>
</template>
<script lang="ts" setup name="JeeSiteExtendField">
  import { ref, watchEffect } from 'vue';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicTable, useTable } from '@jeesite/core/components/Table';
  import { buildUUID } from '@jeesite/core/utils/uuid';
  import type { NamePath } from 'antdv-next/dist/form/types';

  const props = defineProps({
    title: propTypes.oneOfType([propTypes.string, propTypes.bool]).def('扩展字段'),
    collapsed: propTypes.bool.def(true),
    extendS1: propTypes.string.def('字符串 1'),
    extendS2: propTypes.string.def('字符串 2'),
    extendS3: propTypes.string.def('字符串 3'),
    extendS4: propTypes.string.def('字符串 4'),
    extendS5: propTypes.string.def('字符串 5'),
    extendS6: propTypes.string.def('字符串 6'),
    extendS7: propTypes.string.def('字符串 7'),
    extendS8: propTypes.string.def('字符串 8'),
    extendI1: propTypes.string.def('整数 1'),
    extendI2: propTypes.string.def('整数 2'),
    extendI3: propTypes.string.def('整数 3'),
    extendI4: propTypes.string.def('整数 4'),
    extendF1: propTypes.string.def('小数 1'),
    extendF2: propTypes.string.def('小数 2'),
    extendF3: propTypes.string.def('小数 3'),
    extendF4: propTypes.string.def('小数 4'),
    extendD1: propTypes.string.def('日期时间 1'),
    extendD2: propTypes.string.def('日期时间 2'),
    extendD3: propTypes.string.def('日期时间 3'),
    extendD4: propTypes.string.def('日期时间 4'),
    extendMap: propTypes.string.def('自定义属性'),
  });

  const { t } = useI18n('sys.extend');
  const innerCollapsed = ref<boolean>(props.collapsed);

  watchEffect(() => {
    innerCollapsed.value = props.collapsed;
  });

  const inputFormSchemas: FormSchema[] = [
    {
      label: t(String(props.title)),
      field: 'extendInfo',
      component: 'FormGroup',
      componentProps: {
        collapsed: innerCollapsed.value,
        onCollapsed: (value: boolean) => {
          innerCollapsed.value = value;
        },
      },
      colProps: { md: 24, lg: 24 },
      show: () => props.title !== false,
    },
    {
      label: t(props.extendS1),
      field: 'extendS1',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS2),
      field: 'extendS2',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS3),
      field: 'extendS3',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS4),
      field: 'extendS4',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS5),
      field: 'extendS5',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS6),
      field: 'extendS6',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS7),
      field: 'extendS7',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendS8),
      field: 'extendS8',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendI1),
      field: 'extendI1',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendI1),
      field: 'extendI2',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendI3),
      field: 'extendI3',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendI4),
      field: 'extendI4',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendF1),
      field: 'extendF1',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 0.1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendF1),
      field: 'extendF2',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 0.1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendF3),
      field: 'extendF3',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 0.1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendF4),
      field: 'extendF4',
      component: 'InputNumber',
      componentProps: {
        min: 0,
        step: 0.1,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendD1),
      field: 'extendD1',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm:ss',
        showTime: { format: 'HH:mm:ss' },
        allowClear: true,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendD2),
      field: 'extendD2',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm:ss',
        showTime: { format: 'HH:mm:ss' },
        allowClear: true,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendD3),
      field: 'extendD3',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm:ss',
        showTime: { format: 'HH:mm:ss' },
        allowClear: true,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendD4),
      field: 'extendD4',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm:ss',
        showTime: { format: 'HH:mm:ss' },
        allowClear: true,
      },
      show: () => !innerCollapsed.value,
    },
    {
      label: t(props.extendMap),
      field: 'extendMap',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'extendMap',
      show: () => !innerCollapsed.value,
    },
  ];

  const [registerTable, tableAction] = useTable({
    columns: [
      {
        title: t('属性名'),
        dataIndex: 'extendMapKey',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'Input',
      },
      {
        title: t('属性值'),
        dataIndex: 'extendMapValue',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'Input',
      },
    ],
    actionColumn: {
      width: 120,
      actions: (record: Recordable) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: '是否确认删除',
            confirm: handleRowDelete.bind(this, record),
          },
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
  });

  function handleRowClick(record: Recordable) {
    record.onEdit?.(true, false);
  }

  function handleRowAdd() {
    tableAction.insertTableDataRecord({
      id: 'rid_' + new Date().getTime(),
      editable: true,
    });
    // tableAction.scrollTo('bottom');
  }

  function handleRowDelete(record: Recordable) {
    tableAction.deleteTableDataRecord(record);
  }

  async function getTableData(): Promise<Recordable> {
    let extendMap: Recordable = {};
    let valid = true;
    let tableData = tableAction.getDataSource();
    for (const record of tableData) {
      if (!(await record.onEdit?.(false, true))) {
        valid = false;
      }
      extendMap[record.extendMapKey] = record.extendMapValue;
    }
    if (!valid) {
      throw {
        errorFields: [{ name: ['extendMap'] }],
        message: t(props.extendMap + '填写有误，请根据提示修正'),
      };
    }
    return extendMap;
  }

  async function setTableData(res?: Recordable) {
    let extendMapData: any[] = [];
    const extendMap = res?.extendMap || {};
    for (const key in extendMap) {
      extendMapData.push({ id: buildUUID(), extendMapKey: key, extendMapValue: extendMap[key] });
    }
    // console.log(extendMapData);
    tableAction.setTableData(extendMapData);
  }

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  defineExpose({
    resetFields: async () => {
      await resetFields();
      await setTableData();
    },
    setFieldsValue: async (values: Recordable) => {
      await setFieldsValue(values);
      await setTableData(values);
    },
    validate: async (nameList?: NamePath[]) => {
      const data = await validate(nameList);
      data.extendMap = await getTableData();
      return data;
    },
  });
</script>
<style lang="less"></style>

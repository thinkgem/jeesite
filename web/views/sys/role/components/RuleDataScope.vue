<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="pt-2">
    <BasicForm @register="registerForm">
      <template #dataScopeTable>
        <BasicTable @register="registerTable" @row-click="handleRowClick" />
        <a-button class="mt-2" @click="handleRowAdd" v-auth="'sys:role:edit'">
          <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
        </a-button>
      </template>
    </BasicForm>
    <div class="ml-5 mt-2">
      <Alert
        message="匹配值支持如下动态表达式：
          （1）当前用户对象中的属性值：#{currentUser.属性名}
          （2）会话对象中的属性值：#{session.属性名}
          （3）当前查询实体中的属性值：#{属性名}
          （4）当前实体查询不到，则从用户缓存里查询，
               仍然查询不到，则从系统参数配置里获取，
               再查询不到，则原样返回。"
        type="info"
      />
    </div>
  </div>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  // import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicTable, useTable } from '@jeesite/core/components/Table';
  import { Alert } from 'ant-design-vue';
  import { omit, pick } from 'lodash-es';

  const { t } = useI18n('sys.role');
  // const { showMessage } = useMessage();
  const record = ref<Recordable>({});

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('菜单名称'),
      field: 'menuName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('权限标识'),
      field: 'permission',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('规则列表'),
      field: 'authDataScopeRule',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'ruleList',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'dataScopeTable',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, getFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerTable, tableAction] = useTable({
    columns: [
      {
        title: t('关系'),
        dataIndex: 'andOr',
        width: 60,
        align: 'left',
        editRow: true,
        editComponent: 'Select',
        className: 'jeesite-table-tree-name',
        editComponentProps: {
          options: [
            { label: t('并且'), value: 'AND' },
            { label: t('或者'), value: 'OR' },
          ],
        },
        format: (text, _record, _index, column) => {
          return column?.editComponentProps.options.find((item) => item.value === text).label;
        },
      },
      {
        title: t('字段名'),
        dataIndex: 'columnName',
        width: 80,
        align: 'left',
        editRow: true,
        editComponent: 'Input',
        editComponentProps: {},
      },
      {
        title: t('条件'),
        dataIndex: 'queryType',
        width: 50,
        align: 'left',
        editRow: true,
        editComponent: 'Select',
        editComponentProps: {
          options: [
            { label: t('等于'), value: 'EQ' },
            { label: t('不等于'), value: 'NE' },
            { label: t('大于'), value: 'GT' },
            { label: t('大于等于'), value: 'GTE' },
            { label: t('小于'), value: 'LT' },
            { label: t('小于等于'), value: 'LTE' },
            { label: t('等多值'), value: 'IN' },
            { label: t('不等多值'), value: 'NOT_IN' },
            { label: t('包含'), value: 'LIKE' },
            { label: t('不包含'), value: 'NOT_LIKE' },
            { label: t('开始以'), value: 'RIGHT_LIKE' },
            { label: t('不开始以'), value: 'RIGHT_NOT_LIKE' },
            { label: t('结束以'), value: 'LEFT_LIKE' },
            { label: t('不结束以'), value: 'LEFT_NOT_LIKE' },
            { label: t('是空'), value: 'IS_NULL' },
            { label: t('不是空'), value: 'IS_NOT_NULL' },
          ],
        },
        format: (text, _record, _index, column) => {
          return column?.editComponentProps.options.find((item) => item.value === text).label;
        },
      },
      {
        title: t('匹配值'),
        dataIndex: 'value',
        width: 80,
        align: 'left',
        editRow: true,
        editComponent: 'Input',
        editComponentProps: {},
      },
      {
        title: t('值类型'),
        dataIndex: 'columnType',
        width: 50,
        align: 'left',
        editRow: true,
        editComponent: 'Select',
        editComponentProps: {
          options: [
            { label: t('字符串'), value: 'String' },
            { label: t('整型'), value: 'Long' },
            { label: t('长整型'), value: 'Integer' },
            { label: t('浮点型'), value: 'Float' },
            { label: t('双精度'), value: 'Double' },
            { label: t('布尔型'), value: 'Boolean' },
            { label: t('日期'), value: 'Date' },
          ],
        },
        format: (text, _record, _index, column) => {
          return column?.editComponentProps.options.find((item) => item.value === text).label;
        },
      },
    ],
    actionColumn: {
      width: 50,
      actions: (record: Recordable) => [
        {
          icon: 'i-fluent:add-circle-24-regular',
          title: t('新增下级规则'),
          onClick: handleRowAdd.bind(this, record),
          auth: 'sys:role:edit',
        },
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: t('是否确认删除'),
            confirm: handleRowDelete.bind(this, record),
          },
          auth: 'sys:role:edit',
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
    isTreeTable: true,
    childrenColumnName: 'children',
  });

  function handleRowClick(data: Recordable) {
    data.onEdit?.(true, false);
  }

  function handleRowAdd(parent?: Recordable) {
    const record = {
      id: new Date().getTime(),
      editable: true,
      andOr: 'AND',
      queryType: 'EQ',
      columnType: 'String',
      children: [],
    };
    if (parent && parent.children) {
      parent.children.push(record);
      tableAction.expandRows([parent.id]);
    } else {
      tableAction.insertTableDataRecord(record);
    }
  }

  function handleRowDelete(data: Recordable) {
    tableAction.deleteTableDataRecord(data);
  }

  async function getTableData(children?: Recordable[]): Promise<Recordable> {
    let tableList: Recordable[] = [];
    for (const record of children || tableAction.getDataSource()) {
      await record.onEdit?.(false, true);
      const newRecord = {
        ...pick(record, 'id', 'andOr', 'columnName', 'queryType', 'columnType', 'value'),
        children: [],
      } as Recordable;
      if (record.children && record.children.length > 0) {
        newRecord.children = await getTableData(record.children);
      }
      tableList.push(newRecord);
    }
    return tableList;
  }

  async function loadDataScopeFormData(data: Recordable, res?: Recordable) {
    await resetFields();
    record.value = data;
    await setFieldsValue(record.value);
    tableAction.setTableData(data.ruleList || []);
    tableAction.expandAll();
  }

  async function getDataScopeFormData() {
    return {
      ...(await validate()),
      menuCode: record.value.menuCode,
      ruleList: await getTableData(),
    };
  }

  defineExpose({
    loadDataScopeFormData,
    getDataScopeFormData,
  });
</script>

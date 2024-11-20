<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @row-click="handleRowClick" />
    <a-button class="mt-2" @click="handleRowAdd" v-auth="'test:testData:edit'">
      <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
    </a-button>
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { areaTreeData } from '@jeesite/core/api/sys/area';
  import { TestData } from '@jeesite/test/api/test/testData';

  const { t } = useI18n('test.testDataChild');
  const record = ref<TestData>({} as TestData);

  const tableColumns: BasicColumn[] = [
    {
      title: t('单行文本'),
      dataIndex: 'testInput',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'Input',
      editRule: true,
    },
    {
      title: t('多行文本'),
      dataIndex: 'testTextarea',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'InputTextArea',
      // 子表自定义验证实例
      editRule: (value, _record) => {
        return new Promise((resolve, reject) => {
          if (!value || value === '') return resolve();
          if (value.length < 3) return reject('至少3个字符');
          return resolve(); // 验证成功
        });
      },
    },
    {
      title: t('下拉框'),
      dataIndex: 'testSelect',
      width: 130,
      align: 'left',
      dictType: 'sys_menu_type',
      editRow: true,
      editComponent: 'Select',
      editComponentProps: {
        dictType: 'sys_menu_type',
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('下拉多选'),
      dataIndex: 'testSelectMultiple',
      width: 130,
      align: 'left',
      dictType: 'sys_menu_type',
      editRow: true,
      editComponent: 'Select',
      editComponentProps: {
        dictType: 'sys_menu_type',
        mode: 'multiple',
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('日期选择'),
      dataIndex: 'testDate',
      width: 130,
      align: 'center',
      editRow: true,
      editComponent: 'DatePicker',
      editComponentProps: {
        format: 'YYYY-MM-DD',
        showTime: false,
      },
      editRule: false,
    },
    {
      title: t('日期时间'),
      dataIndex: 'testDatetime',
      width: 215,
      align: 'center',
      editRow: true,
      editComponent: 'DatePicker',
      editComponentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
      },
      editRule: false,
    },
    {
      title: t('用户选择'),
      dataIndex: 'testUser.userCode',
      dataLabel: 'testUser.userName',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'TreeSelect',
      editComponentProps: {
        api: officeTreeData,
        params: { isLoadUser: true, userIdPrefix: '' },
        canSelectParent: false,
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('机构选择'),
      dataIndex: 'testOffice.officeCode',
      dataLabel: 'testOffice.officeName',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'TreeSelect',
      editComponentProps: {
        api: officeTreeData,
        canSelectParent: false,
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('区域选择'),
      dataIndex: 'testAreaCode',
      dataLabel: 'testAreaName',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'TreeSelect',
      editComponentProps: {
        api: areaTreeData,
        canSelectParent: false,
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('文件上传'),
      dataIndex: 'dataMap',
      width: 160,
      align: 'left',
      editRow: true,
      editComponent: 'Upload',
      editComponentProps: ({ record: childRecord }) => {
        return {
          loadTime: record.value.__t,
          bizKey: childRecord.id,
          bizType: 'testDataChild_file',
          uploadType: 'all',
          // imageMaxWidth: 1024,
          // imageMaxHeight: 768,
          // imageThumbName: '150x150.jpg',
          size: 'small',
        };
      },
      // 文件上传的必填验证实例
      // editRule: (value: any, record: Recordable) => {
      //   return new Promise((resolve, reject) => {
      //     const len = !value || value['testDataChild_file__len'] || 0;
      //     if (len == 0) reject(t('请上传图片'));
      //     else resolve();
      //   });
      // },
    },
  ];

  const [registerTable, tableAction] = useTable({
    columns: tableColumns,
    actionColumn: {
      width: 60,
      actions: (record: Recordable) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: '是否确认删除',
            confirm: handleRowDelete.bind(this, record),
          },
          auth: 'test:testData:edit',
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
  });

  function handleRowClick(data: Recordable) {
    data.onEdit?.(true, false);
  }

  function handleRowAdd() {
    tableAction.insertTableDataRecord({
      id: 'rid_' + new Date().getTime(),
      isNewRecord: true,
      editable: true,
    });
  }

  function handleRowDelete(data: Recordable) {
    tableAction.deleteTableDataRecord(data);
  }

  async function getTableData(data: Recordable): Promise<Recordable> {
    let valid = true;
    let tableList: Recordable[] = [];
    for (const record of tableAction.getDataSource()) {
      if (!(await record.onEdit?.(false, true))) {
        valid = false;
      }
      tableList.push({
        ...record,
        id: !!record.isNewRecord ? '' : record.id,
      });
    }
    for (const record of tableAction.getDelDataSource()) {
      if (!!record.isNewRecord) continue;
      tableList.push({
        ...record,
        status: '1',
      });
    }
    if (!valid) {
      throw {
        errorFields: [{ name: ['testDataChildList'] }],
        message: t('测试数据子表填写有误，请根据提示修正'),
      };
    }
    data.testDataChildList = tableList;
    return tableList;
  }

  async function setTableData(data: Recordable) {
    record.value = data as TestData;
    tableAction.setTableData(data.testDataChildList || []);
  }

  defineExpose({
    getTableData,
    setTableData,
  });
</script>

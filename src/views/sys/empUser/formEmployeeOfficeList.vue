<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @row-click="handleRowClick" />
    <a-button class="mt-2" @click="handleRowAdd" v-auth="'sys:empUser:edit'">
      <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
    </a-button>
  </div>
</template>
<script lang="ts" setup>
  import { useI18n } from '/@/hooks/web/useI18n';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, useTable } from '/@/components/Table';
  import { officeTreeData } from '/@/api/sys/office';
  import { Ref } from 'vue';

  const { t } = useI18n('sys.empUser');

  const [registerTable, tableAction] = useTable({
    actionColumn: {
      width: 60,
      actions: (record: Recordable) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: t('是否确认删除'),
            confirm: handleRowDelete.bind(this, record),
          },
          auth: 'sys:empUser:edit',
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
    // canResize: true,
    // minHeight: 100,
    // maxHeight: 100,
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
    // await employeeOfficeTable.scrollTo('bottom');
  }

  function handleRowDelete(data: Recordable) {
    tableAction.deleteTableDataRecord(data);
  }

  async function getTableData(data: Recordable): Promise<Recordable> {
    let valid = true;
    let tableList: Recordable[] = tableAction.getDataSource();
    for (const record of tableList) {
      if (!(await record.onEdit?.(false, true))) {
        valid = false;
      }
    }
    if (!valid) {
      throw {
        errorFields: [{ name: ['employeeOfficeList'] }],
        message: t('附属机构填写有误，请根据提示修正'),
      };
    }
    data.employee.employeeOfficeList = tableList;
    return tableList;
  }

  async function setTableData(
    data: Recordable,
    ctrlPermi: Ref<string>,
    postListOptions: Recordable,
  ) {
    tableAction.setColumns([
      {
        title: t('附属机构'),
        dataIndex: 'officeCode',
        dataLabel: 'officeName',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'TreeSelect',
        editComponentProps: {
          api: officeTreeData,
          params: { ctrlPermi },
          canSelectParent: true,
        },
        editRule: true,
      },
      {
        title: t('附属岗位'),
        dataIndex: 'postCode',
        dataLabel: 'postName',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'Select',
        editComponentProps: {
          options: postListOptions,
        },
        editRule: true,
      },
    ]);
    tableAction.setTableData(data.employee?.employeeOfficeList || []);
  }

  function clearTableData() {
    tableAction.setTableData([]);
  }

  defineExpose({
    getTableData,
    setTableData,
    clearTableData,
  });
</script>

<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" />
  </div>
</template>
<script lang="ts" setup>
  import { Ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { BasicTable, useTable } from '/@/components/Table';
  import { roleTreeData } from '/@/api/sys';

  const { t } = useI18n('sys.empUser');

  const [registerTable, tableAction] = useTable({
    columns: [
      {
        title: t('角色名称'),
        dataIndex: 'name',
        width: 260,
        align: 'center',
      },
      {
        title: t('角色编码'),
        dataIndex: 'id',
        width: 260,
        align: 'center',
      },
    ],
    rowSelection: { type: 'checkbox' },
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
  });

  async function getSelectRowKeyString(data: Recordable): Promise<Recordable> {
    data.userRoleString = tableAction.getSelectRowKeys().join(',');
    return data.userRoleString;
  }

  async function setTableData(data: Recordable, ctrlPermi: Ref<string>) {
    const dataSource = await roleTreeData({
      userType: 'employee',
      ctrlPermi: ctrlPermi.value,
    });
    tableAction.setTableData(dataSource || []);
    tableAction.setSelectedRowKeys(data.roleList?.map((item) => item.id) || []);
  }

  function clearTableData() {
    tableAction.setTableData([]);
    tableAction.setSelectedRowKeys([]);
  }

  defineExpose({
    getSelectRowKeyString,
    setTableData,
    clearTableData,
  });
</script>

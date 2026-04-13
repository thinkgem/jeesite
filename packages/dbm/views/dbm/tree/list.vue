<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="fetchSuccess">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="i-bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('折叠全部')">
          <Icon icon="i-bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <Popconfirm :title="t('是否确认执行数结构冗余字段修复吗？')" @confirm="handleFixTreeData({})">
          <a-button danger type="default" v-auth="'dbm:data:edit'" :title="t('修复数结构冗余字段')">
            <Icon icon="i-ant-design:file-exclamation-outlined" /> {{ t('修复') }}
          </a-button>
        </Popconfirm>
        <a-button type="primary" @click="handleForm({})" v-auth="'dbm:data:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record, text, value }">
        <a @click="handleForm(record)" :title="value">
          {{ text }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsDbmTreeList">
  import { nextTick, onMounted, ref, unref, watch } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useQuery } from '@jeesite/core/hooks/web/usePage';
  import { BasicTable, BasicColumn, BasicTableProps, useTable } from '@jeesite/core/components/Table';
  import { FormProps } from '@jeesite/core/components/Form';
  import {
    dbmFixTreeData,
    dbmTreeDelete,
    dbmTreeDisable,
    dbmTreeEnable,
    dbmTreeList,
    dbmTreeListData,
  } from '@jeesite/dbm/api/dbm/tree';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import InputForm from './form.vue';
  import { Popconfirm } from 'ant-design-vue';

  const props = defineProps({
    treeCodes: Array as PropType<string[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('dbm.tree');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref({} as Recordable);
  const rowKey = ref('id');
  const treeViewName = ref('id');
  const query = useQuery();

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('管理数据'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 100,
    showAdvancedButton: false,
  };

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑数据'),
        onClick: handleForm.bind(this, record),
        auth: 'dbm:data:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用数据'),
        popConfirm: {
          title: t('是否确认停用数据'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'dbm:data:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用数据'),
        popConfirm: {
          title: t('是否确认启用数据'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'dbm:data:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除数据'),
        popConfirm: {
          title: t('是否确认删除数据'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'dbm:data:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级数据'),
        onClick: () => {
          const data = {};
          data['__entityId'] = record['__entityId'];
          data['parentCode'] = record[rowKey.value];
          // data[treeViewName.value] = record[treeViewName.value];
          handleForm(data);
        },
        auth: 'dbm:data:edit',
      },
    ],
  };

  const [registerTable, { reload, expandAll, collapseAll, expandCollapse, setProps, getForm }] = useTable({
    api: dbmTreeListData,
    beforeFetch: (params) => {
      params['__entityId'] = record.value.__entityId;
      return params;
    },
    beforeChildFetch: (params) => {
      params['__entityId'] = record.value.__entityId;
      return params;
    },
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    isTreeTable: true,
    pagination: false,
    canResize: true,
    immediate: false,
  });

  onMounted(async () => {
    const __entityId = unref(query)['__entityId'];
    const res = await dbmTreeList({ __entityId });
    record.value = (res.dbmTreeEntity || {}) as Recordable;
    rowKey.value = res.tableProps?.rowKey || 'id';
    treeViewName.value = res.treeViewName || 'id';
    await getForm().setProps(res.formProps || {});
    const tableProps = (res.tableProps || {}) as BasicTableProps;
    tableProps.tableSettingStoreKey = record.value.__entityId;
    setProps(tableProps);
    await reload();
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  watch(
    () => props.treeCodes,
    async () => {
      const values = {};
      values[rowKey.value] = props.treeCodes;
      await getForm().setFieldsValue(values);
      await reload();
    },
  );

  function fetchSuccess() {
    if (props.treeCodes) {
      nextTick(expandAll);
    }
  }

  function handleForm(data: Recordable) {
    data['__entityId'] = record.value.__entityId;
    openDrawer(true, data);
  }

  async function handleFixTreeData(data: Recordable) {
    data['__entityId'] = record.value.__entityId;
    const res = await dbmFixTreeData(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleDisable(data: Recordable) {
    const res = await dbmTreeDisable(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleEnable(data: Recordable) {
    const res = await dbmTreeEnable(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleDelete(data: Recordable) {
    const res = await dbmTreeDelete(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }
</script>

<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @fetchSuccess="fetchSuccess">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
        <span>（ {{ dictType }} ）</span>
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('展开全部')">
          <Icon icon="bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <a-button type="primary" @click="handleForm({ dictType })" v-auth="'sys:dictData:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <Icon
          v-if="record.dictIcon"
          :icon="record.dictIcon"
          @click="expandCollapse(record)"
          class="text-base w-5 mr-2"
        />
        <a @click="handleForm({ dictCode: record.dictCode })" :title="record.dictCode">
          {{ record.dictLabelRaw }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysDictDataList',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, watch, nextTick, ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { dictDataDelete, dictDataListData } from '/@/api/sys/dictData';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCode: String,
  });

  const { t } = useI18n('sys.dictData');
  const { showMessage } = useMessage();
  const getTitle = {
    icon: 'ant-design:book-outlined',
    value: t('字典选项'),
  };
  const dictType = ref<string>(router.currentRoute.value.query.dictType as string);

  const searchForm: FormProps = {
    baseColProps: { lg: 4, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('选项标签'),
        field: 'dictLabelRaw',
        component: 'Input',
      },
      {
        label: t('选项键值'),
        field: 'dictValue',
        component: 'Input',
      },
      {
        label: t('系统内置'),
        field: 'isSys',
        component: 'Select',
        componentProps: {
          dictType: 'sys_yes_no',
        },
      },
      {
        label: t('状态'),
        field: 'status',
        labelWidth: 70,
        component: 'Select',
        componentProps: {
          dictType: 'sys_search_status',
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('选项标签'),
      dataIndex: 'dictLabelRaw',
      width: 230,
      align: 'left',
      slots: { customRender: 'firstColumn' },
    },
    {
      title: t('选项键值'),
      dataIndex: 'dictValue',
      width: 110,
    },
    {
      title: t('排序号'),
      dataIndex: 'treeSort',
      width: 60,
    },
    {
      title: t('系统内置'),
      dataIndex: 'isSys',
      width: 60,
      dictType: 'sys_yes_no',
    },
    {
      title: t('CSS类名'),
      dataIndex: 'cssClass',
      width: 100,
    },
    {
      title: t('CSS样式'),
      dataIndex: 'cssStyle',
      width: 100,
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      width: 120,
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      width: 130,
      align: 'left',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 80,
      dictType: 'sys_status',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 130,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑字典'),
        onClick: handleForm.bind(this, { dictCode: record.dictCode }),
        auth: 'sys:dictData:edit',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除字典'),
        popConfirm: {
          title: t('是否确认删除'),
          confirm: handleDelete.bind(this, { dictCode: record.dictCode }),
        },
        auth: 'sys:dictData:edit',
      },
      {
        icon: 'fluent:add-circle-24-regular',
        title: t('新建下级字典'),
        onClick: handleForm.bind(this, {
          dictType,
          parentCode: record.dictCode,
          parentName: record.dictLabelRaw,
        }),
        auth: 'sys:dictData:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse }] = useTable({
    api: dictDataListData,
    beforeFetch: (params) => {
      params.dictType = dictType.value || 'unknown';
      params.dictCode = props.treeCode;
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    isTreeTable: true,
    pagination: false,
    canResize: true,
  });

  watch(
    () => props.treeCode,
    () => {
      reload();
    },
  );

  function fetchSuccess() {
    if (props.treeCode) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDelete(record: Recordable) {
    const res = await dictDataDelete(record);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    if (record.parentCode) {
      // showMessage('刷新节点：' + record.parentCode);
    }
    reload();
  }
</script>

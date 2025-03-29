<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="fetchSuccess">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
        <span>（ {{ dictType }} ）</span>
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="i-bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('折叠全部')">
          <Icon icon="i-bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <a-button type="primary" @click="handleForm({ dictType })" v-auth="'sys:dictData:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <Icon
          v-if="record.dictIcon"
          :icon="record.dictIcon"
          @click="expandCollapse(record)"
          class="mr-2 w-5 text-base"
        />
        <a @click="handleForm({ dictCode: record.dictCode })" :title="record.dictCode">
          {{ record.dictLabelRaw }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysDictDataList">
  import { watch, nextTick, ref, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { dictDataDelete, dictDataListData } from '/@/api/sys/dictData';
  import { dictDataDisable, dictDataEnable } from '/@/api/sys/dictData';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import { isEmpty } from '/@/utils/is';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCodes: Array as PropType<String[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('sys.dictData');
  const { showMessage } = useMessage();
  const { query } = unref(router.currentRoute);
  const getTitle = {
    icon: 'i-ant-design:book-outlined',
    value: t('字典选项'),
  };
  const dictType = ref<string>(query.dictType as string);

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 4 },
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
          allowClear: true,
        },
      },
      {
        label: t('状态'),
        field: 'status',
        labelWidth: 70,
        component: 'Select',
        componentProps: {
          dictType: 'sys_search_status',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
    ],
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('选项标签'),
      dataIndex: 'dictLabelRaw',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
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
      width: 70,
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
    width: 150,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑选项'),
        onClick: handleForm.bind(this, { dictCode: record.dictCode }),
        auth: 'sys:dictData:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用选项'),
        popConfirm: {
          title: t('是否确认停用选项'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'sys:dictData:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用选项'),
        popConfirm: {
          title: t('是否确认启用选项'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'sys:dictData:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除选项'),
        popConfirm: {
          title: t('是否确认删除选项'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'sys:dictData:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级选项'),
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
      params.dictCode = !isEmpty(props.treeCodes) ? props.treeCodes[0] : '';
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
    () => props.treeCodes,
    () => {
      if (!isEmpty(props.treeCodes)) {
        reload();
      }
    },
  );

  function fetchSuccess() {
    if (!isEmpty(props.treeCodes)) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { dictCode: record.dictCode };
    const res = await dictDataDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { dictCode: record.dictCode };
    const res = await dictDataEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { dictCode: record.dictCode };
    const res = await dictDataDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

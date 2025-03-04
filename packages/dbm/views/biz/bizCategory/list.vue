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
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="i-bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('折叠全部')">
          <Icon icon="i-bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <a-button type="primary" @click="handleForm({})" v-auth="'biz:bizCategory:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <span class="cursor-pointer" @click="expandCollapse(record)">
          ( {{ record.viewCode }} )
        </span>
        <a @click="handleForm({ categoryCode: record.categoryCode })">
          {{ record.categoryName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsBizCategoryList">
  import { unref, watch, nextTick } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { bizCategoryDelete, bizCategoryListData } from '@jeesite/dbm/api/biz/bizCategory';
  import { bizCategoryDisable, bizCategoryEnable } from '@jeesite/dbm/api/biz/bizCategory';
  import { useModal } from '/@/components/Modal';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCode: String,
  });

  const { t } = useI18n('biz.bizCategory');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('分类管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('分类代码'),
        field: 'viewCode',
        component: 'Input',
      },
      {
        label: t('分类名称'),
        field: 'categoryName',
        component: 'Input',
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: 'sys_search_status',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
      {
        label: t('备注信息'),
        field: 'remarks',
        component: 'Input',
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('分类名称'),
      dataIndex: 'categoryName',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('排序号'),
      dataIndex: 'treeSort',
      width: 70,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 70,
      align: 'center',
      dictType: 'sys_search_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      width: 130,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑分类'),
        onClick: handleForm.bind(this, { categoryCode: record.categoryCode }),
        auth: 'biz:bizCategory:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用分类'),
        popConfirm: {
          title: t('是否确认停用分类'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'biz:bizCategory:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用分类'),
        popConfirm: {
          title: t('是否确认启用分类'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'biz:bizCategory:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除分类'),
        popConfirm: {
          title: t('是否确认删除分类'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'biz:bizCategory:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级分类'),
        onClick: handleForm.bind(this, {
          parentCode: record.id,
          parentName: record.categoryName,
        }),
        auth: 'biz:bizCategory:edit',
      },
    ],
  };

  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse, getForm }] = useTable({
    api: bizCategoryListData,
    beforeFetch: (params) => {
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
    async () => {
      await getForm().setFieldsValue({
        categoryCode: props.treeCode,
      });
      reload();
    },
  );

  function fetchSuccess() {
    if (props.treeCode) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openModal(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await bizCategoryDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await bizCategoryEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await bizCategoryDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

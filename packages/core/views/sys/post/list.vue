<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:post:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ postCode: record.postCode })">
          {{ record.postName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysPostList">
  import { unref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { postDelete, postListData } from '@jeesite/core/api/sys/post';
  import { postDisable, postEnable } from '@jeesite/core/api/sys/post';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.post');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('岗位管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('岗位名称'),
        field: 'postName',
        component: 'Input',
      },
      {
        label: t('岗位代码'),
        field: 'viewCode',
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
        label: t('岗位分类'),
        field: 'postType',
        component: 'Select',
        componentProps: {
          dictType: 'sys_post_type',
          allowClear: true,
        },
      },
    ],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('岗位名称'),
      dataIndex: 'postName',
      key: 'a.post_name',
      sorter: true,
      width: 130,
      align: 'center',
      slot: 'firstColumn',
    },
    {
      title: t('岗位代码'),
      dataIndex: 'viewCode',
      key: 'a.view_code',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('岗位分类'),
      dataIndex: 'postType',
      key: 'a.post_type',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_post_type',
    },
    {
      title: t('排序'),
      dataIndex: 'postSort',
      key: 'a.post_sort',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 100,
      align: 'center',
      dictType: 'sys_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      key: 'a.remarks',
      sorter: true,
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑岗位'),
        onClick: handleForm.bind(this, { postCode: record.postCode }),
        auth: 'sys:post:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用岗位'),
        popConfirm: {
          title: t('是否确认停用岗位'),
          confirm: handleDisable.bind(this, { postCode: record.postCode }),
        },
        auth: 'sys:role:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用岗位'),
        popConfirm: {
          title: t('是否确认启用岗位'),
          confirm: handleEnable.bind(this, { postCode: record.postCode }),
        },
        auth: 'sys:role:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除岗位'),
        popConfirm: {
          title: t('是否确认删除岗位'),
          confirm: handleDelete.bind(this, { postCode: record.postCode }),
        },
        auth: 'sys:post:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: postListData,
    beforeFetch: (params) => {
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
  });

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const res = await postDisable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleEnable(record: Recordable) {
    const res = await postEnable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleDelete(record: Recordable) {
    const res = await postDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper
    class="jeesite-state-cache"
    :sidebarWidth="310"
    :sidebarWidthRight="300"
    :contentFullHeight="true"
    :contentMinHeight="400"
  >
    <template #sidebar>
      <BasicTable @register="registerTable" @row-click="handleRowClick">
        <template #tableTitle>
          <Icon icon="icon-social-dribbble" class="m-1 pr-1" />
          <span> {{ t('缓存列表') }} </span>
        </template>
      </BasicTable>
    </template>
    <PageWrapper :sidebarWidth="350" :sidebarWidthRight="300" :contentFullHeight="true" :contentMinHeight="400">
      <template #sidebar>
        <BasicTable @register="registerTable2" @row-click="handleRowClick2">
          <template #tableTitle>
            <Icon icon="icon-menu" class="m-1 pr-1" />
            <span> {{ t('缓存列表') }} </span>
          </template>
        </BasicTable>
      </template>
      <div class="jeesite-basic-form-box">
        <div class="form-title">
          <Icon icon="icon-notebook" class="m-1 pr-1" />
          <span> {{ t('缓存内容') }} </span>
          <div class="float-right p-1 pr-2">
            <a-button size="small" @click="handleClearAll">
              <Icon icon="icon-refresh" class="m-1 pr-1" /> {{ t('清理全部缓存') }}
            </a-button>
          </div>
        </div>
        <BasicForm class="form-content" @register="registerForm" />
      </div>
    </PageWrapper>
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsStateCacheIndex">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Icon } from '@jeesite/core/components/Icon';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { BasicTable, useTable } from '@jeesite/core/components/Table';
  import {
    stateCacheNameList,
    stateCacheKeyList,
    stateCacheValue,
    stateCacheClear,
    stateCacheClearAll,
  } from '@jeesite/core/api/state/cache';
  import { BasicForm, useForm } from '@jeesite/core/components/Form';
  import { useWindowSizeFn } from '@jeesite/core/hooks/event/useWindowSizeFn';

  const { t } = useI18n('state.cache');
  const { showMessage } = useMessage();
  const textAreaHeight = ref<number>(300);
  const cacheName = ref<string>('');

  const [registerTable, tableAction] = useTable({
    api: stateCacheNameList,
    beforeFetch: (params) => {
      return params;
    },
    columns: [
      {
        title: t('缓存名称'),
        dataIndex: 'cacheName',
        key: 'a.cache_name',
        sorter: false,
        width: 150,
        align: 'left',
      },
    ],
    indexColumnProps: {
      width: 50,
    },
    actionColumn: {
      width: 50,
      align: 'center',
      actions: (record: Recordable) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          title: t('删除缓存'),
          popConfirm: {
            title: t('是否确认删除缓存'),
            confirm: handleDelete.bind(this, { cacheName: record.cacheName }),
          },
          auth: 'sys:config:edit',
        },
      ],
    },
    minHeight: 300,
    rowKey: 'cacheName',
    clickToRowSelect: true,
    rowSelection: { type: 'radio', columnWidth: 0 },
    showTableSetting: true,
    tableSetting: {
      redo: true,
      size: false,
      setting: false,
    },
    useSearchForm: false,
    pagination: false,
    canResize: true,
    size: 'small',
  });

  const [registerTable2, tableAction2] = useTable({
    api: stateCacheKeyList,
    beforeFetch: (params) => {
      params.cacheName = cacheName.value;
      return params;
    },
    columns: [
      {
        title: t('缓存键名'),
        dataIndex: 'key',
        key: 'a.key',
        sorter: false,
        width: 190,
        align: 'left',
      },
    ],
    indexColumnProps: {
      width: 50,
    },
    actionColumn: {
      width: 50,
      align: 'center',
      actions: (record: Recordable) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          title: t('删除缓存键值'),
          popConfirm: {
            title: t('是否确认删除缓存键值'),
            confirm: handleDelete.bind(this, { cacheName: record.cacheName, key: record.key }),
          },
          auth: 'sys:config:edit',
        },
      ],
    },
    minHeight: 300,
    rowKey: 'key',
    clickToRowSelect: true,
    rowSelection: { type: 'radio', columnWidth: 0 },
    showTableSetting: true,
    tableSetting: {
      redo: true,
      size: false,
      setting: false,
    },
    useSearchForm: false,
    pagination: false,
    canResize: true,
    size: 'small',
  });

  const [registerForm, { resetFields, setFieldsValue }] = useForm({
    labelWidth: 120,
    schemas: [
      {
        label: t('缓存名称'),
        field: 'cacheName',
        component: 'Input',
        componentProps: {
          maxlength: 100,
        },
      },
      {
        label: t('缓存键名'),
        field: 'key',
        component: 'Input',
        componentProps: {
          maxlength: 100,
        },
      },
      {
        label: t('缓存内容'),
        field: 'value',
        component: 'InputTextArea',
        componentProps: () => {
          return {
            style: { height: `${textAreaHeight.value}px` },
            rows: 12,
          };
        },
      },
    ],
    labelCol: { span: 24, style: 'padding: 0' },
    wrapperCol: { span: 24 },
  });

  function caleTextAreaHeight() {
    const box = document.querySelector('.jeesite-basic-form-box') as HTMLElement;
    textAreaHeight.value = box.offsetHeight - 240;
  }

  useWindowSizeFn(caleTextAreaHeight, 500, { immediate: true });

  function handleRowClick(row: Recordable) {
    cacheName.value = row.cacheName;
    tableAction2.reload();
  }

  async function handleRowClick2(row: Recordable) {
    cacheName.value = row.cacheName;
    const data = await stateCacheValue({ cacheName: cacheName.value, key: row.key });
    await setFieldsValue(data);
  }

  async function handleDelete(record: Recordable) {
    const params = record.key ? { cacheName: record.cacheName, key: record.key } : { cacheName: record.cacheName };
    await stateCacheClear(params);
    if (params.key) {
      await tableAction2.reload();
    } else {
      await tableAction.reload();
    }
  }

  async function handleClearAll() {
    const data = await stateCacheClearAll();
    showMessage(data.message);
    cacheName.value = '';
    await tableAction.reload();
    await tableAction2.reload();
    await resetFields();
  }
</script>
<style lang="less">
  .jeesite-state-cache {
    .ant-table-tbody > tr {
      cursor: pointer;
    }

    .jeesite-basic-form-box {
      background: white;
      border-radius: 5px;
      width: 100%;
      height: 100%;

      .form-title {
        font-size: 16px;
        min-height: 44px;
        padding: 5px 2px 5px 7px;

        &::after {
          content: ' ';
          display: table;
          clear: both;
        }

        > .anticon {
          color: @primary-color;
        }
      }

      .form-content {
        padding: 0 25px;

        .ant-form-item {
          margin-bottom: 5px;
        }
      }
    }
  }

  html[data-theme='dark'] {
    .jeesite-state-cache {
      .jeesite-basic-form-box {
        background: #141414;
      }
    }
  }
</style>

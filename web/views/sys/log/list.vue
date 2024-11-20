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
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })">
          {{ record.logTitle }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysLogList">
  import { unref, h } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  // import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { logListData } from '@jeesite/web/api/sys/log';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.log');
  // const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('访问日志'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('日志类型'),
        field: 'logType',
        component: 'Select',
        componentProps: {
          dictType: 'sys_log_type',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
      {
        label: t('日志标题'),
        field: 'logTitle',
        component: 'Input',
      },
      {
        label: t('请求地址'),
        field: 'requestUri',
        component: 'Input',
      },
      {
        label: t('是否异常'),
        field: 'isException',
        component: 'Select',
        componentProps: {
          dictType: 'sys_yes_no',
          allowClear: true,
        },
      },
      {
        label: t('操作用户'),
        field: 'createBy',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
          params: { isLoadUser: true, userIdPrefix: '' },
          canSelectParent: false,
          allowClear: true,
        },
      },
      {
        label: t('业务类型'),
        field: 'bizType',
        component: 'Input',
      },
      {
        label: t('业务主键'),
        field: 'bizKey',
        component: 'Input',
      },
      {
        label: t('操作时间'),
        field: 'dateRange',
        component: 'RangePicker',
        componentProps: {},
      },
    ],
    fieldMapToTime: [['dateRange', ['createDate_gte', 'createDate_lte']]],
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('日志标题'),
      dataIndex: 'logTitle',
      key: 'a.log_title',
      sorter: true,
      width: 180,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('请求地址'),
      dataIndex: 'requestUri',
      key: 'a.request_uri',
      sorter: true,
      width: 150,
      align: 'left',
      customRender: ({ record }) => {
        const tit = '[' + record.requestMethod + '] ' + record.serverAddr + record.requestUri;
        return h('span', { title: tit }, record.requestUri);
      },
    },
    {
      title: t('日志类型'),
      dataIndex: 'logType',
      key: 'a.log_type',
      sorter: true,
      width: 100,
      align: 'center',
      dictType: 'sys_log_type',
    },
    {
      title: t('操作用户'),
      dataIndex: 'createByName',
      key: 'a.create_by_name',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('业务类型'),
      dataIndex: 'bizType',
      key: 'a.biz_type',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('业务主键'),
      dataIndex: 'bizKey',
      key: 'a.biz_key',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('客户端IP'),
      dataIndex: 'remoteAddr',
      key: 'a.remote_addr',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('操作时间'),
      dataIndex: 'createDate',
      key: 'a.create_date',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('设备名称'),
      dataIndex: 'deviceName',
      key: 'a.device_name',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('浏览器名'),
      dataIndex: 'browserName',
      key: 'a.browser_name',
      sorter: true,
      width: 100,
      align: 'center',
    },
    {
      title: t('响应时间'),
      dataIndex: 'executeTimeFormat',
      key: 'a.execute_time',
      sorter: true,
      width: 100,
      align: 'center',
    },
  ];

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload /*, getForm*/ }] = useTable({
    api: logListData,
    beforeFetch: (params) => {
      // 查询前增加默认条件（例子）
      // params.dateRange = ['2022-05-30', '2022-05-31'];
      // getForm().setFieldsValue(params);
      return params;
    },
    columns: tableColumns,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
  });

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  function handleSuccess() {
    reload();
  }
</script>

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
  import { useI18n } from '/@/hooks/web/useI18n';
  // import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { logListData } from '/@/api/sys/log';
  import { officeTreeData } from '/@/api/sys/office';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('sys.log');
  // const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('访问日志'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
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
  const [registerTable, { reload }] = useTable({
    api: logListData,
    beforeFetch: (params) => {
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

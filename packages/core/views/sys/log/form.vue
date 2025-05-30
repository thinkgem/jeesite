<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer v-bind="$attrs" :showFooter="true" :showOkBtn="false" @register="registerDrawer" width="80%">
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysLogForm">
  import { ref, unref, computed, h } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  // import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Log, logForm } from '@jeesite/core/api/sys/log';
  import { isEmpty } from '@jeesite/core/utils/is';

  const { t } = useI18n('sys.log');
  // const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Log>({} as Log);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增日志') : t('日志详情'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('日志标题'),
      field: 'logTitle',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      required: true,
    },
    {
      label: t('日志类型'),
      field: 'logType',
      component: 'Select',
      componentProps: {
        dictType: 'sys_log_type',
        allowClear: true,
      },
      required: true,
    },
    {
      label: t('请求地址'),
      field: 'requestUriFormat',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      required: true,
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('请求数据'),
      field: 'requestParams',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('操作用户'),
      field: 'createByName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('操作账号'),
      field: 'createBy',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('业务类型'),
      field: 'bizType',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
    },
    {
      label: t('业务主键'),
      field: 'bizKey',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
    },
    {
      label: t('操作时间'),
      field: 'createDate',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
    },
    {
      label: t('客户端IP'),
      field: 'remoteAddr',
      component: 'Input',
      componentProps: {
        maxlength: 255,
      },
    },
    {
      label: t('用户代理'),
      field: 'userAgent',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('设备名称'),
      field: 'deviceName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('浏览器名'),
      field: 'browserName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('响应时间'),
      field: 'executeTime',
      component: 'Input',
      componentProps: {
        maxlength: 19,
        // style: 'width: calc(50% - 60px)',
      },
      // colProps: { md: 24, lg: 24 },
    },
    {
      label: t('差异修改数据'),
      field: 'diffModifyDataTitle',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => !isEmpty(record.value.diffModifyData),
    },
    {
      field: 'diffModifyData',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      render: () => {
        return h('div', { class: 'modify-data', innerHTML: record.value.diffModifyData });
      },
      ifShow: () => !isEmpty(record.value.diffModifyData),
    },
    {
      label: t('异常信息'),
      field: 'exceptionInfoTitle',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => record.value.isException == '1',
    },
    {
      field: 'exceptionInfo',
      component: 'InputTextArea',
      componentProps: {
        rows: 10,
      },
      colProps: { md: 24, lg: 24 },
      ifShow: () => record.value.isException == '1',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await logForm(data);
    record.value = (res.log || {}) as Log;
    record.value.__t = new Date().getTime();
    record.value.requestUriFormat =
      '[' + record.value.requestMethod + '] ' + record.value.serverAddr + record.value.requestUri;
    setFieldsValue(record.value);
    setDrawerProps({ loading: false });
  });
</script>
<style lang="less">
  .modify-data {
    .table {
      td {
        width: 30%;
        word-break: break-all;
      }

      th:first-child,
      td:first-child {
        width: 20%;
        text-align: center;
      }
    }

    > .table {
      > tbody > tr {
        > th:first-child,
        > td:first-child {
          width: 7%;
        }
      }
    }
  }
</style>

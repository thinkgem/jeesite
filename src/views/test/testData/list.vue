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
        <a-button type="primary" @click="handleForm({})" v-auth="'test:testData:edit'">
          <Icon icon="fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })">
          {{ record.testInput }}
        </a>
      </template>
      <template #customFilterIcon="filter">
        <Icon
          icon="ant-design:search-outlined"
          :style="{ color: filter.filtered ? '#108ee9' : undefined }"
        />
      </template>
      <template #customFilterDropdown="filter">
        <div class="p-2" v-if="filter.column.dataIndex == 'testInput'">
          <a-input
            ref="searchInput"
            :placeholder="`${t('搜索')}${filter.column.customTitle}`"
            :value="filter.selectedKeys[0]"
            style="width: 168px; margin-bottom: 8px; display: block"
            @change="(e: any) => filter.setSelectedKeys(e.target.value ? [e.target.value] : [])"
          />
          <a-button type="primary" size="small" class="w-20 mr-2" @click="filter.confirm()">
            {{ t('确定') }}
          </a-button>
          <a-button
            size="small"
            class="w-20"
            @click="
              filter.clearFilters();
              filter.confirm();
            "
          >
            {{ t('重置') }}
          </a-button>
        </div>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
    <InputFormTabs @register="registerDrawer2" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsTestTestDataList">
  import { unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { testDataDelete, testDataListData } from '/@/api/test/testData';
  import { testDataDisable, testDataEnable } from '/@/api/test/testData';
  import { officeTreeData } from '/@/api/sys/office';
  import { areaTreeData } from '/@/api/sys/area';
  import { useDrawer } from '/@/components/Drawer';
  import { FormProps } from '/@/components/Form';
  import InputForm from './form.vue';
  import InputFormTabs from './formTabs.vue';

  const { t } = useI18n('test.testData');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('数据管理'),
  };

  const searchForm: FormProps = {
    baseColProps: { lg: 6, md: 8 },
    labelWidth: 90,
    schemas: [
      {
        label: t('单行文本'),
        field: 'testInput',
        component: 'Input',
      },
      {
        label: t('多行文本'),
        field: 'testTextarea',
        component: 'Input',
      },
      {
        label: t('下拉框'),
        field: 'testSelect',
        component: 'Select',
        componentProps: {
          dictType: 'sys_menu_type',
          allowClear: true,
        },
      },
      {
        label: t('下拉多选'),
        field: 'testSelectMultiple',
        component: 'Select',
        componentProps: {
          dictType: 'sys_menu_type',
          allowClear: true,
          mode: 'multiple',
        },
      },
      {
        label: t('单选框'),
        field: 'testRadio',
        component: 'RadioGroup',
        componentProps: {
          dictType: 'sys_menu_type',
        },
      },
      {
        label: t('复选框'),
        field: 'testCheckbox',
        component: 'CheckboxGroup',
        componentProps: {
          dictType: 'sys_menu_type',
        },
      },
      {
        label: t('日期选择起'),
        field: 'testDate_gte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          showTime: false,
        },
      },
      {
        label: t('日期选择止'),
        field: 'testDate_lte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          showTime: false,
        },
      },
      {
        label: t('日期时间起'),
        field: 'testDatetime_gte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD HH:mm',
          showTime: { format: 'HH:mm' },
        },
      },
      {
        label: t('日期时间止'),
        field: 'testDatetime_lte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD HH:mm',
          showTime: { format: 'HH:mm' },
        },
      },
      {
        label: t('用户选择'),
        field: 'testUser.userCode',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
          params: { isLoadUser: true, userIdPrefix: '' },
          canSelectParent: false,
          allowClear: true,
        },
      },
      {
        label: t('机构选择'),
        field: 'testOffice.officeCode',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
          allowClear: true,
        },
      },
      {
        label: t('区域选择'),
        field: 'testAreaCode',
        component: 'TreeSelect',
        componentProps: {
          api: areaTreeData,
          allowClear: true,
        },
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
      title: t('单行文本'),
      dataIndex: 'testInput',
      key: 'a.test_input',
      sorter: true,
      width: 130,
      align: 'center',
      slot: 'firstColumn',
      // filters: [
      //   { text: 'Male', value: '1' },
      //   { text: 'Female', value: '2' },
      // ],
      // filterMultiple: true,
      // onFilter: (value: string, record: Recordable) => {
      //   console.log('onFilter', value, record);
      //   return record.userName === value;
      // },
      customFilterDropdown: true,
    },
    {
      title: t('多行文本'),
      dataIndex: 'testTextarea',
      key: 'a.test_textarea',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('下拉框'),
      dataIndex: 'testSelect',
      key: 'a.test_select',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('下拉多选'),
      dataIndex: 'testSelectMultiple',
      key: 'a.test_select_multiple',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('单选框'),
      dataIndex: 'testRadio',
      key: 'a.test_radio',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('复选框'),
      dataIndex: 'testCheckbox',
      key: 'a.test_checkbox',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('日期选择'),
      dataIndex: 'testDate',
      key: 'a.test_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('日期时间'),
      dataIndex: 'testDatetime',
      key: 'a.test_datetime',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('用户选择'),
      dataIndex: 'testUser.userName',
      key: 'a.test_user_code',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('机构选择'),
      dataIndex: 'testOffice.officeName',
      key: 'a.test_office_code',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('区域选择'),
      dataIndex: 'testAreaName',
      key: 'a.test_area_code',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 130,
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
    width: 180,
    actions: (record: Recordable) => [
      {
        icon: 'clarity:timeline-line',
        title: t('页签方式编辑'),
        onClick: handleForm2.bind(this, { id: record.id }),
        auth: 'test:testData:edit',
      },
      {
        icon: 'clarity:note-edit-line',
        title: t('编辑数据'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'test:testData:edit',
      },
      {
        icon: 'ant-design:stop-outlined',
        color: 'error',
        title: t('停用数据'),
        popConfirm: {
          title: t('是否确认停用数据'),
          confirm: handleDisable.bind(this, { id: record.id }),
        },
        auth: 'test:testData:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用数据'),
        popConfirm: {
          title: t('是否确认启用数据'),
          confirm: handleEnable.bind(this, { id: record.id }),
        },
        auth: 'test:testData:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'ant-design:delete-outlined',
        color: 'error',
        title: t('删除数据'),
        popConfirm: {
          title: t('是否确认删除数据'),
          confirm: handleDelete.bind(this, { id: record.id }),
        },
        auth: 'test:testData:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerDrawer2, { openDrawer: openDrawer2 }] = useDrawer();
  const [registerTable, { reload }] = useTable({
    api: testDataListData,
    beforeFetch: (params) => {
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
    filterFn: (data: Partial<Recordable<string[]>>) => {
      const testInput = 'a.test_input';
      if (data[testInput]) {
        data['testInput'] = data[testInput]?.join(',') as any;
        delete data[testInput];
      }
      console.log(data);
      return data;
    },
  });

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  function handleForm2(record: Recordable) {
    openDrawer2(true, record);
  }

  async function handleDisable(record: Recordable) {
    const res = await testDataDisable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleEnable(record: Recordable) {
    const res = await testDataEnable(record);
    showMessage(res.message);
    handleSuccess();
  }

  async function handleDelete(record: Recordable) {
    const res = await testDataDelete(record);
    showMessage(res.message);
    handleSuccess();
  }

  function handleSuccess() {
    reload();
  }
</script>

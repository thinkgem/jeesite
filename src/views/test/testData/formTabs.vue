<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'test:testData:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <Tabs v-model:activeKey="activeKey" tabPosition="left">
      <Tabs.TabPane key="1" :forceRender="true" tab="基本信息">
        <BasicForm @register="registerForm1">
          <template #remarks="{ model, field }">
            <WangEditor
              v-model:value="model[field]"
              :bizKey="record.id"
              :bizType="'testDataChild_' + field"
              :height="300"
            />
          </template>
        </BasicForm>
      </Tabs.TabPane>
      <Tabs.TabPane key="2" :forceRender="true" tab="详细信息">
        <BasicForm @register="registerForm2">
          <template #testDataChildList>
            <BasicTable
              @register="registerTestDataChildTable"
              @row-click="handleTestDataChildRowClick"
            >
              <template #testDataChildUpload="{ record: childRecord }">
                <BasicUpload
                  v-model:value="childRecord.dataMap"
                  :bizKey="childRecord.id"
                  :bizType="'testDataChild_file'"
                  :uploadType="'all'"
                  :loadTime="record.__t"
                  :size="'small'"
                />
              </template>
            </BasicTable>
            <a-button class="mt-2" @click="handleTestDataChildAdd" v-auth="'test:testData:edit'">
              <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
            </a-button>
          </template>
        </BasicForm>
      </Tabs.TabPane>
    </Tabs>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsTestTestDataForm">
  import { ref, unref, computed, h } from 'vue';
  import { Tabs } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicTable, useTable } from '/@/components/Table';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { TestData, testDataSave, testDataForm } from '/@/api/test/testData';
  import { officeTreeData } from '/@/api/sys/office';
  import { areaTreeData } from '/@/api/sys/area';
  import { BasicUpload } from '/@/components/Upload';
  import { WangEditor } from '/@/components/WangEditor';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('test.testData');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<TestData>({} as TestData);

  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增数据') : t('编辑数据'),
  }));

  const activeKey = ref<string>('1');

  const inputFormSchemas1: FormSchema[] = [
    {
      label: t('单行文本'),
      field: 'testInput',
      component: 'Input',
      componentProps: {
        maxlength: 200,
      },
      required: true,
    },
    {
      field: 'testInputHelpMessage',
      component: 'Text',
      render: () => h('div', { class: 'ml-4 text-gray-500' }, '这是栅格帮助信息，优点是可以对齐。'),
    },
    {
      label: t('多行文本'),
      field: 'testTextarea',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 200,
      },
      suffix: h('div', { class: 'ml-4 text-gray-500' }, 'Suffix帮助信息，优点是自由嵌入组件后。'),
      rules: [{ required: true }],
      colProps: { md: 24, lg: 24 },
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
      label: t('日期选择'),
      field: 'testDate',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD',
        showTime: false,
      },
    },
    {
      label: t('日期时间'),
      field: 'testDatetime',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
      },
    },
    {
      label: t('用户选择'),
      field: 'testUser.userCode',
      fieldLabel: 'testUser.userName',
      component: 'TreeSelect',
      componentProps: {
        api: officeTreeData,
        params: { isLoadUser: true, userIdPrefix: '' },
        canSelectParent: false,
        allowClear: true,
      },
    },
    {
      label: t('用户列表选择'),
      field: 'testUser.userCode',
      component: 'ListSelect',
      componentProps: {
        selectType: 'empUserSelect',
      },
    },
    {
      label: t('机构选择'),
      field: 'testOffice.officeCode',
      fieldLabel: 'testOffice.officeName',
      component: 'TreeSelect',
      componentProps: {
        api: officeTreeData,
        allowClear: true,
      },
    },
    {
      label: t('区域选择'),
      field: 'testAreaCode',
      fieldLabel: 'testAreaName',
      component: 'TreeSelect',
      componentProps: {
        api: areaTreeData,
        allowClear: true,
      },
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      slot: 'remarks',
      colProps: { md: 24, lg: 24 },
    },
  ];
  const inputFormSchemas2: FormSchema[] = [
    {
      label: t('图片上传'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'testData_image',
        uploadType: 'image',
      },
      suffix: [
        h('div', { class: 'ml-4 text-gray-500' }, '请上传图片格式，文件小于 5 M。'),
        h('a', { href: 'https://jeesite.com', target: '_blank', class: 'mr-8' }, '查看模板'),
      ],
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('文件上传'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'testData_file',
        uploadType: 'all',
      },
      suffix: [
        h('div', { class: 'ml-4 text-gray-500' }, '请上传文档格式，文件小于 5 M。'),
        h('a', { href: 'https://jeesite.com', target: '_blank', class: 'mr-8' }, '查看模板'),
      ],
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('子表数据'),
      field: 'testDataChildList',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'testDataChildList',
    },
  ];

  const [registerForm1, formAction1] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas1,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerForm2, formAction2] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas2,
    baseColProps: { md: 24, lg: 12 },
  });

  async function resetFields() {
    activeKey.value = '1';
    await formAction1.resetFields();
    await formAction2.resetFields();
  }

  async function setFieldsValue(values: Recordable) {
    await formAction1.setFieldsValue(values);
    await formAction2.setFieldsValue(values);
  }

  async function validate(): Promise<Recordable<any>> {
    return Object.assign(await formAction1.validate(), await formAction2.validate());
  }

  const [registerTestDataChildTable, testDataChildTable] = useTable({
    actionColumn: {
      width: 60,
      actions: (record: Recordable) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: '是否确认删除',
            confirm: handleTestDataChildDelete.bind(this, record),
          },
          auth: 'test:testData:edit',
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
    scroll: { x: 1000 },
  });

  async function setTestDataChildTableData(_res: Recordable) {
    testDataChildTable.setColumns([
      {
        title: t('单行文本'),
        dataIndex: 'testInput',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'Input',
        editRule: true,
      },
      {
        title: t('多行文本'),
        dataIndex: 'testTextarea',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'Input',
        editRule: false,
      },
      {
        title: t('下拉框'),
        dataIndex: 'testSelect',
        width: 130,
        align: 'left',
        dictType: 'sys_menu_type',
        editRow: true,
        editComponent: 'Select',
        editComponentProps: {
          dictType: 'sys_menu_type',
          allowClear: true,
        },
        editRule: false,
      },
      {
        title: t('下拉多选'),
        dataIndex: 'testSelectMultiple',
        width: 130,
        align: 'left',
        dictType: 'sys_menu_type',
        editRow: true,
        editComponent: 'Select',
        editComponentProps: {
          dictType: 'sys_menu_type',
          mode: 'multiple',
          allowClear: true,
        },
        editRule: false,
      },
      {
        title: t('日期选择'),
        dataIndex: 'testDate',
        width: 130,
        align: 'center',
        editRow: true,
        editComponent: 'DatePicker',
        editComponentProps: {
          format: 'YYYY-MM-DD',
          showTime: false,
        },
        editRule: false,
      },
      {
        title: t('日期时间'),
        dataIndex: 'testDatetime',
        width: 215,
        align: 'center',
        editRow: true,
        editComponent: 'DatePicker',
        editComponentProps: {
          format: 'YYYY-MM-DD HH:mm',
          showTime: { format: 'HH:mm' },
        },
        editRule: false,
      },
      {
        title: t('用户选择'),
        dataIndex: 'testUser.userCode',
        dataLabel: 'testUser.userName',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'TreeSelect',
        editComponentProps: {
          api: officeTreeData,
          params: { isLoadUser: true, userIdPrefix: '' },
          canSelectParent: false,
          allowClear: true,
        },
        editRule: false,
      },
      {
        title: t('机构选择'),
        dataIndex: 'testOffice.officeCode',
        dataLabel: 'testOffice.officeName',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'TreeSelect',
        editComponentProps: {
          api: officeTreeData,
          canSelectParent: false,
          allowClear: true,
        },
        editRule: false,
      },
      {
        title: t('区域选择'),
        dataIndex: 'testAreaCode',
        dataLabel: 'testAreaName',
        width: 130,
        align: 'left',
        editRow: true,
        editComponent: 'TreeSelect',
        editComponentProps: {
          api: areaTreeData,
          canSelectParent: false,
          allowClear: true,
        },
        editRule: false,
      },
      {
        title: t('文件上传'),
        dataIndex: 'upload',
        width: 160,
        align: 'left',
        slot: 'testDataChildUpload',
      },
    ]);
    testDataChildTable.setTableData(record.value.testDataChildList || []);
  }

  function handleTestDataChildRowClick(record: Recordable) {
    record.onEdit?.(true, false);
  }

  function handleTestDataChildAdd() {
    testDataChildTable.insertTableDataRecord({
      id: new Date().getTime(),
      isNewRecord: true,
      editable: true,
    });
  }

  function handleTestDataChildDelete(record: Recordable) {
    testDataChildTable.deleteTableDataRecord(record);
  }

  async function getTestDataChildList() {
    let testDataChildListValid = true;
    let testDataChildList: Recordable[] = [];
    for (const record of testDataChildTable.getDataSource()) {
      if (!(await record.onEdit?.(false, true))) {
        testDataChildListValid = false;
      }
      testDataChildList.push({
        ...record,
        id: !!record.isNewRecord ? '' : record.id,
      });
    }
    for (const record of testDataChildTable.getDelDataSource()) {
      if (!!record.isNewRecord) continue;
      testDataChildList.push({
        ...record,
        status: '1',
      });
    }
    if (!testDataChildListValid) {
      throw { errorFields: [{ name: ['testDataChildList'] }] };
    }
    return testDataChildList;
  }

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await testDataForm(data);
    record.value = (res.testData || {}) as TestData;
    record.value.__t = new Date().getTime();
    setFieldsValue(record.value);
    setTestDataChildTableData(res);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id,
      };
      data.testDataChildList = await getTestDataChildList();
      // console.log('submit', params, data, record);
      const res = await testDataSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>

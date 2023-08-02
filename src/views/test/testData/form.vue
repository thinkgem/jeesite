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
      <Icon :icon="getTitle.icon" class="pr-1 m-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #remarks="{ model, field }">
        <WangEditor
          v-model:value="model[field]"
          :bizKey="record.id"
          :bizType="'testDataChild_' + field"
          :height="300"
        />
      </template>
      <template #testDataChildList>
        <BasicTable @register="registerTestDataChildTable" @row-click="handleTestDataChildRowClick">
          <template #testDataChildUpload="{ record: childRecord }">
            <BasicUpload
              v-model:value="childRecord.dataMap"
              :bizKey="childRecord.id"
              :bizType="'testDataChild_file'"
              :uploadType="'all'"
              :loadTime="record.__t"
            />
          </template>
        </BasicTable>
        <a-button class="mt-2" @click="handleTestDataChildAdd" v-auth="'test:testData:edit'">
          <Icon icon="ant-design:plus-circle-outlined" /> {{ t('新增') }}
        </a-button>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsTestTestDataForm">
  import { ref, unref, computed } from 'vue';
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

  const inputFormSchemas: FormSchema[] = [
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
      label: t('多行文本'),
      field: 'testTextarea',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 200,
      },
      rules: [{ required: true }],
      colProps: { lg: 24, md: 24 },
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
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('图片上传'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'testData_image',
        uploadType: 'image',
        // imageMaxWidth: 1024,
        // imageMaxHeight: 768,
        // imageThumbName: '150x150.jpg',
      },
      colProps: { lg: 24, md: 24 },
      // 文件上传的必填验证实例
      // rules: [
      //   { required: true },
      //   {
      //     validator(_rule, value) {
      //       return new Promise((resolve, reject) => {
      //         const len = !value || value['testData_image__len'] || 0;
      //         if (len == 0) reject(t('请上传图片'));
      //         else resolve();
      //       });
      //     },
      //   },
      // ],
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
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('子表数据'),
      field: 'testDataChildList',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'testDataChildList',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerTestDataChildTable, testDataChildTable] = useTable({
    actionColumn: {
      width: 60,
      actions: (record: Recordable) => [
        {
          icon: 'ant-design:delete-outlined',
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

<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <CollapseForm
    :config="formConfig"
    :loading="loadingRef"
    :okLoading="okLoadingRef"
    :okAuth="'test:testData:edit'"
    @close="handleClose"
    @ok="handleSubmit"
  >
    <template #form1>
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
    </template>
    <template #form2>
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
              />
            </template>
          </BasicTable>
          <a-button class="mt-2" @click="handleTestDataChildAdd" v-auth="'test:testData:edit'">
            <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
          </a-button>
        </template>
      </BasicForm>
    </template>
  </CollapseForm>
</template>
<script lang="ts" setup name="ViewsTestTestDataFormRoute">
  import { ref, unref, computed, onMounted } from 'vue';
  import { useEmitter } from '/@/store/modules/user';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { CollapseForm } from '/@/components/CollapseForm';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicTable, useTable } from '/@/components/Table';
  import { TestData, testDataSave, testDataForm } from '/@/api/test/testData';
  import { officeTreeData } from '/@/api/sys/office';
  import { areaTreeData } from '/@/api/sys/area';
  import { BasicUpload } from '/@/components/Upload';
  import { WangEditor } from '/@/components/WangEditor';
  import { useQuery } from '/@/hooks/web/usePage';
  import { useTabs } from '/@/hooks/web/useTabs';

  const formConfig = ref<any[]>([
    {
      label: '基础表单',
      value: 'form1',
      open: true,
    },
    {
      label: '子表列表',
      value: 'form2',
      open: true,
    },
  ]);

  const emitter = useEmitter();

  const { t } = useI18n('test.testData');
  const { showMessage } = useMessage();
  const { setTitle, close } = useTabs(router);
  const record = ref<TestData>({} as TestData);
  const loadingRef = ref<boolean>(false);
  const okLoadingRef = ref<boolean>(false);
  const query = useQuery();

  const updateTabTitle = () => {
    setTitle(record.value.isNewRecord ? t('新增数据') : t('编辑数据'));
  };

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
      label: t('列表选择'),
      field: 'testInput2',
      fieldLabel: 'testTextarea',
      component: 'ListSelect',
      componentProps: {
        configFile: import('./select'),
        checkbox: true,
      },
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
        checkbox: true,
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
  ];

  const [registerForm1, formAction1] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas1,
    baseColProps: { lg: 12, md: 24 },
  });

  const inputFormSchemas2: FormSchema[] = [
    {
      field: 'testDataChildList',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'testDataChildList',
    },
  ];

  const [registerForm2, formAction2] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas2,
    baseColProps: { lg: 12, md: 24 },
  });

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

  async function resetFields() {
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

  onMounted(async () => {
    loadingRef.value = true;
    await resetFields();
    const res = await testDataForm(unref(query));
    record.value = (res.testData || {}) as TestData;
    record.value.__t = new Date().getTime();
    setFieldsValue(record.value);
    setTestDataChildTableData(res);
    updateTabTitle();
    loadingRef.value = false;
  });

  function handleClose() {
    setTimeout(close);
  }

  async function handleSubmit() {
    try {
      okLoadingRef.value = true;
      const data = await validate();
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id,
      };
      data.testDataChildList = await getTestDataChildList();
      // console.log('submit', params, data, record);
      const res = await testDataSave(params, data);
      showMessage(res.message);
      emitter.emit('test-testData-reload');
      handleClose();
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      okLoadingRef.value = false;
    }
  }
</script>

import { useI18n } from '/@/hooks/web/useI18n';
import { BasicColumn, BasicTableProps, FormProps } from '/@/components/Table';
import { testDataListData } from '/@/api/test/testData';

const { t } = useI18n('sys.testData');

const modalProps = {
  title: t('测试数据选择'),
};

const searchForm: FormProps = {
  baseColProps: { md: 8, lg: 6 },
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
      component: 'Input',
    },
    {
      label: t('下拉多选'),
      field: 'testSelectMultiple',
      component: 'Input',
    },
    {
      label: t('单选框'),
      field: 'testRadio',
      component: 'Input',
    },
    {
      label: t('复选框'),
      field: 'testCheckbox',
      component: 'Input',
    },
    {
      label: t('日期选择'),
      field: 'testDate',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
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
      field: 'testUserCode',
      component: 'Input',
    },
    {
      label: t('机构选择'),
      field: 'testOfficeCode',
      component: 'Input',
    },
    {
      label: t('区域选择'),
      field: 'testAreaCode',
      component: 'Input',
    },
    {
      label: t('区域名称'),
      field: 'testAreaName',
      component: 'Input',
    },
    {
      label: t('状态'),
      field: 'status',
      component: 'Select',
      componentProps: {
        dictType: 'sys_search_status',
        allowClear: true,
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
    width: 230,
    align: 'left',
    slot: 'firstColumn',
  },
  // {
  //   title: t('多行文本'),
  //   dataIndex: 'testTextarea',
  //   key: 'a.test_textarea',
  //   sorter: true,
  //   width: 130,
  //   align: 'left',
  // },
  {
    title: t('下拉框'),
    dataIndex: 'testSelect',
    key: 'a.test_select',
    sorter: true,
    width: 130,
    align: 'left',
  },
  {
    title: t('下拉多选'),
    dataIndex: 'testSelectMultiple',
    key: 'a.test_select_multiple',
    sorter: true,
    width: 130,
    align: 'left',
  },
  {
    title: t('单选框'),
    dataIndex: 'testRadio',
    key: 'a.test_radio',
    sorter: true,
    width: 130,
    align: 'left',
  },
  {
    title: t('复选框'),
    dataIndex: 'testCheckbox',
    key: 'a.test_checkbox',
    sorter: true,
    width: 130,
    align: 'left',
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
    dataIndex: 'testUserCode',
    key: 'a.test_user_code',
    sorter: true,
    width: 130,
    align: 'left',
  },
  {
    title: t('机构选择'),
    dataIndex: 'testOfficeCode',
    key: 'a.test_office_code',
    sorter: true,
    width: 130,
    align: 'left',
  },
  {
    title: t('区域选择'),
    dataIndex: 'testAreaCode',
    key: 'a.test_area_code',
    sorter: true,
    width: 130,
    align: 'left',
  },
  {
    title: t('区域名称'),
    dataIndex: 'testAreaName',
    key: 'a.test_area_name',
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
    dictType: 'sys_search_status',
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

const tableProps: BasicTableProps = {
  api: testDataListData,
  beforeFetch: (params) => {
    params['isAll'] = true;
    return params;
  },
  columns: tableColumns,
  formConfig: searchForm,
  rowKey: 'id',
};

export default {
  modalProps,
  tableProps,
  itemCode: 'id',
  itemName: 'testInput',
  isShowCode: false,
};

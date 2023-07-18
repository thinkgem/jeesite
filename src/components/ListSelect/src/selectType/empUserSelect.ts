import { useI18n } from '/@/hooks/web/useI18n';
import { BasicColumn, BasicTableProps, FormProps } from '/@/components/Table';
import { officeTreeData } from '/@/api/sys/office';
import { companyTreeData } from '/@/api/sys/company';
import { empUserListData } from '/@/api/sys/empUser';

const { t } = useI18n('sys.empUser');

const modalProps = {
  title: t('用户选择'),
};

const searchForm: FormProps = {
  baseColProps: { lg: 6, md: 8 },
  labelWidth: 60,
  schemas: [
    {
      label: t('机构'),
      field: 'employee.office.officeCode',
      component: 'TreeSelect',
      componentProps: {
        api: officeTreeData,
        allowClear: true,
      },
      show: false,
    },
    {
      label: t('账号'),
      field: 'loginCode',
      component: 'Input',
    },
    {
      label: t('昵称'),
      field: 'userName',
      component: 'Input',
    },
    {
      label: t('状态'),
      field: 'status',
      component: 'Select',
      componentProps: {
        dictType: 'sys_user_status',
        allowClear: true,
      },
    },
    {
      label: t('公司'),
      field: 'employee.company.companyCode',
      component: 'TreeSelect',
      componentProps: {
        api: companyTreeData,
        allowClear: true,
      },
    },
    {
      label: t('姓名'),
      field: 'refName',
      component: 'Input',
    },
    {
      label: t('手机'),
      field: 'mobile',
      component: 'Input',
    },
    {
      label: t('邮箱'),
      field: 'email',
      component: 'Input',
    },
    {
      label: t('电话'),
      field: 'phone',
      component: 'Input',
    },
  ],
};

const tableColumns: BasicColumn[] = [
  {
    title: t('登录账号'),
    dataIndex: 'loginCode',
    key: 'a.login_code',
    sorter: true,
    width: 100,
    slot: 'firstColumn',
  },
  {
    title: t('用户昵称'),
    dataIndex: 'userName',
    key: 'a.user_name',
    sorter: true,
    width: 100,
  },
  {
    title: t('员工姓名'),
    dataIndex: 'refName',
    key: 'a.ref_name',
    sorter: true,
    width: 100,
  },
  {
    title: t('归属机构'),
    dataIndex: 'employee.office.officeName',
    key: 'o.office_name',
    sorter: true,
    width: 100,
  },
  {
    title: t('归属公司'),
    dataIndex: 'employee.company.companyName',
    key: 'c.company_name',
    sorter: true,
    width: 100,
  },
  {
    title: t('状态'),
    dataIndex: 'status',
    key: 'a.status',
    sorter: true,
    width: 80,
    dictType: 'sys_status',
  },
  {
    title: t('更新时间'),
    dataIndex: 'updateDate',
    key: 'a.update_date',
    sorter: true,
    width: 130,
  },
  {
    title: t('电子邮箱'),
    dataIndex: 'email',
    key: 'a.email',
    sorter: true,
    width: 130,
  },
  {
    title: t('手机号码'),
    dataIndex: 'mobile',
    key: 'a.mobile',
    sorter: true,
    width: 130,
  },
  {
    title: t('办公电话'),
    dataIndex: 'phone',
    key: 'a.phone',
    sorter: true,
    width: 130,
  },
];

const tableProps: BasicTableProps = {
  api: empUserListData,
  beforeFetch: (params) => {
    params['isAll'] = true;
    return params;
  },
  columns: tableColumns,
  formConfig: searchForm,
  rowKey: 'userCode',
};

const treeProps: Recordable = {
  api: officeTreeData,
  params: { isAll: true },
  title: t('机构'),
};

const treeTableFieldName = 'employee.office.officeCode';

export default {
  modalProps,
  tableProps,
  itemCode: 'userCode',
  itemName: 'userName',
  treeProps,
  treeTableFieldName,
};

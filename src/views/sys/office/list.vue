<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="fetchSuccess">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button @click="expandAll" :title="t('展开一级')">
          <Icon icon="i-bi:chevron-double-down" /> {{ t('展开') }}
        </a-button>
        <a-button @click="collapseAll" :title="t('折叠全部')">
          <Icon icon="i-bi:chevron-double-up" /> {{ t('折叠') }}
        </a-button>
        <a-button type="default" :loading="loading" @click="handleExport()">
          <Icon icon="i-ant-design:download-outlined" /> {{ t('导出') }}
        </a-button>
        <a-button type="default" @click="handleImport()">
          <Icon icon="i-ant-design:import-outlined" /> {{ t('导入') }}
        </a-button>
        <a-button type="primary" @click="handleForm({})" v-auth="'sys:office:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <span class="cursor-pointer" @click="expandCollapse(record)"> ( {{ record.viewCode }} ) </span>
        <a @click="handleForm({ officeCode: record.officeCode })">
          {{ record.officeName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
    <FormImport @register="registerImportModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsSysOfficeList">
  import { watch, nextTick, unref, ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGlobSetting } from '/@/hooks/setting';
  import { downloadByUrl } from '/@/utils/file/download';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '/@/components/Table';
  import { officeDelete, officeListData } from '/@/api/sys/office';
  import { officeDisable, officeEnable } from '/@/api/sys/office';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { FormProps } from '/@/components/Form';
  import { isEmpty } from '/@/utils/is';
  import InputForm from './form.vue';
  import FormImport from './formImport.vue';

  const props = defineProps({
    treeCodes: Array as PropType<String[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('sys.office');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: meta.title || t('机构管理'),
  };
  const loading = ref(false);

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('机构代码'),
        field: 'viewCode_like',
        component: 'Input',
      },
      {
        label: t('机构名称'),
        field: 'officeName',
        component: 'Input',
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
        label: t('负责人'),
        field: 'leader',
        component: 'Input',
      },
      {
        label: t('机构类型'),
        field: 'officeType',
        component: 'Select',
        componentProps: {
          dictType: 'sys_office_type',
          allowClear: true,
        },
      },
      {
        label: t('机构全称'),
        field: 'fullName',
        component: 'Input',
      },
      {
        label: t('办公电话'),
        field: 'phone',
        component: 'Input',
      },
    ],
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
  };

  const tableColumns: BasicColumn[] = [
    {
      title: t('机构名称'),
      dataIndex: 'officeName',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('机构全称'),
      dataIndex: 'fullName',
      width: 130,
      align: 'left',
    },
    {
      title: t('排序号'),
      dataIndex: 'treeSort',
      width: 90,
      align: 'center',
    },
    {
      title: t('机构类型'),
      dataIndex: 'officeType',
      width: 100,
      align: 'center',
      dictType: 'sys_office_type',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 80,
      align: 'center',
      dictType: 'sys_status',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      width: 130,
      align: 'center',
    },
    {
      title: t('负责人'),
      dataIndex: 'leader',
      width: 130,
      align: 'center',
    },
    {
      title: t('办公电话'),
      dataIndex: 'phone',
      width: 130,
      align: 'center',
    },
    {
      title: t('联系地址'),
      dataIndex: 'address',
      width: 130,
      align: 'center',
    },
    {
      title: t('邮政编码'),
      dataIndex: 'zipCode',
      width: 130,
      align: 'center',
    },
    {
      title: t('电子邮箱'),
      dataIndex: 'email',
      width: 130,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑机构'),
        onClick: handleForm.bind(this, { officeCode: record.officeCode }),
        auth: 'sys:office:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用机构'),
        popConfirm: {
          title: t('是否确认停用机构'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'sys:office:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用机构'),
        popConfirm: {
          title: t('是否确认启用机构'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'sys:office:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除机构'),
        popConfirm: {
          title: t('是否确认删除机构'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'sys:office:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级机构'),
        onClick: handleForm.bind(this, {
          parentCode: record.id,
          parentName: record.officeName,
        }),
        auth: 'sys:office:edit',
      },
    ],
  };

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerTable, { reload, expandAll, collapseAll, expandCollapse, getForm }] = useTable({
    api: officeListData,
    beforeFetch: (params) => {
      params.officeCode = !isEmpty(props.treeCodes) ? props.treeCodes[0] : '';
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    isTreeTable: true,
    pagination: false,
    canResize: true,
  });

  watch(
    () => props.treeCodes,
    () => {
      if (!isEmpty(props.treeCodes)) {
        reload();
      }
    },
  );

  function fetchSuccess() {
    if (!isEmpty(props.treeCodes)) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleExport() {
    loading.value = true;
    const { ctxAdminPath } = useGlobSetting();
    await downloadByUrl({
      url: ctxAdminPath + '/sys/office/exportData',
      params: getForm().getFieldsValue(),
    });
    loading.value = false;
  }

  const [registerImportModal, { openModal: importModal }] = useModal();

  function handleImport() {
    importModal(true, {});
  }

  async function handleDisable(record: Recordable) {
    const params = { officeCode: record.officeCode };
    const res = await officeDisable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { officeCode: record.officeCode };
    const res = await officeEnable(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { officeCode: record.officeCode };
    const res = await officeDelete(params);
    showMessage(res.message);
    handleSuccess(record);
  }

  function handleSuccess(record: Recordable) {
    reload({ record });
  }
</script>

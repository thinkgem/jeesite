<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
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
        <a-button type="primary" @click="handleForm({})" v-auth="'dbm:data:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record, text, value }">
        <a @click="handleForm(record)" :title="value">
          {{ text }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsDbmDataList">
  import { onMounted, ref, unref, watch } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useQuery } from '@jeesite/core/hooks/web/usePage';
  import { BasicTable, BasicColumn, BasicTableProps, useTable } from '@jeesite/core/components/Table';
  import { FormProps } from '@jeesite/core/components/Form';
  import {
    dbmDataDelete,
    dbmDataDisable,
    dbmDataEnable,
    dbmDataList,
    dbmDataListData,
  } from '@jeesite/dbm/api/dbm/data';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { isEmpty } from '@jeesite/core/utils/is';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCodes: Array as PropType<string[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('dbm.data');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref({} as Recordable);
  const query = useQuery();

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('管理数据'),
  };

  const searchForm: FormProps = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 100,
    showAdvancedButton: false,
  };

  const actionColumn: BasicColumn = {
    width: 160,
    actions: (record: Recordable) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑数据'),
        onClick: handleForm.bind(this, record),
        auth: 'dbm:data:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用数据'),
        popConfirm: {
          title: t('是否确认停用数据'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'dbm:data:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用数据'),
        popConfirm: {
          title: t('是否确认启用数据'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'dbm:data:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除数据'),
        popConfirm: {
          title: t('是否确认删除数据'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'dbm:data:edit',
      },
    ],
  };

  const [registerTable, { reload, setProps, getForm }] = useTable({
    api: dbmDataListData,
    beforeFetch: (params) => {
      params['__entityId'] = record.value.__entityId;
      return params;
    },
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
    immediate: false,
  });

  onMounted(async () => {
    const __entityId = unref(query)['__entityId'];
    const res = await dbmDataList({ __entityId });
    record.value = (res.dbmDataEntity || {}) as Recordable;
    await getForm().setProps(res.formProps || {});
    const tableProps = (res.tableProps || {}) as BasicTableProps;
    tableProps.tableSettingStoreKey = record.value.__entityId;
    setProps(tableProps);
    await reload();
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  watch(
    () => props.treeCodes,
    async () => {
      if (!isEmpty(props.treeCodes)) {
        await getForm().setFieldsValue({
          categoryCode: props.treeCodes[0],
        });
        await reload();
      }
    },
  );

  function handleForm(data: Recordable) {
    data['__entityId'] = record.value.__entityId;
    openDrawer(true, data);
  }

  async function handleDisable(data: Recordable) {
    const res = await dbmDataDisable(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleEnable(data: Recordable) {
    const res = await dbmDataEnable(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleDelete(data: Recordable) {
    const res = await dbmDataDelete(data);
    showMessage(res.message);
    await handleSuccess(data);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }
</script>

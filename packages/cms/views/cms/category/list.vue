<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
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
        <a-button type="primary" @click="handleForm({})" v-auth="'cms:category:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <span class="cursor-pointer" @click="expandCollapse(record)"> ( {{ record.categoryCode }} ) </span>
        <a @click="handleForm({ categoryCode: record.categoryCode })" :title="record.categoryName">
          {{ record.categoryName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsCmsCategoryList">
  import { onMounted, ref, unref, watch, nextTick } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import {
    Category,
    categoryList,
    categoryRebuildIndex,
    categoryRebuildVectorStore,
  } from '@jeesite/cms/api/cms/category';
  import { categoryDelete, categoryListData } from '@jeesite/cms/api/cms/category';
  import { categoryDisable, categoryEnable } from '@jeesite/cms/api/cms/category';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import { isEmpty } from '@jeesite/core/utils/is';
  import InputForm from './form.vue';

  const props = defineProps({
    treeCodes: Array as PropType<string[]>,
    siteCode: String,
  });

  const emit = defineEmits(['update:treeCodes']);

  const { t } = useI18n('cms.category');
  const { showMessage } = useMessage();
  const { ctxPath } = useGlobSetting();
  const { meta } = unref(router.currentRoute);
  const record = ref<Category>({} as Category);

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('栏目管理'),
  };

  const searchForm: FormProps<Category> = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('栏目编码'),
        field: 'categoryCode_like',
        component: 'Input',
      },
      {
        label: t('栏目名称'),
        field: 'categoryName',
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
        label: t('内容模型'),
        field: 'moduleType',
        component: 'Select',
        componentProps: {
          dictType: 'cms_module_type',
          allowClear: true,
        },
      },
      {
        label: t('备注信息'),
        field: 'remarks',
        component: 'Input',
      },
    ],
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
  };

  const tableColumns: BasicColumn<Category>[] = [
    {
      title: t('名称'),
      dataIndex: 'categoryName',
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('模型'),
      dataIndex: 'moduleType',
      width: 100,
      align: 'center',
      dictType: 'cms_module_type',
    },
    {
      title: t('排序'),
      dataIndex: 'treeSort',
      width: 100,
      align: 'center',
    },
    {
      title: t('导航栏目'),
      dataIndex: 'inMenu',
      width: 100,
      align: 'center',
      dictType: 'sys_show_hide',
    },
    {
      title: t('栏目列表'),
      dataIndex: 'inList',
      width: 100,
      align: 'center',
      dictType: 'sys_show_hide',
    },
    {
      title: t('展现模式'),
      dataIndex: 'showModes',
      width: 100,
      align: 'center',
      dictType: 'cms_show_modes',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 100,
      align: 'center',
      dictType: 'sys_search_status',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn<Category> = {
    width: 240,
    actions: (record: Category) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑栏目'),
        onClick: handleForm.bind(this, { categoryCode: record.categoryCode }),
        auth: 'cms:category:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用栏目'),
        popConfirm: {
          title: t('是否确认停用栏目'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'cms:category:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用栏目'),
        popConfirm: {
          title: t('是否确认启用栏目'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'cms:category:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除栏目'),
        popConfirm: {
          title: t('是否确认删除栏目'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'cms:category:edit',
      },
      {
        icon: 'i-fluent:add-circle-24-regular',
        title: t('新增下级栏目'),
        onClick: handleForm.bind(this, {
          parentCode: record.id,
          parentName: record.categoryName,
        }),
        auth: 'cms:category:edit',
      },
      {
        icon: 'i-ant-design:aim-outlined',
        title: t('重建该站点索引'),
        popConfirm: {
          title: t('确认重建该站点文章索引吗'),
          confirm: handleRebuildIndex.bind(this, record),
        },
        auth: 'cms:category:rebuildIndex',
      },
      {
        icon: 'i-bx:data',
        title: t('重建该站点向量数据库'),
        popConfirm: {
          title: t('确认重建该站点文章向量数据库吗'),
          confirm: handleRebuildVectorStore.bind(this, record),
        },
        auth: 'cms:category:rebuildVectorStore',
      },
      {
        icon: 'i-mdi:globe',
        title: t('访问栏目'),
        onClick: () => window.open(ctxPath + '/f/list-' + record.categoryCode, '_blank'),
        auth: 'cms:category:edit',
        ifShow: () => record.status === '0',
      },
    ],
  };

  const [registerTable, { reload, expandAll, collapseAll, expandCollapse, getForm }] = useTable<Category>({
    api: categoryListData,
    beforeFetch: (params) => {
      params['site.siteCode'] = props.siteCode || 'main';
      params.categoryCode = !isEmpty(props.treeCodes) ? props.treeCodes[0] : '';
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

  onMounted(async () => {
    const res = await categoryList();
    record.value = (res.category || {}) as Category;
    await getForm().setFieldsValue(record.value);
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  watch([() => props.treeCodes, () => props.siteCode], async (value, oldValue) => {
    if (!isEmpty(props.treeCodes) || value[1] != oldValue[1]) {
      await reload();
    }
  });

  function fetchSuccess() {
    if (!isEmpty(props.treeCodes)) {
      nextTick(expandAll);
    }
  }

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await categoryDisable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await categoryEnable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await categoryDelete(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleRebuildIndex(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await categoryRebuildIndex(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleRebuildVectorStore(record: Recordable) {
    const params = { categoryCode: record.categoryCode };
    const res = await categoryRebuildVectorStore(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }
</script>

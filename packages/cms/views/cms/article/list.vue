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
        <a-button type="default" @click="handleOpenSite"> <Icon icon="i-mdi:globe" /> {{ t('访问站点') }} </a-button>
        <a-button
          type="primary"
          @click="handleForm({ 'category.categoryCode': !isEmpty(props.treeCodes) ? props.treeCodes[0] : '' })"
          v-auth="'cms:article:edit'"
        >
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ id: record.id })" :title="record.title">
          {{ record.title }}
        </a>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts" setup name="ViewsCmsArticleList">
  import { computed, onMounted, ref, unref, watch } from 'vue';
  import { useEmitter } from '@jeesite/core/store/modules/user';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useGo } from '@jeesite/core/hooks/web/usePage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { Article, articleList } from '@jeesite/cms/api/cms/article';
  import { articleDelete, articleListData } from '@jeesite/cms/api/cms/article';
  import { articleDisable, articleEnable } from '@jeesite/cms/api/cms/article';
  import { FormProps } from '@jeesite/core/components/Form';
  import { isEmpty } from '@jeesite/core/utils/is';

  const props = defineProps({
    treeCodes: Array as PropType<string[]>,
  });

  const emit = defineEmits(['update:treeCodes']);

  const emitter = useEmitter();

  const { t } = useI18n('cms.article');
  const { showMessage } = useMessage();
  const { ctxPath } = useGlobSetting();
  const { meta } = unref(router.currentRoute);
  const record = ref<Article>({} as Article);
  const isCanUseAuth = ref(false);

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('文章管理'),
  };
  const go = useGo();

  const searchForm: FormProps<Article> = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('栏目编码'),
        field: 'category.categoryCode',
        component: 'Input',
      },
      {
        label: t('内容标题'),
        field: 'title',
        component: 'Input',
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: computed(() => {
            return isCanUseAuth.value ? 'bpm_biz_status' : 'sys_search_status';
          }),
          allowClear: true,
          onChange: handleSuccess,
        },
      },
    ],
    resetFunc: async () => {
      emit('update:treeCodes', []);
    },
  };

  const tableColumns: BasicColumn<Article>[] = [
    {
      title: t('标题'),
      dataIndex: 'title',
      key: 'a.title',
      sorter: true,
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('栏目'),
      dataIndex: 'category.categoryName',
      key: 'a.category_code',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('权重'),
      dataIndex: 'weight',
      key: 'a.weight',
      sorter: true,
      width: 90,
      align: 'center',
    },
    {
      title: t('点击数'),
      dataIndex: 'hits',
      key: 'a.hits',
      sorter: true,
      width: 90,
      align: 'center',
    },
    {
      title: t('字数'),
      dataIndex: 'wordCount',
      key: 'a.word_count',
      sorter: true,
      width: 90,
      align: 'center',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 90,
      align: 'center',
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
  ];

  const actionColumn: BasicColumn<Article> = {
    width: 140,
    actions: (record: Article) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑文章'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'cms:article:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用文章'),
        popConfirm: {
          title: t('是否确认停用文章'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'cms:article:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用文章'),
        popConfirm: {
          title: t('是否确认启用文章'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'cms:article:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除文章'),
        popConfirm: {
          title: t('是否确认删除文章'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'cms:article:edit',
      },
      {
        icon: 'i-mdi:globe',
        title: t('查看文章'),
        onClick: () => window.open(ctxPath + '/f/view-' + record.category.categoryCode + '-' + record.id, '_blank'),
        auth: 'cms:category:edit',
        ifShow: () => record.status === '0',
      },
    ],
  };

  const [registerTable, { reload, getForm, updateColumn }] = useTable<Article>({
    api: articleListData,
    beforeFetch: (params) => {
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
  });

  onMounted(async () => {
    const res = await articleList();
    record.value = (res.article || {}) as Article;
    isCanUseAuth.value = res.isCanUseAuth as boolean;
    updateColumn({ dataIndex: 'status', dictType: isCanUseAuth.value ? 'bpm_biz_status' : 'sys_status' });
    await getForm().setFieldsValue(record.value);
  });

  watch(
    () => props.treeCodes,
    async () => {
      await getForm().setFieldsValue({
        'category.categoryCode': !isEmpty(props.treeCodes) ? props.treeCodes[0] : '',
      });
      await reload();
    },
  );

  function handleOpenSite() {
    window.open(ctxPath + '/f/index', '_blank');
  }

  function handleForm(record: Recordable) {
    // if (!record.id && !record['category.categoryCode']) {
    //   showMessage(t('请先选择栏目'));
    //   return;
    // }
    go({
      path: '/cms/article/form',
      query: record,
    });
  }

  async function handleDisable(record: Recordable) {
    const params = { id: record.id };
    const res = await articleDisable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { id: record.id };
    const res = await articleEnable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { id: record.id };
    const res = await articleDelete(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }

  emitter.on('cms-article-reload', reload, true);
</script>

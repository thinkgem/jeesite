<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="200">
    <template #sidebar>
      <BasicTree
        :title="t('栏目')"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="categoryTreeData"
        :params="apiParams"
        :immediate="immediate"
        :defaultExpandLevel="2"
        :selectedKeys="treeCodes"
        @click="handleSelectedKeys"
      >
        <template #headerTitle>
          <Dropdown :trigger="['hover']" :dropMenuList="dropMenuList">
            <span class="cursor-pointer">{{ siteName }} <DownOutlined /></span>
          </Dropdown>
        </template>
      </BasicTree>
    </template>
    <ArticleList
      v-if="!treeData.module || treeData.module === 'article'"
      v-model:treeCodes="treeCodes"
      :siteCode="siteCode"
    />
    <div class="p-4" v-else>{{ t('内容') }} {{ treeData.module }} {{ t('模型暂未实现') }}</div>
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsCmsIndex">
  import { onMounted, ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { BasicTree } from '@jeesite/core/components/Tree';
  import { Site, siteSelect } from '@jeesite/cms/api/cms/site';
  import { Category, categoryIndex, categoryTreeData } from '@jeesite/cms/api/cms/category';
  import { Dropdown, DropMenu } from '@jeesite/core/components/Dropdown';
  import { DownOutlined } from '@antdv-next/icons';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

  import ArticleList from './article/list.vue';

  const { t } = useI18n('cms.category');
  const treeData = ref<Category>({} as Category);
  const treeCodes = ref<string[]>([]);
  const apiParams = ref<Recordable>({ siteCode: 'main' });
  const immediate = ref(false);

  const dropMenuList = ref<Array<DropMenu>>([]);
  const siteCode = ref<string>('main');
  const siteName = ref<string>(t('JeeSite'));

  const go = useGo();

  onMounted(async () => {
    const res = await categoryIndex();
    const currentSite = (res.currentSite || {}) as Site;
    siteCode.value = currentSite.siteCode || 'main';
    siteName.value = currentSite.siteName || 'main';
    apiParams.value.siteCode = currentSite.siteCode;
    immediate.value = true;
    await loadSiteCode(res);
  });

  async function loadSiteCode(res: Recordable) {
    dropMenuList.value = (res.siteList || []).map((item) => {
      if (item.value == siteCode.value) {
        siteName.value = item.name;
      }
      return {
        text: item.siteName,
        event: item.siteCode,
        icon: 'i-radix-icons:dot',
        onClick: () => {
          siteCode.value = item.siteCode;
          siteName.value = item.siteName;
          apiParams.value.siteCode = item.siteCode;
          siteSelect({ siteCode: siteCode.value });
          treeCodes.value = [];
        },
      };
    });
  }

  function handleSelectedKeys(_e: any, tree: any) {
    // 展现方式：1 自动识别；2 栏目内容列表；3 栏目第一条内容
    // if (tree.module === 'article' && tree.showModes == '3') {
    //   go('/cms/article/form?id=' + tree.id);
    // }
    treeData.value = tree;
    treeCodes.value = [tree.id];
  }
</script>

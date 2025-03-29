<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230">
    <template #sidebar>
      <BasicTree
        :title="t('分类')"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="bizCategoryTreeData"
        :defaultExpandLevel="2"
        v-model:selectedKeys="treeCodes"
      />
    </template>
    <ListView v-model:treeCodes="treeCodes" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsBizCategoryIndex">
  import { ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { PageWrapper } from '/@/components/Page';
  import { BasicTree } from '/@/components/Tree';
  import { bizCategoryTreeData } from '@jeesite/dbm/api/biz/bizCategory';
  import ListView from './list.vue';

  const { t } = useI18n('biz.bizCategory');
  const treeCodes = ref<string[]>([]);
</script>

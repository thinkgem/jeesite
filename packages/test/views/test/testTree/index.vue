<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230">
    <template #sidebar>
      <BasicTree
        :title="t('数据')"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="testTreeTreeData"
        :defaultExpandLevel="2"
        v-model:selectedKeys="treeCodes"
      />
    </template>
    <ListView v-model:treeCodes="treeCodes" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsTestTestTreeIndex">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { BasicTree } from '@jeesite/core/components/Tree';
  import { testTreeTreeData } from '@jeesite/test/api/test/testTree';
  import ListView from './list.vue';

  const { t } = useI18n('sys.testTree');
  const treeCodes = ref<string[]>([]);
</script>

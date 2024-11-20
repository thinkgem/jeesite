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
        :api="testTreeTreeData"
        :defaultExpandLevel="2"
        @select="handleSelect"
      />
    </template>
    <ListView :treeCode="treeCode" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsTestTestTreeIndex">
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { BasicTree } from '@jeesite/core/components/Tree';
  import { testTreeTreeData } from '@jeesite/test/api/test/testTree';
  import ListView from './list.vue';

  const { t } = useI18n('sys.menu');
  const treeCode = ref<string>('');

  function handleSelect(keys: string[]) {
    treeCode.value = keys[0];
  }
</script>

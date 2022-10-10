<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230">
    <template #sidebar>
      <BasicTree
        :title="t('行政区划')"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="areaTreeData"
        :defaultExpandLevel="2"
        @select="handleSelect"
      />
    </template>
    <ListView :treeCode="treeCode" />
  </PageWrapper>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysAreaIndex',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { PageWrapper } from '/@/components/Page';
  import { BasicTree } from '/@/components/Tree';
  import { areaTreeData } from '/@/api/sys/area';
  import ListView from './list.vue';

  const { t } = useI18n('sys.menu');
  const treeCode = ref<string>('');

  function handleSelect(keys: string[]) {
    treeCode.value = keys[0];
  }
</script>

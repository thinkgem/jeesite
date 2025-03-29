<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230">
    <template #sidebar>
      <BasicTree
        :title="t('机构')"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="officeTreeData"
        :defaultExpandLevel="2"
        v-model:selectedKeys="treeCodes"
      />
    </template>
    <ListView v-model:treeCodes="treeCodes" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsSysOfficeIndex">
  import { ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { PageWrapper } from '/@/components/Page';
  import { BasicTree } from '/@/components/Tree';
  import { officeTreeData } from '/@/api/sys/office';
  import ListView from './list.vue';

  const { t } = useI18n('sys.office');
  const treeCodes = ref<string[]>([]);
</script>

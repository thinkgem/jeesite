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
        :params="{ ctrlPermi }"
        :immediate="immediate"
        :defaultExpandLevel="2"
        @select="handleSelect"
      >
        <!--<template #icon="item">
          <Icon icon="i-ant-design:smile-filled" v-if="item.selected" />
          <Icon icon="i-ant-design:smile-outlined" v-else />
        </template>-->
      </BasicTree>
    </template>
    <ListView :treeCode="treeCode" :treeName="treeName" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsSysEmpUserIndex">
  import { onMounted, ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  // import { Icon } from '@jeesite/core/components/Icon';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { BasicTree } from '@jeesite/core/components/Tree';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { empUserIndex } from '@jeesite/core/api/sys/empUser';
  import ListView from './list.vue';

  const { t } = useI18n('sys.empUser');
  const treeCode = ref<string>('');
  const treeName = ref<string>('');
  const ctrlPermi = ref('');
  const immediate = ref(false);

  onMounted(async () => {
    const res = await empUserIndex();
    ctrlPermi.value = res.ctrlPermi || '2';
    immediate.value = true;
  });

  function handleSelect(keys: string[], { node }) {
    treeCode.value = keys[0];
    treeName.value = node._name;
  }
</script>

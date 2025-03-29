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
        v-model:selectedKeys="treeCodes"
        @select="handleSelect"
      >
        <!--<template #icon="item">
          <Icon icon="i-ant-design:smile-filled" v-if="item.selected" />
          <Icon icon="i-ant-design:smile-outlined" v-else />
        </template>-->
      </BasicTree>
    </template>
    <ListView v-model:treeCodes="treeCodes" :treeName="treeName" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsSysEmpUserIndex">
  import { onMounted, ref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  // import { Icon } from '/@/components/Icon';
  import { PageWrapper } from '/@/components/Page';
  import { BasicTree } from '/@/components/Tree';
  import { officeTreeData } from '/@/api/sys/office';
  import { empUserIndex } from '/@/api/sys/empUser';
  import ListView from './list.vue';

  const { t } = useI18n('sys.empUser');
  const treeCodes = ref<string[]>([]);
  const treeName = ref<string>('');
  const ctrlPermi = ref('');
  const immediate = ref(false);

  onMounted(async () => {
    const res = await empUserIndex();
    ctrlPermi.value = res.ctrlPermi || '2';
    immediate.value = true;
  });

  function handleSelect(_keys: string[], { node }) {
    treeName.value = node._name;
  }
</script>

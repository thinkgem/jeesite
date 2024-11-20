<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230">
    <template #sidebar>
      <BasicTree
        ref="treeRef"
        :title="t('区域管理')"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :treeData="treeData"
        :loadData="onLoadData"
        :treeDataSimpleMode="false"
        @select="handleSelect"
      />
    </template>
    <ListView :treeCode="treeCode" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsSysAreaIndex">
  import { ref, unref, onMounted } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { BasicTree, TreeActionType, TreeItem } from '@jeesite/core/components/Tree';
  import { areaTreeData } from '@jeesite/core/api/sys/area';
  import ListView from './list.vue';
  import { isArray } from '@jeesite/core/utils/is';
  //import { uniq } from 'lodash-es';

  const { t } = useI18n('sys.area');
  const treeCode = ref<string>('');

  function handleSelect(keys: string[]) {
    treeCode.value = keys[0];
  }

  const treeRef = ref<Nullable<TreeActionType>>(null);
  const treeData = ref<TreeItem['treeData']>([]);

  onMounted(async () => {
    treeData.value = await areaTreeData({ parentCode: '0' });
  });

  const onLoadData: TreeItem['loadData'] = (treeNode) => {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise(async (resolve: (value?: unknown) => void) => {
      if (isArray(treeNode.children) && treeNode.children.length > 0) {
        resolve();
        return;
      }
      const treeAction: TreeActionType | null = unref(treeRef);
      if (treeAction) {
        const children = await areaTreeData({ parentCode: treeNode.eventKey });
        treeAction.updateNodeByKey(treeNode.eventKey, { children });
        //treeAction.setExpandedKeys(uniq([treeNode.eventKey, ...treeAction.getExpandedKeys()]));
      }
      resolve();
      return;
    });
  };
</script>

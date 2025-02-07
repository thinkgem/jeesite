<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="flex flex-row flex-wrap">
    <template v-for="item in dataScopes" :key="item.moduleCode">
      <div
        class="mb-5 mr-5"
        v-if="moduleCodes.includes(item.moduleCode) && ctrlPermis.includes(item.ctrlPermi)"
      >
        <BasicTree
          class="bg-gray"
          style="min-width: 300px"
          :title="t(item['ctrlName_' + localeStore.getLocale] || item.ctrlName)"
          :toolbar="true"
          :checkable="true"
          :checkStrictly="checkStrictly"
          :api="api"
          :params="{ url: item.ctrlDataUrl, ctrlPermi: ctrlPermi }"
          :immediate="immediate"
          :defaultExpandLevel="2"
          :ref="setTreeRefs(item.ctrlType)"
          @tree-data-change="handleTreeDataChange"
        />
      </div>
    </template>
  </div>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref, nextTick, type PropType } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useLocaleStore } from '/@/store/modules/locale';
  import { BasicTree, TreeActionType } from '/@/components/Tree';
  import { propTypes } from '/@/utils/propTypes';

  const props = defineProps({
    namespace: propTypes.string,
    checkStrictly: propTypes.bool,
    api: { type: Function as PropType<(arg?: Recordable) => Promise<Recordable>> },
    ctrlPermis: propTypes.array.def(['0', '1']),
  });

  const { t } = useI18n(props.namespace);
  const localeStore = useLocaleStore();

  const dataScopes = ref<Array<Recordable>>([]);
  const dataScopeList = ref<Array<Recordable>>([]);
  const moduleCodes = ref<Array<string>>([]);
  const ctrlPermi = ref<string>('');
  const immediate = ref(false);

  const treeRefs: Recordable<TreeActionType> = {};
  const setTreeRefs = (key: string) => (el: any) => {
    if (el) treeRefs[key] = el;
  };

  let loadTreeDataNum: number;
  async function loadDataScopeList(data: Recordable) {
    dataScopes.value = data.dataScopes || [];
    dataScopeList.value = data.dataScopeList || [];
    moduleCodes.value = data.moduleCodes || [];
    ctrlPermi.value = data.ctrlPermi || '1';
    loadTreeDataNum = 0;
    await nextTick(() => {
      if (immediate.value) {
        for (const key of Object.keys(treeRefs)) {
          treeRefs[key].setCheckedKeys([]);
          treeRefs[key].reload();
        }
      } else {
        immediate.value = true;
      }
    });
  }

  function handleTreeDataChange() {
    const keys = Object.keys(treeRefs);
    if (++loadTreeDataNum == keys.length) {
      let checkedKeys = {};
      dataScopeList.value.forEach((item) => {
        if (!checkedKeys[item.ctrlType]) {
          checkedKeys[item.ctrlType] = [];
        }
        checkedKeys[item.ctrlType].push(item.ctrlData);
      });
      for (const key of keys) {
        treeRefs[key].setCheckedKeys(checkedKeys[key] || []);
      }
    }
  }

  function getDataScopeListJson() {
    const keys = Object.keys(treeRefs);
    let dataScopeData: Array<any> = [];
    for (const key of keys) {
      const ks = treeRefs[key].getCheckedKeys();
      for (const k of ks as Array<any>) {
        dataScopeData.push({
          ctrlType: key,
          ctrlData: k,
        });
      }
    }
    return JSON.stringify(dataScopeData);
  }

  defineExpose({
    loadDataScopeList,
    getDataScopeListJson,
  });
</script>

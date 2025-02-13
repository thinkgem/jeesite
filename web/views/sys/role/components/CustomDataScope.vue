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
          :checkStrictly="!(item.chkboxType?.Y + item.chkboxType?.N).includes('p')"
          :api="api"
          :params="{ url: item.ctrlDataUrl, ctrlPermi: ctrlPermi, parentAttr: 'disableCheckbox' }"
          :canSelectParent="!isLoadUser.includes(item.ctrlType)"
          :immediate="immediate"
          :defaultExpandLevel="Number(item.expandLevel)"
          :ref="setTreeRefs(item.ctrlType)"
          @tree-data-change="handleTreeDataChange"
        />
      </div>
    </template>
  </div>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { ref, nextTick, type PropType } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useLocaleStore } from '@jeesite/core/store/modules/locale';
  import { BasicTree, TreeActionType } from '@jeesite/core/components/Tree';
  import { propTypes } from '@jeesite/core/utils/propTypes';

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
  const menuCode = ref<string>('');
  const immediate = ref(false);
  const isLoadUser = ref<Array<string>>([]);

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
    menuCode.value = data.menuCode || '0';
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
    isLoadUser.value = dataScopes.value
      .filter((item) => String(item.ctrlDataUrl).includes('isLoadUser=true'))
      .map((item) => item.ctrlType);
  }

  function handleTreeDataChange() {
    const keys = Object.keys(treeRefs);
    loadTreeDataNum = loadTreeDataNum + 1;
    if (loadTreeDataNum == keys.length) {
      let checkedKeys = {};
      dataScopeList.value.forEach((item) => {
        if (!checkedKeys[item.ctrlType]) {
          checkedKeys[item.ctrlType] = [];
        }
        checkedKeys[item.ctrlType].push(
          (isLoadUser.value.includes(item.ctrlType) ? 'u_' : '') + item.ctrlData,
        );
      });
      for (const key of keys) {
        treeRefs[key].setCheckedKeys(checkedKeys[key] || []);
      }
    }
  }

  function getDataScopeList() {
    const keys = Object.keys(treeRefs);
    let dataScopeData: Array<any> = [];
    for (const key of keys) {
      const ks = treeRefs[key].getCheckedKeys();
      for (const k of ks as Array<any>) {
        dataScopeData.push({
          ctrlType: String(key),
          ctrlData: String(k).replace(/^u_/g, ''),
          menuCode: menuCode.value,
        });
      }
    }
    return dataScopeData;
  }

  function getDataScopeListJson() {
    return JSON.stringify(getDataScopeList());
  }

  defineExpose({
    loadDataScopeList,
    getDataScopeList,
    getDataScopeListJson,
  });
</script>

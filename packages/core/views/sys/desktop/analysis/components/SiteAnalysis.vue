<template>
  <Card
    v-bind="$attrs"
    :loading="loading"
    :tab-list="tabListTitle"
    :active-tab-key="activeKey"
    @tab-change="onTabChange"
  >
    <p v-if="activeKey === 'tab1'">
      <VisitAnalysis />
    </p>
    <p v-if="activeKey === 'tab2'">
      <VisitAnalysisBar />
    </p>
  </Card>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Card } from 'ant-design-vue';
  import VisitAnalysis from './VisitAnalysis.vue';
  import VisitAnalysisBar from './VisitAnalysisBar.vue';

  const loading = ref(true);
  const activeKey = ref('tab1');

  const tabListTitle = [
    {
      key: 'tab1',
      tab: '流量趋势',
    },
    {
      key: 'tab2',
      tab: '访问量',
    },
  ];

  function onTabChange(key) {
    activeKey.value = key;
  }

  setTimeout(() => {
    loading.value = false;
  }, 700);
</script>

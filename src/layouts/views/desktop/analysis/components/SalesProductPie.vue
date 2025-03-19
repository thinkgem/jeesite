<template>
  <Card title="成交占比" :loading="loading">
    <div ref="chartRef" class="h-75 w-full"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { onMounted, Ref, ref } from 'vue';
  import { Card } from 'ant-design-vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import type { EChartsOption } from 'echarts';

  const loading = ref(true);
  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);

  onMounted(() => {
    const options: EChartsOption = {
      tooltip: {
        trigger: 'item',
      },
      series: [
        {
          name: '访问来源',
          type: 'pie',
          radius: '80%',
          center: ['50%', '50%'],
          color: ['#5ab1ef', '#b6a2de', '#67e0e3', '#2ec7c9'],
          data: [
            { value: 500, name: '电子产品' },
            { value: 310, name: '服装' },
            { value: 274, name: '化妆品' },
            { value: 400, name: '家居' },
          ].sort(function (a, b) {
            return a.value - b.value;
          }),
          roseType: 'radius',
          animationType: 'scale',
          animationEasing: 'exponentialInOut',
          animationDelay: function () {
            return Math.random() * 400;
          },
        },
      ],
    };
    // 此处写后端 API 获取 options 数据
    setTimeout(() => {
      setOptions(options);
      loading.value = false;
    }, 900);
  });
</script>

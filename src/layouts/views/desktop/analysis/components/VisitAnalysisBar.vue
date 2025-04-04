<template>
  <Skeleton active :paragraph="{ rows: 5 }" :loading="loading">
    <div ref="chartRef" class="h-70 w-full"></div>
  </Skeleton>
</template>
<script lang="ts" setup>
  import { onMounted, ref, Ref } from 'vue';
  import { Skeleton } from 'ant-design-vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import type { EChartsOption } from 'echarts';

  const loading = ref(true);
  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);

  onMounted(() => {
    const options: EChartsOption = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          lineStyle: {
            width: 1,
            color: '#019680',
          },
        },
      },
      grid: { left: '1%', right: '1%', top: '2  %', bottom: 0, containLabel: true },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      },
      yAxis: {
        type: 'value',
        max: 8000,
        splitNumber: 4,
      },
      series: [
        {
          data: [3000, 2000, 3333, 5000, 3200, 4200, 3200, 2100, 3000, 5100, 6000, 3200, 4800],
          type: 'bar',
          barMaxWidth: 80,
        },
      ],
    };
    // 此处写后端 API 获取 options 数据
    setOptions(options);
    loading.value = false;
  });
</script>

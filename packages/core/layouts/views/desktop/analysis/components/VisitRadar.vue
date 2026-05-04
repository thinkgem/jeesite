<template>
  <Card title="转化率" :loading="loading">
    <div ref="chartRef" class="h-75 w-full"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { onMounted, Ref, ref, shallowRef } from 'vue';
  import { Card } from 'antdv-next';
  import { useECharts } from '@jeesite/core/hooks/web/useECharts';
  import type { EChartsOption } from 'echarts';

  const loading = ref(true);
  const chartRef = shallowRef<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);

  onMounted(() => {
    const options: EChartsOption = {
      legend: {
        bottom: 0,
        data: ['访问', '购买'],
      },
      tooltip: {},
      radar: {
        radius: '60%',
        splitNumber: 8,
        axisTick: {
          show: false,
        },
        splitLine: {
          lineStyle: {
            color: [
              'rgba(238, 197, 102, 0.1)',
              'rgba(238, 197, 102, 0.2)',
              'rgba(238, 197, 102, 0.4)',
              'rgba(238, 197, 102, 0.6)',
              'rgba(238, 197, 102, 0.8)',
              'rgba(238, 197, 102, 1)',
            ].reverse(),
          },
        },
        splitArea: {
          show: false,
        },
        axisLine: {
          lineStyle: {
            color: 'rgba(238, 197, 102, 0.5)',
          },
        },
        indicator: [
          {
            name: '电脑',
          },
          {
            name: '充电器',
          },
          {
            name: '耳机',
          },
          {
            name: '手机',
          },
          {
            name: 'Ipad',
          },
          {
            name: '耳机',
          },
        ],
      },
      series: [
        {
          type: 'radar',
          symbolSize: 0,
          areaStyle: {
            shadowBlur: 0,
            shadowColor: 'rgba(0,0,0,.2)',
            shadowOffsetX: 0,
            shadowOffsetY: 10,
            opacity: 1,
          },
          data: [
            {
              value: [90, 50, 86, 40, 50, 20],
              name: '访问',
              itemStyle: {
                color: '#b6a2de',
              },
            },
            {
              value: [70, 75, 70, 76, 20, 85],
              name: '购买',
              itemStyle: {
                color: '#5ab1ef',
              },
            },
          ],
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

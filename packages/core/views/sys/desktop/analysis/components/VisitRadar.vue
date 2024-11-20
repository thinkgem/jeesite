<template>
  <Card title="转化率" :loading="loading">
    <div ref="chartRef" class="h-75 w-full"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { onMounted, Ref, ref } from 'vue';
  import { Card } from 'ant-design-vue';
  import { useECharts } from '@jeesite/core/hooks/web/useECharts';
  import type { EChartsOption } from 'echarts';

  const loading = ref(true);
  const chartRef = ref<HTMLDivElement | null>(null);
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
        indicator: [
          {
            name: '电脑',
            max: 100,
          },
          {
            name: '充电器',
            max: 100,
          },
          {
            name: '耳机',
            max: 100,
          },
          {
            name: '手机',
            max: 100,
          },
          {
            name: 'Ipad',
            max: 100,
          },
          {
            name: '耳机',
            max: 100,
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

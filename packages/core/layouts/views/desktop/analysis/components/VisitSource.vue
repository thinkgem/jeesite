<template>
  <Card title="访问来源" :loading="loading">
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
      tooltip: {
        trigger: 'item',
      },
      legend: {
        bottom: '1%',
        left: 'center',
      },
      series: [
        {
          color: ['#5ab1ef', '#b6a2de', '#67e0e3', '#2ec7c9'],
          name: '访问来源',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2,
          },
          label: {
            show: false,
            position: 'center',
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '12',
              fontWeight: 'bold',
            },
          },
          labelLine: {
            show: false,
          },
          data: [
            { value: 1048, name: '搜索引擎' },
            { value: 735, name: '直接访问' },
            { value: 580, name: '邮件营销' },
            { value: 484, name: '联盟广告' },
          ],
          animationType: 'scale',
          animationEasing: 'exponentialInOut',
          animationDelay: function () {
            return Math.random() * 100;
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

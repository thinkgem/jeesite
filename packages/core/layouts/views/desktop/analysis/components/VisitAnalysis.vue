<template>
  <Skeleton active :paragraph="{ rows: 5 }" :loading="loading">
    <div ref="chartRef" class="h-70 w-full"></div>
  </Skeleton>
</template>
<script lang="ts" setup>
  import { onMounted, ref, Ref } from 'vue';
  import { Skeleton } from 'ant-design-vue';
  import { useECharts } from '@jeesite/core/hooks/web/useECharts';
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
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: [
          '6:00',
          '7:00',
          '8:00',
          '9:00',
          '10:00',
          '11:00',
          '12:00',
          '13:00',
          '14:00',
          '15:00',
          '16:00',
          '17:00',
          '18:00',
          '19:00',
          '20:00',
          '21:00',
          '22:00',
          '23:00',
        ],
        splitLine: {
          show: true,
          lineStyle: {
            width: 1,
            type: 'solid',
            color: 'rgba(226,226,226,0.5)',
          },
        },
        axisTick: {
          show: false,
        },
      },
      yAxis: [
        {
          type: 'value',
          max: 80000,
          splitNumber: 4,
          axisTick: {
            show: false,
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: ['rgba(255,255,255,0.2)', 'rgba(226,226,226,0.2)'],
            },
          },
        },
      ],
      grid: { left: '1%', right: '1%', top: '2  %', bottom: 0, containLabel: true },
      series: [
        {
          smooth: true,
          data: [
            111, 222, 4000, 18000, 33333, 55555, 66666, 33333, 14000, 36000, 66666, 44444, 22222, 11111, 4000, 2000,
            500, 333, 222, 111,
          ],
          type: 'line',
          areaStyle: {},
          itemStyle: {
            color: '#5ab1ef',
          },
        },
        {
          smooth: true,
          data: [
            33, 66, 88, 333, 3333, 5000, 18000, 3000, 1200, 13000, 22000, 11000, 2221, 1201, 390, 198, 60, 30, 22, 11,
          ],
          type: 'line',
          areaStyle: {},
          itemStyle: {
            color: '#019680',
          },
        },
      ],
    };
    // 此处写后端 API 获取 options 数据
    setOptions(options);
    loading.value = false;
  });
</script>

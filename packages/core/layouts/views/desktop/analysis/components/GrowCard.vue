<template>
  <div class="md:flex">
    <template v-for="(item, index) in growCardList" :key="item.title">
      <Card
        size="small"
        :loading="loading"
        :title="item.title"
        class="w-full cursor-pointer !mt-4 md:w-1/4 !md:mt-0"
        :class="[index + 1 < 4 && '!md:mr-4']"
        :canExpan="false"
        @click="navPage(item.url)"
      >
        <template #extra>
          <Tag :color="item.color">{{ item.action }}</Tag>
        </template>

        <div class="flex justify-between px-4 py-4">
          <CountTo prefix="" :startVal="1" :endVal="item.value" class="text-2xl" />
          <Icon :icon="item.icon" :size="40" />
        </div>

        <div class="flex justify-between p-2 px-4">
          <span>点击我</span>
          <CountTo prefix="共" :startVal="1" :endVal="item.total" />
        </div>
      </Card>
    </template>
  </div>
</template>
<script lang="ts" setup>
  import { ref, onMounted } from 'vue';
  import { CountTo } from '@jeesite/core/components/CountTo';
  import { Icon } from '@jeesite/core/components/Icon';
  import { Tag, Card } from 'ant-design-vue';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

  const loading = ref(true);
  const growCardList = ref<GrowCardItem[]>();
  const go = useGo();

  interface GrowCardItem {
    icon: string;
    title: string;
    value: number;
    total: number;
    color: string;
    action: string;
    url: string;
  }

  onMounted(() => {
    const list: GrowCardItem[] = [
      {
        title: '工作台',
        icon: 'icons/visit-count.svg',
        value: 1999,
        total: 120000,
        color: 'green',
        action: '时',
        url: '/desktop/workbench',
      },
      {
        title: '关于我们',
        icon: 'icons/total-sales.svg',
        value: 2999,
        total: 500000,
        color: 'blue',
        action: '日',
        url: '/desktop/about',
      },
      {
        title: '源码下载',
        icon: 'icons/download-count.svg',
        value: 3999,
        total: 120000,
        color: 'orange',
        action: '周',
        url: 'https://gitee.com/thinkgem/jeesite-vue',
      },
      {
        title: '官方网站',
        icon: 'icons/transaction.svg',
        value: 9999,
        total: 99999,
        color: 'purple',
        action: '月',
        url: 'http://jeesite.com',
      },
    ];
    // 此处写后端 API 获取 list 数据
    growCardList.value = list;
    setTimeout(() => {
      loading.value = false;
    }, 500);
  });

  function navPage(url: string) {
    if (!url || url === '') {
      return;
    }
    if (url.indexOf('://') != -1) {
      window.open(url);
    } else {
      go(url);
    }
  }
</script>

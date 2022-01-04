<template>
  <div class="md:flex">
    <template v-for="(item, index) in growCardList" :key="item.title">
      <Card
        size="small"
        :loading="loading"
        :title="item.title"
        class="md:w-1/4 w-full !md:mt-0 !mt-4 cursor-pointer"
        :class="[index + 1 < 4 && '!md:mr-4']"
        :canExpan="false"
        @click="navPage(item.url)"
      >
        <template #extra>
          <Tag :color="item.color">{{ item.action }}</Tag>
        </template>

        <div class="py-4 px-4 flex justify-between">
          <CountTo prefix="" :startVal="1" :endVal="item.value" class="text-2xl" />
          <Icon :icon="item.icon" :size="40" />
        </div>

        <div class="p-2 px-4 flex justify-between">
          <span>点击我</span>
          <CountTo prefix="共" :startVal="1" :endVal="item.total" />
        </div>
      </Card>
    </template>
  </div>
</template>
<script lang="ts" setup>
  import { CountTo } from '/@/components/CountTo/index';
  import { Icon } from '/@/components/Icon';
  import { Tag, Card } from 'ant-design-vue';
  import { growCardList } from '../data';
  import { useGo } from '/@/hooks/web/usePage';

  const go = useGo();

  defineProps({
    loading: {
      type: Boolean,
    },
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

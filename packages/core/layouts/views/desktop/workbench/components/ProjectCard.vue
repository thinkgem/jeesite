<template>
  <Card title="项目" v-bind="$attrs">
    <template #extra>
      <a-button type="link" size="small">更多</a-button>
    </template>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div v-for="item in groupItems" :key="item.title" class="cursor-pointer p-4 border rounded" @click="go(item.url)">
        <span class="flex">
          <Icon :icon="item.icon" :color="item.color" size="30" />
          <span class="ml-4 text-lg">{{ item.title }}</span>
        </span>
        <div class="text-secondary mt-2 h-10 flex">{{ item.desc }}</div>
        <div class="text-secondary flex justify-between">
          <span>{{ item.group }}</span>
          <span>{{ item.date }}</span>
        </div>
      </div>
    </div>
  </Card>
</template>
<script lang="ts" setup>
  import { Card } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { groupItems } from './Data';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

  const go = useGo();
</script>

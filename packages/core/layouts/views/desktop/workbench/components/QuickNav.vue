<template>
  <Card title="快捷导航" v-bind="$attrs">
    <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
      <div v-for="item in navItems" :key="item.title" class="cursor-pointer p-3 border rounded" @click="go(item.url)">
        <span class="flex flex-col items-center">
          <Icon :icon="item.icon" :color="item.color" size="20" />
          <span class="text-md mt-2">{{ item.title }}</span>
        </span>
      </div>
    </div>
  </Card>
</template>
<script lang="ts" setup>
  import { Card } from 'antdv-next';
  import { navItems } from './data';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

  const go = useGo();
</script>

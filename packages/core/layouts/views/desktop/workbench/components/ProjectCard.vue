<template>
  <Card title="项目" v-bind="$attrs">
    <template #extra>
      <a-button type="link" size="small">更多</a-button>
    </template>

    <template v-for="item in items" :key="item">
      <CardGrid class="!w-full !md:w-1/3">
        <span class="flex">
          <Icon :icon="item.icon" :color="item.color" size="30" />
          <span class="ml-4 text-lg">{{ item.title }}</span>
        </span>
        <div class="text-secondary mt-2 h-10 flex">{{ item.desc }}</div>
        <div class="text-secondary flex justify-between">
          <span>{{ item.group }}</span>
          <span>{{ item.date }}</span>
        </div>
      </CardGrid>
    </template>
  </Card>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Card } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { groupItems } from './Data';

  export default defineComponent({
    components: { Card, CardGrid: Card.Grid, Icon },
    setup() {
      return { items: groupItems };
    },
  } as any);
</script>

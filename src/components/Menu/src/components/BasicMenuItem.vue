<template>
  <MenuItem :key="item.path" v-bind="getMenuItem">
    <MenuItemContent v-bind="$props" :item="item" />
  </MenuItem>
</template>
<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { Menu } from 'ant-design-vue';
  import { itemProps } from '../props';
  import { omit } from 'lodash-es';
  import MenuItemContent from './MenuItemContent.vue';

  export default defineComponent({
    name: 'BasicMenuItem',
    components: { MenuItem: Menu.Item, MenuItemContent },
    props: itemProps,
    setup(props) {
      const getMenuItem = computed(() => {
        return omit(props.item, 'children', 'icon', 'title', 'color', 'extend');
      });
      return { getMenuItem };
    },
  });
</script>

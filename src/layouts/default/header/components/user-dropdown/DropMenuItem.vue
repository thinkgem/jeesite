<template>
  <MenuItem :key="itemKey">
    <span class="flex items-center">
      <Icon v-if="icon" :icon="icon" class="mr-1" />
      <span>{{ text }}</span>
      <slot name="menuItemAfter"></slot>
    </span>
  </MenuItem>
</template>
<script lang="ts">
  import { Menu } from 'ant-design-vue';

  import { computed, defineComponent, getCurrentInstance } from 'vue';

  import Icon from '/@/components/Icon';
  import { propTypes } from '/@/utils/propTypes';

  export default defineComponent({
    name: 'DropdownMenuItem',
    components: { MenuItem: Menu.Item, Icon },
    props: {
      value: propTypes.string,
      text: propTypes.string,
      icon: propTypes.string,
    },
    setup(props) {
      const instance = getCurrentInstance();
      const itemKey = computed(() => props.value || instance?.vnode?.props?.value);
      return { itemKey };
    },
  });
</script>

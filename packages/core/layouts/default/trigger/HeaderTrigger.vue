<template>
  <span :class="['jeesite-layout-header-trigger', theme]" @click="toggleCollapsed">
    <MenuUnfoldOutlined v-if="getCollapsed" /> <MenuFoldOutlined v-else />
  </span>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { MenuUnfoldOutlined, MenuFoldOutlined } from '@antdv-next/icons';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { propTypes } from '@jeesite/core/utils/propTypes';

  export default defineComponent({
    name: 'HeaderTrigger',
    components: { MenuUnfoldOutlined, MenuFoldOutlined },
    props: {
      theme: propTypes.oneOf(['light', 'dark']),
    },
    setup() {
      const { getCollapsed, toggleCollapsed } = useMenuSetting();
      return { getCollapsed, toggleCollapsed };
    },
  });
</script>

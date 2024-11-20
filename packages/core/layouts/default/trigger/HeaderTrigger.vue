<template>
  <span :class="[prefixCls, theme]" @click="toggleCollapsed">
    <MenuUnfoldOutlined v-if="getCollapsed" /> <MenuFoldOutlined v-else />
  </span>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons-vue';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { propTypes } from '@jeesite/core/utils/propTypes';

  export default defineComponent({
    name: 'HeaderTrigger',
    components: { MenuUnfoldOutlined, MenuFoldOutlined },
    props: {
      theme: propTypes.oneOf(['light', 'dark']),
    },
    setup() {
      const { getCollapsed, toggleCollapsed } = useMenuSetting();
      const { prefixCls } = useDesign('layout-header-trigger');
      return { getCollapsed, toggleCollapsed, prefixCls };
    },
  });
</script>

<template>
  <div :class="['jeesite-layout-content', getLayoutContentMode]" v-loading="getOpenPageLoading && getPageLoading">
    <PageLayout />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import PageLayout from '@jeesite/core/layouts/page/index.vue';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useTransitionSetting } from '@jeesite/core/hooks/setting/useTransitionSetting';
  import { useContentViewHeight } from './useContentViewHeight';

  export default defineComponent({
    name: 'LayoutContent',
    components: { PageLayout },
    setup() {
      const { getOpenPageLoading } = useTransitionSetting();
      const { getLayoutContentMode, getPageLoading } = useRootSetting();

      useContentViewHeight();
      return {
        getOpenPageLoading,
        getLayoutContentMode,
        getPageLoading,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-layout-content {
    position: relative;
    flex: 1 1 auto;
    min-height: 0;
    padding: 12px 12px 0;
    background-color: @content-bg;

    &.fixed {
      width: 1200px;
      margin: 0 auto;
    }

    &-loading {
      position: absolute;
      top: 200px;
      z-index: @page-loading-z-index;
    }
  }

  html[data-theme='dark'] {
    .jeesite-layout-content {
      background: #000;
    }
  }
</style>

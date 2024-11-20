<template>
  <div :class="[prefixCls, getLayoutContentMode]" v-loading="getOpenPageLoading && getPageLoading">
    <PageLayout />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import PageLayout from '@jeesite/core/layouts/page/index.vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useTransitionSetting } from '@jeesite/core/hooks/setting/useTransitionSetting';
  import { useContentViewHeight } from './useContentViewHeight';

  export default defineComponent({
    name: 'LayoutContent',
    components: { PageLayout },
    setup() {
      const { prefixCls } = useDesign('layout-content');
      const { getOpenPageLoading } = useTransitionSetting();
      const { getLayoutContentMode, getPageLoading } = useRootSetting();

      useContentViewHeight();
      return {
        prefixCls,
        getOpenPageLoading,
        getLayoutContentMode,
        getPageLoading,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-layout-content';

  .@{prefix-cls} {
    position: relative;
    flex: 1 1 auto;
    min-height: 0;
    padding: 14px 14px 13px 15px;
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
    .@{prefix-cls} {
      background: transparent;
    }
  }
</style>

<template>
  <div :class="prefixCls" :style="{ width: getCalcContentWidth }">
    <div :class="`${prefixCls}__left`">
      <slot name="left"></slot>
    </div>
    <slot></slot>
    <div :class="`${prefixCls}__right`">
      <slot name="right"></slot>
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';

  export default defineComponent({
    name: 'PageFooter',
    inheritAttrs: false,
    setup() {
      const { prefixCls } = useDesign('page-footer');
      const { getCalcContentWidth } = useMenuSetting();
      return { prefixCls, getCalcContentWidth };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-page-footer';

  .@{prefix-cls} {
    position: fixed;
    right: 0;
    bottom: 0;
    z-index: @page-footer-z-index;
    display: flex;
    width: 100%;
    align-items: center;
    padding: 0 24px;
    line-height: 35.5px;
    background-color: @component-background;
    border-top: 1px solid @header-light-bottom-border-color;
    //box-shadow:
    //  0 -6px 16px -8px rgb(0 0 0 / 8%),
    //  0 -9px 28px 0 rgb(0 0 0 / 5%),
    //  0 -12px 48px 16px rgb(0 0 0 / 3%);
    transition: width 0.2s;

    &__left {
      flex: 1 1;
    }
  }
</style>

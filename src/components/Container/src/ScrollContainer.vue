<template>
  <Scrollbar ref="scrollbarRef" class="scroll-container" v-bind="$attrs">
    <slot></slot>
  </Scrollbar>
</template>
<script lang="ts" setup name="ScrollContainer">
  import { ref, unref, nextTick } from 'vue';
  import { Scrollbar, ScrollbarType } from '/@/components/Scrollbar';
  import { useScrollTo } from '/@/hooks/event/useScrollTo';

  const scrollbarRef = ref<Nullable<ScrollbarType>>(null);

  /**
   * Scroll to the specified position
   */
  function scrollTo(to: number, duration = 500) {
    const scrollbar = unref(scrollbarRef);
    if (!scrollbar) {
      return;
    }
    nextTick(() => {
      const wrap = unref(scrollbar.wrap);
      if (!wrap) {
        return;
      }
      const { start } = useScrollTo({
        el: wrap,
        to,
        duration,
      });
      start();
    });
  }

  /**
   * Scroll to the bottom
   */
  function scrollBottom() {
    const scrollbar = unref(scrollbarRef);
    if (!scrollbar) {
      return;
    }
    nextTick(() => {
      const wrap = unref(scrollbar.wrap) as any;
      if (!wrap) {
        return;
      }
      const scrollHeight = wrap.scrollHeight as number;
      const { start } = useScrollTo({
        el: wrap,
        to: scrollHeight,
      });
      start();
    });
  }

  function getScrollWrap() {
    const scrollbar = unref(scrollbarRef);
    if (!scrollbar) {
      return null;
    }
    return scrollbar.wrap;
  }

  defineExpose({
    scrollTo,
    scrollBottom,
    getScrollWrap,
  });
</script>
<style lang="less">
  .scroll-container {
    width: 100%;
    height: 100%;

    .scrollbar__wrap {
      // margin-bottom: 18px !important; 注释掉，否则 BasicModal 下面会多出一块
      overflow-x: hidden;
    }

    .scrollbar__view {
      box-sizing: border-box;
      height: 100%;
    }
  }
</style>

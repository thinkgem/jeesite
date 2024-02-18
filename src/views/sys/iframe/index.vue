<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div :class="prefixCls" :style="getWrapStyle">
    <Spin :spinning="loading" size="large" :style="getWrapStyle">
      <iframe
        :src="frameSrc"
        :class="`${prefixCls}__main ${props.frame?.name}`"
        ref="frameRef"
        @load="hideLoading"
      ></iframe>
    </Spin>
  </div>
</template>
<script lang="ts" setup>
  import type { CSSProperties } from 'vue';
  import type { AppRouteRecordRaw } from '/@/router/types';
  import { ref, unref, computed, watch } from 'vue';
  import { Spin } from 'ant-design-vue';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useLayoutHeight } from '/@/layouts/default/content/useContentViewHeight';
  import { router } from '/@/router';

  const props = defineProps({
    frame: { type: Object as PropType<AppRouteRecordRaw> },
    query: { type: Object as PropType<Recordable> },
    fullHeight: { type: Boolean, default: false },
  });

  const loading = ref(true);
  const topRef = ref(50);
  const heightRef = ref(window.innerHeight);
  const frameRef = ref<HTMLFrameElement>();
  const { headerHeightRef } = useLayoutHeight();

  const { prefixCls } = useDesign('iframe-page');
  useWindowSizeFn(calcHeight, 150, { immediate: true });

  // const frameSrc = ref(props.frame?.meta?.frameSrc);
  const frameSrc = ref<string>();

  watch(
    () => router.currentRoute.value.query,
    () => {
      // jee site iframe query
      let src = props.frame?.meta?.frameSrc || '';
      let search = window.location.search;
      if (search && search != '') {
        src += search;
      }
      const path = window.location.pathname.replace(/\/\//g, '/');
      if (!frameSrc.value || (frameSrc.value != src && src.indexOf(path) != -1)) {
        frameSrc.value = src;
      }
    },
    { immediate: true },
  );

  watch(
    () => router.currentRoute.value.name,
    () => {
      // jee site iframe refresh
      let params = router.currentRoute.value.params;
      if (params && params.path == props.frame?.path) {
        let src = props.frame?.meta?.frameSrc;
        src += src?.indexOf('?') != -1 ? '&' : '?';
        frameSrc.value = src + '__t' + new Date().getTime();
      }
    },
  );

  const padding = 15; // jee site default padding

  const getWrapStyle = computed((): CSSProperties => {
    return {
      height: `${unref(heightRef) - padding}px`,
    };
  });

  function calcHeight() {
    const iframe = unref(frameRef);
    if (!iframe) {
      return;
    }
    if (props.fullHeight) {
      const clientHeight = document.documentElement.clientHeight;
      iframe.style.height = `${clientHeight}px`;
      return;
    }
    const top = headerHeightRef.value + padding;
    topRef.value = top;
    heightRef.value = window.innerHeight - top;
    const clientHeight = document.documentElement.clientHeight - top;
    iframe.style.height = `${clientHeight - padding}px`;
  }

  function hideLoading() {
    loading.value = false;
    calcHeight();
  }
</script>
<style lang="less" scoped>
  @prefix-cls: ~'jeesite-iframe-page';

  .@{prefix-cls} {
    .ant-spin-nested-loading {
      position: relative;
      height: 100%;

      .ant-spin-container {
        width: 100%;
        height: 100%;
        padding: 10px;
      }
    }

    &__mask {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
    }

    &__main {
      width: 100%;
      height: 100%;
      overflow: hidden;
      background-color: @component-background;
      border: 0;
      box-sizing: border-box;
      border-radius: 5px;
    }
  }
</style>

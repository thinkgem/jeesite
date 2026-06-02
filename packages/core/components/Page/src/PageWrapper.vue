<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div :class="getClass" ref="wrapperRef">
    <div ref="headerRef" v-if="getShowHeader" :class="['page-header', { 'page-header-ghost': ghost }]">
      <div class="page-header-heading">
        <div v-if="$slots.headerTitle" class="page-header-heading-title">
          <slot name="headerTitle"></slot>
        </div>
        <div v-else-if="title" class="page-header-heading-title">
          {{ title }}
        </div>
        <div v-if="$slots.headerSubTitle" class="page-header-heading-sub-title">
          <slot name="headerSubTitle"></slot>
        </div>
      </div>
      <div v-if="content || $slots.headerContent" class="page-header-content">
        <template v-if="content">
          {{ content }}
        </template>
        <slot v-else name="headerContent"></slot>
      </div>
    </div>
    <div :class="getContentClass" :style="getContentStyle" ref="contentRef">
      <Layout v-if="sidebar || sidebarRight" :style="getSidebarContentStyle">
        <LayoutSider
          v-if="sidebar"
          class="sidebar"
          v-model:collapsed="collapsed"
          :collapsedWidth="0"
          :collapsible="true"
          :trigger="null"
          :width="getSidebarWidth"
          breakpoint="md"
          @breakpoint="onBreakpoint"
        >
          <div class="sidebar-content" :style="getSidebarContentStyle">
            <slot name="sidebar"></slot>
          </div>
          <template v-if="!sidebarResizer">
            <div class="sidebar-close" v-if="!collapsed" @click="collapsed = !collapsed">
              <Icon icon="i-fa:angle-left" />
            </div>
            <div class="sidebar-open" v-else @click="collapsed = !collapsed">
              <Icon icon="i-fa:angle-left" style="transform: rotate(180deg)" />
            </div>
          </template>
        </LayoutSider>
        <template v-if="sidebar && sidebarResizer">
          <Resizer position="left" v-model:collapsed="collapsed" @move="onSidebarMove" />
        </template>
        <template v-if="sidebar && !sidebarResizer">
          <div v-if="!collapsed" style="margin-right: 12px"></div>
        </template>
        <LayoutContent>
          <slot></slot>
        </LayoutContent>
        <template v-if="sidebarRight && sidebarResizerRight">
          <Resizer position="right" v-model:collapsed="collapsedRight" @move="onSidebarMoveRight" />
        </template>
        <template v-if="sidebarRight && !sidebarResizerRight">
          <div v-if="!collapsedRight" style="margin-left: 12px"></div>
        </template>
        <LayoutSider
          v-if="sidebarRight"
          :reverseArrow="true"
          class="sidebar right"
          v-model:collapsed="collapsedRight"
          :collapsedWidth="0"
          :collapsible="true"
          :trigger="null"
          :width="getSidebarWidthRight"
          breakpoint="md"
          @breakpoint="onBreakpointRight"
        >
          <template v-if="!sidebarResizerRight">
            <div class="sidebar-close right" v-if="!collapsedRight" @click="collapsedRight = !collapsedRight">
              <Icon icon="i-fa:angle-right" />
            </div>
            <div class="sidebar-open right" v-else @click="collapsedRight = !collapsedRight">
              <Icon icon="i-fa:angle-right" style="transform: rotate(180deg)" />
            </div>
          </template>
          <div class="sidebar-content" :style="getSidebarContentStyle">
            <slot name="sidebarRight"></slot>
          </div>
        </LayoutSider>
      </Layout>
      <slot v-else></slot>
    </div>
    <PageFooter v-if="getShowFooter" ref="footerRef">
      <template #left>
        <slot name="leftFooter"></slot>
      </template>
      <template #right>
        <slot name="rightFooter"></slot>
      </template>
    </PageFooter>
  </div>
</template>
<script lang="ts" setup name="PageWrapper">
  import {
    computed,
    CSSProperties,
    PropType,
    provide,
    onBeforeMount,
    onUpdated,
    useSlots,
    useAttrs,
    watch,
    ref,
    unref,
    shallowRef,
  } from 'vue';
  import { useDebounceFn } from '@vueuse/core';
  import PageFooter from './PageFooter.vue';

  import { useEmitter } from '@jeesite/core/store/modules/user';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { omit } from 'lodash-es';
  import { Layout, LayoutSider, LayoutContent } from 'antdv-next';
  import { useContentHeight } from '@jeesite/core/hooks/web/useContentHeight';
  import { PageWrapperFixedHeightKey } from '..';
  import { Icon } from '@jeesite/core/components/Icon';
  import { Resizer } from '@jeesite/core/components/Resizer';

  defineOptions({
    inheritAttrs: false,
  });

  const props = defineProps({
    title: propTypes.string,
    dense: propTypes.bool,
    ghost: propTypes.bool,
    content: propTypes.string,
    contentStyle: {
      type: Object as PropType<CSSProperties>,
    },
    contentBackground: propTypes.bool.def(true),
    contentFullHeight: propTypes.bool,
    contentMinHeight: propTypes.number.def(300),
    contentMinWidth: propTypes.number.def(300), // 防止侧边栏拖拽过大，导致内容宽度过小
    contentClass: propTypes.string,
    fixedHeight: propTypes.bool.def(true),
    upwardSpace: propTypes.oneOfType([propTypes.number, propTypes.string]).def(0),

    sidebarWidth: propTypes.number.def(230),
    sidebarResizer: propTypes.bool.def(true),
    sidebarMinWidth: propTypes.number.def(160),

    sidebarWidthRight: propTypes.number.def(230),
    sidebarResizerRight: propTypes.bool.def(true),
    sidebarMinWidthRight: propTypes.number.def(160),
  });

  const slots = useSlots();
  const attrs = useAttrs();
  const emitter = useEmitter();
  const wrapperRef = shallowRef(null);
  const headerRef = shallowRef(null);
  const contentRef = shallowRef(null);
  const footerRef = shallowRef(null);

  const collapsed = ref<boolean>(false);
  const sidebar = !!slots.sidebar;
  const offsetXMoved = ref(0);

  const collapsedRight = ref<boolean>(false);
  const sidebarRight = !!slots.sidebarRight;
  const offsetXMovedRight = ref(0);

  provide(
    PageWrapperFixedHeightKey,
    computed(() => props.fixedHeight),
  );

  const getIsContentFullHeight = computed(() => {
    return props.contentFullHeight || sidebar;
  });

  const getUpwardSpace = computed(() => props.upwardSpace);
  const { redoHeight, setCompensation, contentHeight } = useContentHeight(
    getIsContentFullHeight,
    wrapperRef,
    [headerRef, footerRef],
    [contentRef],
    getUpwardSpace,
  );
  setCompensation({ useLayoutFooter: true, elements: [footerRef] });

  const getClass = computed(() => {
    return [
      'jeesite-page-wrapper',
      {
        ['jeesite-page-wrapper--dense']: props.dense || sidebar,
      },
      attrs.class ?? {},
    ];
  });

  // const getHeaderSlots = computed(() => {
  //   return Object.keys(omit(slots, 'default', 'leftFooter', 'rightFooter', 'headerContent'));
  // });

  const getShowHeader = computed(() => {
    return (
      props.title !== 'false' &&
      (props.title || slots.headerTitle || props.content || slots.headerContent) /*|| getHeaderSlots.value.length*/
    );
  });

  const getShowFooter = computed(() => slots?.leftFooter || slots?.rightFooter);

  const getContentStyle = computed((): CSSProperties => {
    const { contentFullHeight, contentMinHeight, contentStyle, fixedHeight } = props;
    const h = (unref(contentHeight) || 800) - (!sidebar ? -12 : 0);
    const height = `${h < contentMinHeight ? contentMinHeight : h}px`;

    if (sidebar) {
      return {
        ...contentStyle,
        minHeight: height,
      };
    } else if (contentFullHeight) {
      return {
        ...contentStyle,
        minHeight: height,
        ...(fixedHeight || sidebar ? { height } : {}),
      };
    }

    return { ...contentStyle };
  });

  const getSidebarContentHeight = ref(0);
  const getSidebarContentStyle = computed((): CSSProperties => {
    if (getSidebarContentHeight.value <= 0) return {};
    return {
      height: `${getSidebarContentHeight.value}px`,
      minHeight: `${getSidebarContentHeight.value}px`,
    };
  });

  // 自适应侧边栏高度 by think gem
  function calcSidebarContentHeight() {
    const { contentFullHeight, contentMinHeight } = props;
    if (contentFullHeight && contentHeight.value) {
      const height = contentHeight.value - 14;
      getSidebarContentHeight.value = height < contentMinHeight ? contentMinHeight : height;
      return;
    }
    let height = 0;
    const el = unref(contentRef) as any;
    if (!el || el.clientHeight <= 0) return;
    const table = el.querySelector('.jeesite-basic-table');
    if (table) {
      height = table.clientHeight;
    }
    if (height <= 0) {
      const content = el.querySelector('.ant-layout-content');
      if (content && content.children && content.children.length > 0) {
        height = content.children[0].clientHeight;
      }
    }
    const mainContentHeight = contentHeight.value || 0;
    if (height < mainContentHeight) {
      height = mainContentHeight - 12;
    }
    // console.log('calcSidebarContentHeight', height);
    getSidebarContentHeight.value = height;
  }

  if (sidebar) {
    onBeforeMount(() => {
      emitter.on('on-page-wrapper-resize', () => {
        setTimeout(calcSidebarContentHeight, 500);
      });
    });
    onUpdated(useDebounceFn(calcSidebarContentHeight, 300));
  }

  const getContentClass = computed(() => {
    const { contentBackground, contentClass } = props;
    return [
      'jeesite-page-wrapper-content',
      contentClass,
      {
        ['jeesite-page-wrapper-content-bg']: contentBackground && !sidebar,
      },
    ];
  });

  watch(
    () => [getShowFooter.value],
    () => {
      redoHeight();
    },
    {
      flush: 'post',
      immediate: true,
    },
  );

  const lastValidOffsetX = ref(0);
  const getSidebarWidth = computed(() => {
    const contentEl = unref(contentRef) as any;
    if (!contentEl) {
      return props.sidebarWidth;
    }

    const content = contentEl.querySelector('.ant-layout-content') as HTMLElement | null;
    if (!content) {
      return props.sidebarWidth;
    }

    let tentativeWidth = props.sidebarWidth + offsetXMoved.value - 12;
    tentativeWidth = Math.max(tentativeWidth, props.sidebarMinWidth);

    // 如果当前 content 已经过窄，且用户还在试图拉宽 sidebar，则阻止
    if (content.offsetWidth <= props.contentMinWidth) {
      if (offsetXMoved.value > lastValidOffsetX.value) {
        tentativeWidth = props.sidebarWidth + lastValidOffsetX.value - 12;
        tentativeWidth = Math.max(tentativeWidth, props.sidebarMinWidth);
      } else {
        lastValidOffsetX.value = offsetXMoved.value;
      }
    } else {
      lastValidOffsetX.value = offsetXMoved.value;
    }

    return tentativeWidth;
  });

  function onBreakpoint(broken: boolean) {
    if (broken) collapsed.value = true;
  }

  function onSidebarMove(_event, offsetX: number) {
    offsetXMoved.value = offsetXMoved.value - offsetX;
  }

  const getSidebarWidthRight = computed(() => {
    const width = props.sidebarWidthRight + offsetXMovedRight.value - 12;
    return width < props.sidebarMinWidthRight ? props.sidebarMinWidthRight : width;
  });

  function onBreakpointRight(broken: boolean) {
    if (broken) collapsedRight.value = true;
  }

  function onSidebarMoveRight(_event, offsetX: number) {
    offsetXMovedRight.value = offsetXMovedRight.value + offsetX;
  }
</script>
<style lang="less">
  .jeesite-page-wrapper {
    position: relative;
    animation-duration: 0.1s;
    animation-name: fade-in;

    @keyframes fade-in {
      from {
        opacity: 0;
      }

      to {
        opacity: 1;
      }
    }

    .jeesite-page-wrapper-content {
      // margin: 16px;
      padding: 12px;
      margin-bottom: 13px;
      border-radius: 10px;
      color: @text-color-base;
      overflow-y: auto;
    }

    .page-header {
      margin-bottom: 12px;
      padding: 4px 16px;
      border-radius: 10px;
      background-color: @component-background;

      .page-header-heading-title {
        font-size: 16px;
        font-weight: normal;
        margin-right: 12px;
        line-height: 32px;
      }

      .page-header-heading-sub-title {
        font-size: 14px;
        color: @text-color-secondary;
        margin-top: 4px;
      }

      .page-header-content {
        font-size: 14px;
        color: #666;
        padding: 0 0 8px;
      }

      &.page-header-ghost {
        background-color: transparent;
      }
    }

    &-content-bg {
      background-color: @component-background;
    }

    &--dense {
      .jeesite-page-wrapper-content {
        margin: 0;
        padding: 0;
        border-radius: 0;
      }

      .page-header {
        margin: 0;
        padding: 0;
        border-radius: 0;
      }
    }

    .jeesite.ant-layout {
      background-color: @content-bg;

      .sidebar {
        background-color: @content-bg;
        transition: none;
        //min-height: 400px;

        &-content {
          // margin: 15px 0 0 15px;
          // margin-right: 15px;
          //height: calc(100% - 14px);
          height: 100%;
          overflow: hidden;
          border-radius: 10px;

          .jeesite-basic-tree {
            //&.h-full {
            //  height: calc(100% - 12px);
            //}

            &-header {
              padding: 10px 6px;
              min-height: 44px;
            }
          }
        }

        &-open,
        &-close {
          cursor: pointer;
          background: #fff;
          position: absolute;
          z-index: 100;
          top: 54px;
          width: 24px;
          height: 24px;
          text-align: center;
          transition: transform 0.3s;
          color: rgb(0 0 0 / 50%);
          border-radius: 40px;
          box-shadow:
            0 2px 8px -2px rgb(0 0 0 / 5%),
            0 1px 4px -1px rgb(25 15 15 / 7%),
            0 0 1px 0 rgb(0 0 0 / 8%);

          &:hover {
            color: rgb(0 0 0 / 80%);
          }

          svg {
            font-size: 12px;
          }
        }

        &-open {
          left: -12px;

          &.right {
            right: -12px;
          }
        }

        &-close {
          right: -12px;

          &.right {
            left: -12px;
          }
        }
      }

      //&-content {
      //  & > .h-full {
      //    height: calc(100% - 12px);
      //  }
      //}
    }
  }

  html[data-theme='dark'] {
    .jeesite-page-wrapper {
      .jeesite.ant-layout {
        background: transparent;
      }

      .page-header .page-header-content {
        color: #bbb;
      }

      .sidebar {
        background: #000 !important;

        &-open,
        &-close {
          border: 1px solid #555;
          background: transparent;
        }

        &-open {
          border-left-width: 0;
        }

        &-close {
          border-right-width: 0;
        }
      }
    }
  }
</style>

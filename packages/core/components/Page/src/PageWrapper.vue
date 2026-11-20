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
          <div v-if="$slots.extra" class="float-right">
            <slot name="extra"></slot>
          </div>
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
          v-if="sidebar && !isMobileDrawer"
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
        <template v-if="sidebar && !isMobileDrawer && sidebarResizer">
          <Resizer position="left" v-model:collapsed="collapsed" @move="onSidebarMove" />
        </template>
        <template v-if="sidebar && !isMobileDrawer && !sidebarResizer">
          <div v-if="!collapsed" style="margin-right: 6px"></div>
        </template>
        <LayoutContent>
          <slot></slot>
        </LayoutContent>
        <template v-if="sidebarRight && sidebarResizerRight">
          <Resizer position="right" v-model:collapsed="collapsedRight" @move="onSidebarMoveRight" />
        </template>
        <template v-if="sidebarRight && !sidebarResizerRight">
          <div v-if="!collapsedRight" style="margin-left: 6px"></div>
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
    <!-- 小窗口下，侧栏以抽屉方式展现 -->
    <Drawer
      v-if="isMobileDrawer"
      placement="left"
      class="sidebar-drawer"
      :size="getSidebarWidth"
      :getContainer="false"
      :open="!collapsed"
      :closable="false"
      @close="collapsed = true"
    >
      <div class="sidebar-content">
        <slot name="sidebar"></slot>
      </div>
    </Drawer>
    <div v-if="isMobileDrawer && collapsed" class="sidebar-open mobile" @click="collapsed = false">
      <Icon icon="i-fa:angle-left" style="transform: rotate(180deg)" />
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
    inject,
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
  import { Layout, LayoutSider, LayoutContent, Drawer } from 'antdv-next';
  import { useContentHeight } from '@jeesite/core/hooks/web/useContentHeight';
  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';
  import { PageWrapperFixedHeightKey, PageWrapperMobileDrawerKey } from '..';
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
    // 高度完全交给 CSS（flex 自适应），不做 JS 高度计算：
    // 用于需要「首帧就是最终高度」的页面，避免 JS 计算完成前后内容位置跳动
    cssFullHeight: propTypes.bool.def(false),
    contentMinHeight: propTypes.number.def(300),
    contentMinWidth: propTypes.number.def(300), // 防止侧边栏拖拽过大，导致内容宽度过小
    contentClass: propTypes.string,
    fixedHeight: propTypes.bool.def(true),
    upwardSpace: propTypes.oneOfType([propTypes.number, propTypes.string]).def(0),

    // 左右侧栏开关：由设计器 props.sidebar / props.sidebarRight 实时控制（响应式）
    sidebar: propTypes.bool.def(false),
    sidebarRight: propTypes.bool.def(false),

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
  const { getIsMobile } = useAppInject();
  const wrapperRef = shallowRef(null);
  const headerRef = shallowRef(null);
  const contentRef = shallowRef(null);
  const footerRef = shallowRef(null);

  const collapsed = ref<boolean>(false);
  // 优先用响应式 prop（设计器动态开关），兼容直接提供 #sidebar 插槽的用法
  // 注意：useSlots() 返回的对象非响应式，单独依赖 slots.sidebar 无法监听变化
  const sidebar = computed(() => props.sidebar || !!slots.sidebar);
  const offsetXMoved = ref(0);

  const collapsedRight = ref<boolean>(false);
  const sidebarRight = computed(() => props.sidebarRight || !!slots.sidebarRight);
  const offsetXMovedRight = ref(0);

  provide(
    PageWrapperFixedHeightKey,
    computed(() => props.fixedHeight),
  );

  // 小窗口下侧栏以抽屉方式展现：PageWrapper 嵌套时，只有最外层有侧栏的使用抽屉，
  // 内层（含祖先已占用抽屉的）保持原有的内联侧栏方式，避免多层抽屉叠加
  const parentMobileDrawer = inject(PageWrapperMobileDrawerKey, ref(false));
  const isMobileDrawer = computed(() => getIsMobile.value && sidebar.value && !unref(parentMobileDrawer));
  provide(
    PageWrapperMobileDrawerKey,
    computed(() => unref(parentMobileDrawer) || isMobileDrawer.value),
  );

  const getIsContentFullHeight = computed(() => {
    return props.contentFullHeight || sidebar.value || sidebarRight.value;
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
        ['jeesite-page-wrapper--dense']: props.dense || sidebar.value || sidebarRight.value,
        ['jeesite-page-wrapper--css-full-height']: props.cssFullHeight,
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
    // cssFullHeight：高度由 CSS flex 决定，不设 minHeight，避免首帧与计算后的高度差造成内容位置跳动
    if (props.cssFullHeight) {
      return { ...contentStyle, marginBottom: 0 };
    }
    const h = (unref(contentHeight) || 800) - (!(sidebar.value || sidebarRight.value) ? -13 : 0);
    const height = `${h < contentMinHeight ? contentMinHeight : h}px`;

    if (sidebar.value || sidebarRight.value) {
      return {
        ...contentStyle,
        minHeight: height,
        marginBottom: 0,
      };
    } else if (contentFullHeight) {
      return {
        ...contentStyle,
        minHeight: height,
        ...(fixedHeight || sidebar.value || sidebarRight.value ? { height } : {}),
        marginBottom: 0,
      };
    }

    return { ...contentStyle };
  });

  const sidebarContentHeight = ref(0);
  const getSidebarContentStyle = computed((): CSSProperties => {
    // cssFullHeight 由 CSS 撑满，不用 JS 计算的高度，避免出现两套高度
    if (props.cssFullHeight) return {};
    if (sidebarContentHeight.value <= 0) return {};
    return {
      height: `${sidebarContentHeight.value}px`,
      minHeight: `${sidebarContentHeight.value}px`,
    };
  });

  // 自适应侧边栏高度 by think gem
  function calcSidebarContentHeight() {
    // cssFullHeight 页面不参与 JS 高度计算
    if (props.cssFullHeight) return;
    const { contentFullHeight, contentMinHeight } = props;
    if (contentFullHeight && contentHeight.value) {
      const height = contentHeight.value - 4;
      sidebarContentHeight.value = height < contentMinHeight ? contentMinHeight : height;
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
      height = mainContentHeight - 4;
    }
    // console.log('calcSidebarContentHeight', height);
    sidebarContentHeight.value = height;
  }

  onBeforeMount(() => {
    emitter.on('on-page-wrapper-resize', () => {
      setTimeout(calcSidebarContentHeight, 500);
    });
  });
  onUpdated(useDebounceFn(calcSidebarContentHeight, 300));

  const getContentClass = computed(() => {
    const { contentBackground, contentClass } = props;
    return [
      'jeesite-page-wrapper-content',
      contentClass,
      {
        ['jeesite-page-wrapper-content-bg']: contentBackground && !(sidebar.value || sidebarRight.value),
      },
    ];
  });

  watch(
    () => [getShowFooter.value, sidebar.value, sidebarRight.value, getIsMobile.value],
    () => {
      redoHeight();
    },
    {
      flush: 'post',
      immediate: true,
    },
  );

  // 使用抽屉的层：小窗口下默认收起（抽屉关闭）；切回内联方式时还原原有状态
  let collapsedBeforeMobile = collapsed.value;
  watch(
    isMobileDrawer,
    (isDrawer) => {
      if (isDrawer) {
        collapsedBeforeMobile = collapsed.value;
        collapsed.value = true;
      } else {
        collapsed.value = collapsedBeforeMobile;
      }
    },
    {
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
      margin-bottom: 5px;
      border-radius: 10px;
      color: @text-color-base;
      overflow-y: auto;
    }

    .page-header {
      margin-bottom: 5px;
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
        //border-radius: 0;
      }

      .page-header {
        margin: 0;
        padding: 0;
        //border-radius: 0;
      }
    }

    // 高度完全交给 CSS：占满可用高度，内容区用 flex 自适应。
    // 首帧即为最终高度，不再等 JS 计算完再调整，避免内容位置闪一下
    &.jeesite-page-wrapper--css-full-height {
      display: flex;
      flex-direction: column;
      height: 100%;
      min-height: 0;

      .jeesite-page-wrapper-content {
        flex: 1 1 auto;
        height: auto;
        min-height: 0;
      }

      // 侧栏容器与内容列一起撑满内容区
      .jeesite-page-wrapper-content > .ant-layout {
        height: 100%;
        min-height: 0;
      }

      .jeesite-page-wrapper-content > .ant-layout > .ant-layout-content {
        min-height: 0;
      }
    }

    .jeesite.ant-layout {
      background-color: transparent;

      .sidebar {
        background-color: transparent;
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
      }

      //&-content {
      //  & > .h-full {
      //    height: calc(100% - 12px);
      //  }
      //}
    }

    // 侧栏展开、收起按钮（含小窗口下抽屉的打开按钮）
    .sidebar-open,
    .sidebar-close {
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      background: #fff;
      position: absolute;
      z-index: 100;
      top: 35px;
      width: 24px;
      height: 24px;
      line-height: 1;
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

    .sidebar-open {
      left: -6px;

      &.right {
        right: -6px;
      }

      // 小窗口下，抽屉方式展现，打开按钮紧贴左侧（保持圆形）
      &.mobile {
        left: -6px;
      }
    }

    .sidebar-close {
      right: -16px;

      &.right {
        left: -16px;
      }
    }

    // 小窗口下侧栏抽屉
    .sidebar-drawer {
      .ant-drawer-body {
        padding: 0;
        overflow: hidden;
      }

      .sidebar-content {
        height: 100%;
        overflow: hidden;
      }
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

      .sidebar-open,
      .sidebar-close {
        border: 1px solid #555;
        background: transparent;
        color: rgb(255 255 255 / 50%);

        &:hover {
          color: rgb(255 255 255 / 80%);
        }
      }

      .sidebar-open {
        border-left-width: 0;
      }

      .sidebar-close {
        border-right-width: 0;
      }
    }
  }
</style>

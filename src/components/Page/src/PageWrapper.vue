<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div :class="getClass" ref="wrapperRef">
    <PageHeader ref="headerRef" v-if="getShowHeader" v-bind="omit($attrs, 'class')" :ghost="ghost" :title="title">
      <template #title v-if="$slots.headerTitle">
        <slot name="headerTitle"></slot>
      </template>
      <template #subTitle v-if="$slots.headerSubTitle">
        <slot name="headerSubTitle"></slot>
      </template>
      <template #default>
        <template v-if="content">
          {{ content }}
        </template>
        <slot name="headerContent" v-else></slot>
      </template>
      <template #[item]="data" v-for="item in getHeaderSlots">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </PageHeader>
    <div :class="getContentClass" :style="getContentStyle" ref="contentRef">
      <Layout v-if="sidebar || sidebarRight" :style="getSidebarContentStyle">
        <Layout.Sider
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
        </Layout.Sider>
        <template v-if="sidebar && sidebarResizer">
          <Resizer position="left" v-model:collapsed="collapsed" @move="onSidebarMove" />
        </template>
        <template v-if="sidebar && !sidebarResizer">
          <div v-if="!collapsed" style="margin-right: 15px"></div>
        </template>
        <Layout.Content>
          <slot></slot>
        </Layout.Content>
        <template v-if="sidebarRight && sidebarResizerRight">
          <Resizer position="right" v-model:collapsed="collapsedRight" @move="onSidebarMoveRight" />
        </template>
        <template v-if="sidebarRight && !sidebarResizerRight">
          <div v-if="!collapsedRight" style="margin-left: 15px"></div>
        </template>
        <Layout.Sider
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
        </Layout.Sider>
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
  } from 'vue';
  import { useDebounceFn } from '@vueuse/core';
  import PageFooter from './PageFooter.vue';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useEmitter } from '/@/store/modules/user';
  import { propTypes } from '/@/utils/propTypes';
  import { omit } from 'lodash-es';
  import { Layout, PageHeader } from 'ant-design-vue';
  import { useContentHeight } from '/@/hooks/web/useContentHeight';
  import { PageWrapperFixedHeightKey } from '..';
  import { Icon } from '/@/components/Icon';
  import { Resizer } from '/@/components/Resizer';

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
    contentClass: propTypes.string,
    fixedHeight: propTypes.bool.def(true),
    upwardSpace: propTypes.oneOfType([propTypes.number, propTypes.string]).def(0),

    sidebarWidth: propTypes.number.def(230),
    sidebarResizer: propTypes.bool.def(true),
    sidebarMinWidth: propTypes.number.def(0),

    sidebarWidthRight: propTypes.number.def(230),
    sidebarResizerRight: propTypes.bool.def(true),
    sidebarMinWidthRight: propTypes.number.def(0),
  });

  const slots = useSlots();
  const attrs = useAttrs();
  const emitter = useEmitter();
  const wrapperRef = ref(null);
  const headerRef = ref(null);
  const contentRef = ref(null);
  const footerRef = ref(null);
  const { prefixCls } = useDesign('page-wrapper');

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
      prefixCls,
      {
        [`${prefixCls}--dense`]: props.dense || sidebar,
      },
      attrs.class ?? {},
    ];
  });

  const getHeaderSlots = computed(() => {
    return Object.keys(omit(slots, 'default', 'leftFooter', 'rightFooter', 'headerContent'));
  });

  const getShowHeader = computed(
    () =>
      props.title !== 'false' && (props.content || slots.headerContent || props.title || getHeaderSlots.value.length),
  );

  const getShowFooter = computed(() => slots?.leftFooter || slots?.rightFooter);

  const getContentStyle = computed((): CSSProperties => {
    const { contentFullHeight, contentStyle, fixedHeight } = props;
    const height = `${(unref(contentHeight) || 800) - (!sidebar ? -15 : 0)}px`;

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
    if (props.contentFullHeight && contentHeight.value) {
      const height = contentHeight.value - 14;
      getSidebarContentHeight.value = height < 300 ? 300 : height;
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
      height = mainContentHeight - 15;
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
      `${prefixCls}-content`,
      contentClass,
      {
        [`${prefixCls}-content-bg`]: contentBackground && !sidebar,
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

  const getSidebarWidth = computed(() => {
    const width = props.sidebarWidth + offsetXMoved.value - 15;
    return width < props.sidebarMinWidth ? props.sidebarMinWidth : width;
  });

  function onBreakpoint(broken: boolean) {
    if (broken) collapsed.value = true;
  }

  function onSidebarMove(_event, offsetX: number) {
    offsetXMoved.value = offsetXMoved.value - offsetX;
  }

  const getSidebarWidthRight = computed(() => {
    const width = props.sidebarWidthRight + offsetXMovedRight.value - 15;
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
  @prefix-cls: ~'jeesite-page-wrapper';

  .@{prefix-cls} {
    position: relative;

    .@{prefix-cls}-content {
      // margin: 16px;
      padding: 15px;
      margin-bottom: 13px;
      border-radius: 5px;
      color: @text-color-base;
      overflow-y: auto;
    }

    .ant-page-header {
      // margin: 16px;
      margin-bottom: 15px;
      padding: 4px 16px;
      border-radius: 5px;

      .ant-page-header-heading-title {
        font-size: 16px;
        font-weight: normal;
      }

      .ant-page-header-content {
        font-size: 14px;
        color: #666;
        padding: 0 0 8px;
      }

      .anticon {
        color: @primary-color;
      }

      &:empty {
        padding: 0;
      }
    }

    &-content-bg {
      background-color: @component-background;
    }

    &--dense {
      .@{prefix-cls}-content {
        margin: 0;
        padding: 0;
        border-radius: 0;
      }

      .ant-page-header {
        margin: 0;
        padding: 0;
        border-radius: 0;
      }
    }

    .ant-layout {
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
          border-radius: 4px;

          .jeesite-basic-tree-header {
            padding: 10px 6px;
            min-height: 44px;
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
    }
  }

  html[data-theme='dark'] {
    .@{prefix-cls} {
      .ant-layout {
        background: transparent;
      }

      .sidebar {
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

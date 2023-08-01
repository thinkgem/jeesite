<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div :class="getClass" ref="wrapperRef">
    <PageHeader
      :ghost="ghost"
      :title="title"
      v-bind="omit($attrs, 'class')"
      ref="headerRef"
      v-if="!title && (content || $slots.headerContent || title || getHeaderSlots.length)"
    >
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
      <a-layout v-if="sidebar">
        <a-layout-sider
          class="sidebar"
          v-model:collapsed="collapsed"
          :collapsedWidth="0"
          :collapsible="true"
          :trigger="null"
          :width="getSidebarWidth"
          :breakpoint="sidebarBreakpoint"
          @breakpoint="onBreakpoint"
        >
          <div class="sidebar-content" :style="getSidebarContentStyle">
            <slot name="sidebar"></slot>
          </div>
          <template v-if="!sidebarResizer">
            <div class="sidebar-close" v-if="!collapsed" @click="collapsed = !collapsed">
              <Icon icon="ant-design:double-left-outlined" />
            </div>
            <div class="sidebar-open" v-else @click="collapsed = !collapsed">
              <Icon icon="ant-design:double-right-outlined" />
            </div>
          </template>
        </a-layout-sider>
        <template v-if="sidebarResizer">
          <Resizer position="left" v-model:collapsed="collapsed" @move="onSiderMove" />
        </template>
        <template v-else>
          <div v-if="!collapsed" style="margin-right: 15px"></div>
        </template>
        <a-layout-content>
          <slot></slot>
        </a-layout-content>
      </a-layout>
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
<script lang="ts">
  import { CSSProperties, PropType, provide, onBeforeMount, onUpdated } from 'vue';

  import { defineComponent, computed, watch, ref, unref } from 'vue';
  import { useDebounceFn } from '@vueuse/core';
  import PageFooter from './PageFooter.vue';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useEmitter } from '/@/store/modules/user';
  import { propTypes } from '/@/utils/propTypes';
  import { omit } from 'lodash-es';
  import { PageHeader } from 'ant-design-vue';
  import { useContentHeight } from '/@/hooks/web/useContentHeight';
  import { PageWrapperFixedHeightKey } from '..';
  import { Icon } from '/@/components/Icon';
  import { Resizer } from '/@/components/Resizer';

  export default defineComponent({
    name: 'PageWrapper',
    components: { PageFooter, PageHeader, Icon, Resizer },
    inheritAttrs: false,
    props: {
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
      fixedHeight: propTypes.bool,
      upwardSpace: propTypes.oneOfType([propTypes.number, propTypes.string]).def(0),
      sidebarWidth: propTypes.number.def(230),
      sidebarResizer: propTypes.bool.def(true),
      sidebarMinWidth: propTypes.number.def(0),
      sidebarBreakpoint: propTypes.string.def('md'),
    },
    setup(props, { slots, attrs }) {
      const emitter = useEmitter();
      const wrapperRef = ref(null);
      const headerRef = ref(null);
      const contentRef = ref(null);
      const footerRef = ref(null);
      const { prefixCls } = useDesign('page-wrapper');
      const sidebar = !!slots.sidebar;
      const offsetXmoved = ref(0);

      provide(
        PageWrapperFixedHeightKey,
        computed(() => props.fixedHeight),
      );

      const getSidebarWidth = computed(() => {
        const width = props.sidebarWidth + offsetXmoved.value - 15;
        return width < props.sidebarMinWidth ? props.sidebarMinWidth : width;
      });

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

      const getShowFooter = computed(() => slots?.leftFooter || slots?.rightFooter);

      const getHeaderSlots = computed(() => {
        return Object.keys(omit(slots, 'default', 'leftFooter', 'rightFooter', 'headerContent'));
      });

      const getContentStyle = computed((): CSSProperties => {
        const { contentFullHeight, contentStyle, fixedHeight } = props;
        const height = `${(unref(contentHeight) || 800) - 15}px`;

        if (sidebar) {
          return {
            ...contentStyle,
            minHeight: height,
          };
        }

        if (contentFullHeight) {
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

      const collapsed = ref<boolean>(false);

      function onBreakpoint(broken: boolean) {
        if (broken) collapsed.value = true;
      }

      //向右移动是负数
      function onSiderMove(_event, offsetX: number) {
        offsetXmoved.value = offsetXmoved.value - offsetX;
      }

      return {
        getContentStyle,
        wrapperRef,
        headerRef,
        contentRef,
        footerRef,
        getClass,
        getHeaderSlots,
        prefixCls,
        getShowFooter,
        omit,
        getContentClass,
        getSidebarContentStyle,
        getSidebarWidth,
        sidebar,
        collapsed,
        onSiderMove,
        onBreakpoint,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-page-wrapper';

  .@{prefix-cls} {
    position: relative;

    .@{prefix-cls}-content {
      // margin: 16px;
      padding: 15px;
      border-radius: 5px;
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
        padding: 0 0 8px 0;
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
        min-height: 400px;

        &-content {
          // margin: 15px 0 0 15px;
          // margin-right: 15px;
          height: calc(100% - 29px);
        }

        &-open,
        &-close {
          cursor: pointer;
          border: 1px solid #ddd;
          padding: 1px 1px 2.5px;
          background: #fff;
          position: absolute;
          z-index: 100;
          opacity: 0.7;
          top: 55px;
        }

        &-open {
          left: -15px;
          padding-right: 0;
          border-left-width: 0;
          border-radius: 0 3px 3px 0;
        }

        &-close {
          right: 0;
          border-right-width: 0;
          border-radius: 3px 0 0 3px;
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

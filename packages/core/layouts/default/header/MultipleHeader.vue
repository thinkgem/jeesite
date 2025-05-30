<template>
  <div :style="getPlaceholderDomStyle" v-if="getIsShowPlaceholderDom"></div>
  <div :style="getWrapStyle" :class="getClass">
    <LayoutHeader v-if="getShowInsetHeaderRef" />
    <MultipleTabs v-if="getShowTabs" v-show="getShowTabs2" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, unref, computed, CSSProperties } from 'vue';

  import LayoutHeader from './index.vue';
  import MultipleTabs from '../tabs/index.vue';

  import { useHeaderSetting } from '@jeesite/core/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { useFullContent } from '@jeesite/core/hooks/web/useFullContent';
  import { useMultipleTabSetting } from '@jeesite/core/hooks/setting/useMultipleTabSetting';
  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useLayoutHeight } from '../content/useContentViewHeight';
  import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';

  const HEADER_HEIGHT = 48;
  const TABS_HEIGHT = 32;
  const TABS_HEIGHT_LARGE = 37;

  export default defineComponent({
    name: 'LayoutMultipleHeader',
    components: { LayoutHeader, MultipleTabs },
    setup() {
      const { setHeaderHeight } = useLayoutHeight();
      const { prefixCls } = useDesign('layout-multiple-header');

      const { getCalcContentWidth, getSplit } = useMenuSetting();
      const { getIsMobile } = useAppInject();
      const { getFixed, getShowInsetHeaderRef, getShowFullHeaderRef, getHeaderTheme, getShowHeader } =
        useHeaderSetting();

      const { getFullContent } = useFullContent();

      const { getShowMultipleTab, getTabsStyle } = useMultipleTabSetting();
      const tabStore = useMultipleTabStore();

      const getShowTabs = computed(() => {
        return unref(getShowMultipleTab) && !unref(getFullContent);
      });

      const getShowTabs2 = computed(() => {
        return tabStore.getTabList.length > 1;
      });

      const getIsShowPlaceholderDom = computed(() => {
        return unref(getFixed) || unref(getShowFullHeaderRef);
      });

      const getWrapStyle = computed((): CSSProperties => {
        const style: CSSProperties = {};
        if (unref(getFixed)) {
          style.width = unref(getIsMobile) ? '100%' : unref(getCalcContentWidth);
        }
        if (unref(getShowFullHeaderRef)) {
          style.top = `${HEADER_HEIGHT}px`;
        }
        return style;
      });

      const getIsFixed = computed(() => {
        return unref(getFixed) || unref(getShowFullHeaderRef);
      });

      const getPlaceholderDomStyle = computed((): CSSProperties => {
        let height = 0;
        if ((unref(getShowFullHeaderRef) || !unref(getSplit)) && unref(getShowHeader) && !unref(getFullContent)) {
          height += HEADER_HEIGHT;
        }
        if (unref(getShowMultipleTab) && !unref(getFullContent) && unref(getShowTabs2)) {
          if (unref(getTabsStyle) == '3') {
            height += TABS_HEIGHT_LARGE;
          } else {
            height += TABS_HEIGHT;
          }
        }
        setHeaderHeight(height);
        return {
          height: `${height}px`,
        };
      });

      const getClass = computed(() => {
        return [prefixCls, `${prefixCls}--${unref(getHeaderTheme)}`, { [`${prefixCls}--fixed`]: unref(getIsFixed) }];
      });

      return {
        getClass,
        prefixCls,
        getPlaceholderDomStyle,
        getIsFixed,
        getWrapStyle,
        getIsShowPlaceholderDom,
        getShowTabs,
        getShowTabs2,
        getShowInsetHeaderRef,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-layout-multiple-header';

  .@{prefix-cls} {
    transition: width 0.2s;
    flex: 0 0 auto;

    &--dark {
      margin-left: -1px;
    }

    &--fixed {
      position: fixed;
      top: 0;
      z-index: @multiple-tab-fixed-z-index;
      width: 100%;
    }
  }
</style>

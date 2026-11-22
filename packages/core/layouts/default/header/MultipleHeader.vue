<template>
  <div :style="getPlaceholderDomStyle" v-if="getIsShowPlaceholderDom"></div>
  <div :style="getWrapStyle" :class="getClass">
    <LayoutHeader v-if="getShowInsetHeaderRef" />
    <MultipleTabs v-if="getShowTabs" v-show="getShowTabs2" />
  </div>
</template>
<script lang="ts" setup name="LayoutMultipleHeader">
  import { unref, computed, CSSProperties } from 'vue';

  import LayoutHeader from './index.vue';
  import MultipleTabs from '../tabs/index.vue';

  import { useHeaderSetting } from '@jeesite/core/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { useFullContent } from '@jeesite/core/hooks/web/useFullContent';
  import { useMultipleTabSetting } from '@jeesite/core/hooks/setting/useMultipleTabSetting';
  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';
  import { useLayoutHeight } from '../content/useContentViewHeight';
  import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';

  const HEADER_HEIGHT = 48;
  const TABS_HEIGHT = 28;
  const TABS_HEIGHT_LARGE = 34;

  const { setHeaderHeight } = useLayoutHeight();

  const { getCalcContentWidth } = useMenuSetting();
  const { getIsMobile } = useAppInject();
  const { getFixed, getShowInsetHeaderRef, getShowFullHeaderRef, getHeaderTheme, getShowHeader } = useHeaderSetting();

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
    // 顶栏只有脱离文档流（固定定位）时才需要占位：
    // 全宽顶栏（顶部菜单混合模式）始终固定；内嵌顶栏随 multiple-header 容器一起固定。
    // 不能用 split 判断，否则顶部菜单/门户等布局在 split=1 时会漏算顶栏高度，导致内容被顶栏遮挡。
    const needHeaderHeight = unref(getShowFullHeaderRef) || (unref(getShowInsetHeaderRef) && unref(getIsFixed));
    if (needHeaderHeight && unref(getShowHeader) && !unref(getFullContent)) {
      height += HEADER_HEIGHT;
    }
    if (unref(getShowMultipleTab) && !unref(getFullContent) && unref(getShowTabs2)) {
      if (['3', '4', '5'].includes(unref(getTabsStyle))) {
        height += TABS_HEIGHT_LARGE;
      } else {
        height += TABS_HEIGHT;
      }
    }
    setHeaderHeight(height);
    return {
      height: `${height}px`,
      // 占位块不允许被弹性布局压缩，避免内容上移被固定顶栏遮挡
      flexShrink: 0,
    };
  });

  const getClass = computed(() => {
    return [
      'jeesite-layout-multiple-header',
      `jeesite-layout-multiple-header--${unref(getHeaderTheme)}`,
      { ['jeesite-layout-multiple-header--fixed']: unref(getIsFixed) },
    ];
  });
</script>
<style lang="less">
  .jeesite-layout-multiple-header {
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

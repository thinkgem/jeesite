<template>
  <StyleProvider hash-priority="high" :transformers="[legacyLogicalPropertiesTransformer as any]">
    <ConfigProvider :locale="getAntdLocale" :theme="getTheme" :wave="{ disabled: false }">
      <App class="h-full">
        <slot></slot>
      </App>
    </ConfigProvider>
  </StyleProvider>
</template>
<script lang="ts" setup name="AppProvider">
  import { computed, unref, ref } from 'vue';
  import { App, StyleProvider, ConfigProvider, theme } from 'antdv-next';
  import { legacyLogicalPropertiesTransformer } from '@antdv-next/cssinjs';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useLocale } from '@jeesite/core/locales/useLocale';
  import { useTitle } from '@jeesite/core/hooks/web/useTitle';
  import { darkPrimaryColor } from '@jeesite/vite/theme/themeConfig';
  import { createAppProviderContext } from './useAppContext';
  import { createBreakpointListen } from '@jeesite/core/hooks/event/useBreakpoint';
  import { ThemeEnum } from '@jeesite/core/enums/appEnum';
  import 'dayjs/locale/zh-cn';

  const isMobile = ref(false);

  const { getAntdLocale } = useLocale();
  const { getDarkMode, getThemeColor } = useRootSetting();

  const getTheme = computed(() => {
    const isDark = unref(getDarkMode) === ThemeEnum.DARK;
    return {
      algorithm: isDark ? theme.darkAlgorithm : theme.defaultAlgorithm,
      cssVar: { key: 'jeesite' },
      token: {
        colorPrimary: isDark ? darkPrimaryColor : unref(getThemeColor),
        colorLink: isDark ? darkPrimaryColor : unref(getThemeColor),
        colorInfo: isDark ? darkPrimaryColor : unref(getThemeColor),
      },
    };
  });

  // Monitor screen breakpoint information changes
  createBreakpointListen(({ screenMap, sizeEnum, width }) => {
    const lgWidth = screenMap.get(sizeEnum.LG);
    if (lgWidth) {
      isMobile.value = width.value - 1 < lgWidth;
    }
  });

  // Inject variables into the global
  createAppProviderContext({ isMobile });

  // Listening to page changes and dynamically changing site titles
  useTitle();
</script>
<style lang="less">
  body {
    line-height: 1.5715;
    font-family:
      -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif,
      'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
  }
</style>

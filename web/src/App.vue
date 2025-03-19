<template>
  <StyleProvider hash-priority="high" :transformers="[legacyLogicalPropertiesTransformer]">
    <ConfigProvider :locale="getAntdLocale" :theme="getTheme">
      <AppProvider prefixCls="jeesite">
        <RouterView />
      </AppProvider>
    </ConfigProvider>
  </StyleProvider>
</template>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import {
    StyleProvider,
    legacyLogicalPropertiesTransformer,
    ConfigProvider,
    theme,
  } from 'ant-design-vue';
  import { AppProvider } from '@jeesite/core/components/Application';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { ThemeEnum } from '@jeesite/core/enums/appEnum';
  import { useLocale } from '@jeesite/core/locales/useLocale';
  import { useTitle } from '@jeesite/core/hooks/web/useTitle';
  import { darkPrimaryColor } from '@jeesite/build/theme/themeConfig';
  import 'dayjs/locale/zh-cn';

  // support Multi-language
  const { getAntdLocale } = useLocale();
  const { getDarkMode, getThemeColor } = useRootSetting();

  const getTheme = computed(() => {
    const isDark = unref(getDarkMode) === ThemeEnum.DARK;
    return {
      algorithm: isDark ? theme.darkAlgorithm : theme.defaultAlgorithm,
      token: {
        colorPrimary: isDark ? darkPrimaryColor : unref(getThemeColor),
        colorLink: isDark ? darkPrimaryColor : unref(getThemeColor),
        colorInfo: isDark ? darkPrimaryColor : unref(getThemeColor),
      },
    };
  });

  // Listening to page changes and dynamically changing site titles
  useTitle();
</script>
<style lang="less">
  body {
    line-height: 1.5715;
    font-family:
      -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans',
      sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
  }
</style>

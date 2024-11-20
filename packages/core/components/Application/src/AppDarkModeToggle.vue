<template>
  <div v-if="getShowDarkModeToggle" :class="getClass" @click="toggleDarkMode">
    <div :class="`${prefixCls}-inner`"></div>
    <Icon icon="i-svg:sun" size="14" />
    <Icon icon="i-svg:moon" size="14" />
  </div>
</template>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import {
    updateHeaderBgColor,
    updateSidebarBgColor,
  } from '@jeesite/core/logics/theme/updateBackground';
  import { updateDarkTheme } from '@jeesite/core/logics/theme/dark';
  import { ThemeEnum } from '@jeesite/core/enums/appEnum';

  const { prefixCls } = useDesign('dark-switch');
  const { getDarkMode, setDarkMode, getShowDarkModeToggle } = useRootSetting();

  const isDark = computed(() => getDarkMode.value === ThemeEnum.DARK);

  const getClass = computed(() => [
    prefixCls,
    {
      [`${prefixCls}--dark`]: unref(isDark),
    },
  ]);

  function toggleDarkMode() {
    const darkMode = getDarkMode.value === ThemeEnum.DARK ? ThemeEnum.LIGHT : ThemeEnum.DARK;
    setDarkMode(darkMode);
    updateDarkTheme(darkMode);
    updateHeaderBgColor();
    updateSidebarBgColor();
  }
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-dark-switch';

  html[data-theme='dark'] {
    .@{prefix-cls} {
      border: 1px solid rgb(196 188 188);
    }
  }

  .@{prefix-cls} {
    z-index: 10 !important;
    position: relative !important;
    display: flex;
    width: 50px;
    height: 26px;
    padding: 0 6px;
    margin-left: auto;
    cursor: pointer;
    background-color: #151515;
    border-radius: 30px;
    justify-content: space-between;
    align-items: center;

    &-inner {
      position: absolute;
      z-index: 1;
      width: 18px;
      height: 18px;
      background-color: #fff;
      border-radius: 50%;
      transition:
        transform 0.5s,
        background-color 0.5s;
      will-change: transform;
    }

    &--dark {
      .@{prefix-cls}-inner {
        transform: translateX(calc(100% + 2px));
      }
    }
  }
</style>

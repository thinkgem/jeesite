<!--
 * @Author: Vben
 * @Description: logo component
-->
<template>
  <div class="anticon" :class="getAppLogoClass" @click="goHome">
    <img :src="publicPath + '/resource/img/logo.png'" v-show="!!!showTitle" />
    <div class="ml-2 truncate md:opacity-100" :class="getTitleClass" v-show="showTitle">
      {{ getTitle }}
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { useGo } from '@jeesite/core/hooks/web/usePage';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { PageEnum } from '@jeesite/core/enums/pageEnum';
  import { useUserStore } from '@jeesite/core/store/modules/user';
  import { publicPath } from '@jeesite/core/utils/env';

  const props = defineProps({
    /**
     * The theme of the current parent component
     */
    theme: { type: String, validator: (v: string) => ['light', 'dark'].includes(v) },
    /**
     * Whether to show title
     */
    showTitle: { type: Boolean, default: true },
    /**
     * The title is also displayed when the menu is collapsed
     */
    alwaysShowTitle: { type: Boolean },
  });

  const { getCollapsedShowTitle } = useMenuSetting();
  const userStore = useUserStore();
  const go = useGo();

  const getTitle = computed(() => {
    const { title } = useGlobSetting();
    return userStore.getPageCacheByKey('title', title);
  });

  const getAppLogoClass = computed(() => [
    'jeesite-app-logo',
    props.theme,
    { 'collapsed-show-title': unref(getCollapsedShowTitle) },
  ]);

  const getTitleClass = computed(() => [
    'jeesite-app-logo__title',
    {
      'xs:opacity-0': !props.alwaysShowTitle,
    },
  ]);

  function goHome() {
    go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
  }
</script>
<style lang="less">
  .jeesite-app-logo {
    display: flex;
    align-items: center;
    padding-left: 7px;
    cursor: pointer;
    transition: all 0.2s ease;

    &.light {
      // border-bottom: 1px solid @border-color-base;
      border-bottom: 0;
    }

    &.collapsed-show-title {
      padding-left: 20px;
    }

    &.light &__title {
      color: #555;
    }

    &.dark &__title {
      color: @white;
    }

    &__title {
      font-size: 20px;
      // font-weight: bold;
      font-family: Arial, 'Microsoft YaHei', sans-serif;
      transition: all 0.5s;
    }
  }
</style>

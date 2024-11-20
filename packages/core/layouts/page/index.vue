<template>
  <RouterView>
    <template #default="{ Component, route }">
      <transition
        :name="
          getTransitionName({
            route,
            openCache,
            enableTransition: getEnableTransition,
            cacheTabs: getCaches,
            def: getBasicTransition,
          })
        "
        mode="out-in"
        appear
      >
        <keep-alive v-if="openCache" :include="getCaches">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
        <component v-else :is="Component" :key="route.fullPath" />
      </transition>
    </template>
  </RouterView>
  <FrameLayout v-if="getCanEmbedIFramePage" />
</template>

<script lang="ts">
  import { computed, defineComponent, unref } from 'vue';

  import FrameLayout from '@jeesite/core/layouts/iframe/index.vue';

  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';

  import { useTransitionSetting } from '@jeesite/core/hooks/setting/useTransitionSetting';
  import { useMultipleTabSetting } from '@jeesite/core/hooks/setting/useMultipleTabSetting';
  import { getTransitionName } from './transition';

  import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';

  export default defineComponent({
    name: 'PageLayout',
    components: { FrameLayout },
    setup() {
      const { getShowMultipleTab } = useMultipleTabSetting();
      const tabStore = useMultipleTabStore();

      const { getOpenKeepAlive, getCanEmbedIFramePage } = useRootSetting();

      const { getBasicTransition, getEnableTransition } = useTransitionSetting();

      const openCache = computed(() => unref(getOpenKeepAlive) && unref(getShowMultipleTab));

      const getCaches = computed((): string[] => {
        if (!unref(getOpenKeepAlive)) {
          return [];
        }
        return tabStore.getCachedTabList;
      });

      return {
        getTransitionName,
        openCache,
        getEnableTransition,
        getBasicTransition,
        getCaches,
        getCanEmbedIFramePage,
      };
    },
  });
</script>

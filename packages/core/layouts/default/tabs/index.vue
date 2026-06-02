<template>
  <div :class="getWrapClass">
    <Tabs
      type="editable-card"
      size="small"
      :animated="false"
      :hideAdd="true"
      :tabBarGutter="3"
      :activeKey="activeKeyRef"
      @change="handleChange"
      @edit="(e) => handleEdit(`${e}`)"
    >
      <template v-for="item in getTabsState" :key="item.query ? item.fullPath : item.path">
        <TabPane :closable="!(item && item.meta && item.meta.affix)">
          <template #tab>
            <TabContent :tabItem="item" />
          </template>
        </TabPane>
      </template>
      <template #rightExtra v-if="getShowRedo || getShowQuick || getShowFold">
        <div>
          <TabRedo v-if="getShowRedo" />
          <TabContent isExtra :tabItem="route" v-if="getShowQuick" />
          <FoldButton v-if="getShowFold" />
        </div>
      </template>
    </Tabs>
  </div>
</template>
<script lang="ts" setup name="MultipleTabs">
  import { RouteLocationNormalized, RouteMeta, useRoute } from 'vue-router';

  import { computed, unref, ref } from 'vue';

  import { Tabs, TabPane } from 'antdv-next';
  import TabContent from './components/TabContent.vue';
  import FoldButton from './components/FoldButton.vue';
  import TabRedo from './components/TabRedo.vue';

  import { useGo } from '@jeesite/core/hooks/web/usePage';

  import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';
  import { useUserStore } from '@jeesite/core/store/modules/user';

  import { initAffixTabs, useTabsDrag } from './useMultipleTabs';
  import { useMultipleTabSetting } from '@jeesite/core/hooks/setting/useMultipleTabSetting';

  import { REDIRECT_NAME } from '@jeesite/core/router/constant';
  import { listenerRouteChange } from '@jeesite/core/logics/mitt/routeChange';

  import { useRouter } from 'vue-router';

  const route = useRoute();
  const affixTextList = initAffixTabs();
  const activeKeyRef = ref('');

  useTabsDrag(affixTextList);
  const tabStore = useMultipleTabStore();
  const userStore = useUserStore();
  const router = useRouter();

  const go = useGo();
  const { getTabsStyle, getShowQuick, getShowRedo, getShowFold } = useMultipleTabSetting();

  const getTabsState = computed(() => {
    return tabStore.getTabList.filter((item) => !item.meta?.hideTab);
  });

  const unClose = computed(() => unref(getTabsState).length === 1);

  const getWrapClass = computed(() => {
    return [
      'jeesite-multiple-tabs',
      `jeesite-multiple-tabs-${unref(getTabsStyle)}`,
      {
        ['jeesite-multiple-tabs-hide-close']: unref(unClose),
      },
    ];
  });

  listenerRouteChange((route) => {
    const { name } = route;
    // if (name === REDIRECT_NAME || !route || !userStore.getToken) {
    if (name === REDIRECT_NAME || !route || userStore.getSessionTimeout) {
      return;
    }

    const { path, fullPath, meta = {} } = route;
    const { currentActiveMenu, hideTab } = meta as RouteMeta;
    const isHide = !hideTab ? null : currentActiveMenu;
    const p = isHide || fullPath || path;
    if (activeKeyRef.value !== p) {
      activeKeyRef.value = p as string;
    }

    if (isHide) {
      const findParentRoute = router.getRoutes().find((item) => item.path === currentActiveMenu);

      findParentRoute && tabStore.addTab(findParentRoute as unknown as RouteLocationNormalized);
    } else {
      tabStore.addTab(unref(route));
    }
  });

  function handleChange(activeKey: any) {
    activeKeyRef.value = activeKey;
    go(activeKey, false);
  }

  // Close the current tab
  function handleEdit(targetKey: string) {
    // Added operation to hide, currently only use delete operation
    if (unref(unClose)) {
      return;
    }

    tabStore.closeTabByKey(targetKey, router);
  }
</script>
<style lang="less">
  @import './index3.less';
</style>

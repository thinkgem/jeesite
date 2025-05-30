import type { RouteLocationNormalized, RouteLocationRaw, Router } from 'vue-router';

import { toRaw, unref } from 'vue';
import { defineStore } from 'pinia';
import { store } from '@jeesite/core/store';

import { useGo, useRedo } from '@jeesite/core/hooks/web/usePage';
import { Persistent } from '@jeesite/core/utils/cache/persistent';

import { PageEnum } from '@jeesite/core/enums/pageEnum';
import { PAGE_NOT_FOUND_ROUTE, REDIRECT_ROUTE } from '@jeesite/core/router/routes/basic';
import { getRawRoute } from '@jeesite/core/utils';
import { MULTIPLE_TABS_KEY } from '@jeesite/core/enums/cacheEnum';

import projectSetting from '@jeesite/core/settings/projectSetting';
import { useUserStore } from '@jeesite/core/store/modules/user';

export interface MultipleTabState {
  cacheTabList: Set<string>;
  tabList: RouteLocationNormalized[];
  lastDragEndIndex: number;
}

// function handleGotoPage(router: Router) {
//   const go = useGo(router);
//   go(unref(router.currentRoute).path, true);
// }

const cacheTab = projectSetting.multiTabsSetting.cache;

export const useMultipleTabStore = defineStore('app-multiple-tab', {
  state: (): MultipleTabState => ({
    // Tabs that need to be cached
    cacheTabList: new Set(),
    // multiple tab list
    tabList: cacheTab ? Persistent.getLocal(MULTIPLE_TABS_KEY) || [] : [],
    // Index of the last moved tab
    lastDragEndIndex: 0,
  }),
  getters: {
    getTabList(): RouteLocationNormalized[] {
      return this.tabList;
    },
    getCachedTabList(): string[] {
      return Array.from(this.cacheTabList);
    },
    getLastDragEndIndex(): number {
      return this.lastDragEndIndex;
    },
  },
  actions: {
    /**
     * Update the cache according to the currently opened tabs
     */
    async updateCacheTab() {
      const cacheMap: Set<string> = new Set();

      for (const tab of this.tabList) {
        const item = getRawRoute(tab);
        // Ignore the cache
        const needCache = !item.meta?.ignoreKeepAlive;
        if (!needCache) {
          continue;
        }
        const name = item.name as string;
        cacheMap.add(name);
      }
      this.cacheTabList = cacheMap;
    },

    /**
     * Refresh tabs
     */
    async refreshPage(router: Router) {
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const name = route.name;

      const findTab = this.getCachedTabList.find((item) => item === name);
      if (findTab) {
        this.cacheTabList.delete(findTab);
      }
      const redo = useRedo(router);
      await redo();
    },
    clearCacheTabs(): void {
      this.cacheTabList = new Set();
    },
    resetState(): void {
      this.tabList = [];
      this.clearCacheTabs();
    },
    goToPage(router: Router) {
      const go = useGo(router);
      const len = this.tabList.length;
      const { path } = unref(router.currentRoute);

      let toPath: PageEnum | string = PageEnum.BASE_HOME;

      if (len > 0) {
        const page = this.tabList[len - 1];
        const p = page.fullPath || page.path;
        if (p) {
          toPath = p;
        }
      }
      // Jump to the current page and report an error
      path !== toPath && go(toPath as PageEnum, true);
    },

    async addTab(route: RouteLocationNormalized) {
      const { path, name, fullPath, params, query } = getRawRoute(route);
      // 404  The page does not need to add a tab
      if (
        path === PageEnum.ERROR_PAGE ||
        path === PageEnum.BASE_LOGIN ||
        !name ||
        [REDIRECT_ROUTE.name, PAGE_NOT_FOUND_ROUTE.name].includes(name as string)
      ) {
        return;
      }

      let updateIndex = -1;
      // Existing pages, do not add tabs repeatedly
      const tabHasExits = this.tabList.some((tab, index) => {
        updateIndex = index;
        return (tab.fullPath || tab.path) === (fullPath || path);
      });

      // If the tab already exists, perform the update operation
      if (tabHasExits) {
        const curTab = toRaw(this.tabList)[updateIndex];
        if (!curTab) {
          return;
        }
        curTab.params = params || curTab.params;
        curTab.query = query || curTab.query;
        curTab.fullPath = fullPath || curTab.fullPath;
        this.tabList.splice(updateIndex, 1, curTab);
      } else {
        // Add tab
        this.tabList.push(route);
      }
      await this.updateCacheTab();
      cacheTab && Persistent.setLocal(MULTIPLE_TABS_KEY, this.tabList);
    },

    async closeTab(tab: RouteLocationNormalized, router: Router) {
      const getToTarget = (tabItem: RouteLocationNormalized) => {
        const { params, path, query } = tabItem;
        return {
          params: params || {},
          path,
          query: query || {},
        };
      };

      const close = (route: RouteLocationNormalized) => {
        const { path, fullPath, meta: { affix } = {} } = route;
        if (affix) {
          return;
        }
        const index = this.tabList.findIndex((item) => (item.fullPath || item.path) === (fullPath || path));
        index !== -1 && this.tabList.splice(index, 1);
      };

      const { currentRoute, replace } = router;

      const { path, fullPath } = unref(currentRoute);
      if ((fullPath || path) !== (tab.fullPath || tab.path)) {
        // Closed is not the activation tab
        close(tab);
        return;
      }

      // Closed is activated atb
      let toTarget: RouteLocationRaw = {};

      const index = this.tabList.findIndex((item) => (item.fullPath || item.path) === (fullPath || path));

      // If the current is the leftmost tab
      if (index === 0) {
        // There is only one tab, then jump to the homepage, otherwise jump to the right tab
        if (this.tabList.length === 1) {
          const userStore = useUserStore();
          toTarget = userStore.getUserInfo.homePath || PageEnum.BASE_HOME;
        } else {
          //  Jump to the right tab
          const page = this.tabList[index + 1];
          toTarget = getToTarget(page);
        }
      } else {
        // Close the current tab
        const page = this.tabList[index - 1];
        toTarget = getToTarget(page);
      }
      close(currentRoute.value);
      await replace(toTarget);
    },

    // Close according to key
    async closeTabByKey(key: string, router: Router) {
      const index = this.tabList.findIndex((item) => (item.fullPath || item.path) === key);
      index !== -1 && this.closeTab(this.tabList[index], router);
    },

    // Sort the tabs
    async sortTabs(oldIndex: number, newIndex: number) {
      const currentTab = this.tabList[oldIndex];
      this.tabList.splice(oldIndex, 1);
      this.tabList.splice(newIndex, 0, currentTab);
      this.lastDragEndIndex = this.lastDragEndIndex + 1;
    },

    // Close the tab on the right and jump
    async closeLeftTabs(route: RouteLocationNormalized, router: Router) {
      const index = this.tabList.findIndex((item) => (item.fullPath || item.path) === (route.fullPath || route.path));

      if (index > 0) {
        const leftTabs = this.tabList.slice(0, index);
        const pathList: string[] = [];
        for (const item of leftTabs) {
          const affix = item?.meta?.affix ?? false;
          if (!affix) {
            pathList.push(item.fullPath || item.path);
          }
        }
        await this.bulkCloseTabs(pathList);
      }
      await this.updateCacheTab();
      // handleGotoPage(router);
    },

    // Close the tab on the left and jump
    async closeRightTabs(route: RouteLocationNormalized, router: Router) {
      const index = this.tabList.findIndex((item) => (item.fullPath || item.path) === (route.fullPath || route.path));

      if (index >= 0 && index < this.tabList.length - 1) {
        const rightTabs = this.tabList.slice(index + 1, this.tabList.length);

        const pathList: string[] = [];
        for (const item of rightTabs) {
          const affix = item?.meta?.affix ?? false;
          if (!affix) {
            pathList.push(item.fullPath || item.path);
          }
        }
        await this.bulkCloseTabs(pathList);
      }
      await this.updateCacheTab();
      // handleGotoPage(router);
    },

    async closeAllTab(router: Router) {
      this.tabList = this.tabList.filter((item) => item?.meta?.affix ?? false);
      this.clearCacheTabs();
      this.goToPage(router);
    },

    /**
     * Close other tabs
     */
    async closeOtherTabs(route: RouteLocationNormalized, router: Router) {
      const closePathList = this.tabList.map((item) => item.fullPath || item.path);

      const pathList: string[] = [];

      for (const fullPath of closePathList) {
        if (fullPath !== (route.fullPath || route.path)) {
          const closeItem = this.tabList.find((item) => (item.fullPath || item.path) === fullPath);
          if (!closeItem) {
            continue;
          }
          const affix = closeItem?.meta?.affix ?? false;
          if (!affix) {
            pathList.push(closeItem.fullPath || closeItem.path);
          }
        }
      }
      await this.bulkCloseTabs(pathList);
      await this.updateCacheTab();
      // handleGotoPage(router);
    },

    /**
     * Close tabs in bulk
     */
    async bulkCloseTabs(pathList: string[]) {
      this.tabList = this.tabList.filter((item) => !pathList.includes(item.fullPath || item.path));
    },

    /**
     * Set tab's title
     */
    async setTabTitle(title: string, route: RouteLocationNormalized) {
      const findTab = this.getTabList.find((item) => item === route);
      if (findTab) {
        findTab.meta.title = title;
        await this.updateCacheTab();
      }
    },

    /**
     * replace tab's path
     */
    async updateTabPath(fullPath: string, route: RouteLocationNormalized) {
      const findTab = this.getTabList.find((item) => item === route);
      if (findTab) {
        findTab.fullPath = fullPath;
        findTab.path = fullPath;
        await this.updateCacheTab();
      }
    },
  },
});

// Need to be used outside the setup
export function useMultipleTabWithOutStore() {
  return useMultipleTabStore(store);
}

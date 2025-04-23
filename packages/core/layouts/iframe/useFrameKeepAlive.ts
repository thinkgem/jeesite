import type { AppRouteRecordRaw } from '@jeesite/core/router/types';

import { computed, toRaw, ref, unref } from 'vue';

import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';

import { uniqBy } from 'lodash-es';

import { useMultipleTabSetting } from '@jeesite/core/hooks/setting/useMultipleTabSetting';
import { useFullContent } from '@jeesite/core/hooks/web/useFullContent';

import { RouteRecordRaw, useRouter } from 'vue-router';
import { LAYOUT, IFRAME_BLANK } from '@jeesite/core/router/constant';
import { router } from '@jeesite/core/router';

const framePages = ref<AppRouteRecordRaw[]>();
const tempFramePages = ref<AppRouteRecordRaw[]>();

export function useFrameKeepAlive() {
  const router = useRouter();
  const { currentRoute } = router;
  const { getShowMultipleTab } = useMultipleTabSetting();
  const { getFullContent } = useFullContent();
  const tabStore = useMultipleTabStore();

  const getFramePages = computed(() => {
    if (!unref(framePages.value)) {
      framePages.value = getAllFramePages(toRaw(router.getRoutes()) as AppRouteRecordRaw[]);
    }
    return [...(framePages.value || []), ...(tempFramePages.value || [])];
  });

  const getOpenTabList = computed((): string[] => {
    return tabStore.getTabList.reduce((prev: string[], next) => {
      if (next.meta && Reflect.has(next.meta, 'frameSrc')) {
        prev.push(next.name as string);
      }
      return prev;
    }, []);
  });

  function getAllFramePages(routes: AppRouteRecordRaw[]): AppRouteRecordRaw[] {
    let res: AppRouteRecordRaw[] = [];
    for (const route of routes) {
      const { meta: { frameSrc } = {}, children } = route;
      if (frameSrc && !route.name.startsWith('JeeSite')) {
        res.push(route);
      }
      if (children && children.length) {
        res.push(...getAllFramePages(children));
      }
    }
    res = uniqBy(res, 'name');
    return (res || []) as AppRouteRecordRaw[];
  }

  function showIframe(item: AppRouteRecordRaw) {
    return item.name === unref(currentRoute).name;
  }

  function hasRenderFrame(name: string) {
    if (!unref(getShowMultipleTab) || unref(getFullContent)) {
      return router.currentRoute.value.name === name;
    }
    return unref(getOpenTabList).includes(name);
  }

  return { hasRenderFrame, getFramePages, showIframe, getAllFramePages };
}

export function initFramePage() {
  tempFramePages.value = JSON.parse(sessionStorage.getItem('temp-frame-pages') || '[]');
  tempFramePages.value?.forEach((r) => addFramePage(r, false));
  return addFramePage;
}

function addFramePage(route: AppRouteRecordRaw, store = true) {
  if (store) {
    let array = tempFramePages.value || [];
    if (array.length > 10) {
      array = array.slice(-10);
    }
    for (let i = array.length - 1; i >= 0; i--) {
      if (array[i].name === route.name) {
        array.splice(i, 1);
      }
    }
    array.push(route);
    tempFramePages.value = array;
    sessionStorage.setItem('temp-frame-pages', JSON.stringify(array));
  }
  router.addRoute({
    component: LAYOUT,
    meta: { single: true, affix: false },
    name: `${route.name}Parent`,
    children: [
      {
        component: IFRAME_BLANK,
        ...route,
      },
    ],
  } as unknown as RouteRecordRaw);
}

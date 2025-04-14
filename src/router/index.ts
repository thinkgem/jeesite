import type { RouteRecordRaw } from 'vue-router';
import type { App } from 'vue';

import { createRouter } from 'vue-router';
import { basicRoutes } from './routes';
import { createRouteHistory } from './helper/routeHelper';
import { useMessage } from '/@/hooks/web/useMessage';
import { useGlobSetting } from '/@/hooks/setting';
import { useTabs } from '/@/hooks/web/useTabs';
import { useGo } from '/@/hooks/web/usePage';
import { initFramePage } from '/@/layouts/iframe/useFrameKeepAlive';
import { encryptByMd5 } from '/@/utils/cipher';
// import qs from 'qs';

// 白名单应该包含基本静态路由
const WHITE_NAME_LIST: string[] = [];
const getRouteNames = (array: any[]) =>
  array.forEach((item) => {
    WHITE_NAME_LIST.push(item.name);
    getRouteNames(item.children || []);
  });
getRouteNames(basicRoutes);

// app router
export const router = createRouter({
  history: createRouteHistory(),
  routes: basicRoutes as unknown as RouteRecordRaw[],
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// reset router
export function resetRouter() {
  router.getRoutes().forEach((route) => {
    const { name } = route;
    if (name && !WHITE_NAME_LIST.includes(name as string)) {
      router.hasRoute(name) && router.removeRoute(name);
    }
  });
}

function initTabPage() {
  const { showMessage, showMessageModal } = useMessage();
  const { ctxAdminPath } = useGlobSetting();
  const addFramePage = initFramePage();
  const go = useGo(router);
  window['tabPage'] = Object.assign(window['tabPage'] || {}, {
    addTabPage: async function (_$this: any, title: string, url: string) {
      if (url && !String(url).startsWith(ctxAdminPath)) {
        await go(url);
        return;
      }
      const idx = url.indexOf('?');
      const path = (idx == -1 ? url : url.substring(0, idx)).replace(ctxAdminPath, '');
      // const paramStr = idx == -1 ? '' : url.substring(idx + 1);
      // const params = (paramStr && paramStr != '' ? qs.parse(paramStr) : {});
      const name = encryptByMd5(url).substring(5, 15);
      const route = {
        meta: { frameSrc: url, title },
        path: path + '/' + name,
        name: 'JeeSite' + name,
        url,
      };
      addFramePage(route);
      await go(route.path);
    },
    getCurrentTabPage: function (currentTabCallback: Fn) {
      const route = router.currentRoute.value;
      const frame = document.querySelector(`.jeesite-iframe-page .${route.name as string}`);
      if (frame && typeof currentTabCallback == 'function') {
        try {
          currentTabCallback(frame['contentWindow']);
        } catch (e) {
          console.error(e);
        }
      }
      return frame;
    },
    getPrevTabPage: async function (preTabCallback: Fn, isCloseCurrentTab = false) {
      const { tabStore, closeCurrent } = useTabs(router);
      const index = tabStore.getTabList.findIndex((item) => item.path === router.currentRoute.value.path);
      if (index > 1) {
        const rotue = tabStore.getTabList[index - 1];
        const frame = document.querySelector(`.jeesite-iframe-page .${rotue.name as string}`);
        if (frame && typeof preTabCallback == 'function') {
          try {
            preTabCallback(frame['contentWindow']);
          } catch (e) {
            console.error(e);
          }
        }
      }
      if (isCloseCurrentTab) {
        setTimeout(closeCurrent, 0);
      }
    },
    closeCurrentTabPage: async function (preTabCallback: Fn) {
      await this.getPrevTabPage(preTabCallback, true);
    },
  });
  window['toastr'] = Object.assign(window['toastr'] || {}, {
    options: { positionClass: {}, timeOut: undefined },
    showMessage: function (msg: string, _title?: string, type = 'info') {
      if (this.options?.positionClass == 'toast-top-full-width') {
        return showMessage('posfull:' + msg, type, this.options?.timeOut);
      }
      return showMessage(msg, type, this.options?.timeOut);
    },
    error: function (msg: string, title?: string) {
      this.showMessage(msg, title, 'error');
    },
    warning: function (msg: string, title?: string) {
      this.showMessage(msg, title, 'warning');
    },
    success: function (msg: string, title?: string) {
      this.showMessage(msg, title, 'success');
    },
    info: function (msg: string, title?: string) {
      this.showMessage(msg, title, 'info');
    },
  });
}

// config router
export function setupRouter(app: App<Element>) {
  app.use(router);
  initTabPage();
}

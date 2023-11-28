import type { RouteRecordRaw } from 'vue-router';
import type { App } from 'vue';

import { createRouter } from 'vue-router';
import { basicRoutes } from './routes';
import { createRouteHistory } from './helper/routeHelper';
// import { useGlobSetting } from '/@/hooks/setting';
// import { useTabs } from '/@/hooks/web/useTabs';
// // import { buildUUID } from '/@/utils/uuid';
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

// const { ctxAdminPath } = useGlobSetting();

// function initTabPage() {
//   window['tabPage'] = Object.assign(window['tabPage'] || {}, {
//     addTabPage: async function (_$this, title, url, _closeable, _refresh, _lazyload) {
//       const idx = url.indexOf('?');
//       const path = (idx == -1 ? url : url.substring(0, idx)).replace(ctxAdminPath, '');
//       const paramStr = idx == -1 ? '' : url.substring(idx + 1);
//       const params = (paramStr && paramStr != '' ? qs.parse(paramStr) : {}) as any;
//       // const route = {
//       //   meta: {
//       //     title: title,
//       //     frameSrc: url,
//       //     hideMenu: false,
//       //   },
//       //   path: path,
//       //   url: path,
//       //   name: buildUUID(),
//       //   component: IFRAME,
//       // } as unknown as RouteRecordRaw;
//       await router.push({
//         path: path,
//         query: params,
//       });
//       if (title) {
//         useTabs(router).setTitle(title);
//       }
//     },
//     getCurrentTabPage: function (currentTabCallback) {
//       const route = router.currentRoute.value;
//       const frame = document.querySelector(`.jeesite-iframe-page .${route.name as string}`);
//       if (frame && typeof currentTabCallback == 'function') {
//         try {
//           currentTabCallback(frame['contentWindow']);
//         } catch (e) {
//           console.error(e);
//         }
//       }
//       return frame;
//     },
//     getPrevTabPage: function (preTabCallback, isCloseCurrentTab = false) {
//       const { tabStore, closeCurrent } = useTabs(router);
//       const index = tabStore.getTabList.findIndex(
//         (item) => item.path === router.currentRoute.value.path,
//       );
//       if (index > 1) {
//         const rotue = tabStore.getTabList[index - 1];
//         const frame = document.querySelector(`.jeesite-iframe-page .${rotue.name as string}`);
//         if (frame && typeof preTabCallback == 'function') {
//           try {
//             preTabCallback(frame['contentWindow']);
//           } catch (e) {
//             console.error(e);
//           }
//         }
//       }
//       if (isCloseCurrentTab) {
//         closeCurrent();
//       }
//     },
//     closeCurrentTabPage: function (preTabCallback) {
//       this.getPrevTabPage(preTabCallback, true);
//     },
//   });
// }

// config router
export function setupRouter(app: App<Element>) {
  app.use(router);
  // initTabPage();
}

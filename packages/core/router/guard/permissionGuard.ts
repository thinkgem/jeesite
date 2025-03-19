import type { Router, RouteRecordRaw } from 'vue-router';
import { usePermissionStoreWithOut } from '@jeesite/core/store/modules/permission';
import { RootRoute } from '@jeesite/core/router/routes';
import { PageEnum } from '@jeesite/core/enums/pageEnum';
import { useUserStoreWithOut } from '@jeesite/core/store/modules/user';
import { PAGE_NOT_FOUND_ROUTE } from '@jeesite/core/router/routes/basic';

const ROOT_PATH = RootRoute.path;
const HOME_PATH = PageEnum.BASE_HOME;
const LOGIN_PATH = PageEnum.BASE_LOGIN;
const MOD_PWD_PAGE = PageEnum.MOD_PWD_PAGE;

// 白名单路由列表，无需权限即可访问的页面
const whitePathList: PageEnum[] = [LOGIN_PATH, MOD_PWD_PAGE];

export function createPermissionGuard(router: Router) {
  const userStore = useUserStoreWithOut();
  const permissionStore = usePermissionStoreWithOut();
  router.beforeEach(async (to, from, next) => {
    if (
      from.path === ROOT_PATH &&
      to.path === HOME_PATH &&
      userStore.getUserInfo.homePath &&
      userStore.getUserInfo.homePath !== HOME_PATH
    ) {
      next(userStore.getUserInfo.homePath);
      return;
    }

    // const token = userStore.getToken;
    const token = !userStore.getSessionTimeout;

    // Whitelist can be directly entered
    if (whitePathList.includes(to.path as PageEnum)) {
      // if (to.path === LOGIN_PATH && token) {
      //   const isSessionTimeout = userStore.getSessionTimeout;
      //   try {
      //     await userStore.afterLoginAction();
      //     if (!isSessionTimeout) {
      //       next((to.query?.redirect as string) || '/');
      //       return;
      //     }
      //   } catch {}
      // }
      if (to.path === MOD_PWD_PAGE) {
        try {
          await userStore.getUserInfoAction();
        } catch (error: any) {
          console.error(error);
        }
      }
      next();
      return;
    }

    // force modify password
    if (userStore.getPageCacheByKey('modifyPasswordMsg')) {
      next({
        path: MOD_PWD_PAGE,
        replace: true,
      });
      return;
    }

    // token does not exist
    if (!token) {
      // You can access without permission. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
        next();
        return;
      }

      // redirect login page
      const redirectData: { path: string; replace: boolean; query?: Recordable<string> } = {
        path: LOGIN_PATH,
        replace: true,
      };
      if (to.path) {
        redirectData.query = {
          ...redirectData.query,
          redirect: to.path,
        };
      }
      next(redirectData);
      return;
    }

    // Jump to the 404 page after processing the login
    if (
      from.path === LOGIN_PATH &&
      to.name === PAGE_NOT_FOUND_ROUTE.name &&
      to.fullPath !== (userStore.getUserInfo.homePath || HOME_PATH)
    ) {
      // 如果用户定义的 desktopUrl 是非法路径，就跳转到 404，防止无法进入系统
      next('/404/' + (userStore.getUserInfo.homePath || HOME_PATH));
      return;
    }

    // get userinfo while last fetch time is empty
    if (userStore.getLastUpdateTime === 0) {
      try {
        await userStore.getUserInfoAction();
      } catch (error: any) {
        // const err: string = error?.toString?.() ?? '';
        // if (
        //   from.fullPath === '/' &&
        //   ((error?.code === 'ECONNABORTED' && err.indexOf('timeout of') !== -1) ||
        //     err.indexOf('Network Error') !== -1)
        // ) {
        //   next(LOGIN_PATH);
        //   return;
        // }
        let path = LOGIN_PATH as string;
        if (to.path !== '/' && to.path !== LOGIN_PATH) {
          path = path + '?redirect=' + to.fullPath;
        }
        next(path);
        return;
      }
    }

    if (permissionStore.getIsDynamicAddedRoute) {
      next();
      return;
    }

    const routes = await permissionStore.buildRoutesAction();

    routes.forEach((route) => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });

    router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);

    permissionStore.setDynamicAddedRoute(true);

    if (to.name === PAGE_NOT_FOUND_ROUTE.name) {
      // 动态添加路由后，此处应当重定向到fullPath，否则会加载404页面内容
      next({ path: to.fullPath, replace: true, query: to.query });
    } else {
      const redirectPath = (from.query.redirect || to.path) as string;
      const redirect = decodeURIComponent(redirectPath);
      const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect };
      next(nextData);
    }
  });
}

import type { Router, RouteRecordRaw } from 'vue-router';
import { useUserStoreWithOut } from '@jeesite/core/store/modules/user';
import { usePermissionStoreWithOut } from '@jeesite/core/store/modules/permission';
import { PAGE_NOT_FOUND_ROUTE, RootRoute } from '@jeesite/core/router/routes/basic';
import { PageEnum } from '@jeesite/core/enums/pageEnum';

const ROOT_PATH = RootRoute.path;
const HOME_PATH = PageEnum.BASE_HOME;
const LOGIN_PATH = PageEnum.BASE_LOGIN;
const MOD_PWD_PATH = PageEnum.MOD_PWD_PAGE;

// 页面访问白名单路由列表，无需权限即可访问的页面，使用完整匹配
const whitePathList: string[] = [LOGIN_PATH, MOD_PWD_PATH];

export function createPermissionGuard(router: Router) {
  const userStore = useUserStoreWithOut();
  const permissionStore = usePermissionStoreWithOut();
  router.beforeEach(async (to, from) => {
    const homePath = userStore.getUserInfo.homePath;
    if (from.path === ROOT_PATH && to.path === HOME_PATH && homePath && homePath !== HOME_PATH) {
      return homePath;
    }

    // Whitelist can be directly entered
    if (whitePathList.includes(to.path as PageEnum)) {
      if (to.path === MOD_PWD_PATH) {
        try {
          await userStore.getUserInfoAction();
        } catch (error: any) {
          console.error(error);
        }
      }
      return;
    }

    // force modify password
    if (userStore.getPageCacheByKey('modifyPasswordMsg')) {
      return {
        path: MOD_PWD_PATH,
        replace: true,
      };
    }

    // const token = userStore.getToken;
    const token = !userStore.getSessionTimeout;

    // token does not exist
    if (!token) {
      // You can access without permission. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
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
          // 使用 encodeURIComponent 编码 fullPath，确保查询参数（如 ?procDefKey=xx&formVersion=7）
          // 不会被浏览器解析为 /login 页面的独立查询参数
          redirect: encodeURIComponent(to.fullPath),
        };
      }
      return redirectData;
    }

    // Jump to the 404 page after processing the login
    if (from.path === LOGIN_PATH && to.name === PAGE_NOT_FOUND_ROUTE.name && to.fullPath !== (homePath || HOME_PATH)) {
      // 如果用户定义的 desktopUrl 是非法路径，就跳转到 404，防止无法进入系统
      return buildNotFoundPath(homePath || HOME_PATH);
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
          // 编码 fullPath，确保查询参数不会丢失
          path = path + '?redirect=' + encodeURIComponent(to.fullPath);
        }
        return path;
      }
    }

    if (permissionStore.getIsDynamicAddedRoute) {
      // 动态路由已添加完毕，若仍未匹配到任何路由（命中的是 404 父路由），则跳转到 404 页面
      if (to.name === PAGE_NOT_FOUND_ROUTE.name) {
        return { path: buildNotFoundPath(to.path), replace: true, query: to.query, hash: to.hash };
      }
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
      // 使用 to.path + to.hash 避免 hash 丢失（to.fullPath 赋值给 path 会导致 # 被当作路径字面量）
      return { path: to.path, replace: true, query: to.query, hash: to.hash };
    } else {
      const redirectPath = (from.query.redirect || to.path) as string;
      const redirect = decodeURIComponent(redirectPath);
      // 从 redirect URL 中提取 hash（如 #section1），避免刷新后锚点丢失
      const hashIndex = redirect.indexOf('#');
      const redirectWithoutHash = hashIndex > -1 ? redirect.substring(0, hashIndex) : redirect;
      const redirectHash = hashIndex > -1 ? redirect.substring(hashIndex) : '';
      // 解析 redirect URL 中的查询参数，确保所有参数都被完整携带至目标页面
      const queryIndex = redirectWithoutHash.indexOf('?');
      if (queryIndex > -1) {
        const targetPath = redirectWithoutHash.substring(0, queryIndex);
        const targetQuery = Object.fromEntries(new URLSearchParams(redirectWithoutHash.substring(queryIndex + 1)));
        return { path: targetPath, query: targetQuery, hash: redirectHash, replace: true };
      }
      return to.path === redirectWithoutHash
        ? { ...to, replace: true }
        : {
            path: redirectWithoutHash,
            hash: redirectHash,
          };
    }
  });
}

/**
 * 组装 404 页面地址：404 页面使用 /404/ 前缀的子路由定义，确保在 Layout 布局内渲染
 * @param path 访问的原始路径
 */
function buildNotFoundPath(path: string) {
  return path.startsWith('/') ? `/404${path}` : `/404/${path}`;
}

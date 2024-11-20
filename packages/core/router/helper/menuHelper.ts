import { AppRouteModule } from '@jeesite/core/router/types';
import type { MenuModule, Menu, AppRouteRecordRaw } from '@jeesite/core/router/types';
import { findPath } from '@jeesite/core/utils/helper/treeHelper';
import { isUrl } from '@jeesite/core/utils/is';
import { RouteParams } from 'vue-router';
import { toRaw } from 'vue';

export function getAllParentPath<T = Recordable>(treeData: T[], path: string) {
  const menuList = findPath(treeData, (n) => n.path === path) as Menu[];
  return (menuList || []).map((item) => item.path);
}

function joinParentPath(menus: Menu[], parentPath = '') {
  for (let index = 0; index < menus.length; index++) {
    const menu = menus[index];
    // https://next.router.vuejs.org/guide/essentials/nested-routes.html
    // Note that nested paths that start with / will be treated as a root path.
    // This allows you to leverage the component nesting without having to use a nested URL.
    if (!(menu.path.startsWith('/') || isUrl(menu.path))) {
      // path doesn't start with /, nor is it a url, join parent path
      menu.path = `${parentPath}/${menu.path}`;
    }
    if (menu?.children?.length) {
      joinParentPath(menu.children, menu.meta?.hidePathForChildren ? parentPath : menu.path);
    }
  }
}

// Parsing the menu module
export function transformMenuModule(menuModule: MenuModule): Menu {
  const { menu } = menuModule;
  const menuList = [menu];
  joinParentPath(menuList);
  return menuList[0];
}

export function transformRouteToMenu(
  routeModuleList: AppRouteModule[],
  routerMapping = false,
  parentPath = '',
) {
  const routeList: AppRouteRecordRaw[] = [];

  routeModuleList.forEach((node) => {
    if (node.meta.hideMenu) {
      return;
    }

    const item = {
      ...(node.meta || {}),
      meta: node.meta,
      name: node.meta.title,
      path: node.path,
      target: node.target,
      ...(node.redirect ? { redirect: node.redirect } : {}),
      children: node.children || [],
    };

    if (item.children) {
      item.children = transformRouteToMenu(
        item.children,
        routerMapping,
        item.meta?.hidePathForChildren ? parentPath : item.path,
      );
    }

    if (routerMapping && item.meta.hideChildrenInMenu && typeof item.redirect === 'string') {
      item.path = item.redirect;
    }

    // https://next.router.vuejs.org/guide/essentials/nested-routes.html
    // Note that nested paths that start with / will be treated as a root path.
    // This allows you to leverage the component nesting without having to use a nested URL.
    // if (!(item.path.startsWith('/') || isUrl(item.path))) {
    //   // path doesn't start with /, nor is it a url, join parent path
    //   item.path = `${parentPath}/${item.path}`;
    // }

    if (item.meta?.single) {
      const realItem = item?.children?.[0];
      realItem && routeList.push(realItem);
    } else {
      routeList.push(item);
    }
  });
  return routeList;
}

/**
 * config menu with given params
 */
const menuParamRegex = /(?::)([\s\S]+?)((?=\/)|$)/g;
export function configureDynamicParamsMenu(menu: Menu, params: RouteParams) {
  const { path, paramPath } = toRaw(menu);
  let realPath = paramPath ? paramPath : path;
  const matchArr = realPath.match(menuParamRegex);

  matchArr?.forEach((it) => {
    const realIt = it.substr(1);
    if (params[realIt]) {
      realPath = realPath.replace(`:${realIt}`, params[realIt] as string);
    }
  });
  // save original param path.
  if (!paramPath && matchArr && matchArr.length > 0) {
    menu.paramPath = path;
  }
  menu.path = realPath;
  // children
  menu.children?.forEach((item) => configureDynamicParamsMenu(item, params));
}

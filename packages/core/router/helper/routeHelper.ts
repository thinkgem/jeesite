/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
 */
import type { AppRouteModule, AppRouteRecordRaw } from '@jeesite/core/router/types';
import type { Router, RouteRecordNormalized } from 'vue-router';

import { cloneDeep, omit } from 'lodash-es';
import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router';
import { LAYOUT, IFRAME_BLANK, IFRAME_SIMPLE, EXCEPTION_COMPONENT } from '@jeesite/core/router/constant';
import { warn, env } from '@jeesite/core/utils/log';

// Dynamic introduction
function asyncImportRoute(
  routes: AppRouteRecordRaw[] | undefined,
  parent: AppRouteRecordRaw | undefined,
  root: AppRouteRecordRaw | undefined,
) {
  if (!routes) return;
  routes.forEach((item) => {
    item.meta = item.meta || {};
    if (!item.meta.icon) {
      item.meta.icon = 'bx:bx-circle';
    }
    if (parent && item.meta.hideMenu) {
      item.meta.currentActiveMenu = parent.path;
    }
    const component = (item.component as string).toUpperCase();
    if (!component || component === 'LAYOUT') {
      item.component = LAYOUT;
    } else if (component === 'IFRAME') {
      item.component = root?.component ? IFRAME_BLANK : IFRAME_SIMPLE;
    } else {
      item.component = dynamicImport(item.component as string);
    }
    if (!item.component) {
      item.component = EXCEPTION_COMPONENT;
      item.props = item.props || {};
      item.props.status = 404;
    }
    item.children && asyncImportRoute(item.children, item, root);
  });
}

let dynamicViewsModules: Record<string, () => Promise<Recordable>>;

export function dynamicImport(component: string) {
  if (!dynamicViewsModules) {
    dynamicViewsModules = import.meta.glob('../../../../**/views/**/*.{vue,tsx}');
  }
  const keys = Object.keys(dynamicViewsModules);
  const matchKeys = keys.filter((key) => {
    const viewsPath = '/views',
      l = viewsPath.length,
      index = key.indexOf(viewsPath);
    let k = key.substring(index + l);
    const lastIndex = k.lastIndexOf('.');
    k = k.substring(0, lastIndex);
    return k === component;
  });
  if (matchKeys?.length === 1) {
    const matchKey = matchKeys[0];
    return dynamicViewsModules[matchKey];
  }
  if (matchKeys?.length > 1) {
    warn(
      'Please do not create `.vue` and `.TSX` files with the same file name in the same hierarchical directory under the views folder. This will cause dynamic introduction failure',
    );
    return;
  }
}

// Turn background objects into routing objects
export function transformObjToRoute<T = AppRouteModule>(routeList: AppRouteModule[]): T[] {
  routeList.forEach((item) => {
    const component = (item.component as string).toUpperCase();
    if (component === 'BLANK') {
      item.component = item.path;
      item.children = [cloneDeep(item)];
      item.component = undefined;
    } else {
      item.children = [cloneDeep(item)];
      item.component = LAYOUT;
    }
    item.path = '';
    item.name = `${item.name}Parent`;
    item.meta = item.meta || {};
    item.meta.single = true;
    item.meta.affix = false;
    item.children && asyncImportRoute(item.children, item, item);
  });
  return routeList as unknown as T[];
}

/**
 * Convert multi-level routing to level 2 routing
 */
export function flatMultiLevelRoutes(routeModules: AppRouteModule[]) {
  const modules: AppRouteModule[] = cloneDeep(routeModules);
  for (let index = 0; index < modules.length; index++) {
    const routeModule = modules[index];
    if (!isMultipleRoute(routeModule)) {
      continue;
    }
    promoteRouteLevel(routeModule);
  }
  return modules;
}

export function createRouteHistory() {
  if (env.VITE_ROUTE_WEB_HISTORY == 'true') {
    return createWebHistory(env.VITE_PUBLIC_PATH);
  } else {
    return createWebHashHistory(env.VITE_PUBLIC_PATH);
  }
}

// Routing level upgrade
function promoteRouteLevel(routeModule: AppRouteModule) {
  // Use vue-router to splice menus
  let router: Router | null = createRouter({
    routes: [routeModule as unknown as RouteRecordNormalized],
    history: createRouteHistory(),
  });

  const routes = router.getRoutes();
  addToChildren(routes, routeModule.children || [], routeModule);
  router = null;

  routeModule.children = routeModule.children?.map((item) => omit(item, 'children'));
}

// Add all sub-routes to the secondary route
function addToChildren(routes: RouteRecordNormalized[], children: AppRouteRecordRaw[], routeModule: AppRouteModule) {
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    const route = routes.find((item) => item.name === child.name);
    if (!route) {
      continue;
    }
    routeModule.children = routeModule.children || [];
    if (!routeModule.children.find((item) => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteModule);
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule);
    }
  }
}

// Determine whether the level exceeds 2 levels
function isMultipleRoute(routeModule: AppRouteModule) {
  if (!routeModule || !Reflect.has(routeModule, 'children') || !routeModule.children?.length) {
    return false;
  }

  const children = routeModule.children;

  let flag = false;
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    if (child.children?.length) {
      flag = true;
      break;
    }
  }
  return flag;
}

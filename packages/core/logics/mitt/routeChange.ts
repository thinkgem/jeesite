/**
 * Used to monitor routing changes to change the status of menus and tabs. There is no need to monitor the route, because the route status change is affected by the page rendering time, which will be slow
 */

import type { RouteLocationNormalized } from 'vue-router';
import { getRawRoute } from '@jeesite/core/utils';
import { mitt } from '@jeesite/core/utils/mitt';

const emitter = mitt<{
  [key]: RouteLocationNormalized;
}>();

const key = Symbol();

let lastChangeTab: RouteLocationNormalized;

export function setRouteChange(lastChangeRoute: RouteLocationNormalized) {
  const r = getRawRoute(lastChangeRoute);
  emitter.emit(key, r);
  lastChangeTab = r;
}

export function listenerRouteChange(callback: (route: RouteLocationNormalized) => void, immediate = true) {
  emitter.on(key, callback);
  immediate && lastChangeTab && callback(lastChangeTab);
}

export function removeTabChangeListener() {
  emitter.clear();
}

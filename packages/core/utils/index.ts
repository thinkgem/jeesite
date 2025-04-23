import type { RouteLocationNormalized, RouteRecordNormalized } from 'vue-router';
import type { App, Events, HTMLAttributes, Plugin } from 'vue';

import { unref, Component } from 'vue';
import { isObject } from '@jeesite/core/utils/is';

export const noop = () => {};

/**
 * @description:  Set ui mount node
 */
export function getPopupContainer(node?: HTMLElement): HTMLElement {
  return (node?.parentNode as HTMLElement) ?? document.body;
}

/**
 * Add the object as a parameter to the URL
 * @param baseUrl url
 * @param obj
 * @returns {string}
 * eg:
 *  let obj = {a: '3', b: '4'}
 *  setObjToUrlParams('www.baidu.com', obj)
 *  ==>www.baidu.com?a=3&b=4
 */
export function setObjToUrlParams(baseUrl: string, obj: any): string {
  let parameters = '';
  for (const key in obj) {
    parameters += key + '=' + encodeURIComponent(obj[key]) + '&';
  }
  parameters = parameters.replace(/&$/, '');
  return /\?$/.test(baseUrl) ? baseUrl + parameters : baseUrl.replace(/\/?$/, '?') + parameters;
}

export function deepMerge<T = any>(src: any = {}, target: any = {}): T {
  let key: string;
  for (key in target) {
    src[key] = isObject(src[key]) ? deepMerge(src[key], target[key]) : (src[key] = target[key]);
  }
  return src;
}

export function openWindow(
  url: string,
  opt?: { target?: TargetContext | string; noopener?: boolean; noreferrer?: boolean },
) {
  const { target = '__blank', noopener = true, noreferrer = true } = opt || {};
  const feature: string[] = [];
  noopener && feature.push('noopener=yes');
  noreferrer && feature.push('noreferrer=yes');
  // t h i n k g e m / j e e s i t e
  if (url && (url.endsWith('?___blank') || url.endsWith('&___blank'))) {
    url = url.substring(0, url.length - 9);
  }
  window.open(url, target, feature.join(','));
}

export function openWindowLayer(url: string, opt?: { width?: number; height?: number }) {
  const win = window as any;
  let layerWidth = opt?.width || win.$(win).width();
  if (layerWidth < 800) {
    layerWidth -= 15 * 2;
  } else {
    layerWidth -= 100 * 2;
  }
  let layerHeight = opt?.height || win.$(win).height();
  if (layerHeight < 500) {
    layerHeight -= 15 * 2;
  } else {
    layerHeight -= 25 * 2;
  }
  win.layer.open({
    type: 2,
    maxmin: true,
    shadeClose: true, // 点击背景关闭
    title: false,
    area: [layerWidth + 'px', layerHeight + 'px'],
    method: 'get',
    content: url,
  });
}

// dynamic use hook props
export function getDynamicProps<T, U>(props: T): Partial<U> {
  const ret: Recordable = {};

  Object.keys(props as any).map((key) => {
    ret[key] = unref((props as Recordable)[key]);
  });

  return ret as Partial<U>;
}

export function getRawRoute(route: RouteLocationNormalized): RouteLocationNormalized {
  if (!route) return route;
  const { matched, ...opt } = route;
  return {
    ...opt,
    matched: (matched
      ? matched.map((item) => ({
          meta: item.meta,
          name: item.name,
          path: item.path,
        }))
      : undefined) as RouteRecordNormalized[],
  };
}

export const withInstall = <T extends Component>(component: T, alias?: string) => {
  component['install'] = (app: App) => {
    app.component(component.name || component['displayName'], component);
    if (alias) {
      app.config.globalProperties[alias] = component;
    }
  };
  return component as T & Plugin & HTMLAttributes & Events;
};

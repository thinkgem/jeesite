import type { AppRouteRecordRaw } from '/@/router/types';
import { t } from '/@/hooks/web/useI18n';
import {
  REDIRECT_NAME,
  LAYOUT,
  EXCEPTION_COMPONENT,
  PAGE_NOT_FOUND_NAME,
} from '/@/router/constant';

// 404 on a page
export const PAGE_NOT_FOUND_ROUTE: AppRouteRecordRaw = {
  path: '/:path(.*)*',
  name: PAGE_NOT_FOUND_NAME,
  component: LAYOUT,
  meta: {
    title: 'ErrorPage',
    hideBreadcrumb: true,
    hideMenu: true,
  },
  children: [
    {
      path: '/404/:path(.*)*',
      name: PAGE_NOT_FOUND_NAME + '404',
      component: EXCEPTION_COMPONENT,
      meta: {
        title: '404',
        hideBreadcrumb: true,
        hideMenu: true,
      },
    },
  ],
};

export const REDIRECT_ROUTE: AppRouteRecordRaw = {
  path: '/redirect',
  component: LAYOUT,
  name: 'RedirectTo',
  meta: {
    title: REDIRECT_NAME,
    hideBreadcrumb: true,
    hideMenu: true,
  },
  children: [
    {
      path: '/redirect/:path(.*)',
      name: REDIRECT_NAME,
      component: () => import('/@/layouts/views/redirect/index.vue'),
      meta: {
        title: '',
        hideBreadcrumb: true,
      },
    },
  ],
};

export const ERROR_LOG_ROUTE: AppRouteRecordRaw = {
  path: '/errorLog',
  name: 'ErrorLog',
  component: LAYOUT,
  redirect: '/errorLog/list',
  meta: {
    title: 'ErrorLog',
    hideBreadcrumb: true,
    hideChildrenInMenu: true,
  },
  children: [
    {
      path: 'list',
      name: 'ErrorLogList',
      component: () => import('/@/layouts/views/errorLog/index.vue'),
      meta: {
        title: t('routes.basic.errorLogList'),
        hideBreadcrumb: true,
        currentActiveMenu: '/errorLog',
      },
    },
  ],
};

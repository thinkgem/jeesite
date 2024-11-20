import type { AppRouteRecordRaw, AppRouteModule } from '@jeesite/core/router/types';

import { PAGE_NOT_FOUND_ROUTE, REDIRECT_ROUTE } from '@jeesite/core/router/routes/basic';

import { mainOutRoutes } from './mainOut';
import { PageEnum } from '@jeesite/core/enums/pageEnum';
import { t } from '@jeesite/core/hooks/web/useI18n';

const modules = import.meta.glob('./modules/**/*.ts', { eager: true });

const routeModuleList: AppRouteModule[] = [];

Object.keys(modules).forEach((key) => {
  const mod = (modules as Recordable)[key].default || {};
  const modList = Array.isArray(mod) ? [...mod] : [mod];
  routeModuleList.push(...modList);
});

export const asyncRoutes = [PAGE_NOT_FOUND_ROUTE, ...routeModuleList];

export const RootRoute: AppRouteRecordRaw = {
  path: '/',
  name: 'Root',
  redirect: PageEnum.BASE_LOGIN,
  meta: {
    title: 'Root',
  },
};

export const LoginRoute: AppRouteRecordRaw = {
  path: '/login',
  name: 'Login',
  component: () => import('@jeesite/core/views/sys/login/Login.vue'),
  meta: {
    title: t('routes.basic.login'),
  },
};

const ModPwdRoute: AppRouteModule = {
  path: '/modPwd',
  name: 'ModPwd',
  component: () => import('@jeesite/core/views/sys/account/modPwd.vue'),
  meta: {
    icon: 'i-ion:key-outline',
    title: t('sys.account.modifyPwd'),
  },
};

// Basic routing without permission
export const basicRoutes = [
  LoginRoute,
  ModPwdRoute,
  RootRoute,
  ...mainOutRoutes,
  REDIRECT_ROUTE,
  PAGE_NOT_FOUND_ROUTE,
];

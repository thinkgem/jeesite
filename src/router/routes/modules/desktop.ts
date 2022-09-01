import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const desktop: AppRouteModule = {
  path: '/desktop',
  name: 'Desktop',
  component: LAYOUT,
  redirect: '/desktop/analysis',
  meta: {
    orderNo: 10,
    icon: 'ant-design:home-outlined',
    title: t('routes.dashboard.dashboard'),
  },
  children: [
    {
      path: 'analysis',
      name: 'Analysis',
      component: () => import('/@/views/sys/desktop/analysis/index.vue'),
      meta: {
        // affix: true,
        icon: 'ant-design:home-outlined',
        tabIcon: 'ant-design:home-outlined',
        title: t('routes.dashboard.analysis'),
      },
    },
    {
      path: 'workbench',
      name: 'Workbench',
      component: () => import('/@/views/sys/desktop/workbench/index.vue'),
      meta: {
        icon: 'ant-design:read-outlined',
        title: t('routes.dashboard.workbench'),
      },
    },
    {
      path: 'about',
      name: 'AboutPage',
      component: () => import('/@/views/sys/desktop/about/index.vue'),
      meta: {
        title: t('routes.dashboard.about'),
        icon: 'ant-design:tag-outlined',
        hideMenu: true,
      },
    },
  ],
};

export default desktop;

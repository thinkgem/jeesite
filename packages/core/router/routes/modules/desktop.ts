import type { AppRouteModule } from '@jeesite/core/router/types';

import { LAYOUT } from '@jeesite/core/router/constant';
import { t } from '@jeesite/core/hooks/web/useI18n';

const desktop: AppRouteModule = {
  path: '/desktop',
  name: 'Desktop',
  component: LAYOUT,
  redirect: '/desktop/analysis',
  meta: {
    orderNo: 10,
    icon: 'i-ant-design:home-outlined',
    title: t('routes.dashboard.dashboard'),
  },
  children: [
    {
      path: 'analysis',
      name: 'Analysis',
      component: () => import('@jeesite/core/layouts/views/desktop/analysis/index.vue'),
      meta: {
        // affix: true,
        icon: 'i-ant-design:home-outlined',
        tabIcon: 'i-ant-design:home-outlined',
        title: t('routes.dashboard.analysis'),
      },
    },
    {
      path: 'workbench',
      name: 'Workbench',
      component: () => import('@jeesite/core/layouts/views/desktop/workbench/index.vue'),
      meta: {
        icon: 'i-ant-design:read-outlined',
        title: t('routes.dashboard.workbench'),
      },
    },
    {
      path: 'about',
      name: 'AboutPage',
      component: () => import('@jeesite/core/layouts/views/desktop/about/index.vue'),
      meta: {
        title: t('routes.dashboard.about'),
        icon: 'i-ant-design:tag-outlined',
        hideMenu: true,
      },
    },
  ],
};

export default desktop;

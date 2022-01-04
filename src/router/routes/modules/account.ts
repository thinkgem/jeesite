import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const dashboard: AppRouteModule = {
  path: '/account',
  name: 'Account',
  component: LAYOUT,
  redirect: '/account/center',
  meta: {
    icon: 'ion:person-outline',
    title: t('sys.account.center'),
    orderNo: 100000,
  },
  children: [
    {
      path: 'center',
      name: 'AccountCenter',
      component: () => import('/@/views/sys/account/center.vue'),
      meta: {
        icon: 'ion:person-outline',
        title: t('sys.account.center'),
      },
    },
    {
      path: 'modPwd',
      name: 'AccountModPwd',
      component: () => import('/@/views/sys/account/modPwd.vue'),
      meta: {
        icon: 'ion:key-outline',
        title: t('sys.account.modifyPwd'),
      },
    },
  ],
};

export default dashboard;

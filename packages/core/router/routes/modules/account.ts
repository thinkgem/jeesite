import type { AppRouteModule } from '@jeesite/core/router/types';

import { LAYOUT } from '@jeesite/core/router/constant';
import { t } from '@jeesite/core/hooks/web/useI18n';

const account: AppRouteModule = {
  path: '/account',
  name: 'Account',
  component: LAYOUT,
  redirect: '/account/center',
  meta: {
    icon: 'i-ion:person-outline',
    title: t('sys.account.center'),
    orderNo: 100000,
  },
  children: [
    {
      path: 'center',
      name: 'AccountCenter',
      component: () => import('@jeesite/core/views/sys/account/center.vue'),
      meta: {
        icon: 'i-ion:person-outline',
        title: t('sys.account.center'),
      },
    },
    {
      path: 'modPwd',
      name: 'AccountModPwd',
      component: () => import('@jeesite/core/views/sys/account/modPwd.vue'),
      meta: {
        icon: 'i-ion:key-outline',
        title: t('sys.account.modifyPwd'),
      },
    },
  ],
};

export default account;

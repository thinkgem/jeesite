import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

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
      component: () => import('/@/views/sys/account/center.vue'),
      meta: {
        icon: 'i-ion:person-outline',
        title: t('sys.account.center'),
      },
    },
    {
      path: 'modPwd',
      name: 'AccountModPwd',
      component: () => import('/@/views/sys/account/modPwd.vue'),
      meta: {
        icon: 'i-ant-design:key-outlined',
        title: t('sys.account.modifyPwd'),
      },
    },
    {
      path: 'modPwdQuestion',
      name: 'AccountModPwdQuestion',
      component: () => import('/@/views/sys/account/modPwdQuestion.vue'),
      meta: {
        icon: 'i-ant-design:key-outlined',
        title: t('sys.account.modifyPqa'),
      },
    },
  ],
};

export default account;

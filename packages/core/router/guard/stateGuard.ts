import type { Router } from 'vue-router';
import { useAppStore } from '@jeesite/core/store/modules/app';
import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';
import { useUserStore } from '@jeesite/core/store/modules/user';
import { usePermissionStore } from '@jeesite/core/store/modules/permission';
import { PageEnum } from '@jeesite/core/enums/pageEnum';
import { removeTabChangeListener } from '@jeesite/core/logics/mitt/routeChange';

export function createStateGuard(router: Router) {
  router.afterEach((to) => {
    const tabStore = useMultipleTabStore();
    const userStore = useUserStore();
    const appStore = useAppStore();
    const permissionStore = usePermissionStore();
    // Just enter the login page and clear the authentication information
    if (to.path === PageEnum.BASE_LOGIN) {
      appStore.resetAllState();
      permissionStore.resetState();
      tabStore.resetState();
      userStore.resetState();
      removeTabChangeListener();
    }
  });
}

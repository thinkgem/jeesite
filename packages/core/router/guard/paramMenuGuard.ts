import type { Router } from 'vue-router';
import { configureDynamicParamsMenu } from '../helper/menuHelper';
import { Menu } from '../types';
import { PermissionModeEnum } from '@jeesite/core/enums/appEnum';
import { useAppStoreWithOut } from '@jeesite/core/store/modules/app';

import { usePermissionStoreWithOut } from '@jeesite/core/store/modules/permission';

export function createParamMenuGuard(router: Router) {
  const permissionStore = usePermissionStoreWithOut();
  router.beforeEach(async (to, _, next) => {
    // filter no name route
    if (!to.name) {
      next();
      return;
    }

    // menu has been built.
    if (!permissionStore.getIsDynamicAddedRoute) {
      next();
      return;
    }

    let menus: Menu[] = [];
    if (isBackMode()) {
      menus = permissionStore.getBackMenuList;
    } else if (isRouteMappingMode()) {
      menus = permissionStore.getFrontMenuList;
    }
    menus.forEach((item) => configureDynamicParamsMenu(item, to.params));

    next();
  });
}

const getPermissionMode = () => {
  const appStore = useAppStoreWithOut();
  return appStore.getProjectConfig.permissionMode;
};

const isBackMode = () => {
  return getPermissionMode() === PermissionModeEnum.BACK;
};

const isRouteMappingMode = () => {
  return getPermissionMode() === PermissionModeEnum.ROUTE_MAPPING;
};

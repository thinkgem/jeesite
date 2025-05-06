import { MenuModeEnum } from '@jeesite/core/enums/menuEnum';
import type { Menu as MenuType } from '@jeesite/core/router/types';
import type { MenuState } from './types';

import { computed, Ref, toRaw } from 'vue';

import { unref } from 'vue';
import { uniq } from 'lodash-es';
import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
import { getAllParentPath } from '@jeesite/core/router/helper/menuHelper';
import { useTimeoutFn } from '@jeesite/core/hooks/core/useTimeout';

export function useOpenKeys(
  menuState: MenuState,
  menus: Ref<MenuType[]>,
  mode: Ref<MenuModeEnum>,
  accordion: Ref<boolean>,
) {
  const { getCollapsed, getIsMixSidebar } = useMenuSetting();

  async function setOpenKeys(path: string) {
    if (mode.value === MenuModeEnum.HORIZONTAL) {
      return;
    }
    const native = unref(getIsMixSidebar);
    useTimeoutFn(
      () => {
        const menuList = toRaw(menus.value);
        // console.log('TopMenu.menuList', menuList);
        if (menuList?.length === 0) {
          menuState.openKeys = [];
          return;
        }
        let keys: string[] = getAllParentPath(menuList, path);
        // console.log('TopMenu.getAllParentPath', path, keys, menuList);
        // if (keys.length === 0) {
        //   const currentPaths = sessionStorage.getItem('temp-menu-paths');
        //   if (currentPaths) {
        //     keys = currentPaths.split(',');
        //   }
        // } else {
        //   sessionStorage.setItem('temp-menu-paths', keys.join(','));
        // }
        if (keys.length === 0) {
          return;
        }

        if (!unref(accordion)) {
          menuState.openKeys = uniq([...menuState.openKeys, ...keys]);
        } else {
          menuState.openKeys = keys;
        }
        // console.log('TopMenu.setOpenKeys', path, menuState.openKeys);
      },
      16,
      !native,
    );
  }

  const getOpenKeys = computed(() => {
    const collapse = unref(getIsMixSidebar) ? false : unref(getCollapsed);

    return collapse ? menuState.collapsedOpenKeys : menuState.openKeys;
  });

  /**
   * @description:  重置值
   */
  function resetKeys() {
    menuState.selectedKeys = [];
    menuState.openKeys = [];
  }

  type Key = string | number;

  function handleOpenChange(openKey: Key[]) {
    const openKeys = openKey as unknown as string[];
    if (unref(mode) === MenuModeEnum.HORIZONTAL || !unref(accordion) || unref(getIsMixSidebar)) {
      menuState.openKeys = openKeys;
    } else {
      // const menuList = toRaw(menus.value);
      // getAllParentPath(menuList, path);
      const rootSubMenuKeys: string[] = [];
      for (const { children, path } of unref(menus)) {
        if (children && children.length > 0) {
          rootSubMenuKeys.push(path);
        }
      }
      if (!unref(getCollapsed)) {
        const latestOpenKey = openKeys.find((key) => menuState.openKeys.indexOf(key) === -1);
        if (rootSubMenuKeys.indexOf(latestOpenKey as string) === -1) {
          menuState.openKeys = openKeys;
        } else {
          menuState.openKeys = latestOpenKey ? [latestOpenKey] : [];
        }
      } else {
        menuState.collapsedOpenKeys = openKeys;
      }
    }
  }
  return { setOpenKeys, resetKeys, getOpenKeys, handleOpenChange };
}

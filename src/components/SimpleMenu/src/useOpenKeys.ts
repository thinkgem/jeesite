import type { Menu as MenuType } from '/@/router/types';
import type { MenuState } from './types';

import { computed, Ref, toRaw } from 'vue';

import { unref } from 'vue';
import { uniq } from 'lodash-es';
import { getAllParentPath } from '/@/router/helper/menuHelper';

import { useTimeoutFn } from '/@/hooks/core/useTimeout';
import { useDebounceFn } from '@vueuse/core';

export function useOpenKeys(
  menuState: MenuState,
  menus: Ref<MenuType[]>,
  accordion: Ref<boolean>,
  mixSider: Ref<boolean>,
  collapse: Ref<boolean>,
) {
  const debounceSetOpenKeys = useDebounceFn(setOpenKeys, 50);
  async function setOpenKeys(path: string) {
    const native = !mixSider.value;
    useTimeoutFn(
      () => {
        const menuList = toRaw(menus.value);
        // console.log('SidebarMenu.menuList', menuList);
        if (menuList?.length === 0) {
          menuState.activeSubMenuNames = [];
          menuState.openNames = [];
          return;
        }
        let keys: string[] = getAllParentPath(menuList, path);
        // console.log('SidebarMenu.getAllParentPath', path, keys, menuList);
        // if (keys.length === 0) {
        //   const currentPaths = sessionStorage.getItem('temp-sidebar-paths');
        //   if (currentPaths) {
        //     keys = currentPaths.split(',');
        //   }
        // } else {
        //   sessionStorage.setItem('temp-sidebar-paths', keys.join(','));
        // }
        if (keys.length === 0) {
          return;
        }

        if (!unref(collapse)) {
          if (!unref(accordion)) {
            menuState.openNames = uniq([...menuState.openNames, ...keys]);
          } else {
            menuState.openNames = keys;
          }
        }
        menuState.activeSubMenuNames = menuState.openNames;
        // console.log('SidebarMenu.setOpenKeys', path, menuState.openNames);
      },
      30,
      native,
    );
  }

  const getOpenKeys = computed(() => {
    return menuState.openNames;
  });

  return { setOpenKeys: debounceSetOpenKeys, getOpenKeys };
}

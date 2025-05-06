import type { Menu } from '@jeesite/core/router/types';
import type { Ref } from 'vue';
import { watch, unref, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { MenuSplitTyeEnum } from '@jeesite/core/enums/menuEnum';
import { useThrottleFn } from '@vueuse/core';
import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
import { getChildrenMenus, getCurrentParentPath, getMenus, getShallowMenus } from '@jeesite/core/router/menus';
import { usePermissionStore } from '@jeesite/core/store/modules/permission';
import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';

export function useSplitMenu(splitType: Ref<MenuSplitTyeEnum>) {
  // Menu array
  const menusRef = ref<Menu[]>([]);
  const { currentRoute } = useRouter();
  const { getIsMobile } = useAppInject();
  const permissionStore = usePermissionStore();
  const { setMenuSetting, getIsHorizontal, getSplit } = useMenuSetting();

  const throttleHandleSplitLeftMenu = useThrottleFn(handleSplitLeftMenu, 50);

  const getSplitNotLeft = computed(() => unref(splitType) !== MenuSplitTyeEnum.LEFT && unref(getSplit));

  const getSplitLeft = computed(() => !unref(getSplit) || unref(splitType) !== MenuSplitTyeEnum.LEFT);

  const getSpiltTop = computed(() => unref(splitType) === MenuSplitTyeEnum.TOP);

  const getNormalType = computed(() => {
    return unref(splitType) === MenuSplitTyeEnum.NONE || !unref(getSplit);
  });

  watch(
    [() => unref(currentRoute).path, () => unref(splitType)],
    async ([path]: [string, MenuSplitTyeEnum]) => {
      if (unref(getSplitNotLeft) || unref(getIsMobile)) return;

      const { meta } = unref(currentRoute);
      const currentPath = (meta.currentActiveMenu as string) || path;
      let parentPath: string | null = await getCurrentParentPath(currentPath);
      // if (parentPath) {
      //   sessionStorage.setItem('temp-parent-path', parentPath);
      // } else {
      //   parentPath = sessionStorage.getItem('temp-parent-path');
      // }
      if (!parentPath) {
        const menus = await getMenus();
        parentPath = menus[0] && menus[0].path;
      }
      // console.log('parentPath', parentPath, path, currentActiveMenu);
      parentPath && throttleHandleSplitLeftMenu(parentPath);
    },
    {
      immediate: true,
    },
  );

  // Menu changes
  watch(
    [() => permissionStore.getLastBuildMenuTime, () => permissionStore.getBackMenuList],
    () => {
      genMenus();
    },
    {
      immediate: true,
    },
  );

  // split Menu changes
  watch(
    () => getSplit.value,
    () => {
      if (unref(getSplitNotLeft)) return;
      genMenus();
    },
  );

  // Handle left menu split
  async function handleSplitLeftMenu(parentPath: string) {
    if (unref(getSplitLeft) || unref(getIsMobile)) return;

    // spilt mode left
    const children = await getChildrenMenus(parentPath);

    if (!children || !children.length) {
      setMenuSetting({ hidden: true });
      menusRef.value = [];
      return;
    }

    setMenuSetting({ hidden: false });
    menusRef.value = children;
  }

  // get menus
  async function genMenus() {
    // normal mode
    if (unref(getNormalType) || unref(getIsMobile)) {
      menusRef.value = await getMenus();
      return;
    }

    // split-top
    if (unref(getSpiltTop)) {
      const shallowMenus = await getShallowMenus();

      menusRef.value = shallowMenus;
      return;
    }
  }

  return { menusRef };
}

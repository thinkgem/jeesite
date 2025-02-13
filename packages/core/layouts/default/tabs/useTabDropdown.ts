import type { TabContentProps } from './types';
import type { DropMenu } from '@jeesite/core/components/Dropdown';
import type { ComputedRef } from 'vue';

import { computed, unref, reactive } from 'vue';
import { MenuEventEnum } from './types';
import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';
import { RouteLocationNormalized, useRouter } from 'vue-router';
import { useTabs } from '@jeesite/core/hooks/web/useTabs';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';
import { useGo } from '@jeesite/core/hooks/web/usePage';

export function useTabDropdown(tabContentProps: TabContentProps, getIsTabs: ComputedRef<boolean>) {
  const state = reactive({
    current: null as Nullable<RouteLocationNormalized>,
    currentIndex: 0,
  });

  const { t } = useI18n();
  const tabStore = useMultipleTabStore();
  const { currentRoute } = useRouter();
  const { refreshPage, closeAll, close, closeLeft, closeOther, closeRight } = useTabs();

  const getTargetTab = computed((): RouteLocationNormalized => {
    return unref(getIsTabs) ? tabContentProps.tabItem : unref(currentRoute);
  });

  const go = useGo();

  /**
   * @description: drop-down list
   */
  const getDropMenuList = computed(() => {
    if (!unref(getTargetTab)) {
      return;
    }
    const { meta } = unref(getTargetTab);
    const { path } = unref(currentRoute);

    // Refresh button
    const curItem = state.current;
    const index = state.currentIndex;
    const refreshDisabled = curItem ? curItem.path !== path : true;
    // Close left
    const closeLeftDisabled = index === 0;

    const disabled = tabStore.getTabList.length === 1;

    // Close right
    const closeRightDisabled =
      index === tabStore.getTabList.length - 1 && tabStore.getLastDragEndIndex >= 0;
    const dropMenuList: DropMenu[] = [
      {
        icon: 'i-ant-design:reload-outlined',
        event: MenuEventEnum.REFRESH_PAGE,
        text: t('layout.multipleTab.reload'),
        disabled: refreshDisabled,
      },
      {
        icon: 'i-ant-design:close-outlined',
        event: MenuEventEnum.CLOSE_CURRENT,
        text: t('layout.multipleTab.close'),
        disabled: !!meta?.affix || disabled,
        divider: true,
      },
      {
        icon: 'i-ant-design:arrow-left-outlined',
        event: MenuEventEnum.CLOSE_LEFT,
        text: t('layout.multipleTab.closeLeft'),
        disabled: closeLeftDisabled,
        divider: false,
      },
      {
        icon: 'i-ant-design:arrow-right-outlined',
        event: MenuEventEnum.CLOSE_RIGHT,
        text: t('layout.multipleTab.closeRight'),
        disabled: closeRightDisabled,
        divider: true,
      },
      {
        icon: 'i-ant-design:pic-center-outlined',
        event: MenuEventEnum.CLOSE_OTHER,
        text: t('layout.multipleTab.closeOther'),
        disabled: disabled,
      },
      {
        icon: 'i-ant-design:line-outlined',
        event: MenuEventEnum.CLOSE_ALL,
        text: t('layout.multipleTab.closeAll'),
        disabled: disabled,
      },
    ];

    return dropMenuList;
  });

  function handleContextMenu(tabItem: RouteLocationNormalized) {
    return async (e: Event) => {
      if (!tabItem) {
        return;
      }
      e?.preventDefault();
      const index = tabStore.getTabList.findIndex((tab) => tab.path === tabItem.path);
      state.current = tabItem;
      state.currentIndex = index;
      await go(tabItem); // 右键激活页签
    };
  }

  // Handle right click event
  function handleMenuEvent(menu: DropMenu): void {
    const { event } = menu;
    switch (event) {
      case MenuEventEnum.REFRESH_PAGE:
        // refresh page
        refreshPage();
        break;
      // Close current
      case MenuEventEnum.CLOSE_CURRENT:
        close(tabContentProps.tabItem);
        break;
      // Close left
      case MenuEventEnum.CLOSE_LEFT:
        closeLeft();
        break;
      // Close right
      case MenuEventEnum.CLOSE_RIGHT:
        closeRight();
        break;
      // Close other
      case MenuEventEnum.CLOSE_OTHER:
        closeOther();
        break;
      // Close all
      case MenuEventEnum.CLOSE_ALL:
        closeAll();
        break;
    }
  }
  return { getDropMenuList, handleMenuEvent, handleContextMenu };
}

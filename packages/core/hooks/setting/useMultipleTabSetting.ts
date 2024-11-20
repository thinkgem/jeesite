import type { MultiTabsSetting } from '@jeesite/types/config';

import { computed } from 'vue';

import { useAppStore } from '@jeesite/core/store/modules/app';

export function useMultipleTabSetting() {
  const appStore = useAppStore();

  const getShowMultipleTab = computed(() => appStore.getMultiTabsSetting.show);

  const getTabsStyle = computed(() => appStore.getMultiTabsSetting.style);

  const getShowQuick = computed(() => appStore.getMultiTabsSetting.showQuick);

  const getShowRedo = computed(() => appStore.getMultiTabsSetting.showRedo);

  const getShowFold = computed(() => appStore.getMultiTabsSetting.showFold);

  function setMultipleTabSetting(multiTabsSetting: Partial<MultiTabsSetting>) {
    appStore.setProjectConfig({ multiTabsSetting });
  }
  return {
    setMultipleTabSetting,
    getShowMultipleTab,
    getTabsStyle,
    getShowQuick,
    getShowRedo,
    getShowFold,
  };
}

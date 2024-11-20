<script lang="ts">
  import { defineComponent, toRefs, ref, unref } from 'vue';
  import { createAppProviderContext } from './useAppContext';
  import { createBreakpointListen } from '@jeesite/core/hooks/event/useBreakpoint';
  // import { useAppStore } from '@jeesite/core/store/modules/app';
  // import { MenuModeEnum, MenuTypeEnum } from '@jeesite/core/enums/menuEnum';

  export default defineComponent({
    name: 'AppProvider',
    inheritAttrs: false,
    props: {
      prefixCls: {
        type: String,
        default: 'jeesite',
      },
    },
    setup(props, { slots }) {
      const isMobile = ref(false);
      // const isSetState = ref(false);
      // const appStore = useAppStore();

      // Monitor screen breakpoint information changes
      createBreakpointListen(({ screenMap, sizeEnum, width }) => {
        const lgWidth = screenMap.get(sizeEnum.LG);
        if (lgWidth) {
          isMobile.value = width.value - 1 < lgWidth;
        }
        // handleRestoreState();
      });

      const { prefixCls } = toRefs(props);

      // Inject variables into the global
      createAppProviderContext({ prefixCls, isMobile });

      // /**
      //  * Used to maintain the state before the window changes
      //  */
      // function handleRestoreState() {
      //   if (unref(isMobile)) {
      //     if (!unref(isSetState)) {
      //       isSetState.value = true;
      //       const {
      //         menuSetting: {
      //           type: menuType,
      //           mode: menuMode,
      //           collapsed: menuCollapsed,
      //           split: menuSplit,
      //         },
      //       } = appStore.getProjectConfig;
      //       appStore.setProjectConfig({
      //         menuSetting: {
      //           mode: MenuModeEnum.INLINE,
      //           type: MenuTypeEnum.MIX,
      //           split: false,
      //         },
      //       });
      //       appStore.setBeforeMiniInfo({ menuMode, menuCollapsed, menuType, menuSplit });
      //     }
      //   } else {
      //     if (unref(isSetState)) {
      //       isSetState.value = false;
      //       const { menuMode, menuCollapsed, menuType, menuSplit } = appStore.getBeforeMiniInfo;
      //       appStore.setProjectConfig({
      //         menuSetting: {
      //           type: menuType,
      //           mode: menuMode,
      //           collapsed: menuCollapsed,
      //           split: menuSplit,
      //         },
      //       });
      //     }
      //   }
      // }
      return () => slots.default?.();
    },
  });
</script>

<template>
  <Menu
    v-bind="getBindValues"
    :activeName="activeName"
    :openNames="getOpenKeys"
    :class="prefixCls"
    :activeSubMenuNames="activeSubMenuNames"
    @select="handleSelect"
  >
    <slot name="menuBefore"></slot>
    <template v-for="item in items" :key="item.path">
      <SimpleSubMenu :item="item" :parent="true" :collapsedShowTitle="collapsedShowTitle" :collapse="collapse" />
    </template>
    <slot name="menuAfter"></slot>
  </Menu>
</template>
<script lang="ts">
  import type { MenuState } from './types';
  import type { Menu as MenuType } from '@jeesite/core/router/types';
  import type { RouteLocationNormalizedLoaded } from 'vue-router';
  import { defineComponent, computed, ref, Ref, unref, reactive, toRefs, watch } from 'vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import Menu from './components/Menu.vue';
  import SimpleSubMenu from './SimpleSubMenu.vue';
  import { listenerRouteChange } from '@jeesite/core/logics/mitt/routeChange';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { REDIRECT_NAME } from '@jeesite/core/router/constant';
  import { useRouter } from 'vue-router';
  import { isFunction, isUrl } from '@jeesite/core/utils/is';
  import { openWindow } from '@jeesite/core/utils';

  import { useOpenKeys } from './useOpenKeys';

  const props = {
    items: {
      type: Array as PropType<MenuType[]>,
      default: () => [],
    },
    collapse: propTypes.bool,
    mixSider: propTypes.bool,
    theme: propTypes.string,
    accordion: propTypes.bool.def(true),
    collapsedShowTitle: propTypes.bool,
    beforeClickFn: {
      type: Function as PropType<(key: string) => Promise<boolean>>,
    },
    isSplitMenu: propTypes.bool,
  };

  export default defineComponent({
    name: 'SimpleMenu',
    components: {
      Menu,
      SimpleSubMenu,
    },
    inheritAttrs: false,
    props,
    emits: ['menuClick'],
    setup(props, { attrs, emit }) {
      // const currentActiveMenu = ref('');
      const isClickGo = ref(false);

      const menuState = reactive<MenuState>({
        activeName: '',
        openNames: [],
        activeSubMenuNames: [],
      });

      const { currentRoute } = useRouter();
      const { prefixCls } = useDesign('simple-menu');
      const { items, accordion, mixSider, collapse } = toRefs(props) as {
        items: Ref<MenuType[]>;
        accordion: Ref<boolean>;
        mixSider: Ref<boolean>;
        collapse: Ref<boolean>;
      };

      const { setOpenKeys, getOpenKeys } = useOpenKeys(menuState, items, accordion, mixSider, collapse);

      const getBindValues = computed(() => ({ ...attrs, ...props }));

      watch(
        () => props.collapse,
        (collapse) => {
          if (collapse) {
            menuState.openNames = [];
          } else {
            // setOpenKeys(currentRoute.value.path);
            handleMenuChange();
          }
        },
        { immediate: true },
      );

      watch(
        () => props.items,
        () => {
          if (!props.isSplitMenu) {
            return;
          }
          // setOpenKeys(currentRoute.value.path);
          handleMenuChange();
        },
        { flush: 'post' },
      );

      listenerRouteChange((route) => {
        if (route.name === REDIRECT_NAME) return;

        // currentActiveMenu.value = route.meta?.currentActiveMenu as string;
        handleMenuChange(route);

        // if (unref(currentActiveMenu)) {
        //   menuState.activeName = unref(currentActiveMenu);
        //   setOpenKeys(unref(currentActiveMenu));
        // }
      });

      async function handleMenuChange(route?: RouteLocationNormalizedLoaded) {
        if (unref(isClickGo)) {
          isClickGo.value = false;
          return;
        }
        // const path = (route || unref(currentRoute)).path;
        const currRoute = route || unref(currentRoute);
        const path = (currRoute.meta?.currentActiveMenu as string) || currRoute.path;

        menuState.activeName = path;

        setOpenKeys(path);
      }

      async function handleSelect(key: string, item: any) {
        if (isUrl(key)) {
          openWindow(key);
          return;
        }
        const { beforeClickFn } = props;
        if (beforeClickFn && isFunction(beforeClickFn)) {
          const flag = await beforeClickFn(key);
          if (!flag) return;
        }
        emit('menuClick', key, item);

        isClickGo.value = true;
        setOpenKeys(key);
        menuState.activeName = key;
      }

      return {
        prefixCls,
        getBindValues,
        handleSelect,
        getOpenKeys,
        ...toRefs(menuState),
      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
</style>

<template>
  <AMenu
    :selectedKeys="selectedKeys"
    :defaultSelectedKeys="defaultSelectedKeys"
    :mode="mode"
    :openKeys="getOpenKeys"
    :inlineIndent="inlineIndent"
    :theme="theme"
    @open-change="handleOpenChange"
    :class="getMenuClass"
    @click="handleMenuClick"
    :subMenuOpenDelay="0.2"
    v-bind="getInlineCollapseOptions"
  >
    <slot name="menuBefore"></slot>
    <template v-for="item in items" :key="item.path">
      <BasicSubMenuItem :item="item" :theme="theme" :isHorizontal="isHorizontal" />
    </template>
    <slot name="menuAfter"></slot>
  </AMenu>
</template>
<script lang="ts">
  import type { MenuState } from './types';
  import { computed, defineComponent, reactive, ref, toRefs, unref, watch } from 'vue';
  import { Menu } from 'ant-design-vue';
  import BasicSubMenuItem from './components/BasicSubMenuItem.vue';
  import { MenuModeEnum, MenuTypeEnum } from '@jeesite/core/enums/menuEnum';
  import { useOpenKeys } from './useOpenKeys';
  import { RouteLocationNormalizedLoaded, useRouter } from 'vue-router';
  import { isFunction } from '@jeesite/core/utils/is';
  import { basicProps } from './props';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { REDIRECT_NAME } from '@jeesite/core/router/constant';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { getCurrentParentPath } from '@jeesite/core/router/menus';
  import { listenerRouteChange } from '@jeesite/core/logics/mitt/routeChange';
  import { getAllParentPath } from '@jeesite/core/router/helper/menuHelper';
  import { MenuInfo } from 'ant-design-vue/es/menu/src/interface';

  export default defineComponent({
    name: 'BasicMenu',
    components: {
      AMenu: Menu,
      BasicSubMenuItem,
    },
    props: basicProps,
    emits: ['menuClick'],
    setup(props, { emit }) {
      const isClickGo = ref(false);
      // const currentActiveMenu = ref('');

      const menuState = reactive<MenuState>({
        defaultSelectedKeys: [],
        openKeys: [],
        selectedKeys: [],
        collapsedOpenKeys: [],
      });

      const { prefixCls } = useDesign('basic-menu');
      const { items, mode, accordion } = toRefs(props);

      const { getCollapsed, getTopMenuAlign, getSplit } = useMenuSetting();

      const { currentRoute } = useRouter();

      const { handleOpenChange, setOpenKeys, getOpenKeys } = useOpenKeys(menuState, items, mode as any, accordion);

      const getIsTopMenu = computed(() => {
        const { type, mode } = props;

        return (
          (type === MenuTypeEnum.TOP_MENU && mode === MenuModeEnum.HORIZONTAL) ||
          (props.isHorizontal && unref(getSplit))
        );
      });

      const getMenuClass = computed(() => {
        const align = props.isHorizontal && unref(getSplit) ? 'start' : unref(getTopMenuAlign);
        return [
          prefixCls,
          `justify-${align}`,
          {
            [`${prefixCls}__second`]: !props.isHorizontal && unref(getSplit),
            [`${prefixCls}__sidebar-hor`]: unref(getIsTopMenu),
          },
        ];
      });

      const getInlineCollapseOptions = computed(() => {
        const isInline = props.mode === MenuModeEnum.INLINE;

        const inlineCollapseOptions: { inlineCollapsed?: boolean } = {};
        if (isInline) {
          inlineCollapseOptions.inlineCollapsed = props.mixSider ? false : unref(getCollapsed);
        }
        return inlineCollapseOptions;
      });

      !props.mixSider &&
        watch(
          () => props.items,
          () => {
            handleMenuChange();
          },
        );

      listenerRouteChange((route) => {
        if (route.name === REDIRECT_NAME) return;
        // currentActiveMenu.value = route.meta?.currentActiveMenu as string;
        // if (unref(currentActiveMenu)) {
        //   menuState.selectedKeys = [unref(currentActiveMenu)];
        //   setOpenKeys(unref(currentActiveMenu));
        // }
        handleMenuChange(route);
      });

      async function handleMenuChange(route?: RouteLocationNormalizedLoaded) {
        if (unref(isClickGo)) {
          isClickGo.value = false;
          return;
        }
        // const path = (route || unref(currentRoute)).path;
        const currRoute = route || unref(currentRoute);
        const path = (currRoute.meta?.currentActiveMenu as string) || currRoute.path;

        await setOpenKeys(path);

        if (menuState.openKeys.length > 0) {
          menuState.selectedKeys = menuState.openKeys;
        } else {
          if (props.isHorizontal && unref(getSplit)) {
            const parentPath = await getCurrentParentPath(path);
            menuState.selectedKeys = [parentPath];
          } else {
            menuState.selectedKeys = getAllParentPath(props.items, path);
          }
        }
      }

      async function handleMenuClick({ item, key }: MenuInfo) {
        // { item: any; key: string; keyPath: string[] }) {
        const { beforeClickFn } = props;
        if (beforeClickFn && isFunction(beforeClickFn)) {
          const flag = await beforeClickFn(key as string);
          if (!flag) return;
        }
        emit('menuClick', key, item);

        isClickGo.value = true;

        await setOpenKeys(key as string);

        if (menuState.openKeys.length > 0) {
          menuState.selectedKeys = menuState.openKeys;
        } else {
          menuState.selectedKeys = [key as string];
        }
        // console.log('TopMenuClick', menuState.selectedKeys, menuState.openKeys);
      }

      return {
        getInlineCollapseOptions,
        getMenuClass,
        handleOpenChange,
        getOpenKeys,
        handleMenuClick,
        ...toRefs(menuState),
      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
</style>

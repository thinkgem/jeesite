<script lang="tsx">
  import type { PropType, CSSProperties } from 'vue';

  import { computed, defineComponent, unref, toRef } from 'vue';
  import { BasicMenu } from '@jeesite/core/components/Menu';
  import { SimpleMenu } from '@jeesite/core/components/SimpleMenu';
  import { AppLogo } from '@jeesite/core/components/Application';

  import { MenuModeEnum, MenuSplitTyeEnum, MenuTypeEnum } from '@jeesite/core/enums/menuEnum';

  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { ScrollContainer } from '@jeesite/core/components/Container';

  import { useGo } from '@jeesite/core/hooks/web/usePage';
  import { useSplitMenu } from './useLayoutMenu';
  import { openWindow } from '@jeesite/core/utils';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { isUrl } from '@jeesite/core/utils/is';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';

  import { UserDropDown } from '../header/components';

  export default defineComponent({
    name: 'LayoutMenu',
    props: {
      theme: propTypes.oneOf(['light', 'dark']),

      splitType: {
        type: Number as PropType<MenuSplitTyeEnum>,
        default: MenuSplitTyeEnum.NONE,
      },

      isHorizontal: propTypes.bool,
      // menu Mode
      menuMode: {
        type: [String] as PropType<Nullable<MenuModeEnum>>,
        default: '',
      },
    },
    setup(props) {
      const go = useGo();

      const {
        getMenuMode,
        getMenuType,
        getMenuTheme,
        getCollapsed,
        getCollapsedShowTitle,
        getAccordion,
        getIsHorizontal,
        getIsSidebarType,
        getSplit,
      } = useMenuSetting();
      const { getShowLogo } = useRootSetting();

      const { menusRef } = useSplitMenu(toRef(props, 'splitType'));

      const { getIsMobile } = useAppInject();

      const getComputedMenuMode = computed(() =>
        unref(getIsMobile) ? MenuModeEnum.INLINE : props.menuMode || unref(getMenuMode),
      );

      const getComputedMenuTheme = computed(() => props.theme || unref(getMenuTheme));

      const getIsShowLogo = computed(() => unref(getShowLogo) && unref(getIsSidebarType));

      const getUseScroll = computed(() => {
        return (
          !unref(getIsHorizontal) &&
          (unref(getIsSidebarType) ||
            props.splitType === MenuSplitTyeEnum.LEFT ||
            props.splitType === MenuSplitTyeEnum.NONE)
        );
      });

      const getWrapperStyle = computed((): CSSProperties => {
        return {
          height: `calc(100% - ${unref(getIsShowLogo) ? '48px' : '0px'})`,
        };
      });

      const getLogoClass = computed(() => {
        return [
          'jeesite-layout-menu-logo',
          unref(getComputedMenuTheme),
          {
            ['jeesite-layout-menu--mobile']: unref(getIsMobile),
          },
        ];
      });

      const getCommonProps = computed(() => {
        const menus = unref(menusRef);
        return {
          menus,
          beforeClickFn: beforeMenuClickFn,
          items: menus,
          theme: unref(getComputedMenuTheme),
          accordion: unref(getAccordion),
          collapse: unref(getCollapsed),
          collapsedShowTitle: unref(getCollapsedShowTitle),
          onMenuClick: handleMenuClick,
        };
      });

      /**
       * click menu
       * @param menu
       */
      function handleMenuClick(path: string) {
        const menus = unref(menusRef);
        const item = findMenuItem(menus, path);
        if (item && item.target === '_blank') {
          window.open(path);
        } else {
          // const url = String(item.url);
          // const paramIdx = url.indexOf('?');
          // if (paramIdx != -1 && !item.meta.frameSrc) {
          //   const params = url.substring(paramIdx);
          //   go(item.path + params);
          // } else {
          go(path);
          // }
        }
      }

      function findMenuItem(items: any[], key: string): any | null {
        for (const item of items) {
          if (item.path === key) {
            return item;
          }
          if (item.children && item.children.length > 0) {
            const found = findMenuItem(item.children, key);
            if (found) {
              return found;
            }
          }
        }
        return null;
      }

      /**
       * before click menu
       * @param menu
       */
      async function beforeMenuClickFn(path: string) {
        if (!isUrl(path)) {
          return true;
        }
        openWindow(path);
        return false;
      }

      function renderHeader() {
        if (!unref(getIsShowLogo) && !unref(getIsMobile)) return null;

        return (
          <AppLogo showTitle={!unref(getCollapsed)} class={unref(getLogoClass)} theme={unref(getComputedMenuTheme)} />
        );
      }

      function renderUserInfo() {
        if (unref(getMenuType) === MenuTypeEnum.SIDEBAR) return null;
        return <UserDropDown theme={unref(getMenuTheme)} sidebar={true} />;
      }

      function renderMenu() {
        const { menus, ...menuProps } = unref(getCommonProps);
        // console.log(menus);
        if (!menus || !menus.length) return null;
        return !props.isHorizontal ? (
          <SimpleMenu
            {...menuProps}
            isSplitMenu={unref(getSplit)}
            items={menus}
            v-slots={{
              menuBefore: () => renderUserInfo(),
            }}
          />
        ) : (
          <BasicMenu
            {...(menuProps as any)}
            isHorizontal={props.isHorizontal}
            type={unref(getMenuType)}
            showLogo={unref(getIsShowLogo)}
            mode={unref(getComputedMenuMode as any)}
            items={menus}
          />
        );
      }

      return () => {
        return (
          <>
            {renderHeader()}
            {unref(getUseScroll) ? (
              <ScrollContainer style={unref(getWrapperStyle)}>{() => renderMenu()}</ScrollContainer>
            ) : (
              renderMenu()
            )}
          </>
        );
      };
    },
  });
</script>
<style lang="less">
  @logo-prefix-cls: ~'jeesite-app-logo';

  .jeesite-layout-menu {
    &-logo {
      height: @header-height;
      padding: 10px 4px 10px 10px;

      img {
        width: @logo-width;
        height: @logo-width;
      }
    }

    &--mobile {
      .@{logo-prefix-cls} {
        &__title {
          opacity: 1;
        }
      }
    }
  }
</style>

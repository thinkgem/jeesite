<script lang="tsx">
  import type { Menu as MenuType } from '@jeesite/core/router/types';
  import { defineComponent, computed } from 'vue';
  import { SubMenu, MenuItem } from 'antdv-next';
  import { itemProps } from '../props';
  import { omit } from 'lodash-es';
  import MenuItemContent from './MenuItemContent.vue';

  export default defineComponent({
    name: 'BasicSubMenuItem',
    props: itemProps,
    setup(props) {
      const getShowMenu = computed(() => !props.item.meta?.hideMenu);

      const getMenuItem = computed(() => {
        return omit(props.item, 'children', 'icon', 'title', 'color', 'extend');
      });

      function menuHasChildren(menuTreeItem: MenuType): boolean {
        return (
          !menuTreeItem.meta?.hideChildrenInMenu &&
          Reflect.has(menuTreeItem, 'children') &&
          !!menuTreeItem.children &&
          menuTreeItem.children.length > 0
        );
      }

      function renderMenuItem(item: MenuType, level: number) {
        if (!getShowMenu.value) return null;

        if (menuHasChildren(item)) {
          return (
            <SubMenu key={item.path} name={item.path} popupClassName="app-top-menu-popup" class={[props.theme]}>
              {{
                title: () => <MenuItemContent item={item} level={level} isHorizontal={props.isHorizontal} />,
                default: () => (item.children || []).map((childrenItem) => renderMenuItem(childrenItem, level + 1)),
              }}
            </SubMenu>
          );
        }

        return (
          <MenuItem key={item.path} {...getMenuItem.value}>
            <MenuItemContent item={item} level={level} isHorizontal={props.isHorizontal} />
          </MenuItem>
        );
      }

      return () => renderMenuItem(props.item, props.level || 0);
    },
  });
</script>

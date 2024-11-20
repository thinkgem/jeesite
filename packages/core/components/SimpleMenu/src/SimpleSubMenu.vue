<template>
  <MenuItem
    :name="item.path"
    v-if="!menuHasChildren(item) && getShowMenu"
    v-bind="$props"
    :class="getLevelClass"
    :style="`color: ${getColor}`"
    :title="getI18nName"
    :item="getMenuItem"
  >
    <Icon v-if="getIcon" :icon="getIcon" :size="16" />
    <div v-if="collapsedShowTitle && getIsCollapseParent" class="collapse-title mt-1">
      {{ getI18nName }}
    </div>
    <template #title>
      <span :class="['ml-2', `${prefixCls}-sub-title`]">
        {{ getI18nName }}
      </span>
      <SimpleMenuTag :item="item" :collapseParent="getIsCollapseParent" />
    </template>
  </MenuItem>
  <SubMenu
    :name="item.path"
    v-if="menuHasChildren(item) && getShowMenu"
    :class="[getLevelClass, theme]"
    :collapsedShowTitle="collapsedShowTitle"
    :style="`color: ${getColor}`"
    :title="getI18nName"
  >
    <template #title>
      <Icon v-if="getIcon" :icon="getIcon" :size="16" />

      <div v-if="collapsedShowTitle && getIsCollapseParent" class="collapse-title mt-2">
        {{ getI18nName }}
      </div>

      <span v-show="getShowSubTitle" :class="['ml-2', `${prefixCls}-sub-title`]">
        {{ getI18nName }}
      </span>
      <SimpleMenuTag :item="item" :collapseParent="!!collapse && !!parent" />
    </template>
    <template v-for="childrenItem in item.children || []" :key="childrenItem.path">
      <SimpleSubMenu v-bind="$props" :item="childrenItem" :parent="false" />
    </template>
  </SubMenu>
</template>
<script lang="ts">
  import type { PropType } from 'vue';
  import type { Menu } from '@jeesite/core/router/types';

  import { defineComponent, computed } from 'vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import Icon from '@jeesite/core/components/Icon';

  import MenuItem from './components/MenuItem.vue';
  import SubMenu from './components/SubMenuItem.vue';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import SimpleMenuTag from './SimpleMenuTag.vue';
  import { omit } from 'lodash-es';

  const props = {
    item: {
      type: Object as PropType<Menu>,
      default: () => ({}),
    },
    parent: propTypes.bool,
    collapsedShowTitle: propTypes.bool,
    collapse: propTypes.bool,
    theme: propTypes.oneOf(['dark', 'light']),
  };

  export default defineComponent({
    name: 'SimpleSubMenu',
    components: {
      SubMenu,
      MenuItem,
      SimpleMenuTag,
      Icon,
    },
    props,
    setup(props) {
      const { t } = useI18n();
      const { prefixCls } = useDesign('simple-menu');

      const getShowMenu = computed(() => !props.item?.meta?.hideMenu);
      const getIcon = computed(() => props.item?.icon);
      const getColor = computed(() => props.item?.color);
      const getI18nName = computed(() => t(props.item?.name));
      const getShowSubTitle = computed(() => !props.collapse || !props.parent);
      const getIsCollapseParent = computed(() => !!props.collapse && !!props.parent);
      const getLevelClass = computed(() => {
        return [
          {
            [`${prefixCls}__parent`]: props.parent,
            [`${prefixCls}__children`]: !props.parent,
          },
        ];
      });
      const getMenuItem = computed(() => {
        return omit(props.item, 'children', 'icon', 'title', 'color', 'extend');
      });

      function menuHasChildren(menuTreeItem: Menu): boolean {
        return (
          !menuTreeItem.meta?.hideChildrenInMenu &&
          Reflect.has(menuTreeItem, 'children') &&
          !!menuTreeItem.children &&
          menuTreeItem.children.length > 0
        );
      }

      return {
        prefixCls,
        menuHasChildren,
        getShowMenu,
        getIcon,
        getColor,
        getI18nName,
        getShowSubTitle,
        getLevelClass,
        getIsCollapseParent,
        getMenuItem,
      };
    },
  });
</script>

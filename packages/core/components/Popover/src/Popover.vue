<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <Popover
    :trigger="trigger"
    v-bind="$attrs"
    v-model:open="open"
    overlayClassName="jeesite-basic-popover"
    :mouseEnterDelay="0.05"
    :placement="placement"
  >
    <span>
      <slot></slot>
    </span>
    <template #content>
      <Menu :selectedKeys="selectedKeys" :mode="menuMode" :disabledOverflow="true">
        <template v-for="item in dropMenuList" :key="`${item.event}`">
          <MenuItem
            v-bind="getAttr(item.event)"
            @click="handleClickMenu(item)"
            :disabled="item.disabled"
            :title="item.iconTitle"
          >
            <Popconfirm v-if="popconfirm && item.popConfirm" v-bind="getPopConfirmAttrs(item.popConfirm)">
              <template #icon v-if="item.popConfirm.icon">
                <Icon :icon="item.popConfirm.icon" />
              </template>
              <div>
                <Icon :icon="item.icon" v-if="item.icon" />
                <span class="ml-1">{{ item.text }}</span>
              </div>
            </Popconfirm>
            <template v-else>
              <Icon :icon="item.icon" v-if="item.icon" />
              <span class="ml-1">{{ item.text }}</span>
            </template>
          </MenuItem>
          <MenuDivider v-if="item.divider" :key="`d-${item.event}`" />
        </template>
      </Menu>
    </template>
  </Popover>
</template>
<script lang="ts">
  import { computed, PropType, ref } from 'vue';
  import type { DropMenu } from './typing';

  import { defineComponent } from 'vue';
  import { Popover, Popconfirm, Menu } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { omit } from 'lodash-es';
  import { isFunction } from '@jeesite/core/utils/is';

  export default defineComponent({
    name: 'BasicPopover',
    components: {
      Popover,
      Menu,
      MenuItem: Menu.Item,
      MenuDivider: Menu.Divider,
      Icon,
      Popconfirm,
    },
    props: {
      popconfirm: Boolean,
      /**
       * the trigger mode which executes the drop-down action
       * @default ['hover']
       * @type string[]
       */
      trigger: {
        type: [Array] as PropType<('contextmenu' | 'click' | 'hover')[]>,
        default: () => {
          return ['contextmenu'];
        },
      },
      dropMenuList: {
        type: Array as PropType<(DropMenu & Recordable)[]>,
        default: () => [],
      },
      selectedKeys: {
        type: Array as PropType<string[]>,
        default: () => [],
      },
      placement: {
        type: String as PropType<
          | 'left'
          | 'right'
          | 'bottom'
          | 'top'
          | 'bottomRight'
          | 'bottomLeft'
          | 'topLeft'
          | 'topRight'
          | 'leftTop'
          | 'leftBottom'
          | 'rightTop'
          | 'rightBottom'
        >,
        default: 'left',
      },
      menuMode: {
        type: String as PropType<'horizontal' | 'vertical' | 'inline'>,
        default: 'horizontal',
      },
    },
    emits: ['menuEvent'],
    setup(props, { emit }) {
      const open = ref(false);

      function handleClickMenu(item: DropMenu) {
        if (!item['popConfirm']) {
          open.value = false;
        }
        const { event } = item;
        const menu = props.dropMenuList.find((item) => `${item.event}` === `${event}`);
        emit('menuEvent', menu);
        item.onClick?.();
      }

      const getPopConfirmAttrs = computed(() => {
        return (attrs) => {
          const originAttrs = omit(attrs, ['confirm', 'cancel', 'icon']);
          if (!attrs.onConfirm && attrs.confirm && isFunction(attrs.confirm)) originAttrs['onConfirm'] = attrs.confirm;
          if (!attrs.onCancel && attrs.cancel && isFunction(attrs.cancel)) originAttrs['onCancel'] = attrs.cancel;
          return originAttrs;
        };
      });

      return {
        open,
        handleClickMenu,
        getPopConfirmAttrs,
        getAttr: (key: string | number) => ({ key }),
      };
    },
  });
</script>
<style lang="less">
  .ant-popover.jeesite-basic-popover {
    .ant-popover-content,
    .ant-popover-inner,
    .ant-menu-horizontal {
      border-radius: 6px;
      padding: 0;
    }

    .ant-popover-inner-content {
      padding: 0 5px;
    }

    .ant-menu-horizontal {
      line-height: 36px;
      border-bottom: 0;
      padding-bottom: 2px;

      > .ant-menu-item,
      > .ant-menu-submenu {
        padding: 0 10px;

        .ant-menu-title-content {
          transition: none;

          &:hover {
            color: @primary-color !important;
          }
        }

        &::after {
          border-bottom: 0 !important;
        }
      }
    }
  }

  html[data-theme='dark'] {
    .ant-popover.jeesite-basic-popover {
      .ant-menu-horizontal:not(.ant-menu-dark) {
        > .ant-menu-item:hover,
        > .ant-menu-submenu:hover,
        > .ant-menu-item-active,
        > .ant-menu-submenu-active,
        > .ant-menu-item-open,
        > .ant-menu-submenu-open,
        > .ant-menu-item-selected,
        > .ant-menu-submenu-selected {
          color: #1890ff;
        }
      }
    }
  }
</style>
